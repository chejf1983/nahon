/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.ifs;

import nahon.comm.event.EventListener;
import nahon.dev.search.NahonDevInfo;

/**
 *
 * @author Administrator
 */
public interface ISPDevControl {

    public enum CSTATE {
        CLOSE,
        DISCONNECT,
        CONNECT,
        COLLECT
    }

    public boolean Open(NahonDevInfo spdev);

    public boolean Delete();

    public ISPDevConfig GetSPDevConfig();

    public ISPDataCollect GetDataCollecor();

    public CSTATE GetControlState();

    public void RegisterStateChange(EventListener<CSTATE> list);
}
