/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.inst;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import lam.devcontrol.ifs.ISPDataCollect;
import lam.devcontrol.ifs.ISPDevControl;
import lam.devcontrol.ifs.ISPDevControl.CSTATE;
import lam.faultcenter.FaultCenter;
import lam.faultcenter.SYSLOG;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import spdev.alg.spdatacal.SpectralDataFactory;
import spdev.dev.data.CancelException;
import spdev.dev.data.DataCollectPar;
import static spdev.dev.data.DataCollectPar.SoftMode;
import spdev.dev.data.SPPacket;
import spdev.dev.data.SpectralDataPacket;
import spdev.dev.data.SpectralPar;
import spdev.dev.ifs.IDataCollect;
import sps.system.SPDevSystem;

/**
 *
 * @author Administrator
 */
public class SPDataCollect implements ISPDataCollect {

    private SPDevControl control;

    public SPDataCollect(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        this.MEventCenter = new EventCenter();
        //初始化光谱数据修正器
        this.spdataFactory = new SpectralDataFactory();
        this.spdataFactory.GetWaveCalModel().SetWaveCaculatePar(this.control.GetSPDevConfig().GetWaveParameter());

        //获取采样参数
        this.collectpar = this.control.spdev.GetCollectComp().GetDataCollectPar();
        TimeUnit.MILLISECONDS.sleep(10);
    }

    // <editor-fold defaultstate="collapsed" desc="采集数据事件">  
    private EventCenter<SpectralDataPacket> MEventCenter;

    //注册数据响应监听函数
    @Override
    public void RegisterDataCollectListener(EventListener<SpectralDataPacket> listener) {
        this.MEventCenter.RegeditListener(listener);
    }
    
