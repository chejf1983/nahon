/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table.std;

import table.watch.WatchTable;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.exl.AbstractExcelTable;
import nahon.comm.exl.ExcelWriter;
import nahon.comm.tool.languange.LanguageHelper;
import table.data.TSPData;
import table.watch.WatchBean;
import table.watch.WatchPoint;

/**
 *
 * @author jiche
 */
public class SPDataTablePane extends javax.swing.JPanel {

    private JSplitPane chartAnddataSplit;

    /**
     * Creates new form DataPane
     */
    public SPDataTablePane() {
        initComponents();

        /*初始化控件*/
        this.InitCommonComponent();

        /* 初始化语言 */
        this.IniLanguage();
    }

    // <editor-fold defaultstate="collapsed" desc="公共控件"> 
    private void IniLanguage() {
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                if (!"".equals(ChartName)) {
                    //标题名称
                    Icon_InternalFrame.setTitle(LanguageHelper.getIntance().GetText(ChartName));
                }

                //按钮提示信息
                ToggleButton_EnableWatch.setToolTipText(LanguageHelper.getIntance().GetText("Filter_Data_Switch"));
                Button_wadd.setToolTipText(LanguageHelper.getIntance().GetText("Button_wadd"));
                Button_wremove.setToolTipText(LanguageHelper.getIntance().GetText("Button_wremove"));
                ToggleButton_flag.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_flag"));
                Button_save.setToolTipText(LanguageHelper.getIntance().GetText("Button_save"));

                //刷新表格
                stdmodel.fireTableStructureChanged();
                watchmodel.fireTableStructureChanged();
            }

        });
    }

    private void InitCommonComponent() {
        //设置窗口ICON
        this.Icon_InternalFrame.setFrameIcon(null);

        //初始化SPData表格
        this.InitNodeWaveTable();
        //初始化SPData表格
        this.InitWatchTable();

        //初始化过滤窗口
        chartAnddataSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, nodewavepane, null);
        //设置窗口比例
        chartAnddataSplit.setResizeWeight(0.8);

        this.table_Area.setLayout(new CardLayout());
        this.table_Area.add(chartAnddataSplit);

    }

    public void UpdateData(TSPData data) {
//        nodeandwaveTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.watchmodel.Update(data);
        this.stdmodel.Update(data);
    }

    String ChartName = "";

    public void SetTitle(String name, String valuename) {
        this.ChartName = name;
        this.stdmodel.SetValueName(valuename);
        this.watchmodel.SetTableValueName(valuename);
    }

    public void SetPrec(int num) {

    }
    // </editor-fold>   

    private void FitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();

        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="光谱数据表格"> 
    private javax.swing.JScrollPane nodewavepane = new javax.swing.JScrollPane();
    private javax.swing.JTable nodeandwaveTable = new javax.swing.JTable();
    private SPDataTable stdmodel = new SPDataTable();

    private void InitNodeWaveTable() {
        nodewavepane.setViewportView(nodeandwaveTable);
        nodewavepane.setPreferredSize(this.table_Area.getPreferredSize());

        //初始化SPData表格
        this.nodeandwaveTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, isFocus, row, column);

                if (isSelected) {
                    setBackground(new Color(135, 206, 250, 100));
                } else {
                    if ((row + 1) % 2 == 0) {
                        //偶数行时的颜色
                        setBackground(Color.WHITE);
                    } else if ((row + 1) % 2 == 1) {
                        //设置单数行的颜色
                        setBackground(new Color(192, 192, 192, 140));
                    }
                }

                return cell;
            }
        });
        this.nodeandwaveTable.getTableHeader().setReorderingAllowed(false);
        this.nodeandwaveTable.getTableHeader().setResizingAllowed(false);
//        this.nodeandwaveTable.getTableHeader().setp
        this.nodeandwaveTable.setModel(stdmodel);
