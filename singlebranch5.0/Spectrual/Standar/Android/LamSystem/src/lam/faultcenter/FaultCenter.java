/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.faultcenter;

import java.util.logging.Level;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;

/**
 *
 * @author Administrator
 */
public class FaultCenter {

    private static FaultCenter instance;
    private EventCenter<Level> FaultEvent = new EventCenter();
    private FaultCenter(){    
    }
    
    public static FaultCenter getReporter(){
        if(instance == null){
            instance = new FaultCenter();
        }
        
        return instance;
    }   

    public void SendFaultReport(Level level, String faultinfo) {
        SYSLOG.getLog().PrintLog(level, faultinfo);
        FaultEvent.CreateEventAsync(level, faultinfo);        
    }
    
    public void RegisterFaultEvent(EventListener<Level> list){
        this.FaultEvent.RegeditListener(list);
    }
}
