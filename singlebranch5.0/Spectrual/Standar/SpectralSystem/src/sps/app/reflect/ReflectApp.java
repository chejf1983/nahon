/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.reflect;

import sps.app.absorbe.*;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelWriter;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.tool.languange.LanguageHelper;
import sps.dev.data.SSPData;

/**
 *
 * @author Administrator
 */
public class ReflectApp {

    // <editor-fold defaultstate="collapsed" desc="采集数据事件">     
    private final ReentrantLock controllock = new ReentrantLock();
    private SRefData currentdata;
    public static final int MaxTransmitRate = 300;
    public static final int MaxSnapShotNum = 8;

    public SRefData GetCurrentData() {
        return this.currentdata;
    }

    public void InputData(SSpectralDataPacket spdata) {
        //修改精度
        for (int i = 0; i < spdata.data.datavalue.length; i++) {
            spdata.data.datavalue[i] = new BigDecimal(spdata.data.datavalue[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        controllock.lock();
        try {
            SRefData refdata;
            if (!this.IsRefSetup()) {
                //如果参考光没有使能，只有原始光
                this.basedata = spdata;
                refdata = new SRefData(spdata, null);
            } else {
                //计算反射率
                double[] tmp = new double[spdata.data.datavalue.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (spdata.data.datavalue[i] == 0) {
                        tmp[i] = 4;
                    } else {
                        tmp[i] = spdata.data.datavalue[i] / basedata.data.datavalue[i];
                        tmp[i] = tmp[i] > MaxTransmitRate ? MaxTransmitRate : tmp[i];
                        tmp[i] = new BigDecimal(tmp[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                }
                refdata = new SRefData(spdata, new SSPData(spdata.data.waveIndex, tmp));
            }

            this.MEventCenter.CreateEvent(refdata);
        } finally {
            controllock.unlock();
        }
    }

    private EventCenter<SRefData> MEventCenter = new EventCenter();

    //注册数据响应监听函数
    public void RegisterDataCollectListener(EventListener<SRefData> listener) {
        this.MEventCenter.RegeditListener(listener);
    }

    public void UnRegisterDataCollectListener(EventListener<SRefData> listener) {
        this.MEventCenter.RemoveListenner(listener);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="参考光光控制"> 
    //参考光
    private SSpectralDataPacket basedata;

    public SSpectralDataPacket GetBaseData() {
        if (this.IsRefSetup()) {
            return this.basedata;
        } else {
            return null;
        }
    }

    //使能参考光
    private boolean isRefSet = false;

    public boolean IsRefSetup() {
        return this.isRefSet && this.basedata != null;
    }

    public void EnableRefLight(boolean value) {
        controllock.lock();
        try {
            this.isRefSet = value;
        } finally {
            controllock.unlock();
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="保存EXCEL"> 
    public void SaveToExcel(String filepath) throws Exception {
        if (!this.IsRefSetup()) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "没有数据");
            return;
        }
        ExcelWriter writer = ExcelWriter.CreateExcel(filepath);
        controllock.lock();
        try {
            writer.CreateSheet("Data", 0);
            //保存参考光
            DefaultExcelTable table = DefaultExcelTable.createTable(
                    LanguageHelper.getIntance().GetText("BaseData"),
                    new String[]{LanguageHelper.getIntance().GetText("Index"),
                        LanguageHelper.getIntance().GetText("Wave"),
                        LanguageHelper.getIntance().GetText("Value")});

            for (int i = 0; i < basedata.data.datavalue.length; i++) {
                table.AddRow(new Object[]{
                    basedata.data.pixelIndex[i],
                    basedata.data.waveIndex[i],
                    basedata.data.datavalue[i]});
            }

            writer.WriteTable(table, ExcelWriter.Direction.Horizontal);

            //保存测量数据
            table = DefaultExcelTable.createTable(
                    LanguageHelper.getIntance().GetText("Rate"),
                    new String[]{LanguageHelper.getIntance().GetText("Index"),
                        LanguageHelper.getIntance().GetText("Wave"),
                        LanguageHelper.getIntance().GetText("Value"),
                        LanguageHelper.getIntance().GetText("Rate")});

            for (int i = 0; i < basedata.data.datavalue.length; i++) {
                table.AddRow(new Object[]{
                    currentdata.original_data.data.pixelIndex[i],
                    currentdata.original_data.data.waveIndex[i],
                    currentdata.original_data.data.datavalue[i],
                    currentdata.absorbe_rate.datavalue[i]});
            }
            writer.WriteTable(table, ExcelWriter.Direction.Horizontal);

        } finally {
            controllock.unlock();
            writer.Close();
        }
    }
    // </editor-fold>   
}
