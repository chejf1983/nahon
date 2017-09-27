/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.ora;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import sps.dev.data.SSpectralDataPacket;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.comm.exl.AbstractExcelTable;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelWriter;
import nahon.comm.tool.languange.LanguageHelper;

/**
 *
 * @author Administrator
 */
public class CommonApp {

    // <editor-fold defaultstate="collapsed" desc="采集数据事件">      
    private final ReentrantLock controllock = new ReentrantLock();
    private SSpectralDataPacket currentdata;

    public SSpectralDataPacket GetCurrentData() {
        return this.currentdata;
    }

    public void InputData(SSpectralDataPacket spdata) {
        controllock.lock();
        try {
            for (int i = 0; i < spdata.data.datavalue.length; i++) {
                spdata.data.datavalue[i] = new BigDecimal(spdata.data.datavalue[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            this.currentdata = spdata;
            this.MEventCenter.CreateEvent(spdata);
        } finally {
            controllock.unlock();
        }
    }

    private EventCenter<SSpectralDataPacket> MEventCenter = new EventCenter();

    //注册数据响应监听函数
    public void RegisterDataCollectListener(EventListener<SSpectralDataPacket> listener) {
        this.MEventCenter.RegeditListener(listener);
    }

    public void UnRegisterDataCollectListener(EventListener<SSpectralDataPacket> listener) {
        this.MEventCenter.RemoveListenner(listener);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="快照">  
    private ArrayList<SSpectralDataPacket> snapshots = new ArrayList();

    private int MaxSnapShot = 10;

    public void AddSnapShot() {
        controllock.lock();
        try {
            if (this.currentdata != null) {
                if (this.snapshots.size() < MaxSnapShot) {
                    this.snapshots.add(currentdata);
                }
            }
        } finally {
            controllock.unlock();
        }
    }

    public void DelSnapShot() {
        if (!this.snapshots.isEmpty()) {
            this.snapshots.remove(this.snapshots.size() - 1);
        }
    }

    public SSpectralDataPacket[] GetSnapShots() {
        return this.snapshots.toArray(new SSpectralDataPacket[0]);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="保存EXCEL"> 
    private AbstractExcelTable ConvertToExcel(String name, SSpectralDataPacket data) {
        DefaultExcelTable table = DefaultExcelTable.createTable(name,
                new String[]{
                    LanguageHelper.getIntance().GetText("Index"),
                    LanguageHelper.getIntance().GetText("Wave"),
                    LanguageHelper.getIntance().GetText("Value")});

        for (int i = 0; i < data.data.datavalue.length; i++) {
            table.AddRow(new Object[]{data.data.pixelIndex[i], data.data.waveIndex[i], data.data.datavalue[i]});
        }

        return table;
    }

    public void SaveToExcel(String filepath) throws Exception {
        ExcelWriter writer = ExcelWriter.CreateExcel(filepath);
        controllock.lock();
        try {
            writer.CreateSheet("Data", 0);

            if (this.currentdata != null) {
                writer.WriteTable(ConvertToExcel(LanguageHelper.getIntance().GetText("CurrentData"), this.currentdata), ExcelWriter.Direction.Horizontal);
            }

            if (!this.snapshots.isEmpty()) {
                for (int i = 0; i < this.snapshots.size(); i++) {
                    writer.WriteTable(ConvertToExcel(LanguageHelper.getIntance().GetText("Snap") + "-" + i, this.snapshots.get(i)), ExcelWriter.Direction.Horizontal);
                }
            }
        } finally {
            controllock.unlock();
            writer.Close();
        }
    }
    // </editor-fold>   

    //计算RMS
    public double GetRMS() {
        if (this.currentdata != null) {
            double dataRMS = 0.0f;
            double dataAve = 0.0f;

            int length = currentdata.data.datavalue.length;

            for (int i = 0; i < length; i++) {
                dataAve += currentdata.data.datavalue[i];
            }

            dataAve = dataAve / length;

            for (int i = 0; i < length; i++) {
                dataRMS += (currentdata.data.datavalue[i] - dataAve) * (currentdata.data.datavalue[i] - dataAve) / length;
            }

            dataRMS = Math.sqrt(dataRMS);
            return dataRMS;
        }
        return Double.NaN;
    }

}
