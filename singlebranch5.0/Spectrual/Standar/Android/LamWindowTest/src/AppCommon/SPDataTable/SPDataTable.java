/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataTable;

import spdev.dev.data.SPData;
import nahon.comm.tool.languange.LanguageHelper;

/**
 *
 * @author Administrator
 */
public class SPDataTable extends AbstractSPDataTable{

    protected String[] name = new String[]{"PixelIndex", "WaveIndex", "Original"};
    protected SPData data = new SPData(new double[]{}, new double[]{});

    @Override
    public void Update(SPData data) {
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
                return String.format("%.2f",
                        data.datavalue[rowIndex]);
            default:
                return null;
        }
    }
};
