/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import sps.dev.data.SSPDevInfo;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import sps.dev.control.ifs.ISPDataCollect;
import sps.dev.control.ifs.ISPDevConfig;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.ifs.ISPDevLightControl;
import sps.dev.drv.ISPDevice;

/**
 *
 * @author Administrator
 */
public class SPDevControl implements ISPDevControl {

    public SPDevControl() {
    }

    // <editor-fold defaultstate="collapsed" desc="设备资源控制"> 
    private ISPDevice spdevice;

    public ISPDevice GetDevice() {
        return this.spdevice;
    }

    private final ReentrantLock controllock = new ReentrantLock();

    public void LockDevice() throws Exception {
        controllock.lock();
        try {
            switch (this.GetControlState()) {
                case CONNECT:
                    this.spdevice.Open();
                    this.STATE = CSTATE.BUSSY;
                    this.StateChange.CreateEventAsync(CSTATE.BUSSY, CSTATE.CONNECT);
                    break;
                case BUSSY:
                    throw new Exception("设备正忙，无法设置光谱数据!");
                case CLOSE:
                    throw new Exception("没有连接设备!");
                default:
                    throw new Exception("异常状态!");
            }
        } finally {
            this.controllock.unlock();
        }
    }

    public void UnlockDevice() {
        if (this.GetControlState() == CSTATE.BUSSY) {
            this.controllock.lock();
            try {
                if (this.GetControlState() == CSTATE.BUSSY) {
                    this.spdevice.Close();
                    this.STATE = CSTATE.CONNECT;
                    this.StateChange.CreateEventAsync(CSTATE.CONNECT, CSTATE.BUSSY);
                }
            } finally {
                this.controllock.unlock();
            }
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="设备加载控制"> 
    @Override
    public boolean Open(ISPDevice spdevice) {
        try {
            this.controllock.lock();
            this.Close();

            this.spdevice = spdevice;
            this.spdevice.Open();

            this.spdevconfig = new SPDevConfig(this);
            this.spdevconfig.Init();

            this.datacollector = new SPDataCollect(this);
            this.datacollector.Init();

            this.splightcontrol = new SPLightControl(this);
            this.splightcontrol.Init();

            this.STATE = CSTATE.CONNECT;
            this.StateChange.CreateEventAsync(CSTATE.CONNECT, CSTATE.CLOSE);
            return true;
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "连接设备失败 \r\n");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        } finally {
            this.controllock.unlock();
            this.spdevice.Close();
        }

        return false;
    }

    @Override
    public void Close() {
        try {
            this.controllock.lock();
            if (this.GetControlState() != CSTATE.CLOSE) {
                this.STATE = CSTATE.CLOSE;
                this.spdevice = null;
                this.StateChange.CreateEventAsync(CSTATE.CLOSE, CSTATE.CONNECT);
            }
        } finally {
            this.controllock.unlock();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="网络连接信息"> 
    @Override
    public SSPDevInfo GetDevConnectInfo() {
        if (this.GetDevice() != null) {
            return this.GetDevice().GetDevInfo();
        } else {
            return null;
        }
    }
    // </editor-fold> 
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="设备控制"> 
    public SPDevConfig spdevconfig;

    @Override
    public ISPDevConfig GetSPDevConfig() {
        return this.spdevconfig;
    }
    public SPDataCollect datacollector;

    @Override
    public ISPDataCollect GetDataCollecor() {
        return this.datacollector;
    }

    public SPLightControl splightcontrol;

    @Override
    public ISPDevLightControl GetLightControl() {
        return this.splightcontrol;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="控制器状态改变事件">  
    private CSTATE STATE = CSTATE.CLOSE;
    public EventCenter<CSTATE> StateChange = new EventCenter();

    @Override
    public CSTATE GetControlState() {
        return this.STATE;
    }

    @Override
    public void RegisterStateChange(EventListener<CSTATE> list) {
        StateChange.RegeditListener(list);
    }
    // </editor-fold>

}
