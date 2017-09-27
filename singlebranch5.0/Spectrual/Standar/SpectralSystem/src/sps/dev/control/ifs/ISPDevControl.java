/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.SSPDevInfo;
import nahon.comm.event.EventListener;
import sps.dev.drv.ISPDevice;

/**
 *
 * @author Administrator
 */
public interface ISPDevControl {

    // <editor-fold defaultstate="collapsed" desc="控制开关"> 
    public boolean Open(ISPDevice spdevice);

    public void Close();

    //获取设备连接信息
    public SSPDevInfo GetDevConnectInfo();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="设备控制"> 
    public ISPDevConfig GetSPDevConfig();

    public ISPDataCollect GetDataCollecor();

    public ISPDevLightControl GetLightControl();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="控制器状态"> 
    public enum CSTATE {
        CLOSE,
        CONNECT,
        BUSSY
    }

    public CSTATE GetControlState();

    public void RegisterStateChange(EventListener<CSTATE> list);
    // </editor-fold> 
}