    @Override
    public void UnRegisterDataCollectListener(EventListener<SpectralDataPacket> listener){
        this.MEventCenter.RemoveListenner(listener);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="数据采集参数">  
    //采样数据
    private DataCollectPar collectpar;

    //获取采样数据
    @Override
    public final DataCollectPar GetDataCollectPar() {
        return new DataCollectPar(this.collectpar);
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="上一次原始光谱数据">  
    private double[] lastorignaldata = null;

    @Override
    public double[] GetLastOriginalData() {
        return this.lastorignaldata;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="数据采集"> 
    // <editor-fold defaultstate="collapsed" desc="连续采集数据"> 
    @Override
    public boolean StartSustainCollect(DataCollectPar par, int inteveral) {
        //在连接状态下，开始监听
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CONNECT) {
            //状态不正确
            FaultCenter.getReporter().SendFaultReport(Level.SEVERE,
                    "无法在当前状态下开始连续采集\r\n" + this.control.GetControlState());
            return false;
        }

        control.controllock.lock();
        try {
            //切换到监听状态下
            this.control.ChangeState(ISPDevControl.CSTATE.COLLECT);

            //启动监听进程
            SPDevSystem.GetInstance().systemthreadpool.submit(new SusCollectProcess(par, inteveral));

            return true;
        } finally {
            control.controllock.unlock();
        }
    }

    private class SusCollectProcess implements Runnable {

        private DataCollectPar par; //采集参数
        private int inteveral;      //周期间隔

        public SusCollectProcess(DataCollectPar par, int inteveral) {
            this.par = par;
            this.inteveral = inteveral;
        }

        @Override
        public void run() {
            SYSLOG.getLog().PrintLog(Level.INFO, "开始连续采集数据");

            control.controllock.lock();
            try {
                control.spdev.Connect();

                while (SartOneTest(par)) {
                    TimeUnit.MILLISECONDS.sleep(inteveral);
                }
            } catch (Exception ex) {
                FaultCenter.getReporter().SendFaultReport(Level.SEVERE,
                        "采集数据异常\r\n" + ex.toString());
            } finally {
                control.spdev.DisConnect();
                control.controllock.unlock();
            }
            SYSLOG.getLog().PrintLog(Level.INFO, "停止连续采集数据");
            control.ChangeState(ISPDevControl.CSTATE.CONNECT);
        }
    };
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="单次采集">  
    @Override
    public boolean StartSingleTest(DataCollectPar par) {

        if (this.control.GetControlState() != CSTATE.CONNECT) {
            //状态不正确
            FaultCenter.getReporter().SendFaultReport(Level.SEVERE,
                    "无法在当前状态下采集数据\r\n" + this.control.GetControlState());
            return false;
        }

        control.controllock.lock();
        try {

            //切换到采集状态下
            this.control.ChangeState(ISPDevControl.CSTATE.COLLECT);

            //采集一次数据
            boolean ret = this.SartOneTest(par);

            //恢复成连接状态
            this.control.ChangeState(ISPDevControl.CSTATE.CONNECT);

            //返回采集结果
            return ret;

        } finally {
            control.controllock.unlock();
        }
    }
    // </editor-fold>      

    //停止采集数据
    @Override
    public void StopCollectData() {
        //在采集状态下，停止采集
        if (control.GetControlState() == CSTATE.COLLECT) {
            //在采集状态下，每50ms打断一次采集，来终端采集过程
            while (control.GetControlState() == CSTATE.COLLECT) {
                this.control.spdev.GetCollectComp().BreakSingleTest();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    //开始一次采数据过程
    private boolean SartOneTest(DataCollectPar par) {
        try {
            this.control.spdev.Connect();

            SpectralPar sppar = this.control.GetSPDevConfig().GetSpectralPar();
            IDataCollect collector = this.control.spdev.GetCollectComp();

            //下发采集参数
            this.SetCollectPar(par);

            //采集数据
            double[] data = collector.GetSingleTest(sppar.nodeNumber, this.GetCollectTimeout(par));
            //生成光谱数据
            this.ReportSPData(data);

            if (par.WorkMode != SoftMode) {
                //硬件触发下，采集成功后，设备会自动切换成软件模式
                this.collectpar.WorkMode = SoftMode;
            }
            //采集成功
            return true;
        } catch (CancelException ex) {
            //如果是取消了，重新下发软件模式到设备上
            if (par.WorkMode != SoftMode) {
                DataCollectPar npar = new DataCollectPar(par);
                npar.WorkMode = SoftMode;
                try {
                    this.control.spdev.DisConnect();
                    this.control.spdev.Connect();
                    this.SetCollectPar(npar);
                } catch (Exception ex1) {
                    //采集异常
                    FaultCenter.getReporter().SendFaultReport(Level.SEVERE,
                            "重置软件模式异常\r\n" + ex.toString());
                }
            }
            //如果是取消，不报任何错误，直接返回没有采集到数据
            return false;
        } catch (Exception ex) {
            //采集异常
            FaultCenter.getReporter().SendFaultReport(Level.SEVERE,
                    "采集数据失败\r\n" + ex.toString());
            return false;
        } finally {
            this.control.spdev.DisConnect();
        }
    }

    //计算超时时间
    private int GetCollectTimeout(DataCollectPar par) {
        if (par.WorkMode == SoftMode) {
            //软件模式，根据平均次数，积分时间计算超时时间
            int CMDLagtime = 1000; //安全余量
            int HWTimePeiod = 5;//硬件平均周期
            return (int) (par.integralTime + HWTimePeiod) * par.averageTime + CMDLagtime;
        } else {
            //硬件模式，超时时间无穷大
            return Integer.MAX_VALUE;
        }
    }

    //下发采样参数
    private void SetCollectPar(DataCollectPar par) throws Exception {
        if (!this.collectpar.EqualTo(par)) {
            this.control.spdev.GetCollectComp().SetDataCollectPar(par);
            //更新本地缓存
            this.collectpar = new DataCollectPar(par);
        }
    }

    //上报光谱数据
    private void ReportSPData(double[] data) throws Exception {
        //保存最后一次原始数据
        lastorignaldata = new double[data.length];
        System.arraycopy(data, 0, lastorignaldata, 0, lastorignaldata.length);

        //生成光谱数据
        SPPacket spdata = spdataFactory.BuildSpectralDataPacket(data);

        //若采到数据，上报
        MEventCenter.CreateEventAsync(new SpectralDataPacket(
                control.spdevconfig.GetEquipmentInfo(),
                collectpar,
                spdata.spdata,
                spdata.IP));
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光谱数据修正">  
    private SpectralDataFactory spdataFactory;

    @Override
    public SpectralDataFactory GetSpBuildModel() {
        return this.spdataFactory;
    }
    // </editor-fold> 

}
