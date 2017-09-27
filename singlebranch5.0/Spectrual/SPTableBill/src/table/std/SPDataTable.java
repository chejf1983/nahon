/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table.std;

import javax.swing.table.AbstractTableModel;
import nahon.comm.tool.languange.LanguageHelper;
import table.data.TSPData;

/**
 *
 * @author Administrator
 */
public class SPDataTable extends AbstractTableModel {

    private String[] name = new String[]{"PixelIndex", "WaveIndex", "Original"};
    private TSPData data = new TSPData(new double[]{}, new double[]{});
    
    public void SetValueName(String name){
        this.name[2] = name;
        this.fireTableDataChanged();
    }

    public void Update(TSPData data) {
        this.data = data;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return LanguageHelper.getIntance().GetText(name[column]);
    }

    @Override
    public int getRowCount() {
        if (data == null) {
            return 0;
        }
        return data.pixelIndex.length;
    }

    @Override
    public int getColumnCount() {
        return this.name.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (data == null) {
            return null;
        }
        switch (columnIndex) {
            case 0:
                return data.pixelIndex[rowIndex];
            case 1:
                return data.waveIndex[rowIndex];
            case 2:
                return String.valueOf(data.datavalue[rowIndex]);
            default:
                return null;
        }
    }
};
