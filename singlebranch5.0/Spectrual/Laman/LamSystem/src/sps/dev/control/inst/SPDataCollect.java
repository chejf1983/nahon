/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.filter.BattwoseFilter;
import sps.dev.control.bill.DarkDataFilter;
import sps.dev.control.bill.LinearCal;
import sps.dev.control.bill.SPDataBuilder;
import sps.dev.control.bill.SmoothFilter;
import sps.dev.control.bill.WindowSmoothFilter;
import sps.dev.control.ifs.ISPDataCollect;
import sps.dev.control.ifs.ISPDataReceive;
import sps.dev.control.ifs.ISPDevControl.CSTATE;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSpectralPar;
import sps.dev.manager.ISPDevDriver;
import sps.platform.SpectralPlatService;

/**
 *
 * @author Administrator
 */
public class SPDataCollect implements ISPDataCollect {

    private final SPDevControl control;
    //初始化数据生成器
    private SPDataBuilder builder = new SPDataBuilder();
    //初始化巴特沃斯滤波器
    private BattwoseFilter batfilter = new BattwoseFilter();
    //初始化暗电流滤波
    private DarkDataFilter dkfilter = new DarkDataFilter();
    //初始化滑动平均
    private WindowSmoothFilter windowfilter = new WindowSmoothFilter();
    //初始化平滑计算器
    private SmoothFilter smoothfilter = new SmoothFilter();
    private LinearCal linearCal;

    public SPDataCollect(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        //初始化巴特沃斯滤波器        
        SSpectralPar sppar = this.control.spdevconfig.sppar;
        this.batfilter.InitBattwoseWindow(sppar.nodeNumber, sppar.nodeNumber / 10, 16);
    }

    public void Close() {
        if (this.iscollecting) {
            this.StopCollectData();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="暗电流控制"> 
    @Override
    public boolean IsDKEnable() {
        return this.dkfilter.IsDKEnable();
    }

    @Override
    public void SetDkEnable(boolean value) {
        this.dkfilter.SetDkEnable(value);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="数据采集"> 
    private boolean iscollecting = false;

    @Override
    public boolean IsStartCollect() {
        return this.iscollecting;
    }

    // <editor-fold defaultstate="collapsed" desc="连续采集数据"> 
    @Override
    public boolean StartSustainCollect(final ISPDataReceive receive, final SSDataCollectPar suspar, final int window, final int inteveral) {
        try {
            //在连接状态下，开始监听
            this.control.LockDevice();
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            return false;
        }

        this.iscollecting = true;

        //启动连续进程
        SpectralPlatService.GetInstance().GetThreadPools().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    while (control.GetControlState() == CSTATE.BUSSY) {
                        //采集一次数据
                        SingleTestImpl(receive, suspar, window);
                        //生成光谱数据
//                        SPDevSystem.GetInstance().GetDataStore().InputData(data);

                        TimeUnit.MILLISECONDS.sleep(inteveral + 1);
                    }
                } catch (Exception ex) {
                    //如果不是取消异常，就认为失败
                    if (!control.GetDevice().IsCmdCancled()) {
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                                "采集数据异常\r\n");
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
                iscollecting = false;
                control.UnlockDevice();
            }
        });

        return true;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="单次采集">  
    @Override
    public boolean StartSingleTest(final ISPDataReceive receive, final SSDataCollectPar singlepar, final int window) {
        //在连接状态下，开始测试
        try {
            //在连接状态下，开始监听
            this.control.LockDevice();
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            return false;
        }
        this.iscollecting = true;
        //启动连续进程
        SpectralPlatService.GetInstance().GetThreadPools().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //采集一次数据
                    SingleTestImpl(receive, singlepar, window);
                    //生成光谱数据
//                    SPDevSystem.GetInstance().GetDataStore().InputData(data);
                } catch (Exception ex) {
                    //如果不是取消异常，就认为失败
                    if (!control.GetDevice().IsCmdCancled()) {
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                                "采集数据异常\r\n");
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
                iscollecting = false;
                control.UnlockDevice();
            }
        });

        return true;
    }
    // </editor-fold>    

    //停止采集数据
    @Override
    public void StopCollectData() {
        //在采集状态下，停止采集
        if (this.iscollecting) {
            control.UnlockDevice();
            //在采集状态下，每50ms打断一次采集，来终端采集过程
            while (this.iscollecting) {
                try {
                    this.control.GetDevice().StopCollectData();
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (Exception ex) {
                    FaultCenter.Instance().SendFaultReport(Level.SEVERE, "停止设备采集失败！请重启设备");
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                    break;
                }
            }
        }
    }

    private void SingleTestImpl(ISPDataReceive receive, SSDataCollectPar singlepar, int window) throws Exception {
        ISPDevDriver dev = this.control.GetDevice();
        //采集原始AD
        double[] oradata = dev.CollectData(singlepar, this.control.GetSPDevConfig().GetSpectralPar().nodeNumber);
        //生成光谱数据
        SSpectralDataPacket spdata = this.builder.BuildSPData(oradata, this.control.GetSPDevConfig().GetWaveParameter());

        //暗电流修正
        if (!this.IsDKEnable()) {
            //关闭暗电流，自动更新暗电流
            this.dkfilter.UpdateDKData(oradata);
        } else {
            this.dkfilter.Filter(spdata.data.datavalue);
        }

        //非线性校准
        if (this.control.spdevconfig.linearpar.SynType != SSLinearParameter.NonLinearPar) {
            this.linearCal.LinearCalibrate(spdata.data, this.control.spdevconfig.linearpar);
        }

        //巴特沃斯滤波
        if (this.batfilter.IsBatFilterEnable()) {
            spdata.data.datavalue = this.batfilter.Filter(spdata.data.datavalue);
        }

        //滑动窗口
        if (window > 1) {
            this.windowfilter.Filter(spdata.data.datavalue, window);
        }

        //最小二项式平滑
        if (this.smoothfilter.IsSmoothEnable()) {
            this.smoothfilter.Filter(spdata.data.pixelIndex, spdata.data.datavalue);
        }

        //发送数据
        receive.ReceiveData(spdata);
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="电流电压控制"> 
    @Override
    public float GetBatteryPower() throws Exception {
        this.control.LockDevice();
        try {
            return this.control.GetDevice().GetBatteryPower();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            throw new Exception("获取电压失败!");
        } finally {
            this.control.UnlockDevice();
        }
    }

    @Override
    public float GetTemperature() throws Exception {
        this.control.LockDevice();
        try {
            return this.control.GetDevice().GetTemperature();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            throw new Exception("获取电流失败!");
        } finally {
            this.control.UnlockDevice();
        }
    }
    // </editor-fold>  
}
