/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.device.control;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import sps.dev.data.SSDataCollectPar;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.ifs.ISPDevControl.CSTATE;
import sps.dev.control.ifs.ISPDevLightControl;
import sps.platform.SpectralPlatService;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.tool.languange.LanguageHelper;
import nexus.devcie.control.config.DeviceParSetDialog;

/**
 *
 * @author Administrator
 */
public class DevControlPane extends javax.swing.JPanel {

    private ISPDevControl devcontrol = SpectralPlatService.GetInstance().GetDevManager().GetDevControl();

    /**
     * Creates new form DevControlPane
     */
    public DevControlPane() {
        initComponents();

        //初始化设备信息列表
        this.InitDevInfo();

        this.InitDevControl();

        this.InitLanguange();
    }

    // <editor-fold defaultstate="collapsed" desc="初始化代码"> 
    //初始化设备信息表
    private void InitDevInfo() {

        //设置窗口ICON
        this.Frame_info.setFrameIcon(null);

        //初始控制设备信息
        this.DeviceInfo.setModel(new EIAModel(null));

        this.DeviceInfo.getTableHeader().setReorderingAllowed(false); //不可整列移动   
        this.DeviceInfo.getTableHeader().setResizingAllowed(false);   //不可拉动表格
        this.DeviceInfo.getTableHeader().setVisible(false);

        this.DeviceInfo.getColumnModel().getColumn(0).setPreferredWidth(55);
    }

