/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.inst;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import lam.devcontrol.ifs.ISPDataCollect;
import lam.devcontrol.ifs.ISPDevConfig;
import lam.devcontrol.ifs.ISPDevControl;
import lam.faultcenter.FaultCenter;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.dev.search.NahonDevInfo;
import spdev.dev.ifs.ISpectralDevice;
import spdev.dev.search.SPDevFactory;

/**
 *
 * @author Administrator
 */
public class SPDevControl implements ISPDevControl {

    public ISpectralDevice spdev;
    public final ReentrantLock controllock = new ReentrantLock();
    private SPKeepAlive keepalive;

    @Override
    public boolean Open(NahonDevInfo spdevinfo) {
        if (this.Delete()) {
            try {
                this.spdev = SPDevFactory.BuildNahonSPDev(spdevinfo);
                this.controllock.lock();
                this.spdev.Connect();
                this.spdevconfig = new SPDevConfig(this);
                this.spdevconfig.Init();

                this.datacollector = new SPDataCollect(this);
                this.datacollector.Init();

                this.keepalive = new SPKeepAlive(this);
//                this.keepalive.StartKeepAlive();
                
                this.ChangeState(CSTATE.CONNECT);
                return true;
            } catch (Exception ex) {
                FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "连接设备失败 \r\n" + ex.toString());
            } finally {
                this.spdev.DisConnect();
                this.controllock.unlock();
            }
        }
        return false;
    }

    @Override
    public boolean Delete() {
        if (this.GetControlState() == CSTATE.CLOSE) {
            return true;
        }

        if (this.GetControlState() == CSTATE.CONNECT || this.GetControlState() == CSTATE.DISCONNECT) {
//            this.keepalive.StopKeepAlive();
            this.keepalive = null;
            
            this.spdevconfig = null;
            this.datacollector = null;
            this.spdev = null;
            this.ChangeState(CSTATE.CLOSE);
            return true;
        }

        FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "设备忙，无法关闭设备，当前状态:" + this.GetControlState());
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="获取设备参数设备控制"> 
    public SPDevConfig spdevconfig;

    @Override
    public ISPDevConfig GetSPDevConfig() {
        return this.spdevconfig;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="获取设备采集控制"> 
    public SPDataCollect datacollector;

    @Override
    public ISPDataCollect GetDataCollecor() {
        return this.datacollector;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="控制器状态改变事件">  
    private CSTATE STATE = CSTATE.CLOSE;
    public EventCenter<CSTATE> StateChange = new EventCenter();

    @Override
    public CSTATE GetControlState() {
        return this.STATE;
    }

    public void ChangeState(CSTATE state) {
        this.STATE = state;
        this.StateChange.CreateEventAsync(state);
    }

    @Override
    public void RegisterStateChange(EventListener<CSTATE> list) {
        StateChange.RegeditListener(list);
    }
    // </editor-fold> 

}
