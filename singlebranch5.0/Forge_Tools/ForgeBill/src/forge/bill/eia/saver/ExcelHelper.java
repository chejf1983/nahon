/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.saver;

import forge.bill.eia.builder.SEiaRecord;
import forge.bill.platform.ForgeSystem;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelRead;
import nahon.comm.exl.ExcelWriter;

/**
 *
 * @author Administrator
 */
public class ExcelHelper implements IEiaDB {

    public String FileDir = "";

    public void InitConfig() {
        this.FileDir = ForgeSystem.GetInstance().systemConfig.GetExcelPath();
        if (!FileDir.endsWith("/")) {
            this.FileDir += "/";
        }
        File dir = new File(this.FileDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void SetConfig(String dirpath) {
        if (this.FileDir != dirpath) {
            if (!dirpath.endsWith("/")) {
                dirpath += "/";
            }
            ForgeSystem.GetInstance().systemConfig.SetExcelPath(dirpath);
            this.FileDir = dirpath;
        }
    }

    @Override
    public void Connect() throws Exception {

    }

    @Override
    public void DisConnectDB() throws Exception {

    }

    @Override
    public String readLastestSerialNum(String TableName) throws Exception {
        ExcelTable table = this.getTableFromExcel(TableName);

        if (table == null) {
            return null;
        }

        return table.GetRecord(table.getRowCount() - 1).devserialID;
    }

    @Override
    public SEiaRecord readRecord(String TableName, String devserialID) {
        ExcelTable table;
        try {
            table = this.getTableFromExcel(TableName);
        } catch (Exception ex) {
            return null;
        }

        if (table == null) {
            return null;
        }

        return table.GetRecord(devserialID);
    }

    @Override
    public void updateRecord(String TableName, String oldserial, SEiaRecord newrecord) throws Exception {
        ExcelTable table = this.getTableFromExcel(TableName);

        if (table == null) {
            throw new Exception("no data founde");
        }

        table.UpdateRecord(oldserial, newrecord);
        this.writeTable(TableName, table);
    }

    @Override
    public void deleteRecord(String TableName, String oldserial) throws Exception {
        ExcelTable table = this.getTableFromExcel(TableName);

        if (table == null) {
            throw new Exception("no data founde");
        }

        table.DeleteRecord(oldserial);
        this.writeTable(TableName, table);
    }

    @Override
    public void createTable(String TableName) throws Exception {
        File file = new File(convertToFileName(TableName));
        if (!file.exists()) {
            ExcelWriter writer = ExcelWriter.CreateExcel(convertToFileName(TableName));
            writer.CreateSheet(TableName, 0);
            writer.WriteTable(new ExcelTable(TableName));
            writer.Close();
        }
    }

    @Override
    public void addnewRecord(String TableName, SEiaRecord newrecord) throws Exception {
        ExcelTable table = this.getTableFromExcel(TableName);

        if (table == null) {
            throw new Exception("no data founde");
        }

        table.AddRecord(newrecord);
        this.writeTable(TableName, table);
    }

    private ExcelTable getTableFromExcel(String TableName) throws Exception {
        ExcelRead excel = ExcelRead.ReadExcel(convertToFileName(TableName));
        DefaultExcelTable[] table = excel.ReadExcelTable();
        if (table.length == 0) {
            return null;
        }
        excel.Close();

        return new ExcelTable(table[0]);
    }

    private void writeTable(String TableName, ExcelTable table) throws Exception {
        ExcelWriter writer = ExcelWriter.CreateExcel(convertToFileName(TableName));
        try {
            writer.CreateSheet(TableName, 0);
            writer.WriteTable(table);
        } finally {
            writer.Close();
        }
    }

    private String convertToFileName(String TableName) {
        return FileDir + TableName + ".xls";
    }

}
