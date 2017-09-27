/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.form.binfactory;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import nahon.drv.data.UpdateFileHead;

/**
 *
 * @author jiche
 */
public class BinFileHeadModel extends AbstractTableModel {

    private String[] ColumNames = {"变量", "值"};
    private String[] names = {"设备名称：", "硬件版本:", "软件版本:", "cpu个数:"};
    public boolean canModify = true;
    private UpdateFileHead head;

    public BinFileHeadModel(UpdateFileHead filehead) {
        this.head = filehead;
    }

    public UpdateFileHead GetFileHead() {
        return this.head;
    }

    @Override
    public int getRowCount() {
        return names.length;
    }

    @Override
    public int getColumnCount() {
        return ColumNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return ColumNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (this.canModify && columnIndex == 0) {
            return false;
        }
        return this.canModify;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return names[rowIndex];
        } else {
            switch (rowIndex) {
                case 0:
                    return head.DeviceName;
                case 1:
                    return head.Hardversion;
                case 2:
                    return head.SoftwareVersion;
                case 3:
                    return head.BinFileNumbier;
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
                switch (rowIndex) {
                    case 0:
                        head.DeviceName = aValue.toString().substring(0, UpdateFileHead.DeviceName_Length);
                        break;
                    case 1:
                        head.Hardversion = aValue.toString().substring(0, 1);
                        break;
                    case 2:
                        head.SoftwareVersion = aValue.toString().substring(0, UpdateFileHead.SoftwareVersion_Length);
                        break;
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
