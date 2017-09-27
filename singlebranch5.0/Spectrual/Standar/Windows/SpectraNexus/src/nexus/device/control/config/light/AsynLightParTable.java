/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control.config.light;

import javax.swing.table.AbstractTableModel;
import nahon.comm.tool.languange.LanguageHelper;
import sps.dev.data.SSAsyLightPar;

/**
 *
 * @author jiche
 */
public class AsynLightParTable extends AbstractTableModel {

    private SSAsyLightPar par;
//

    AsynLightParTable(SSAsyLightPar syLight) {
        this.par = syLight;
    }

    public SSAsyLightPar GetPar() {
        return this.par;
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                switch (rowIndex) {
                    case 0:
                        return LanguageHelper.getIntance().GetText("AsyLight_hightime");
//                        return "非同步光源高电平时间";
                    case 1:
                        return LanguageHelper.getIntance().GetText("AsyLight_lowtime");
//                        return "非同步光源低电平时间";
                }
            case 1:
                switch (rowIndex) {
                    case 0:
                        return par.hightime;
                    case 1:
                        return par.lowtime;
                }
                return "2";
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            switch (rowIndex) {
                case 0:
                    par.hightime = Integer.valueOf(aValue.toString());
                    break;
                case 1:
                    par.lowtime = Integer.valueOf(aValue.toString());
                    break;
            }
        }
    }

}