//
//        JTableHeader header = this.nodeandwaveTable.getTableHeader();
//        header.setResizingColumn(this.nodeandwaveTable.getColumnModel().getColumn(0)); // 此行很重要
//        this.nodeandwaveTable.getColumnModel().getColumn(0).setWidth(10);
//        header.setResizingColumn(this.nodeandwaveTable.getColumnModel().getColumn(1)); // 此行很重要
//        this.nodeandwaveTable.getColumnModel().getColumn(1).setWidth(10);
//        header.setResizingColumn(this.nodeandwaveTable.getColumnModel().getColumn(2)); // 此行很重要
//        this.nodeandwaveTable.getColumnModel().getColumn(2).setWidth(40);
    }
    
    private void setColumnSize(JTable table, int i, int preferedWidth, int maxWidth, int minWidth){  
    //表格的列模型  
    TableColumnModel cm = table.getColumnModel();  
    //得到第i个列对象   
    TableColumn column = cm.getColumn(i);    
    column.setPreferredWidth(preferedWidth);  
    column.setMaxWidth(maxWidth);  
    column.setMinWidth(minWidth);  
    }  
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="观察数据表格"> 
    private javax.swing.JScrollPane watchpane = new javax.swing.JScrollPane();
    private javax.swing.JTable watch_table;
    private WatchTable watchmodel = new WatchTable();

    private void InitWatchTable() {
        watch_table = new javax.swing.JTable();
        watchpane.setViewportView(watch_table);
        watchpane.setPreferredSize(this.table_Area.getPreferredSize());

        //初始化过滤窗口
        this.watch_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, isFocus, row, column);

                if (isSelected) {
                    setBackground(new Color(135, 206, 250, 100));
                } else {
                    if ((row + 1) % 2 == 0) {
                        //偶数行时的颜色
                        setBackground(Color.WHITE);
                    } else if ((row + 1) % 2 == 1) {
                        //设置单数行的颜色
                        setBackground(new Color(192, 192, 192, 140));
                    }
                }

                return cell;
            }
        });
        this.watch_table.getTableHeader().setReorderingAllowed(false);
        this.watch_table.getTableHeader().setResizingAllowed(false);
        this.watch_table.setModel(watchmodel);
        this.watch_table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        this.Button_wadd.setVisible(false);
        this.Button_wremove.setVisible(false);
        this.Button_save.setVisible(false);
        this.ToggleButton_flag.setVisible(false);
    }
