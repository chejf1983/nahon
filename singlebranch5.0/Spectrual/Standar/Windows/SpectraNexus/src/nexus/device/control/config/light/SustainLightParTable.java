/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control.config.light;

import javax.swing.table.AbstractTableModel;
import nahon.comm.tool.languange.LanguageHelper;

/**
 *
 * @author jiche
 */
public class SustainLightParTable extends AbstractTableModel {

    boolean[] lightlist;

    public SustainLightParTable(boolean[] lightlist) {
        this.lightlist = lightlist;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return LanguageHelper.getIntance().GetText("LightParName");
        } else {
            return LanguageHelper.getIntance().GetText("LightParValue");
        }
    }

    @Override
    public int getRowCount() {
        return this.lightlist.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Boolean.class;
        } else {
            return super.getColumnClass(columnIndex); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return LanguageHelper.getIntance().GetText("Light") + "-[" + rowIndex + "]";
        } else {
            return this.lightlist[rowIndex];
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            this.lightlist[rowIndex] = (boolean) aValue;
        }
    }
}
