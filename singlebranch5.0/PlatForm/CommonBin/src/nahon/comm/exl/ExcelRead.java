/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl;

import nahon.comm.exl.AbstractExcelTable;
import java.io.File;
import java.util.ArrayList;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author jiche
 */
public class ExcelRead {
    private int MaxColumn = 10000;
    private Workbook readworkBook = null;
    private Sheet readSheet = null;

    private ExcelRead() {
    }

    private void initExcel(String fileName, int sheetIndex) throws Exception {
        this.readworkBook = Workbook.getWorkbook(new File(fileName));
        this.readSheet = readworkBook.getSheet(sheetIndex);
    }

    public static ExcelRead ReadExcel(String fileName) throws Exception {
        return ReadExcel(fileName, 0);
    }

    public static ExcelRead ReadExcel(String fileName, int sheetIndex) throws Exception {
        ExcelRead inst = new ExcelRead();
        inst.initExcel(fileName, sheetIndex);
        return inst;
    }

    public DefaultExcelTable[] ReadExcelTable() {
        //寻找有table标记的表格
        tableInfo[] tablepoints = this.searchTable(readSheet);
        DefaultExcelTable[] tables = new DefaultExcelTable[tablepoints.length];

        //读取表格内容
        for (int i = 0; i < tablepoints.length; i++) {
            tables[i] = this.readTable(tablepoints[i], readSheet);
        }

        return tables;
    }

    //表格信息
    private class tableInfo {

        public String tablename; //表格名称，去掉关键字AbstractExcelTable.TableName 和表格大小
        public int start_x; //表格起始X坐标
        public int start_y; //表格起始Y坐标
        public int columns;   //表格宽
        public int rows;   //表格高
    }

    //搜索带有  Table[x_len,y_len]:name  标签的表格
    private tableInfo[] searchTable(Sheet sheet) {
        ArrayList<tableInfo> tables = new ArrayList();
        int columns = sheet.getColumns();
        int rows = sheet.getRows();
        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                Cell cell = sheet.getCell(c, r);
                //如果当前单元内有表头标志，获取表格信息
                if (this.isTableNameCell(cell)) {
                    tableInfo tinfo = new tableInfo();
                    tinfo.start_x = c;   //表格名称正下方就是表格
                    tinfo.start_y = r + 1;
                    //获取表格的大小和名称
                    String contents = cell.getContents();
                    int start = contents.indexOf("[");
                    int end = contents.indexOf("]");
                    if (start == -1 || end == -1) {
                        break;
                    }
                    String x_len = contents.substring(start + 1, end);
                    tinfo.columns = Integer.valueOf(x_len);
                    
                    start = contents.indexOf(":");
                    if (start == -1 || end == -1) {
                        break;
                    }

                    if (start + 1 < contents.length()) {
                        tinfo.tablename = contents.substring(start + 1);
                    } else {
                        tinfo.tablename = "";
                    }
                    
                    tinfo.rows = MaxColumn;
                    for(int i = 1; i < MaxColumn; i++){
                        Cell ecell = sheet.getCell(c, r + i);
                        if(this.isTableEndCell(ecell)){
                            tinfo.rows = i - 1;
                            break;
                        }
                    }
                    tables.add(tinfo);
                }
            }
        }
        return tables.toArray(new tableInfo[0]);
    }

    private DefaultExcelTable readTable(tableInfo table, Sheet sheet) {
        String[] columnNames = new String[table.columns];
        String[][] data = new String[table.rows - 1][table.columns];

        //找到column names
        for (int column = 0; column < table.columns; column++) {
            //获取column名称
            columnNames[column] = sheet.getCell(table.start_x + column, table.start_y).getContents();
        }

        //名称row下面是数据
        for (int row_ind = 0; row_ind < table.rows - 1; row_ind++) {
            String[] row = new String[table.columns];
            for (int column_ind = 0; column_ind < table.columns; column_ind++) {
                Cell cell = sheet.getCell(table.start_x + column_ind, table.start_y + 1 + row_ind);
                row[column_ind] = cell.getContents();
            }
            data[row_ind] = row;
        }
        return DefaultExcelTable.createTable(table.tablename, columnNames, data);
    }

    private boolean isTableEndCell(Cell cell){
        return cell.getContents() != null && cell.getContents().startsWith(AbstractExcelTable.TableEND);
    }
    
    private boolean isTableNameCell(Cell cell) {
        return cell.getContents() != null && cell.getContents().startsWith(AbstractExcelTable.TableName);
    }

    public void Close() throws Exception {

        if (this.readworkBook != null) {
            this.readworkBook.close();
            this.readworkBook = null;
        }
    }
}
