/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import lam.faultcenter.FaultCenter;
import lam.faultcenter.SYSLOG;
import nahon.absractio.AbstractIO;
import nahon.dev.search.NahonDevInfo;
import nahon.dev.search.NahonDevSearch;
import sps.system.SPDevSystem;

/**
 *
 * @author jiche
 */
public class SPDevManager {

    private ArrayList<NahonDevInfo> devlist = new ArrayList();

    public ArrayList<NahonDevInfo> GetDeviceList() {
        return this.devlist;
    }

    // <editor-fold defaultstate="collapsed" desc="搜索设备">  
    public String currentSearchIP = "";
    private final Lock devlistlock = new ReentrantLock();
    private int finishprocess = 0;//完成的进程
    private int totalprocess = 0; //总共需要的进程
    private final int MaxDevAddr = 0x1A;
    private boolean isStart = false;

    public void StartSearchIO(AbstractIO[] iolist, boolean block) {
        if (this.isStart()) {
            FaultCenter.getReporter().SendFaultReport(Level.INFO, "搜索进程已经开始，请勿重复启动");
            return;
        }
        this.isStart = true;

        //初始化变量
        this.devlist.clear();
        this.finishprocess = 0;
        this.totalprocess = iolist.length;

        SYSLOG.getLog().PrintLog(Level.INFO, "开始搜索设备，总共IO口有：" + this.totalprocess);
        //启动多进程跑
        ArrayList<Future> spro = new ArrayList();
        for (int i = 0; i < totalprocess; i++) {
            //每个IO对应一个进程
            spro.add(SPDevSystem.GetInstance().systemthreadpool.submit(
                    new SearchingProcess(iolist[i])));
        }

        //是否阻塞等待搜索结构
        if (block) {
            //等待所有端口搜索完毕
            for (Future ret : spro) {
                try {
                    ret.get(10, TimeUnit.SECONDS);
                } catch (Exception ex) {
                }
            }
        }
        SYSLOG.getLog().PrintLog(Level.INFO, "搜索完毕，总共搜索到设备：" + this.devlist.size());
        this.isStart = false;
    }

    private class SearchingProcess implements Runnable {

        private AbstractIO io;

        public SearchingProcess(AbstractIO io) {
            this.io = io;
        }

        @Override
        public void run() {
            //搜索从0-最大地址的所有可能的探头，
            ArrayList<NahonDevInfo> result = NahonDevSearch.SearchNDevs(io, (byte) 0, (byte) MaxDevAddr, 100);

            devlistlock.lock();
            try {
                //收集找到的设备
                devlist.addAll(result);
                //增加搜索完成的进度
                finishprocess++;
            } finally {
                devlistlock.unlock();
            }
        }

    }

    public int GetTotalProcess() {
        return totalprocess;
    }

    public int Getfinishprocess() {
        int ret = this.finishprocess;
        return ret;
    }

    public boolean isStart() {
        return this.isStart;
    }
    // </editor-fold>         
}
