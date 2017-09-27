/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.TimeUnit;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.filter.BattwoseFilter;
import sps.dev.control.bill.DarkDataFilter;
import sps.dev.control.bill.KalManFilter;
import sps.dev.control.bill.SPDataBuilder;
import sps.dev.control.bill.SmoothFilter;
import sps.dev.control.bill.WindowSmoothFilter;
import sps.dev.control.ifs.ISPDataCollect;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSpectralPar;
import sps.platform.SpectralPlatService;
import sps.dev.drv.ISPDevice;

/**
 *
 * @author Administrator
 */
public class SPDataCollect implements ISPDataCollect {

    private final SPDevControl control;

    public SPDataCollect(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        this.collectpar = this.control.GetDevice().GetCollectPar();
        TimeUnit.MILLISECONDS.sleep(10);
        //清空暗电流
        dkfilter.UpdateDKData(new double[0]);

        //初始化巴特沃斯滤波器
        SSpectralPar sppar = this.control.spdevconfig.sppar;
        this.batfilter.InitBattwoseWindow(sppar.nodeNumber, sppar.nodeNumber / 10, 16);

        this.kalfilter.InitKalman(sppar.nodeNumber);
        
        
        //使能滤波
        String isenable = SpectralPlatService.GetInstance().GetConfig().getProperty(EnableKey, "false");
        this.SetFilterEnable(Boolean.valueOf(isenable));

        //使能非线性
        isenable = SpectralPlatService.GetInstance().GetConfig().getProperty(LinearKey, "false");
        this.SetLinearEnable(Boolean.valueOf(isenable));
    }

    // <editor-fold defaultstate="collapsed" desc="暗电流控制">
    private DarkDataFilter dkfilter = new DarkDataFilter();
    private SSDataCollectPar dkpar;

    @Override
    public boolean IsDKEnable() {
        return this.dkfilter.IsDKEnable();
    }

