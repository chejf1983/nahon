/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.app.stander;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import sps.app.solapp.ObsBill;
import sps.platform.SoildSystem;

/**
 *
 * @author Administrator
 */
public class CalTable extends AbstractTableModel {

    private String[] names = new String[]{"参数", "是否定标", "值"};

    private ArrayList<String[]> data = new ArrayList();
    {        
        SoildSystem.GetInstance().GetSoilControl().GetObsBill().ConfigChange.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
               Update();
            }
        });
        Update();
    }

    private void Update() {
        data.clear();
        ObsBill bill = SoildSystem.GetInstance().GetSoilControl().GetObsBill();
        data.add(new String[]{"标准灯是否设置", bill.IsBaseEnable() + "", ""});
        data.add(new String[]{"氮定标", bill.IsBaseN0Enable() + "", bill.IsBaseN0Enable() ? bill.GetBaseN0() + "" : ""});
        data.add(new String[]{"磷定标", bill.IsBaseP0Enable() + "", bill.IsBaseP0Enable() ? bill.GetBaseP0() + "" : ""});
        data.add(new String[]{"钾定标", bill.IsBaseK0Enable() + "", bill.IsBaseK0Enable() ? bill.GetBaseK0() + "" : ""});
    }

    @Override
    public String getColumnName(int column) {
        return this.names[column];
    }

    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public int getColumnCount() {
        return this.names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data.get(rowIndex)[columnIndex];
    }

}
