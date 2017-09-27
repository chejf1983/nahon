/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.raman;

import java.math.BigDecimal;
import java.util.ArrayList;
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
public class RamanApp {

    // <editor-fold defaultstate="collapsed" desc="采集数据事件">  
    private SSpectralDataPacket currentdata;

    public SSpectralDataPacket GetCurrentData() {
        return this.currentdata;
    }

    public void InputData(SSpectralDataPacket spdata) {
        for(int i = 0; i < spdata.data.datavalue.length; i++){
            spdata.data.datavalue[i] = new BigDecimal(spdata.data.datavalue[i]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
//        
//        this.snapshots.clear();
//        SSpectralDataPacket tmp = new SSpectralDataPacket(spdata);
//        double[] wave = new double [spdata.data.datavalue.length - 100];
//        double[] value = new double [spdata.data.datavalue.length - 100];
//        for(int i = 100; i < spdata.data.datavalue.length; i++){
//            wave[i - 100] = spdata.data.waveIndex[i];
//            value[i - 100] = spdata.data.datavalue[i];
//        }
//        for(int i = 0; i < 100; i++){
//            tmp.data.datavalue[i] = Newton.predict(wave, value, 50, spdata.data.waveIndex[i]);
//        }
//        this.snapshots.add(tmp);
//        
        this.currentdata = spdata;
        this.MEventCenter.CreateEvent(spdata);
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
        if (this.currentdata != null) {
            if (this.snapshots.size() < MaxSnapShot) {
                this.snapshots.add(currentdata);
            }
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
        DefaultExcelTable table = DefaultExcelTable.createTable(name, new String[]{"Index", "Wave", "Value"});

        for (int i = 0; i < data.data.datavalue.length; i++) {
            table.AddRow(new Object[]{data.data.pixelIndex[i], data.data.waveIndex[i], data.data.datavalue[i]});
        }

        return table;
    }

    public void SaveToExcel(String filepath) throws Exception {
        ExcelWriter writer = ExcelWriter.CreateExcel(filepath);
        
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
            writer.Close();
        }
    }
    // </editor-fold>   
}