    @Override
    public void SetDkEnable(boolean value) {
        this.dkfilter.SetDkEnable(value);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集控制">
    //初始化数据生成器
    private SPDataBuilder builder = new SPDataBuilder();
    //初始化滑动平均
    private WindowSmoothFilter windowfilter = new WindowSmoothFilter();

    private SSDataCollectPar collectpar = null;

    public SSDataCollectPar GetCollectPar() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.collectpar;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    private boolean isCancled = false;

    @Override
    public boolean IsCanceled() {
        return this.isCancled;
    }

    @Override
    public void PrepareCollect(SSDataCollectPar par) throws Exception {
        this.control.LockDevice();
        if (!this.collectpar.EqualTo(par)) {
            try {
                this.control.GetDevice().SetCollectPar(par);
                this.collectpar = new SSDataCollectPar(par);
            } catch (Exception ex) {
                this.control.UnlockDevice();
                throw ex;
            }
        }
        this.isCancled = false;
    }

    @Override
    public void FinishCollect() {
        this.control.UnlockDevice();
    }

    //计算超时时间
    private int GetCollectTimeout(SSDataCollectPar par) {
        if (par.WorkMode == SSDataCollectPar.SoftMode) {
            //软件模式，根据平均次数，积分时间计算超时时间
            int CMDLagtime = 2000; //安全余量
            int HWTimePeiod = 5;//硬件平均周期
            return (int) (par.integralTime + HWTimePeiod) * par.averageTime + CMDLagtime;
        } else {
            //硬件模式，超时时间无穷大
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public SSpectralDataPacket StartSingleTest(int window) throws Exception {
        ISPDevice dev = this.control.GetDevice();
        double[] oradata = dev.CollectData(this.control.GetSPDevConfig().GetSpectralPar().nodeNumber,
                this.GetCollectTimeout(this.collectpar));
        SSpectralDataPacket spdata = this.builder.BuildSPData(oradata, this.control.GetSPDevConfig().GetWaveParameter());

        //修正暗电流
        if (!this.IsDKEnable()) {
            dkfilter.UpdateDKData(oradata);
//            this.dkpar = this.collectpar;
        } else {
//            if (dkpar.averageTime == this.collectpar.averageTime && dkpar.integralTime == this.collectpar.integralTime) {
            this.dkfilter.Filter(spdata.data.datavalue);
//            } else {
//                this.SetDkEnable(false);
//            }
        }

        //非线性校准
        //巴特沃斯滤波
        if (this.batfilter.IsBatFilterEnable()) {
            spdata.data.datavalue = this.batfilter.Filter(spdata.data.datavalue);
        }

        //滑动平均
        if (window > 1) {
            spdata.data.datavalue = this.windowfilter.Filter(spdata.data.datavalue, window);
        }

        //平滑
        if (this.smoothfilter.IsSmoothEnable()) {
            this.smoothfilter.Filter(spdata.data.pixelIndex, spdata.data.datavalue);
        }
        
        if(this.kalfilter.IsKalmanFilterEnable()){            
            spdata.data.datavalue = this.kalfilter.Filter(spdata.data.datavalue);
        }

                //修正暗电流
//        if (!this.IsDKEnable()) {
//            dkfilter.UpdateDKData(spdata.data.datavalue);
////            this.dkpar = this.collectpar;
//        } else {
////            if (dkpar.averageTime == this.collectpar.averageTime && dkpar.integralTime == this.collectpar.integralTime) {
//            this.dkfilter.Filter(spdata.data.datavalue);
////            } else {
////                this.SetDkEnable(false);
////            }
//        }
        
        //发送数据
        return spdata;
    }

    @Override
    public void CancelCollectData() throws Exception{
        this.control.GetDevice().Cancel();

        //如果是取消了，重新下发软件模式到设备上
        if (this.collectpar.WorkMode != SSDataCollectPar.SoftMode) {
            this.collectpar.WorkMode = SSDataCollectPar.SoftMode;
            SSDataCollectPar npar = new SSDataCollectPar(collectpar.integralTime, collectpar.averageTime, SSDataCollectPar.SoftMode);
            this.control.GetDevice().SetCollectPar(npar);
            this.collectpar = new SSDataCollectPar(npar);
        }
        
        this.isCancled = true;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="滤波控制采集">  
    //初始化平滑计算器
    private SmoothFilter smoothfilter = new SmoothFilter();
    //巴特沃斯滤波器
    private BattwoseFilter batfilter = new BattwoseFilter();
    private KalManFilter kalfilter = new KalManFilter();
    private boolean isfilterenable = false;
    private String EnableKey = "FilterEnable";

    @Override
    public boolean IsFilterEnable() {
        return isfilterenable;
    }

    @Override
    public void SetFilterEnable(boolean value) {
        if (value != this.IsFilterEnable()) {
            this.batfilter.SetBatFilterEnable(value);
            this.smoothfilter.SetSmoothEnable(value);
            this.kalfilter.SetKalmanFilterEnable(value);
            
            this.isfilterenable = this.batfilter.IsBatFilterEnable() || this.smoothfilter.IsSmoothEnable();
            SpectralPlatService.GetInstance().GetConfig().setProperty(EnableKey, String.valueOf(this.isfilterenable));
            SpectralPlatService.GetInstance().GetConfig().SaveToFile();
        }
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="非线性控制">
    private String LinearKey = "LinearEnable";
    private boolean isLinearEnable = false;

    @Override
    public boolean IsLinearEnable() {
        return this.isLinearEnable
                && this.control.spdevconfig.linearpar != null
                && this.control.spdevconfig.linearpar.SynType != SSLinearParameter.NonLinearPar;
    }

    @Override
    public void SetLinearEnable(boolean value) {
        if (this.isLinearEnable != value) {
            this.isLinearEnable = value;
            SpectralPlatService.GetInstance().GetConfig().setProperty(LinearKey, String.valueOf(this.isLinearEnable));
            SpectralPlatService.GetInstance().GetConfig().SaveToFile();
        }
    }
    // </editor-fold> 

}
