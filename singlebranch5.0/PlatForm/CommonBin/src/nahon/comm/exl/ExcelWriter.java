/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl;

import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author jiche
 */
public class ExcelWriter {

    private WritableWorkbook workbook;
    private WritableSheet sheet;

    public enum Direction {
        Vertical,
        Horizontal
    }

    private int tableStart_column = 1;//新table的column启始位置
    private int tableStart_row = 1;   //新table的row启始位置

    private ExcelWriter() {
    }

    private void initExcelWriter(String fileName) throws IOException {
        //创建excel
        this.workbook = Workbook.createWorkbook(new File(fileName));
    }

    public static ExcelWriter CreateExcel(String fileName) throws IOException {
        ExcelWriter inst = new ExcelWriter();
        inst.initExcelWriter(fileName);
        return inst;
    }

    public void CreateSheet(String sheetName, int sheetIndex) throws IOException {
        //创建新的sheet
        this.sheet = workbook.createSheet(sheetName, sheetIndex);
        //跟新table 启始位置
        this.tableStart_column = 1;
        this.tableStart_row = 1;
    }

    // <editor-fold defaultstate="collapsed" desc="插入表格"> 
    //垂直增长
    public void WriteTable(AbstractExcelTable table) throws Exception {
        this.WriteTable(table, Direction.Vertical);
    }

    //插入一张表格
    /**
     *
     * @param table
     * @throws java.lang.Exception
     */
    public void WriteTable(AbstractExcelTable table, Direction dirc) throws Exception {
        if(table == null){
            return;
        }
        
        WritableCellFormat wcf = this.createCellFormat();

        //写入table名称  Table[cnum,rnum]:name
        jxl.write.Label tablename = new jxl.write.Label(tableStart_column, tableStart_row,
                AbstractExcelTable.TableName
                + "[" + table.getColumnCount() + "]"
                + ":" + table.getTableName());
        this.sheet.addCell(tablename);

        for (int i = 0; i < table.getColumnCount(); i++) {
            //写入column名称
            tablename = new jxl.write.Label(tableStart_column + i,
                    tableStart_row + 1, table.getColumnName(i), wcf);
            this.sheet.addCell(tablename);
        }

        for (int column = 0; column < table.getColumnCount(); column++) {
            //填入内容
            for (int row = 0; row < table.getRowCount(); row++) {
                Object value = table.getValueAt(row, column);

                if (value == null) {
                    jxl.write.Label data = new jxl.write.Label(tableStart_column + column,
                            row + tableStart_row + 2, "null", wcf);
                    this.sheet.addCell(data);
                } else if (java.lang.Number.class.isAssignableFrom(value.getClass())) {
                    jxl.write.Number data = new jxl.write.Number(tableStart_column + column,
                            row + tableStart_row + 2, Double.valueOf(value.toString()), wcf);
                    this.sheet.addCell(data);
                } else {
                    jxl.write.Label data = new jxl.write.Label(tableStart_column + column,
                            row + tableStart_row + 2, value.toString(), wcf);
                    this.sheet.addCell(data);
                }
            }
        }
        jxl.write.Label data = new jxl.write.Label(tableStart_column + 0,
                table.getRowCount() + tableStart_row + 2, AbstractExcelTable.TableEND);
        this.sheet.addCell(data);

        //表格是水平增长还是垂直增长
        if (dirc == Direction.Vertical) {
            this.tableStart_row += table.getRowCount() + 3 + 1; // +2(tablename + columnname + tableendmark) + 1 (blank row);
        } else {
            this.tableStart_column += table.getColumnCount() + 1;//+ 1 (blank row);
        }
    }

    //创建cell 格式
    private WritableCellFormat createCellFormat() throws Exception {
        WritableCellFormat wcf = new WritableCellFormat();
        wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        wcf.setAlignment(jxl.format.Alignment.CENTRE);

        return wcf;
    }

    public void Close() throws Exception {
        if (workbook != null) {
            workbook.write();
            workbook.close();
            workbook = null;
        }
    }
    // </editor-fold> 
}
