/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.form.other;

import forge.bill.platform.ForgeSystem;
import javax.swing.table.AbstractTableModel;
import nahon.drv.data.EquipmentInfo;

/**
 *
 * @author jiche
 */
public class EiaTableModel extends AbstractTableModel {

    private String[] ColumNames = {"EIA信息", "值"};
    private String[] names = {"设备名称：", "生产序列:", "生产日期:", "硬件版本:", "软件版本:"};
    private EquipmentInfo eiainfo;
    //默认可以编辑
    private boolean isEditable = true;

    public final static int DeviceName_Length = 0x10;
//    public final static int Hardversion_Length = 0x01;
//    public final static int SoftwareVersion_Length = 0x04;
    public final static int BuildSerialNum_Length = 0x20;
//    public final static int BuildDate_Length = 0x10;
//    public final static int DevCatalog_Length = 0x02;
//    public final static int DevType_Length = 0x02;

    public EiaTableModel(EquipmentInfo eiainfo) {
        if (eiainfo == null) {
            this.eiainfo = new EquipmentInfo();
        } else {
            this.eiainfo = eiainfo;
        }
    }

    public void EnableEditable(boolean value) {
        isEditable = value;
    }

    public EquipmentInfo GetEIAInfo() {
        return this.eiainfo;
    }

    @Override
    public int getRowCount() {
        return this.names.length;
    }

    @Override
    public int getColumnCount() {
        return this.ColumNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.names[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //禁止编辑标志判断,同时标题不能修改
        if (!this.isEditable || columnIndex == 0) {
            return false;
        } else {
            //设备名称可以修改
            if (rowIndex == 0) {
                return true;
            }
            
            //内部版本才可以修改设备序列号
            if (ForgeSystem.GetInstance().systemConfig.IsInternal()
                && rowIndex == 1) {
                return true;
            }

            return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return names[rowIndex];
        } else {
            switch (rowIndex) {
                case 0:
                    return eiainfo.DeviceName;
                case 1:
                    return eiainfo.BuildSerialNum;
                case 2:
                    return eiainfo.BuildDate;
                case 3:
                    return eiainfo.Hardversion;
                case 4:
                    return eiainfo.SoftwareVersion;
            }
            defualt:
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
        } else {
            try {
                int length = 0;
                switch (rowIndex) {
                    case 0:
                        length = aValue.toString().length() > DeviceName_Length
                                ? DeviceName_Length : aValue.toString().length();
                        eiainfo.DeviceName = aValue.toString().substring(0, length);
                        break;
                    case 1:
                        length = aValue.toString().length() > BuildSerialNum_Length
                                ? BuildSerialNum_Length : aValue.toString().length();
                        eiainfo.BuildSerialNum = aValue.toString().substring(0, length);
                        break;
                }
            } catch (Exception ex) {
            }
        }
    }
}
