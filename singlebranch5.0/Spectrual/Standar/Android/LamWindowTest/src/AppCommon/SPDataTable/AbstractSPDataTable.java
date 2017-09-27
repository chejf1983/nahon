/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AppCommon.SPDataTable;

import javax.swing.table.AbstractTableModel;
import spdev.dev.data.SPData;

/**
 *
 * @author jiche
 */
public abstract class AbstractSPDataTable extends AbstractTableModel{
    public abstract void Update(SPData data);
}
