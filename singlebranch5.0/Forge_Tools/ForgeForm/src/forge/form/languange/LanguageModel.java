/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.form.languange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import nahon.comm.tool.languange.LanguageNode;
import nahon.comm.un.other.XMLAdapter;

/**
 *
 * @author jiche
 */
public class LanguageModel implements TableModel {
    private XMLAdapter saver;
    private ArrayList<LanguageNode> nodes = new ArrayList();

    public LanguageModel(String FileName) {
        saver = new XMLAdapter(FileName);
        for (int i = 0; i < saver.GetNodeNumber(); i++) {
            try {
                LanguageNode n = new LanguageNode("");
                saver.GetNodeByIndex(n, i);
                nodes.add(n);
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Save(){
        try {
            this.saver.Clean();
            this.saver.AddNodeArray(this.nodes.toArray(new LanguageNode[0]));
            this.saver.Save();
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getRowCount() {
        return nodes.size() - 1;
    }

    @Override
    public int getColumnCount() {
        return LanguageNode.Language.values().length + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return nodes.get(rowIndex + 1).MainKey();
        }
        return nodes.get(rowIndex + 1).GetText(LanguageNode.Language.values()[columnIndex - 1]);
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Name";
        }
        return LanguageNode.Language.values()[columnIndex - 1].toString();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
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
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            nodes.get(rowIndex + 1).SetText(LanguageNode.Language.values()[columnIndex - 1], aValue.toString());
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
    
    public void DeleteRows(int index){
        int pos = index + 1;
        if(pos < this.nodes.size()){
            this.nodes.remove(pos);
        }
    }
}