// </editor-fold>  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Icon_InternalFrame = new javax.swing.JInternalFrame();
        table_Area = new javax.swing.JPanel();
        ToggleButton_EnableWatch = new javax.swing.JToggleButton();
        Button_wadd = new javax.swing.JButton();
        Button_wremove = new javax.swing.JButton();
        Button_save = new javax.swing.JButton();
        ToggleButton_flag = new javax.swing.JToggleButton();

        setMinimumSize(new java.awt.Dimension(264, 300));
        setName(""); // NOI18N
        setOpaque(false);

        Icon_InternalFrame.setToolTipText("");
        Icon_InternalFrame.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Icon_InternalFrame.setName(""); // NOI18N
        Icon_InternalFrame.setVisible(true);

        javax.swing.GroupLayout table_AreaLayout = new javax.swing.GroupLayout(table_Area);
        table_Area.setLayout(table_AreaLayout);
        table_AreaLayout.setHorizontalGroup(
            table_AreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );
        table_AreaLayout.setVerticalGroup(
            table_AreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        ToggleButton_EnableWatch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/table/resource/watch_data.png"))); // NOI18N
        ToggleButton_EnableWatch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_EnableWatchItemStateChanged(evt);
            }
        });

        Button_wadd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/table/resource/watch_add.png"))); // NOI18N
        Button_wadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_waddActionPerformed(evt);
            }
        });

        Button_wremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/table/resource/watch_remove.png"))); // NOI18N
        Button_wremove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_wremoveMouseClicked(evt);
            }
        });

        Button_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/table/resource/watch_write.png"))); // NOI18N
        Button_save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_saveMouseClicked(evt);
            }
        });
        Button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_saveActionPerformed(evt);
            }
        });

        ToggleButton_flag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/table/resource/watch_flag.png"))); // NOI18N
        ToggleButton_flag.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_flagItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout Icon_InternalFrameLayout = new javax.swing.GroupLayout(Icon_InternalFrame.getContentPane());
        Icon_InternalFrame.getContentPane().setLayout(Icon_InternalFrameLayout);
        Icon_InternalFrameLayout.setHorizontalGroup(
            Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table_Area, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Icon_InternalFrameLayout.createSequentialGroup()
                .addComponent(ToggleButton_EnableWatch, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_wadd, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_wremove, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(ToggleButton_flag, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_save, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Icon_InternalFrameLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Button_save, Button_wadd, Button_wremove, ToggleButton_EnableWatch, ToggleButton_flag});

        Icon_InternalFrameLayout.setVerticalGroup(
            Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Icon_InternalFrameLayout.createSequentialGroup()
                .addComponent(table_Area, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(ToggleButton_EnableWatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_wadd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_wremove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_save))
                    .addComponent(ToggleButton_flag)))
        );

        Icon_InternalFrameLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_save, Button_wadd, Button_wremove, ToggleButton_EnableWatch, ToggleButton_flag});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Icon_InternalFrame)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Icon_InternalFrame, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ToggleButton_EnableWatchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_EnableWatchItemStateChanged
        if (this.ToggleButton_EnableWatch.isSelected()) {
            this.chartAnddataSplit.setRightComponent(this.watchpane);
            this.Button_wadd.setVisible(true);
            this.Button_wremove.setVisible(true);
            this.ToggleButton_flag.setVisible(true);
            this.Button_save.setVisible(true);
        } else {
            WatchTable table = (WatchTable) this.watch_table.getModel();
            table.GetWatchBean().EnableHistory(false);
            //table.
            this.chartAnddataSplit.setRightComponent(null);
            this.Button_wadd.setVisible(false);
            this.Button_wremove.setVisible(false);
            this.ToggleButton_flag.setVisible(false);
            this.ToggleButton_flag.setSelected(false);
            this.Button_save.setVisible(false);
        }
    }//GEN-LAST:event_ToggleButton_EnableWatchItemStateChanged

    private void Button_wremoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_wremoveMouseClicked
        int index = watch_table.getSelectedRow();
        ((WatchTable) watch_table.getModel()).GetWatchBean().RemoveIndex(index);
        this.watch_table.updateUI();
    }//GEN-LAST:event_Button_wremoveMouseClicked

    private void Button_saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_saveMouseClicked
        if (((WatchTable) watch_table.getModel()).GetWatchBean().GetNodes().length == 0) {
            return;
        }

        File file = TableFileDialogHelp.GetFilePath(".xls");
        if (file != null) {
            try {
                ExcelWriter writer = ExcelWriter.CreateExcel(file.getAbsolutePath());
                try {
                    writer.CreateSheet("History", 0);
                    WatchBean bean = ((WatchTable) watch_table.getModel()).GetWatchBean();

                    AbstractExcelTable WatchTable = bean.WatchTable();
                    if (WatchTable != null) {
                        writer.WriteTable(WatchTable, ExcelWriter.Direction.Horizontal);
                    }

                    for (AbstractExcelTable tabelh : bean.HistoryToExcelTable()) {
                        writer.WriteTable(tabelh, ExcelWriter.Direction.Horizontal);
                    }
                    JOptionPane.showMessageDialog(this, "保存数据成功");
                } finally {
                    writer.Close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "保存数据失败");
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_Button_saveMouseClicked

    private void ToggleButton_flagItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_flagItemStateChanged
        ((WatchTable) watch_table.getModel()).GetWatchBean().EnableHistory(this.ToggleButton_flag.isSelected());
        this.Button_wadd.setEnabled(!this.ToggleButton_flag.isSelected());
        this.Button_save.setEnabled(!this.ToggleButton_flag.isSelected());
    }//GEN-LAST:event_ToggleButton_flagItemStateChanged

    private void Button_waddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_waddActionPerformed
        WatchPoint npoint = new WatchPoint(null, true);
        npoint.setLocationRelativeTo(this.Button_wadd);
        npoint.setVisible(true);
        if (!npoint.GetWatchNode().isNaN()) {
            ((WatchTable) watch_table.getModel()).GetWatchBean().AddWatchNode(npoint.GetWatchNode().doubleValue());
            this.watch_table.updateUI();
        }
    }//GEN-LAST:event_Button_waddActionPerformed

    private void Button_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_saveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Button_saveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_save;
    private javax.swing.JButton Button_wadd;
    private javax.swing.JButton Button_wremove;
    private javax.swing.JInternalFrame Icon_InternalFrame;
    private javax.swing.JToggleButton ToggleButton_EnableWatch;
    private javax.swing.JToggleButton ToggleButton_flag;
    private javax.swing.JPanel table_Area;
    // End of variables declaration//GEN-END:variables
}
