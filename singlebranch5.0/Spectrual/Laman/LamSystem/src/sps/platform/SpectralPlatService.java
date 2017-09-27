/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import sps.app.manager.AppManager;
import sps.dev.control.inst.SPDevControl;
import sps.dev.manager.SPDevManager;
import sps.dev.control.ifs.ISPDevControl;
import sps.platform.log.SYSLOG;

/**
 *
 * @author jiche
 */
public class SpectralPlatService {

    private static SpectralPlatService instance;
    public static SpectralPlatService GetInstance() {
        if (instance == null) {
            instance = new SpectralPlatService();
        }
        return instance;
    }
    
    private int maxprocessnum = 200; //最大进程数
    private SpectralPlatService() {
        //初始化系统进程池
        this.systemthreadpool = Executors.newFixedThreadPool(maxprocessnum);
//        SYSLOG.getLog().SetLogPath("./log");
        //初始化设备管理器
        this.dev_manager = new SPDevManager();
        //初始化设备控制器
        
        this.dataStore = new AppManager();
    }
        
    private ExecutorService systemthreadpool;
    public ExecutorService GetThreadPools(){
        return this.systemthreadpool;
    }
    
    private SPDevManager dev_manager;
    public SPDevManager GetDevManager(){
        return this.dev_manager;
    }

    private AppManager dataStore;
    public AppManager GetAppManager() {        
        return this.dataStore;
    }
}
