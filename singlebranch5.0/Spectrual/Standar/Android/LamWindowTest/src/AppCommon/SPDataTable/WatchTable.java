/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataTable;

import app.common.chartmath.NewTonHelper;
import spdev.dev.data.SPData;
import java.util.ArrayList;
import nahon.comm.tool.languange.LanguageHelper;

/**
 *
 * @author jiche
 */
public class WatchTable extends AbstractWatchTable {

    protected String[] name = new String[]{"WaveIndex", "Original"};
    protected SPData data = new SPData(new double[]{}, new double[]{});

    protected ArrayList<Double> SearchIndex = new ArrayList();
    protected ArrayList<Double> ResultIndex = new ArrayList();

    @Override
    public void AddRecord() {
        SearchIndex.add(Double.NaN);
        ResultIndex.add(Double.NaN);
    }

    @Override
    public void RemoveRecord(int index) {
        if(index >= 0 && index < this.getRowCount()){
            this.SearchIndex.remove(index);
            this.ResultIndex.remove(index);
        }
    }

    @Override
    public void Update(SPData data) {
        this.data = data;
        if (this.data != null) {
            for (int i = 0; i < this.SearchIndex.size(); i++) {
                if (!Double.isNaN(this.SearchIndex.get(i))) {
                    double value = NewTonHelper.NewInt(this.data.waveIndex,
                            this.data.datavalue, this.data.waveIndex.length, this.SearchIndex.get(i), true);
                    this.ResultIndex.set(i, value);
                }
            }
        }
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return LanguageHelper.getIntance().GetText(name[column]);
    }

    @Override
    public int getRowCount() {
        return SearchIndex.size();
    }

    @Override
    public int getColumnCount() {
        return this.name.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return SearchIndex.get(rowIndex);
            case 1:
                return String.format("%.2f",
                        ResultIndex.get(rowIndex));
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            try {
                Double index = Double.valueOf(aValue.toString());
                this.SearchIndex.set(rowIndex, index);
            } catch (Exception ex) {
            }
        }
    }
}
