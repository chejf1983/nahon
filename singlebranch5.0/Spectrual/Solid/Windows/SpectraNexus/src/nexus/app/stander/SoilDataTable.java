/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.app.stander;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import sps.app.solapp.data.NPKData;
import sps.platform.SoildSystem;

/**
 *
 * @author Administrator
 */
public class SoilDataTable extends AbstractTableModel {

    private ArrayList<String[]> database = new ArrayList();
    private String[] names = new String[]{"时间", "氮吸光度", "氮含量", "磷吸光度", "磷含量", "钾吸光度", "钾含量"};

    {
          SoildSystem.GetInstance().GetSoilControl().GetAppBill().DataCenter.RegeditListener(new EventListener<NPKData>(){
              @Override
              public void recevieEvent(Event<NPKData> event) {
                UpdateData(event.GetEvent());
              }
          });
    }


    private void UpdateData(NPKData data) {
        if(database.size() > 1000){
            database.remove(database.size() - 1);
        }
        
        database.add(0, new String[]{
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(data.oradata.original_data.time),
            data.oradata.Nd + "", data.Ndata + "mg/kg",
            data.oradata.Pd + "", data.Ndata + "mg/kg",
            data.oradata.Kd + "", data.Ndata + "mg/kg"});
        
        this.fireTableDataChanged();
    }

     @Override
    public String getColumnName(int column) {
        return this.names[column];
    }

    @Override
    public int getRowCount() {
        return this.database.size();
    }

    @Override
    public int getColumnCount() {
        return this.names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.database.get(rowIndex)[columnIndex];
    }
}
