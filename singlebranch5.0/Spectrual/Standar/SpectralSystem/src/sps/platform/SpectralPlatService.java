/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import sps.app.manager.AppManager;
import sps.dev.manager.SPDevManager;

/**
 *
 * @author jiche
 */
public class SpectralPlatService {

    private static SpectralPlatService Instance = new SpectralPlatService();

    private SpectralPlatService() {
    }

    public static SpectralPlatService GetInstance() {
        return Instance;
    }

    public void InitPlatForm() throws Exception{
        //初始化进程池
        this.systemthreadpool = Executors.newFixedThreadPool(200);
        
        //初始化系统配置
        this.config = new SystemConfig();
        config.ReadFromFile();
   
        //初始化LOG路径
        SYSLOG.getLog().SetLogPath("./log");
        
        //初始化设备管理器
        this.devmanager = new SPDevManager();
        
        //初始化控制管理器
        this.controlmanager = new AppManager();
    }
    
    private ExecutorService systemthreadpool;
    public ExecutorService GetThreadPools(){
        return this.systemthreadpool;
    }

    private SystemConfig config;
    public SystemConfig GetConfig(){
        return this.config;
    }
    
    private SPDevManager devmanager;
    public SPDevManager GetDevManager(){
        return this.devmanager;
    }  
    
    private AppManager controlmanager;
    public AppManager GetAppManager(){
        return this.controlmanager;
    }

    public void Close() {
        config.SaveToFile();
    }
}
