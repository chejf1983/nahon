/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataTable;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.tool.languange.LanguageHelper;

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

    private void IniLanguage() {
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                if (ChartName != "") {
                    Icon_InternalFrame.setTitle(LanguageHelper.getIntance().GetText(ChartName));
                }

                ToggleButton_EnableWatch.setToolTipText(LanguageHelper.getIntance().GetText("Filter_Data_Switch"));

                spmodel.fireTableStructureChanged();
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
        chartAnddataSplit.setResizeWeight(0.8);

        this.table_Area.setLayout(new CardLayout());
        this.table_Area.add(chartAnddataSplit);

    }

    // <editor-fold defaultstate="collapsed" desc="Title"> 
    String ChartName = "";

    public void SetTitle(String name) {
        this.ChartName = name;
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="node and wave table"> 
    private javax.swing.JScrollPane nodewavepane = new javax.swing.JScrollPane();
    private javax.swing.JTable nodeandwaveTable = new javax.swing.JTable();
    private SPDataTable spmodel = new SPDataTable();

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
        this.nodeandwaveTable.setModel(spmodel);
//        this.FitTableColumns(this.nodeandwaveTable);
    }

    public void SetNodeWaveTableModel(TableModel dataModel) {
        this.nodeandwaveTable.setModel(dataModel);
    }
    
    public AbstractSPDataTable GetNodeWaveTable(){
        return (AbstractSPDataTable)this.nodeandwaveTable.getModel();
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="watch table"> 
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
    }

    public void SetWatchTableModel(AbstractWatchTable dataModel) {
        this.watch_table.setModel(dataModel);
    }
    
    public AbstractWatchTable GetWatchTableModel(){
        return (AbstractWatchTable) this.watch_table.getModel();
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
            .addGap(0, 463, Short.MAX_VALUE)
        );

        ToggleButton_EnableWatch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/watch_data.png"))); // NOI18N
        ToggleButton_EnableWatch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_EnableWatchItemStateChanged(evt);
            }
        });

        Button_wadd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/watch_add.png"))); // NOI18N
        Button_wadd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_waddMouseClicked(evt);
            }
        });

        Button_wremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/watch_remove.png"))); // NOI18N
        Button_wremove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_wremoveMouseClicked(evt);
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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Icon_InternalFrameLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Button_wadd, Button_wremove, ToggleButton_EnableWatch});

        Icon_InternalFrameLayout.setVerticalGroup(
            Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Icon_InternalFrameLayout.createSequentialGroup()
                .addComponent(table_Area, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(Icon_InternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ToggleButton_EnableWatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Button_wadd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Button_wremove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Icon_InternalFrameLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_wadd, Button_wremove, ToggleButton_EnableWatch});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Icon_InternalFrame)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Icon_InternalFrame)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ToggleButton_EnableWatchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_EnableWatchItemStateChanged
        if (this.ToggleButton_EnableWatch.isSelected()) {
            this.chartAnddataSplit.setRightComponent(this.watchpane);
            this.Button_wadd.setVisible(true);
            this.Button_wremove.setVisible(true);
        } else {
            this.chartAnddataSplit.setRightComponent(null);
            this.Button_wadd.setVisible(false);
            this.Button_wremove.setVisible(false);
        }
    }//GEN-LAST:event_ToggleButton_EnableWatchItemStateChanged

    private void Button_waddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_waddMouseClicked
        ((WatchTable) watch_table.getModel()).AddRecord();

        this.watch_table.updateUI();
    }//GEN-LAST:event_Button_waddMouseClicked

    private void Button_wremoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_wremoveMouseClicked
        int index = watch_table.getSelectedRow();
        ((WatchTable) watch_table.getModel()).RemoveRecord(index);
        this.watch_table.updateUI();
    }//GEN-LAST:event_Button_wremoveMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_wadd;
    private javax.swing.JButton Button_wremove;
    private javax.swing.JInternalFrame Icon_InternalFrame;
    private javax.swing.JToggleButton ToggleButton_EnableWatch;
    private javax.swing.JPanel table_Area;
    // End of variables declaration//GEN-END:variables
}
