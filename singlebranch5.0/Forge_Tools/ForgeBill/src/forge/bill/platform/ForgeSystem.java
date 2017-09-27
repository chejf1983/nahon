/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.platform;

import forge.bill.dev.NahonDevControl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import forge.bill.eia.builder.EIABuilder;

/**
 *
 * @author jiche
 */
public class ForgeSystem {

    public ExecutorService systemthreadpool;     //进程池
    public SystemConfig systemConfig;            //系统配置文件
    public EIABuilder eiaBuilder;                     //出厂信息数据库
    public NahonDevControl devControl;           //设备控制器

    private static ForgeSystem Instance;

    public static ForgeSystem GetInstance() {
        if (Instance == null) {
            Instance = new ForgeSystem();
        }
        return Instance;
    }

    private ForgeSystem() {
    }

    public void initSystem() throws Exception {
        //创建系统进程池（100）
        this.systemthreadpool = Executors.newFixedThreadPool(20);

        //初始化配置文件（200）
        this.systemConfig = new SystemConfig();
        this.systemConfig.initConfig();

        //出厂信息数据库（400）
        this.eiaBuilder = new EIABuilder();
        this.eiaBuilder.InitBuilder();

        //设备控制器（500）
        this.devControl = new NahonDevControl();
    }
}
