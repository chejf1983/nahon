/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl;

import java.util.ArrayList;

/**
 *
 * @author jiche
 */
public class DefaultExcelTable implements AbstractExcelTable {

    public String tableName;
    protected String[] columnNames;
    protected ArrayList<Object[]> data = new ArrayList();

//    protected Object[][] data;//{{row1},{row2},....}
    public static DefaultExcelTable createTable(String tablename, String[] names, Object[][] columnValues) {
        if (names == null || columnValues == null) {
            return null;
        }
        DefaultExcelTable table = new DefaultExcelTable();
        table.tableName = tablename;
        table.columnNames = names;
        for (int i = 0; i < columnValues.length; i++) {
            if (columnValues[i].length == names.length) {
                table.data.add(columnValues[i]);
            }
        }
        return table;
    }

    public static DefaultExcelTable createTable(String tablename, String[] names) {
        if (names == null) {
            return null;
        }
        DefaultExcelTable table = new DefaultExcelTable();
        table.tableName = tablename;
        table.columnNames = names;
        return table;
    }

    public boolean AddRow(Object[] columnValues) {
        if (columnValues.length != this.columnNames.length) {
            return false;
        }
        data.add(columnValues);
        return true;
    }

    public void Remove(int index) {
        if (index < this.data.size()) {
            this.data.remove(index);
        }
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getColumnName(int i) {
        return this.columnNames[i];
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return this.data.get(row)[column];
    }

}