    //初始化设备控制界面
    private void InitDevControl() {
        //注册控制状态更新事件
        this.devcontrol.RegisterStateChange(new EventListener<CSTATE>() {
            @Override
            public void recevieEvent(Event<CSTATE> event) {
                UpdateControlPaneState(event.GetEvent());

                if (event.GetEvent() == CSTATE.CONNECT && event.Info() == CSTATE.CLOSE) {
                    try {                        
                        DeviceInfo.setModel(new EIAModel(devcontrol.GetDevConnectInfo().eia));
                        Button_LightEnable.setSelected(devcontrol.GetLightControl().IsLightEnable());
                    } catch (Exception ex) {
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        this.UpdateControlPaneState(this.devcontrol.GetControlState());
    }

    //更新控制界面状态
    private void UpdateControlPaneState(CSTATE state) {
        //工作状态，进度条自动激活
        this.workingProgress.setIndeterminate(state == CSTATE.BUSSY);

//        this.Button_AutoTestTime.setEnabled(state == CSTATE.CONNECT);
        this.Button_DarkModify.setEnabled(state == CSTATE.CONNECT);
        this.Button_FrequenCollect.setEnabled(state == CSTATE.CONNECT);
        this.Button_SetParameter.setEnabled(state == CSTATE.CONNECT);
        this.Button_SingelCollect.setEnabled(state == CSTATE.CONNECT);
        this.Button_StopCollect.setEnabled(state == CSTATE.BUSSY && this.devcontrol.GetDataCollecor().IsStartCollect());
        this.Button_LightEnable.setEnabled(state == CSTATE.CONNECT);

        this.window_Input.setEnabled(state == CSTATE.CONNECT);
        this.Average_Input.setEnabled(state == CSTATE.CONNECT);
        this.IntegeralTime_Input.setEnabled(state == CSTATE.CONNECT);
        this.IntervalTime_Input.setEnabled(state == CSTATE.CONNECT);
        this.ComboBox_CollectMode.setEnabled(state == CSTATE.CONNECT);
    }

    //初始化语言
    private void InitLanguange() {
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener() {

            @Override
            public void recevieEvent(Event event) {
                //输入框，表头title
                Frame_info.setTitle(LanguageHelper.getIntance().GetText("Lable_DevInfo"));
                //设备信息表更新语言
                ((AbstractTableModel) DeviceInfo.getModel()).fireTableDataChanged();

                //输入Label
                Label_IntergalTime.setText(LanguageHelper.getIntance().GetText("Label_IntergalTime"));
                Label_IntegeralTimeUnit.setText(LanguageHelper.getIntance().GetText("Label_IntegeralTimeUnit"));

                Label_IntervalTime.setText(LanguageHelper.getIntance().GetText("Label_IntervalTime"));
                Label_IntervalTimeUnit.setText(LanguageHelper.getIntance().GetText("Label_IntegevalTimeUnit"));

                Label_AverageTime.setText(LanguageHelper.getIntance().GetText("Label_AverageTime"));
                Label_AverageUnit.setText(LanguageHelper.getIntance().GetText("Label_AverageUnit"));

                Label_Window.setText(LanguageHelper.getIntance().GetText("Label_AvWindow"));
                Label_CollectMode.setText(LanguageHelper.getIntance().GetText("Label_CollectMode"));

                //按钮提示信息
//                Button_AutoTestTime.setToolTipText(LanguageHelper.getIntance().GetText("Button_AutoTestTime"));
                Button_LightEnable.setToolTipText(LanguageHelper.getIntance().GetText("Button_LightEnable"));
                Button_FrequenCollect.setToolTipText(LanguageHelper.getIntance().GetText("Button_FrequenCollect_ToolTip"));
                Button_SingelCollect.setToolTipText(LanguageHelper.getIntance().GetText("Button_SingelCollect_ToolTip"));
                Button_StopCollect.setToolTipText(LanguageHelper.getIntance().GetText("Button_StopCollect_ToolTip"));
                Button_SetParameter.setToolTipText(LanguageHelper.getIntance().GetText("Button_SetParameter_ToolTip"));
                Button_DarkModify.setToolTipText(LanguageHelper.getIntance().GetText("Button_DarkModify_ToolTip"));

                //采集模式
                ComboBox_CollectMode.setModel(new javax.swing.DefaultComboBoxModel(
                        new String[]{
                            LanguageHelper.getIntance().GetText("SoftMode"),
                            LanguageHelper.getIntance().GetText("UpEdge"),
                            LanguageHelper.getIntance().GetText("DowEdge"),
                            LanguageHelper.getIntance().GetText("HighVolt"),
                            LanguageHelper.getIntance().GetText("LowVolt")}));
            }
        });

    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="获取参数"> 
    //获取采集模式
    private int GetCollectMode() {
        int selectedIndex = this.ComboBox_CollectMode.getSelectedIndex();
        int ret = 0;
        switch (selectedIndex) {
            case 0:
                ret = SSDataCollectPar.SoftMode;
                break;
            case 1:
                ret = SSDataCollectPar.UpEdge;
                break;
            case 2:
                ret = SSDataCollectPar.DowEdge;
                break;
            case 3:
                ret = SSDataCollectPar.HighVolt;
                break;
            case 4:
                ret = SSDataCollectPar.LowVolt;
                break;
        }
        return ret;
    }

    //获取积分时间
    private int GetSmoothWindow() {
//        SpectralPar par = this.devcontrol.GetDevConfig().GetSPDevParameter();
//
//        if (par == null) {
//
//        }

        //获取积分时间,平均次数,采集模式
        int ret = Integer.valueOf(this.window_Input.getText());

        return ret;
    }

    //获取积分时间
    private float GetIntegertime() {
        //获取积分时间,平均次数,采集模式
        float ret = Float.valueOf(this.IntegeralTime_Input.getText());

        return ret;
    }

    //获取平均时间
    private int GetAverageTime() {
        //获取平均次数
        int ret = Integer.valueOf(this.Average_Input.getText());
        return ret;
    }

    //获取时间间隔
    private int GetIntervalTime() {
        //获取采样间隔
        float ret = Float.valueOf(this.IntervalTime_Input.getText());
        return (int) (ret * 1000);
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

        Frame_info = new javax.swing.JInternalFrame();
        jPanel2 = new javax.swing.JPanel();
        Button_StopCollect = new javax.swing.JButton();
        Button_SingelCollect = new javax.swing.JButton();
        Button_FrequenCollect = new javax.swing.JButton();
        Average_Input = new javax.swing.JTextField();
        IntegeralTime_Input = new javax.swing.JTextField();
        Label_IntergalTime = new javax.swing.JLabel();
        Label_AverageTime = new javax.swing.JLabel();
        Label_AverageUnit = new javax.swing.JLabel();
        Label_IntegeralTimeUnit = new javax.swing.JLabel();
        Button_SetParameter = new javax.swing.JButton();
        Label_IntervalTime = new javax.swing.JLabel();
        IntervalTime_Input = new javax.swing.JTextField();
        Label_IntervalTimeUnit = new javax.swing.JLabel();
        ComboBox_CollectMode = new javax.swing.JComboBox();
        Label_CollectMode = new javax.swing.JLabel();
        Button_DarkModify = new javax.swing.JToggleButton();
        Label_Window = new javax.swing.JLabel();
        window_Input = new javax.swing.JTextField();
        Button_LightEnable = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DeviceInfo = new javax.swing.JTable();
        workingProgress = new javax.swing.JProgressBar();

        Frame_info.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        Frame_info.setVisible(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Button_StopCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/stop.png"))); // NOI18N
        Button_StopCollect.setEnabled(false);
        Button_StopCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StopCollectActionPerformed(evt);
            }
        });

        Button_SingelCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/step.png"))); // NOI18N
        Button_SingelCollect.setEnabled(false);
        Button_SingelCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SingelCollectActionPerformed(evt);
            }
        });

        Button_FrequenCollect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/starting.png"))); // NOI18N
        Button_FrequenCollect.setEnabled(false);
        Button_FrequenCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_FrequenCollectActionPerformed(evt);
            }
        });

        Average_Input.setText("1");
        Average_Input.setEnabled(false);

        IntegeralTime_Input.setText("1");
        IntegeralTime_Input.setEnabled(false);

        Label_IntergalTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntergalTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_IntergalTime.setText("积分时间：");

        Label_AverageTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_AverageTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_AverageTime.setText("平均次数：");

        Label_AverageUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_AverageUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_AverageUnit.setText("time");

        Label_IntegeralTimeUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntegeralTimeUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_IntegeralTimeUnit.setText("ms");

        Button_SetParameter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/setting.png"))); // NOI18N
        Button_SetParameter.setEnabled(false);
        Button_SetParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SetParameterActionPerformed(evt);
            }
        });

        Label_IntervalTime.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntervalTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_IntervalTime.setText("采样间隔：");

        IntervalTime_Input.setText("0.1");
        IntervalTime_Input.setEnabled(false);

        Label_IntervalTimeUnit.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_IntervalTimeUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Label_IntervalTimeUnit.setText("s");

        ComboBox_CollectMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboBox_CollectMode.setEnabled(false);

        Label_CollectMode.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_CollectMode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_CollectMode.setText("采集模式：");

        Button_DarkModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/darkcollect.png"))); // NOI18N
        Button_DarkModify.setEnabled(false);
        Button_DarkModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_DarkModifyActionPerformed(evt);
            }
        });

        Label_Window.setFont(new java.awt.Font("微软雅黑", 0, 15)); // NOI18N
        Label_Window.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Window.setText("滑动窗口：");

        window_Input.setText("1");
        window_Input.setEnabled(false);

        Button_LightEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/device/resources/light_off.png"))); // NOI18N
        Button_LightEnable.setEnabled(false);
        Button_LightEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_LightEnableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Label_Window, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Label_CollectMode, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Label_AverageTime, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Label_IntervalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ComboBox_CollectMode, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(Label_IntergalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(window_Input, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Average_Input, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(IntegeralTime_Input, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(IntervalTime_Input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Label_AverageUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Label_IntegeralTimeUnit)
                                    .addComponent(Label_IntervalTimeUnit)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Button_SingelCollect)
                            .addComponent(Button_SetParameter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Button_FrequenCollect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Button_LightEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_StopCollect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_DarkModify, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Button_DarkModify, Button_FrequenCollect, Button_LightEnable, Button_SetParameter, Button_SingelCollect, Button_StopCollect});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Label_AverageTime, Label_CollectMode, Label_IntergalTime, Label_IntervalTime, Label_Window});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(IntervalTime_Input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Label_IntervalTime, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(Label_IntervalTimeUnit))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(Label_IntegeralTimeUnit)
                    .addComponent(IntegeralTime_Input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_IntergalTime))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Average_Input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_AverageTime, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(window_Input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_Window, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label_CollectMode, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ComboBox_CollectMode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Button_SingelCollect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Button_FrequenCollect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Button_StopCollect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Button_DarkModify, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(Label_AverageUnit)))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Button_LightEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_SetParameter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_DarkModify, Button_FrequenCollect, Button_LightEnable, Button_SetParameter, Button_SingelCollect, Button_StopCollect});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Label_AverageTime, Label_CollectMode, Label_IntergalTime, Label_IntervalTime, Label_Window});

        DeviceInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(DeviceInfo);

        javax.swing.GroupLayout Frame_infoLayout = new javax.swing.GroupLayout(Frame_info.getContentPane());
        Frame_info.getContentPane().setLayout(Frame_infoLayout);
        Frame_infoLayout.setHorizontalGroup(
            Frame_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(workingProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Frame_infoLayout.setVerticalGroup(
            Frame_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Frame_infoLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(workingProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Frame_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Frame_info)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Button_StopCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StopCollectActionPerformed
        //停止连续采集
        SpectralPlatService.GetInstance().GetAppManager().GetCollectControl().StopCollectData();
    }//GEN-LAST:event_Button_StopCollectActionPerformed

    private void Button_SingelCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SingelCollectActionPerformed
        try {
            //如果设备不忙，开始采集
            SpectralPlatService.GetInstance().GetAppManager().GetCollectControl().StartSingleTest(new SSDataCollectPar(this.GetIntegertime(), this.GetAverageTime(), this.GetCollectMode()),
                    this.GetSmoothWindow());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_Button_SingelCollectActionPerformed

    private void Button_FrequenCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_FrequenCollectActionPerformed
        try {
            //如果设备不忙，开始采集
            SpectralPlatService.GetInstance().GetAppManager().GetCollectControl().StartSustainCollect(new SSDataCollectPar(this.GetIntegertime(), this.GetAverageTime(), this.GetCollectMode()),
                    this.GetSmoothWindow(),
                    this.GetIntervalTime());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_Button_FrequenCollectActionPerformed

    private void Button_SetParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SetParameterActionPerformed
        //显示光谱仪参数设置
        new DeviceParSetDialog().setVisible(true);
    }//GEN-LAST:event_Button_SetParameterActionPerformed

    private void Button_DarkModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_DarkModifyActionPerformed
        try {
            this.devcontrol.GetDataCollecor().SetDkEnable(this.Button_DarkModify.isSelected());
            this.Button_DarkModify.setSelected((this.devcontrol.GetDataCollecor().IsDKEnable()));
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex.getMessage());
        }
    }//GEN-LAST:event_Button_DarkModifyActionPerformed

    private void Button_LightEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_LightEnableActionPerformed
        ISPDevLightControl lcontrol = this.devcontrol.GetLightControl();
        if (lcontrol != null) {
            try {
                lcontrol.EnableLight(Button_LightEnable.isSelected());

                Button_LightEnable.setSelected(lcontrol.IsLightEnable());
                if (lcontrol.IsLightEnable()) {
                    Button_LightEnable.setIcon(new javax.swing.ImageIcon(
                            getClass().getResource("/nexus/device/resources/light_on.png")));
                } else {
                    Button_LightEnable.setIcon(new javax.swing.ImageIcon(
                            getClass().getResource("/nexus/device/resources/light_off.png")));
                }
            } catch (Exception ex) {
                FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex.getMessage());
            }
        }
    }//GEN-LAST:event_Button_LightEnableActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Average_Input;
    private javax.swing.JToggleButton Button_DarkModify;
    private javax.swing.JButton Button_FrequenCollect;
    private javax.swing.JToggleButton Button_LightEnable;
    private javax.swing.JButton Button_SetParameter;
    private javax.swing.JButton Button_SingelCollect;
    private javax.swing.JButton Button_StopCollect;
    private javax.swing.JComboBox ComboBox_CollectMode;
    private javax.swing.JTable DeviceInfo;
    private javax.swing.JInternalFrame Frame_info;
    private javax.swing.JTextField IntegeralTime_Input;
    private javax.swing.JTextField IntervalTime_Input;
    private javax.swing.JLabel Label_AverageTime;
    private javax.swing.JLabel Label_AverageUnit;
    private javax.swing.JLabel Label_CollectMode;
    private javax.swing.JLabel Label_IntegeralTimeUnit;
    private javax.swing.JLabel Label_IntergalTime;
    private javax.swing.JLabel Label_IntervalTime;
    private javax.swing.JLabel Label_IntervalTimeUnit;
    private javax.swing.JLabel Label_Window;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField window_Input;
    private javax.swing.JProgressBar workingProgress;
    // End of variables declaration//GEN-END:variables
}
