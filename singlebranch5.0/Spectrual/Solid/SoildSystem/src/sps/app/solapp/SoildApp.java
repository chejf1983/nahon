/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.solapp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import nahon.comm.event.EventCenter;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelWriter;
import sps.app.solapp.data.NPKData;
import sps.app.solapp.data.SolTestData;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class SoildApp {

    private CollectControl control;

    public SoildApp(CollectControl obsbill) {
        this.control = obsbill;
    }

    // <editor-fold defaultstate="collapsed" desc="数据处理">     
    private final ReentrantLock controllock = new ReentrantLock();
    public EventCenter<NPKData> DataCenter = new EventCenter();
    private ArrayList<NPKData> database = new ArrayList();
    private int maxdatalen = 10000;

    public void InputData(SSpectralDataPacket data) {
        controllock.lock();
        try {
            SolTestData testdata = this.control.GetObsBill().BuildTestData(data);
            if (testdata != null) {
                NPKData ret = new NPKData();
                ret.oradata = testdata;

                ObsBill calbill = this.control.GetObsBill();
                if (calbill.GetBaseN0() == 0) {
                    ret.Ndata = 0;
                } else {
                    ret.Ndata = testdata.Nd / calbill.GetBaseN0() * 48;
                    ret.Ndata = new BigDecimal(ret.Ndata).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }

                if (calbill.GetBaseP0() == 0) {
                    ret.Pdata = 0;
                } else {
                    ret.Pdata = testdata.Pd / calbill.GetBaseP0() * 48;
                    ret.Pdata = new BigDecimal(ret.Ndata).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }

                if (calbill.GetBaseN0() == 0) {
                    ret.Kdata = 0;
                } else {
                    ret.Kdata = testdata.Kd / calbill.GetBaseK0() * 200;
                    ret.Kdata = new BigDecimal(ret.Ndata).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                UpdateData(ret);
            }
        } finally {
            controllock.unlock();
        }
    }

    private void UpdateData(NPKData data) {
        if (database.size() > this.maxdatalen) {
            database.remove(0);
        }

        database.add(data);
        DataCenter.CreateEvent(data);
    }

    public void SaveAs(String filepath) throws Exception {
        ExcelWriter writer = ExcelWriter.CreateExcel(filepath);
        controllock.lock();
        try {
            writer.CreateSheet("Data", 0);

            DefaultExcelTable table = DefaultExcelTable.createTable("测试数据",
                    new String[]{"时间", "氮吸光度", "氮含量", "磷吸光度", "磷含量", "钾吸光度", "钾含量"});

            for (int i = 0; i < database.size(); i++) {
                NPKData pkt = database.get(i);
                table.AddRow(new Object[]{new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(pkt.oradata.original_data.time),
                    pkt.oradata.Nd + "", pkt.Ndata + "mg/kg",
                    pkt.oradata.Pd + "", pkt.Ndata + "mg/kg",
                    pkt.oradata.Kd + "", pkt.Ndata + "mg/kg"
                });
            }
            writer.WriteTable(table, ExcelWriter.Direction.Horizontal);
        } finally {
            controllock.unlock();
            writer.Close();
        }
    }
    // </editor-fold>
}
