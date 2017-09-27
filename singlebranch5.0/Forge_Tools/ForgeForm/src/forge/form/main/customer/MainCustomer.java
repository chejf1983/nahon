/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.form.main.customer;

import forge.bill.dev.NahonDevControl;
import forge.bill.dev.NahonDevControl.ControlState;
import forge.form.other.CommInfoDialog;
import forge.bill.platform.ForgeSystem;
import forge.form.seteia.CustomEIASetupPane;
import forge.form.update.UpdateForm;
import java.awt.CardLayout;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.io.libs.WindowsIOFactory;

/**
 *
 * @author jiche
 */
public class MainCustomer extends javax.swing.JFrame {

    /**
     * Creates new form CustomTool
     */
    public MainCustomer() {
        try {
            initComponents();
            //居中显示
            setLocationRelativeTo(null);

            WindowsIOFactory.InitWindowsIODriver();
            
            ForgeSystem.GetInstance().initSystem();
            ForgeSystem.GetInstance().systemConfig.SetInternalFlag(false);

            FaultCenter.Instance().RegisterEvent(new nahon.comm.event.EventListener<Level>() {
                @Override
                public void recevieEvent(Event<Level> event) {
                    JOptionPane.showMessageDialog(null, event.Info().toString(), "错误信息", ERROR_MESSAGE);
                }
            });

            this.MainArea.setLayout(workScreenLayout);

            this.MainArea.add(this.upform.getClass().getName(), this.upform);
            this.MainArea.add(this.aboutform.getClass().getName(), this.aboutform);
            this.MainArea.add(this.eiaform.getClass().getName(), this.eiaform);

            workScreenLayout.show(this.MainArea, aboutform.getClass().getName());

            ForgeSystem.GetInstance().devControl.StateCenter.RegeditListener(new EventListener<ControlState>() {
                @Override
                public void recevieEvent(Event<ControlState> event) {
                    updateState(event.GetEvent());
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(MainCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private UpdateForm upform = new UpdateForm(this);
    private CustomerAbout aboutform = new CustomerAbout();
    private CustomEIASetupPane eiaform = new CustomEIASetupPane(this);
    private CardLayout workScreenLayout = new CardLayout();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainArea = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        ConnectStateIcon = new javax.swing.JMenu();
        FileMenu = new javax.swing.JMenu();
        ExitMenuItem = new javax.swing.JMenuItem();
        MenuItem_Open = new javax.swing.JMenuItem();
        MenuItem_Close = new javax.swing.JMenuItem();
        Menu_eia = new javax.swing.JMenu();
        Menu_update = new javax.swing.JMenu();
        HelpMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Forge");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/forge/form/resource/FormIcon.png")));
        setLocationByPlatform(true);
        setName("Forge"); // NOI18N
        setResizable(false);

        MainArea.setPreferredSize(new java.awt.Dimension(320, 473));

        javax.swing.GroupLayout MainAreaLayout = new javax.swing.GroupLayout(MainArea);
        MainArea.setLayout(MainAreaLayout);
        MainAreaLayout.setHorizontalGroup(
            MainAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );
        MainAreaLayout.setVerticalGroup(
            MainAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );

        ConnectStateIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/forge/form/resource/disconnect_1.png"))); // NOI18N
        jMenuBar1.add(ConnectStateIcon);

        FileMenu.setText("设备");

        ExitMenuItem.setText("退出");
        ExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(ExitMenuItem);

        MenuItem_Open.setText("连接设备");
        MenuItem_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_OpenActionPerformed(evt);
            }
        });
        FileMenu.add(MenuItem_Open);

        MenuItem_Close.setText("断开设备");
        MenuItem_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_CloseActionPerformed(evt);
            }
        });
        FileMenu.add(MenuItem_Close);

        jMenuBar1.add(FileMenu);

        Menu_eia.setText("设备信息");
        Menu_eia.setEnabled(false);
        Menu_eia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Menu_eiaMouseClicked(evt);
            }
        });
        jMenuBar1.add(Menu_eia);

        Menu_update.setText("升级");
        Menu_update.setEnabled(false);
        Menu_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Menu_updateMouseClicked(evt);
            }
        });
        jMenuBar1.add(Menu_update);

        HelpMenu.setText("帮助");
        HelpMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HelpMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainArea, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(MainArea, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ExitMenuItemActionPerformed

    private void MenuItem_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_OpenActionPerformed
        ForgeSystem.GetInstance().devControl.DisConnect();

        if (ForgeSystem.GetInstance().devControl.State() == NahonDevControl.ControlState.DISCONNECT) {
            CommInfoDialog dilog = new CommInfoDialog(this, true);
            dilog.setVisible(true);
            if (dilog.GetIOInfo() != null) {
                ForgeSystem.GetInstance().devControl.Connect(dilog.GetIOInfo(), dilog.GetAddr());
            }
        }
    }//GEN-LAST:event_MenuItem_OpenActionPerformed

    private void updateState(ControlState state) {
        java.net.URL disurl;
        /* set device icon */
        if (state == ControlState.CONNECT) {
            disurl = MainCustomer.class.getResource("/forge/form/resource/connect_1.png");
            workScreenLayout.show(this.MainArea, this.eiaform.getClass().getName());
            this.Menu_eia.setEnabled(true);
            this.Menu_update.setEnabled(true);
            this.eiaform.UpdateEIA();
            this.upform.UpdateEIA();
            this.ConnectStateIcon.setIcon(new ImageIcon(disurl));
        } else if (state == ControlState.DISCONNECT) {
            disurl = MainCustomer.class.getResource("/forge/form/resource/disconnect_1.png");
            workScreenLayout.show(this.MainArea, this.aboutform.getClass().getName());
            this.Menu_eia.setEnabled(false);
            this.Menu_update.setEnabled(false);
            this.ConnectStateIcon.setIcon(new ImageIcon(disurl));
        }

    }

    private void MenuItem_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_CloseActionPerformed
        ForgeSystem.GetInstance().devControl.DisConnect();
    }//GEN-LAST:event_MenuItem_CloseActionPerformed

    private void HelpMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpMenuMouseClicked
        workScreenLayout.show(this.MainArea, aboutform.getClass().getName());
    }//GEN-LAST:event_HelpMenuMouseClicked

    private void Menu_eiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Menu_eiaMouseClicked
        workScreenLayout.show(this.MainArea, this.eiaform.getClass().getName());
    }//GEN-LAST:event_Menu_eiaMouseClicked

    private void Menu_updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Menu_updateMouseClicked
        // TODO add your handling code here:
        workScreenLayout.show(this.MainArea, this.upform.getClass().getName());
    }//GEN-LAST:event_Menu_updateMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainCustomer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu ConnectStateIcon;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JPanel MainArea;
    private javax.swing.JMenuItem MenuItem_Close;
    private javax.swing.JMenuItem MenuItem_Open;
    private javax.swing.JMenu Menu_eia;
    private javax.swing.JMenu Menu_update;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}