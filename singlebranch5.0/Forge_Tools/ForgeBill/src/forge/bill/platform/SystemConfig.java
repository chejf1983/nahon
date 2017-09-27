/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.drv.absractio.IOInfo;

/**
 *
 * @author jiche
 */
public class SystemConfig {

    // <editor-fold defaultstate="collapsed" desc="配置文件设置"> 
    private static String ConfigFile = "SystemConfig.xml"; //文件名
    private final Properties Config;
    private static String ConfigPath = "./DevConfig";      //文件夹

    public SystemConfig() {
        //初始化系统配置
        this.Config = new Properties();
    }

    //初始化，配置文件
    public void initConfig() throws Exception {
        //创建系统配置文件目录
        File f = new File(ConfigPath);

        // 创建文件夹
        if (!f.exists()) {
            f.mkdirs();
        }

        this.readFromFile();
    }

    //从文件读取配置信息
    private void readFromFile() throws Exception {
        //初始化配置文件(文件夹+文件名）
        File file = new File(ConfigPath + "/" + ConfigFile);
        if (!file.exists()) {
            file.createNewFile();
            new Properties().storeToXML(new FileOutputStream(file), "");
//            file = new File(ConfigPath + "/" + ConfigFile);
        }
        //加载配置内容
        this.Config.loadFromXML(new FileInputStream(file));
    }

    //保存到文件
    public void SaveToFile() {
        //保存配置文件(文件夹+文件名）
        File file = new File(ConfigPath + "/" + ConfigFile);
        try {
            this.Config.storeToXML(new FileOutputStream(file), "");
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    //获取系统配置文件路径
    public String getSystemConfigPath() {
        return ConfigPath;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="配置变量">
    //内部版本标志
    private boolean isinternal = true;

    public boolean IsInternal() {
        return this.isinternal;
    }

    public void SetInternalFlag(boolean value) {
        this.isinternal = value;
    }

    //从配置文件中读取默认io信息
    public IOInfo GetDefaultIO() {
        //如果没有IO类型，表示没有保存任何信息，返回空
        String iotype = this.Config.getProperty("netType", "");
        if (iotype.equals("")) {
            return null;
        }

        //读取IO参数
        ArrayList<String> pars = new ArrayList();
        for (int i = 0; i < 10; i++) {
            String tmp = this.Config.getProperty("par" + i, "");
            if (tmp.equals("")) {
                break;
            } else {
                pars.add(tmp);
            }
        }

        //返回IO信息
        IOInfo par = new IOInfo(iotype, pars.toArray(new String[0]));
        return par;
    }

    //保存默认io信息到配置文件
    public void SaveDefaultIO(IOInfo par) {
        if (par == null) {
            //保存IO类型
            Config.setProperty("netType", "");
            return;
        }

        //保存IO类型
        Config.setProperty("netType", par.iotype);
        //保存IO参数
        for (int i = 0; i < par.par.length; i++) {
            Config.setProperty("par" + i, par.par[i].toString());
        }
        //写配置文件
        this.SaveToFile();

    }

    //设备地址
    public byte GetAddr() {
        return Byte.valueOf(this.Config.getProperty("devaddr", "0"));
    }
    
    public void SaveAddr(byte addr) {
        //保存IO类型
        Config.setProperty("devaddr", String.valueOf(addr));
        //写配置文件
        this.SaveToFile();
    }

    //EIA备注记录是否用本地DB服务器
    public boolean IsLocalEiaDB(){
        return Boolean.valueOf(this.Config.getProperty("LOCALENABLE", "true"));
    }
    
    public void SetLocalEiaDB(boolean value){
        this.Config.setProperty("LOCALENABLE", String.valueOf(value));
        this.SaveToFile();
    }
    
    public String GetExcelPath(){
        return this.Config.getProperty("DIRPATH", "./information");
    }
    
    public void SetExcelPath(String dirpath){
        this.Config.setProperty("DIRPATH", dirpath);
        this.SaveToFile();
    }
    
    public String[] GetServerDBPar(){
        String[] par = new String[3];
        par[0] = ForgeSystem.GetInstance().systemConfig.Config.getProperty("SERVERDB", "jdbc:mysql://192.168.1.110/nahon?characterEncoding=utf8");
        par[1] = ForgeSystem.GetInstance().systemConfig.Config.getProperty("SERVERUSER", "nahon");
        par[2] = ForgeSystem.GetInstance().systemConfig.Config.getProperty("SERVERPASS", "nahon");
        return par;
    }
    
    public void SetServeDBPar(String url, String user, String password){
        this.Config.setProperty("SERVERDB", url);
        this.Config.setProperty("SERVERUSER", user);
        this.Config.setProperty("SERVERPASS", password);
        this.SaveToFile();
    }
    // </editor-fold> 
}
