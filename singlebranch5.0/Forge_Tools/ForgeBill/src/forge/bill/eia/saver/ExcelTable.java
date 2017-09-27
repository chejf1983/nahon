/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.saver;

import forge.bill.eia.builder.SEiaRecord;
import java.util.ArrayList;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.AbstractExcelTable;

/**
 *
 * @author Administrator
 */
public class ExcelTable implements AbstractExcelTable {
    
    public String tableName;
    protected ArrayList<SEiaRecord> data = new ArrayList();
    
    public ExcelTable(String name) {
        this.tableName = name;
    }
    
    public ExcelTable(DefaultExcelTable excel) {
        this.tableName = excel.tableName;
        if (excel.getColumnCount() == SEiaRecord.names.length) {
            for (int i = 0; i < excel.getRowCount(); i++) {
                data.add(new SEiaRecord(excel.getValueAt(i, 0).toString(),
                        excel.getValueAt(i, 1).toString(),
                        excel.getValueAt(i, 2).toString(),
                        excel.getValueAt(i, 3).toString(),
                        excel.getValueAt(i, 4).toString()));
            }
        }
    }
    
    @Override
    public String getTableName() {
        return this.tableName;
    }
    
    @Override
    public String getColumnName(int column) {
        return SEiaRecord.names[column];
    }
    
    @Override
    public int getColumnCount() {
        return SEiaRecord.names.length;
    }
    
    @Override
    public int getRowCount() {
        return this.data.size();
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        SEiaRecord rec = data.get(row);
        switch (column) {
            case 0:
                return rec.devname;
            case 1:
                return rec.devserialID;
            case 2:
                return rec.company;
            case 3:
                return rec.info2;
            case 4:
                return rec.info3;
        }
        
        return null;
    }
    
    public void AddRecord(SEiaRecord rec) {
        this.data.add(rec);
    }
    
    public void DeleteRecord(String oldserial) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).devserialID.contentEquals(oldserial)) {
                data.remove(i);
                return;
            }
        }
    }
    
    public void UpdateRecord(String oldserial, SEiaRecord newrecord) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).devserialID.contentEquals(oldserial)) {
                data.get(i).update(newrecord);
                return;
            }
        }
    }
    
    public SEiaRecord GetRecord(String devserialID) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).devserialID.contentEquals(devserialID)) {
                return data.get(i);
            }
        }
        return null;
    }
    
    public SEiaRecord GetRecord(int index) {
        return this.data.get(index);
    }
    
}
