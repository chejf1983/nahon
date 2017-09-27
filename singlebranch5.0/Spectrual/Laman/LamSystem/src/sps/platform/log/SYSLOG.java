/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.platform.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Administrator
 */
public class SYSLOG {

    public static String SyslogFile = "log.log";//系统日志文件名称
    private static SYSLOG instance;
    private int maxfilenum = 10;

    private SYSLOG() {

    }

    public void SetLogPath(String filepath) {
        this.CleanOldLog(filepath);
        this.CreateNewLog(filepath);
    }

    private void CleanOldLog(String filepath) {
        File dir = new File(filepath);
        //如果文件夹不存在，创建文件夹
        if (!dir.exists()) {
            try {
                dir.mkdir();
                dir = new File(filepath);
            } catch (Exception ex) {
            }
        }
        
        //查看log下旧的文件个数，超过最大值，就清除旧的文件
        int morefile = dir.listFiles().length - maxfilenum;

        for (File f : dir.listFiles()) {
            if (morefile >= 0) {
                morefile--;
                f.delete();
//            String logname = f.getName();
//            if(logname.startsWith("[")){
//                String time = logname.substring(1, logname.indexOf("]"));
//                try {
//                    Date logtime = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss").parse(time);
//                } catch (ParseException ex) {
////                    Logger.getLogger(SYSLOG.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.out.println(time);
//            }
            } else {
                break;
            }
        }
    }

    private void CreateNewLog(String filepath) {
        try {
            if (!filepath.endsWith("/")) {
                filepath += "/";
            }
            SyslogFile = filepath + new SimpleDateFormat("[yyyy_MM_dd HH_mm_ss]").format(new Date()) + SyslogFile;
            //修改默认LOG输出
            FileHandler fileHandler = new FileHandler(SyslogFile, 1024 * 1024 * 2, 1);//(2m)
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getGlobal().addHandler(fileHandler);
            this.PrintLog(Level.INFO, "开始记录LOG");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static SYSLOG getLog() {
        if (instance == null) {
            instance = new SYSLOG();
        }

        return instance;
    }

    public void PrintLog(Level level, String msg) {
        Logger.getGlobal().log(level, msg);
    }

    public void PrintLog(Level level, String msg, Object params[]) {
        Logger.getGlobal().log(level, msg, params);
    }
}
