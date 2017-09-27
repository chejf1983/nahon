/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.main.entry;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.tool.languange.LanguageHelper;
import nahon.comm.tool.languange.LanguageNode;
import nexus.app.stander.CalSoildCollect;
import nexus.app.stander.SoildCollct;
import nexus.device.control.LeftPane;
import nexus.main.compent.AboutDialog;
import sps.app.solapp.CollectControl.SAppState;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.ifs.ISPDevControl.CSTATE;
import sps.platform.SoildSystem;
import sps.platform.SpectralPlatService;
import sps.platform.SystemConfig;

/**
 *
 * @author jiche
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();

        FaultCenter.Instance().RegisterEvent(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                JOptionPane.showMessageDialog(MainForm.this, event.Info().toString());
            }
        });
        
        //初始化界面
        this.InitUI();

        //初始化
        this.InitApplication();

        //初始化设备控制
        this.InitDevControl();

        //触发语言
        this.TrigerLanguage();

        //全屏
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // <editor-fold defaultstate="collapsed" desc="UI初始化"> 
    private void InitUI() {
        this.setLocationRelativeTo(null);

        this.SetFormIcon();

        this.InitDeviceArea();

        this.InitTailPane();

        this.RegistItemLanguage();
    }

    private void SetFormIcon() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.net.URL disurl = MainForm.class.getResource("/nexus/main/resource/SN.png");
        java.awt.Image image = tk.createImage(disurl);
        this.setIconImage(image);

        String flag = SpectralPlatService.GetInstance().GetConfig().getProperty(SystemConfig.InternalFlag, "1");
        if (Integer.valueOf(flag) == 1) {
            this.setTitle("SpectralNexus-Internal");
//            this.MenuItem_SynCalibrate.setVisible(true);
        } else {
            this.setTitle("SpectralNexus");
//            this.MenuItem_SynCalibrate.setVisible(false);
        }

    }

    private LeftPane devmanager = new LeftPane();

    private void InitDeviceArea() {
        this.SplitPane_main.setLeftComponent(devmanager);
        this.SplitPane_main.setResizeWeight(0);
        this.SplitPane_main.setOneTouchExpandable(false);
        this.SplitPane_main.setEnabled(false);
    }

    private void RegistItemLanguage() {
        this.LanguageGroup.add(this.MenuItem_Chinese);
        this.LanguageGroup.add(this.MenuItem_English);
        this.LanguageGroup.add(this.MenuItem_Japanese);

        switch (LanguageHelper.getIntance().GetLanguage()) {
            case Chinese:
                this.MenuItem_Chinese.setSelected(true);
                break;
            case English:
                this.MenuItem_English.setSelected(true);
                break;
            case Japanese:
                this.MenuItem_Japanese.setSelected(true);
                break;
        }

        LanguageHelper.getIntance().EventCenter.RegeditListener(
                new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                MainForm.this.MenuItem_Chinese.setText(LanguageHelper.getIntance().GetText("MenuItem_Chinese"));
                MainForm.this.MenuItem_English.setText(LanguageHelper.getIntance().GetText("MenuItem_English"));
                MainForm.this.MenuItem_Japanese.setText(LanguageHelper.getIntance().GetText("MenuItem_Japanese"));

//                MainForm.this.Menu_Application.setText(LanguageHelper.getIntance().GetText("Menu_Application"));
//                MainForm.this.MenuItem_Source.setText(LanguageHelper.getIntance().GetText("MenuItem_Source"));
//                MainForm.this.MenuItem_Color.setText(LanguageHelper.getIntance().GetText("MenuItem_Color"));
//                MainForm.this.MenuItem_Reflact.setText(LanguageHelper.getIntance().GetText("MenuItem_Transmission"));
//                MainForm.this.MenuItem_Absorbe.setText(LanguageHelper.getIntance().GetText("MenuItem_Absorbe"));
//                MainForm.this.MenuItem_SynCalibrate.setText(LanguageHelper.getIntance().GetText("MenuItem_SynCalibrate"));
//                MainForm.this.MenuItem_UPF.setText(LanguageHelper.getIntance().GetText("MenuItem_UPF"));
//                MainForm.this.MenuItem_Mv.setText(LanguageHelper.getIntance().GetText("MenuItem_Mv"));
                MainForm.this.Menu_File.setText(LanguageHelper.getIntance().GetText("Menu_File"));
                MainForm.this.Menu_Language.setText(LanguageHelper.getIntance().GetText("Menu_Language"));
                MainForm.this.MenuItem_Exit.setText(LanguageHelper.getIntance().GetText("MenuItem_Exit"));

                MainForm.this.Menu_config.setText(LanguageHelper.getIntance().GetText("Menu_config"));
                MainForm.this.MenuItem_Filter.setText(LanguageHelper.getIntance().GetText("MenuItem_Filter"));
                MainForm.this.MenuItem_LinearEnable.setText(LanguageHelper.getIntance().GetText("ToggleButton_LineCalibrate"));

                MainForm.this.Menu_Help.setText(LanguageHelper.getIntance().GetText("Menu_Help"));
                MainForm.this.MenuItem_SerialNum.setText(LanguageHelper.getIntance().GetText("MenuItem_SerialNum"));
                MainForm.this.MenuItem_About.setText(LanguageHelper.getIntance().GetText("MenuItem_About"));
            }
        });
    }

    private void TrigerLanguage() {
        LanguageHelper.getIntance().SetLanguage(LanguageHelper.getIntance().GetLanguage());
    }

    private void InitTailPane() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Label_SystemTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            }
        }, 0, 1000);

    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Application MenutItem Init"> 
    private String Flag_SourcePane = "SourceApplicationPane";
    private String Flag_CalPane = "CalApplicationPane";
    private CardLayout applicationAreaLayout = new CardLayout();

    private void InitApplication() {
        this.ApplicationArea.setLayout(applicationAreaLayout);
        ApplicationArea.add(Flag_SourcePane, new SoildCollct());
        ApplicationArea.add(Flag_CalPane, new CalSoildCollect());

        SoildSystem.GetInstance().GetSoilControl().StateCenter.RegeditListener(new EventListener<SAppState>() {
            @Override
            public void recevieEvent(Event<SAppState> event) {
                if (event.GetEvent() == SAppState.Test) {
                    applicationAreaLayout.show(ApplicationArea, Flag_SourcePane);
                } else {
                    applicationAreaLayout.show(ApplicationArea, Flag_CalPane);
                }
            }
        });
    }
    // </editor-fold> 

    private ISPDevControl devcontrol = SpectralPlatService.GetInstance().GetDevManager().GetDevControl();

    private void InitDevControl() {
        devcontrol.RegisterStateChange(new EventListener<CSTATE>() {
            @Override
            public void recevieEvent(Event<CSTATE> event) {
                MenuItem_Filter.setEnabled(event.GetEvent() != CSTATE.CLOSE);
                MenuItem_LinearEnable.setEnabled(event.GetEvent() != CSTATE.CLOSE);
                if (event.GetEvent() == CSTATE.CONNECT && event.Info() == CSTATE.CLOSE) {
                    MenuItem_Filter.setSelected(devcontrol.GetDataCollecor().IsFilterEnable());
                    MenuItem_LinearEnable.setSelected(devcontrol.GetDataCollecor().IsLinearEnable());
                }
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

        LanguageGroup = new javax.swing.ButtonGroup();
        ApplicationGroup = new javax.swing.ButtonGroup();
        SplitPane_main = new javax.swing.JSplitPane();
        DeviceArea = new javax.swing.JPanel();
        ApplicationArea1 = new javax.swing.JPanel();
        ApplicationArea = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Label_SystemTime = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        Menu_File = new javax.swing.JMenu();
        Menu_Language = new javax.swing.JMenu();
        MenuItem_Chinese = new javax.swing.JRadioButtonMenuItem();
        MenuItem_English = new javax.swing.JRadioButtonMenuItem();
        MenuItem_Japanese = new javax.swing.JRadioButtonMenuItem();
        MenuItem_Exit = new javax.swing.JMenuItem();
        Menu_config = new javax.swing.JMenu();
        MenuItem_Filter = new javax.swing.JCheckBoxMenuItem();
        MenuItem_LinearEnable = new javax.swing.JCheckBoxMenuItem();
        Menu_Help = new javax.swing.JMenu();
        MenuItem_SerialNum = new javax.swing.JMenuItem();
        MenuItem_About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1100, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        SplitPane_main.setResizeWeight(1.0);

        DeviceArea.setPreferredSize(new java.awt.Dimension(100, 555));

        javax.swing.GroupLayout DeviceAreaLayout = new javax.swing.GroupLayout(DeviceArea);
        DeviceArea.setLayout(DeviceAreaLayout);
        DeviceAreaLayout.setHorizontalGroup(
            DeviceAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 345, Short.MAX_VALUE)
        );
        DeviceAreaLayout.setVerticalGroup(
            DeviceAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        SplitPane_main.setLeftComponent(DeviceArea);

        javax.swing.GroupLayout ApplicationAreaLayout = new javax.swing.GroupLayout(ApplicationArea);
        ApplicationArea.setLayout(ApplicationAreaLayout);
        ApplicationAreaLayout.setHorizontalGroup(
            ApplicationAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 882, Short.MAX_VALUE)
        );
        ApplicationAreaLayout.setVerticalGroup(
            ApplicationAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ApplicationArea1Layout = new javax.swing.GroupLayout(ApplicationArea1);
        ApplicationArea1.setLayout(ApplicationArea1Layout);
        ApplicationArea1Layout.setHorizontalGroup(
            ApplicationArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ApplicationArea1Layout.createSequentialGroup()
                .addComponent(ApplicationArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        ApplicationArea1Layout.setVerticalGroup(
            ApplicationArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ApplicationArea1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ApplicationArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SplitPane_main.setRightComponent(ApplicationArea1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Label_SystemTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_SystemTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_SystemTime, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_SystemTime, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        Menu_File.setText("File");

        Menu_Language.setText("Language");

        MenuItem_Chinese.setSelected(true);
        MenuItem_Chinese.setText("Chinese");
        MenuItem_Chinese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_ChineseActionPerformed(evt);
            }
        });
        Menu_Language.add(MenuItem_Chinese);

        MenuItem_English.setSelected(true);
        MenuItem_English.setText("English");
        MenuItem_English.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_EnglishActionPerformed(evt);
            }
        });
        Menu_Language.add(MenuItem_English);

        MenuItem_Japanese.setSelected(true);
        MenuItem_Japanese.setText("Japanese");
        MenuItem_Japanese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_JapaneseActionPerformed(evt);
            }
        });
        Menu_Language.add(MenuItem_Japanese);

        Menu_File.add(Menu_Language);

        MenuItem_Exit.setText("Exit");
        MenuItem_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_ExitActionPerformed(evt);
            }
        });
        Menu_File.add(MenuItem_Exit);

        jMenuBar1.add(Menu_File);

        Menu_config.setText("设置");

        MenuItem_Filter.setText("滤波使能");
        MenuItem_Filter.setEnabled(false);
        MenuItem_Filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_FilterActionPerformed(evt);
            }
        });
        Menu_config.add(MenuItem_Filter);

        MenuItem_LinearEnable.setText("非线性使能");
        MenuItem_LinearEnable.setEnabled(false);
        MenuItem_LinearEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_LinearEnableActionPerformed(evt);
            }
        });
        Menu_config.add(MenuItem_LinearEnable);

        jMenuBar1.add(Menu_config);

        Menu_Help.setText("Help");

        MenuItem_SerialNum.setText("序列号");
        MenuItem_SerialNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_SerialNumActionPerformed(evt);
            }
        });
        Menu_Help.add(MenuItem_SerialNum);

        MenuItem_About.setText("关于");
        MenuItem_About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_AboutActionPerformed(evt);
            }
        });
        Menu_Help.add(MenuItem_About);

        jMenuBar1.add(Menu_Help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SplitPane_main, javax.swing.GroupLayout.DEFAULT_SIZE, 1234, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SplitPane_main, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuItem_ChineseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_ChineseActionPerformed
        LanguageHelper.getIntance().SetLanguage(LanguageNode.Language.Chinese);
    }//GEN-LAST:event_MenuItem_ChineseActionPerformed

    private void MenuItem_EnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_EnglishActionPerformed
        LanguageHelper.getIntance().SetLanguage(LanguageNode.Language.English);
    }//GEN-LAST:event_MenuItem_EnglishActionPerformed

    private void MenuItem_JapaneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_JapaneseActionPerformed
        LanguageHelper.getIntance().SetLanguage(LanguageNode.Language.Japanese);
    }//GEN-LAST:event_MenuItem_JapaneseActionPerformed

    private void MenuItem_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_MenuItem_ExitActionPerformed

    private void MenuItem_AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_AboutActionPerformed
//        new AboutDialog(this, true).setVisible(true);
    }//GEN-LAST:event_MenuItem_AboutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // SoildSystem.Instance.Close();
    }//GEN-LAST:event_formWindowClosing

    private void MenuItem_FilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_FilterActionPerformed
        if (devcontrol.GetControlState() != CSTATE.CLOSE) {
            devcontrol.GetDataCollecor().SetFilterEnable(MenuItem_Filter.isSelected());
            MenuItem_Filter.setSelected(devcontrol.GetDataCollecor().IsFilterEnable());
        }
    }//GEN-LAST:event_MenuItem_FilterActionPerformed

    private void MenuItem_LinearEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_LinearEnableActionPerformed
        if (devcontrol.GetControlState() != CSTATE.CLOSE) {
            devcontrol.GetDataCollecor().SetLinearEnable(MenuItem_LinearEnable.isSelected());
        }
        MenuItem_LinearEnable.setSelected(devcontrol.GetDataCollecor().IsLinearEnable());
    }//GEN-LAST:event_MenuItem_LinearEnableActionPerformed

    private void MenuItem_SerialNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_SerialNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuItem_SerialNumActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApplicationArea;
    private javax.swing.JPanel ApplicationArea1;
    private javax.swing.ButtonGroup ApplicationGroup;
    private javax.swing.JPanel DeviceArea;
    private javax.swing.JLabel Label_SystemTime;
    private javax.swing.ButtonGroup LanguageGroup;
    private javax.swing.JMenuItem MenuItem_About;
    private javax.swing.JRadioButtonMenuItem MenuItem_Chinese;
    private javax.swing.JRadioButtonMenuItem MenuItem_English;
    private javax.swing.JMenuItem MenuItem_Exit;
    private javax.swing.JCheckBoxMenuItem MenuItem_Filter;
    private javax.swing.JRadioButtonMenuItem MenuItem_Japanese;
    private javax.swing.JCheckBoxMenuItem MenuItem_LinearEnable;
    private javax.swing.JMenuItem MenuItem_SerialNum;
    private javax.swing.JMenu Menu_File;
    private javax.swing.JMenu Menu_Help;
    private javax.swing.JMenu Menu_Language;
    private javax.swing.JMenu Menu_config;
    private javax.swing.JSplitPane SplitPane_main;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
