/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.faultsystem;

import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;

/**
 *
 * @author Administrator
 */
public class FaultCenter {

    private static FaultCenter instance;
    private EventCenter<Level> FaultEventCenter = new EventCenter();

    private FaultCenter() {
    }

    public static FaultCenter Instance() {
        if (instance == null) {
            instance = new FaultCenter();
        }

        return instance;
    }

    public void SendFaultReport(Level level, String info) {
        Logger.getGlobal().log(Level.SEVERE, info);
        FaultEventCenter.CreateEventAsync(level, info);
    }
        
    public void SendFaultReport(Level level, Exception ex) {
        Logger.getGlobal().log(Level.SEVERE, null, ex);
        FaultEventCenter.CreateEventAsync(level, ex.getMessage());
    }
    
    public void RegisterEvent(EventListener<Level> list){
        this.FaultEventCenter.RegeditListener(list);
    }
    
    public void UnRegisterEvent(EventListener<Level> list){
        this.FaultEventCenter.RemoveListenner(list);
    }
}
