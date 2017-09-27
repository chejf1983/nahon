/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control.config.light;

//import SPDevice.DevLight.SynLight;
import javax.swing.table.AbstractTableModel;
import nahon.comm.tool.languange.LanguageHelper;
import sps.dev.data.SSynLightPar;

/**
 *
 * @author jiche
 */
public class SynLightParTable extends AbstractTableModel {

    private SSynLightPar par;

    SynLightParTable(SSynLightPar syLight) {
        this.par = syLight;
    }

    public SSynLightPar GetPar() {
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
        return 5;
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
                        return LanguageHelper.getIntance().GetText("SyLight_plusetype");
//                        return "同步闪烁脉冲类型";
                    case 1:
                        return LanguageHelper.getIntance().GetText("SyLight_plusewidth");
//                        return "同步闪烁光源脉冲宽度";
                    case 2:
                        return LanguageHelper.getIntance().GetText("SyLight_pluseinterval");
//                        return "同步闪烁光源间隔时间";
                    case 3:
                        return LanguageHelper.getIntance().GetText("SyLight_pluselag");
//                        return "同步闪烁光源延时";
                    case 4:
                        return LanguageHelper.getIntance().GetText("SyLight_plusenum");
//                        return "同步闪烁光源脉冲数量";
                }
            case 1:
                switch (rowIndex) {
                    case 0:
                        return par.plustype;
                    case 1:
                        return par.pluswidth;
                    case 2:
                        return par.plusinterval;
                    case 3:
                        return par.pluslag;
                    case 4:
                        return par.plusnum;
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
                    par.plustype = Integer.valueOf(aValue.toString()).byteValue();
                    break;
                case 1:
                    par.pluswidth = Integer.valueOf(aValue.toString());
                    break;
                case 2:
                    par.plusinterval = Integer.valueOf(aValue.toString());
                    break;
                case 3:
                    par.pluslag = Integer.valueOf(aValue.toString());
                    break;
                case 4:
                    par.plusnum = Integer.valueOf(aValue.toString());
                    break;
            }
        }
    }
}
