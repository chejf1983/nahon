/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataTable;

import spdev.dev.data.SPData;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jiche
 */
public abstract class AbstractWatchTable extends AbstractTableModel {

    public abstract void AddRecord();

    public abstract void RemoveRecord(int index);

    public abstract void Update(SPData data);
}
