/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lam.devcontrol.inst.SPDevControl;
import sps.dev.manager.SPDevManager;

/**
 *
 * @author jiche
 */
public class SPDevSystem {

    private static SPDevSystem instance;
    private int maxprocessnum = 200;

    private SPDevSystem() {
        this.systemthreadpool = Executors.newFixedThreadPool(maxprocessnum);
        this.dev_manager = new SPDevManager();
        this.spdevcontrol = new SPDevControl();
        
    }
    
    
  
    public static SPDevSystem GetInstance() {
        if (instance == null) {
            instance = new SPDevSystem();
        }
        return instance;
    }

    public final ExecutorService systemthreadpool;
    public final SPDevManager dev_manager;
    public final SPDevControl spdevcontrol;
}
