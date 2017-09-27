/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.manager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.faultsystem.FaultCenter;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.SpectralPlatService;

/**
 *
 * @author Administrator
 */
public class CollectControl {

    private AppManager appmanager;
    private ReentrantLock collectinglock = new ReentrantLock();
    
    public CollectControl(AppManager appmanger) {
        this.appmanager = appmanger;
    }

    private boolean iscollecting = false;

    public boolean IsStartCollect() {
        return this.iscollecting;
    }

    // <editor-fold defaultstate="collapsed" desc="连续采集数据"> 
    //连续采集数据
    public boolean StartSustainCollect(SSDataCollectPar suspar, final int window, final int inteveral) {
        try {
            //在连接状态下，开始监听
            SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().PrepareCollect(suspar);
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
                ISPDevControl control = SpectralPlatService.GetInstance().GetDevManager().GetDevControl();
                try {
                    while (iscollecting) {
                        //采集一次数据
                        SSpectralDataPacket data = control.GetDataCollecor().StartSingleTest(window);
                        //生成光谱数据
                        appmanager.InputData(data);

                        TimeUnit.MILLISECONDS.sleep(inteveral + 1);
                    }
                } catch (Exception ex) {
                    //如果不是取消异常，就认为失败
                    if (!control.GetDataCollecor().IsCanceled()) {
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                                "采集数据异常\r\n");
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
                iscollecting = false;
                control.GetDataCollecor().FinishCollect();
            }
        });

        return true;
    }

    //单次采集
    public boolean StartSingleTest(SSDataCollectPar singlepar, final int window) {
        //在连接状态下，开始测试
        try {
            //在连接状态下，开始监听
            SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().PrepareCollect(singlepar);
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
                ISPDevControl control = SpectralPlatService.GetInstance().GetDevManager().GetDevControl();
                try {
                    //采集一次数据
                    SSpectralDataPacket data = control.GetDataCollecor().StartSingleTest(window);
                    //生成光谱数据
                    appmanager.InputData(data);
                } catch (Exception ex) {
                    //如果不是取消异常，就认为失败
                    if (!control.GetDataCollecor().IsCanceled()) {
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                                "采集数据异常\r\n");
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
                iscollecting = false;
                control.GetDataCollecor().FinishCollect();
            }
        });

        return true;
    }

    //停止采集
    public void StopCollectData() {
        if (!this.iscollecting) {
            return;
        }else{
            this.iscollecting = false;
        }

        //在采集状态下，打断一次采集，来终端采集过程
        try {
            SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().CancelCollectData();
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "停止设备采集失败！请重启设备");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
}
