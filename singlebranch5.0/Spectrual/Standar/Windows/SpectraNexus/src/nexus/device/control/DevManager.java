/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control;

import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.tool.languange.LanguageHelper;
import nahon.comm.tool.languange.LanguageNode.Language;
import nexus.main.io.IOManager;
import sps.dev.data.SSPDevInfo;
import sps.dev.drv.ISPDevice;
import sps.dev.manager.SPDevManager;
import sps.platform.SpectralPlatService;

/**
 *
 * @author Administrator
 */
public class DevManager extends javax.swing.JPanel {

    /**
     * Creates new form DevManager
     */
    public DevManager() {
        initComponents();

        //初始化设备列表
        this.InitDeviceList();

        //注册语言包
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener<Language>() {
            @Override
            public void recevieEvent(Event<Language> event) {
                Frame_list.setTitle(LanguageHelper.getIntance().GetText("Lable_DevList"));
//                Frame_info.setTitle(LanguageHelper.getIntance().GetText("Lable_DevInfo"));
                Button_Detect.setText(LanguageHelper.getIntance().GetText("Button_Search"));
//                ((AbstractTableModel) DeviceInfo.getModel()).fireTableDataChanged();
            }
        });
    }
    
    private SPDevManager spdevmanager = SpectralPlatService.GetInstance().GetDevManager();

    //初始化设备列表
    private void InitDeviceList() {

        //设置窗口ICON
        this.Frame_list.setFrameIcon(null);

        /* 初始化设备列表皮肤 */
        this.List_DeviceName.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {

                setText(value.toString());

                java.net.URL disurl;
                /* 如果设备有控制器在使用，更新链接图片 */
                SSPDevInfo tmpdevinfo = spdevmanager.GetDeviceList().get(index).GetDevInfo();
                if (tmpdevinfo.SameAs(spdevmanager.GetDevControl().GetDevConnectInfo())) {
                    disurl = DevManager.class.getResource("/nexus/device/resources/connect.png");
                } else {
                    disurl = DevManager.class.getResource("/nexus/device/resources/disconnect.png");
                }

                setIcon(new ImageIcon(disurl));
                if (isSelected) {
                    /* reflash device list */
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    // 设置选取与取消选取的前景与背景颜色.  
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                return this;
            }
        });

        //设备列表，显示设备名称
        this.List_DeviceName.setModel(new AbstractListModel() {
            ArrayList<ISPDevice> devList;

            {
                devList = spdevmanager.GetDeviceList();
            }

            @Override
            public int getSize() {
                return devList.size();
            }

            @Override
            public Object getElementAt(int index) {
                if (devList.get(index) != null) {
                    return devList.get(index).GetDevInfo().eia.DeviceName;
                } else {
                    return "Unknow";
                }
            }
        });

        //注册设备列表刷新事件
        spdevmanager.EventPod.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                //更新设备界面
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //无论是列表元素改变，还是设备信息变化，都要更新列表，
                        //因为列表上还有设备控制状态灯
                        List_DeviceName.updateUI();
                    }
                });
            }
        });

        //注册设备控制状态变化事件
        spdevmanager.GetDevControl().RegisterStateChange(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                //更新设备界面
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        List_DeviceName.updateUI();
                    }
                });
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Frame_list = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        List_DeviceName = new javax.swing.JList();
        Button_Detect = new javax.swing.JButton();
        searchProgressBar = new javax.swing.JProgressBar();

        Frame_list.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        Frame_list.setVisible(true);

        List_DeviceName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                List_DeviceNameMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(List_DeviceName);

        Button_Detect.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Button_Detect.setText("Detect");
        Button_Detect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_DetectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Frame_listLayout = new javax.swing.GroupLayout(Frame_list.getContentPane());
        Frame_list.getContentPane().setLayout(Frame_listLayout);
        Frame_listLayout.setHorizontalGroup(
            Frame_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(Button_Detect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Frame_listLayout.setVerticalGroup(
            Frame_listLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Frame_listLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_Detect)
                .addGap(2, 2, 2)
                .addComponent(searchProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(Frame_list)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Frame_list, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Button_DetectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_DetectActionPerformed
        //关闭搜索按钮，避免重复触发
        this.Button_Detect.setEnabled(false);

        //创建进程，来更新界面进度条
        ExecutorService threadpools = SpectralPlatService.GetInstance().GetThreadPools();
        this.searchProgressBar.setIndeterminate(true);
        threadpools.submit(new Runnable() {
            @Override
            public void run() {
                Logger.getGlobal().log(Level.INFO, "进入搜索进程");
                spdevmanager.StartSearchIO(IOManager.CreateAllIO());
                if (spdevmanager.GetDeviceList().size() > 0) {
                    spdevmanager.GetDevControl().Open(spdevmanager.GetDeviceList().get(0));
                } else {
                    FaultCenter.Instance().SendFaultReport(Level.SEVERE, "没有找到设备");
                }

                searchProgressBar.setIndeterminate(false);
                //搜索完毕，恢复 
                Button_Detect.setEnabled(true);
            }
        });
    }//GEN-LAST:event_Button_DetectActionPerformed

    private void List_DeviceNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_List_DeviceNameMouseClicked
        if (evt.getClickCount() == 2) {
            try {
                //设置选择的设备
                int setlectIndex = this.List_DeviceName.getSelectedIndex();
                spdevmanager.GetDevControl().Open(spdevmanager.GetDeviceList().get(setlectIndex));
            } catch (Exception ex) {
                FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex);
            }
        }
    }//GEN-LAST:event_List_DeviceNameMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_Detect;
    private javax.swing.JInternalFrame Frame_list;
    private javax.swing.JList List_DeviceName;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JProgressBar searchProgressBar;
    // End of variables declaration//GEN-END:variables
}
