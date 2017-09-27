/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control;

import javax.swing.table.AbstractTableModel;
import nahon.comm.tool.languange.LanguageHelper;
import sps.dev.data.SSEquipmentInfo;

/**
 *
 * @author Administrator
 */
public class EIAModel extends AbstractTableModel {

    private final String[] name = new String[]{
        "DeviceName",
        "BuildSerialNum",
        "BuildDate",
        "SoftwareVersion",
        "Hardversion"};

    private String[] value;

    public EIAModel(SSEquipmentInfo eia) {
        if (eia != null) {
            value = new String[]{
                eia.DeviceName,
                eia.BuildSerialNum,
                eia.BuildDate,
                eia.SoftwareVersion,
                String.valueOf(eia.Hardversion)};
        } else {
            value = new String[name.length];
            for (int i = 0; i < value.length; i++) {
                value[i] = "";
            }
        }
    }

    @Override
    public int getRowCount() {
        return name.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0) {
            return LanguageHelper.getIntance().GetText(name[rowIndex]);
        } else {
            return value[rowIndex];
        }
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

}
