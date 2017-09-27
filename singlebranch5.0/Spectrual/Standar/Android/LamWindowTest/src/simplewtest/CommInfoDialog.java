/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplewtest;

//import USBDriver.USBLib;
import USBDriver.USBLib;
import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.CommPortIdentifier;
import javax.swing.JPanel;
import nahon.absractio.IOInfo;
import nahon.comm.io.libs.WindowsIOFactory;

/**
 *
 * @author jiche
 */
public class CommInfoDialog extends javax.swing.JDialog {
//    public static Color BackGroundColor = obw.form.main.ObserverMainForm.BackGroundColor;
    public static Color FontColor = Color.WHITE;
    /**
     * Creates new form CommInfoDialog
     */
    public CommInfoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setUndecorated(true);
        initComponents();

        this.InitInfoDialogSkin();

        InitComParameter();
        InitUSBParameter();
        InitTCPParameter();
    }

    private void InitInfoDialogSkin() {
        setLocationRelativeTo(null);
//        this.getContentPane().setBackground(BackGroundColor);
        ((JPanel) this.getContentPane()).setBorder(
                javax.swing.BorderFactory.createLineBorder(FontColor, 1, true));

//        this.Panel_COM.setBackground(BackGroundColor);
//        this.Panel_USB.setBackground(BackGroundColor);
//        this.Panel_TCP.setBackground(BackGroundColor);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CommInfoTabbed = new javax.swing.JTabbedPane();
        Panel_COM = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        BaundRateInput = new javax.swing.JComboBox();
        CommNameInput = new javax.swing.JComboBox();
        Panel_USB = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        USBNameInput = new javax.swing.JComboBox();
        Panel_TCP = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Ipaddr = new javax.swing.JTextField();
        portNum = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COM Name:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Baundrate:");

        BaundRateInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CommNameInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout Panel_COMLayout = new javax.swing.GroupLayout(Panel_COM);
        Panel_COM.setLayout(Panel_COMLayout);
        Panel_COMLayout.setHorizontalGroup(
            Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_COMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BaundRateInput, 0, 104, Short.MAX_VALUE)
                    .addComponent(CommNameInput, 0, 104, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        Panel_COMLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BaundRateInput, CommNameInput});

        Panel_COMLayout.setVerticalGroup(
            Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_COMLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(CommNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_COMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BaundRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        CommInfoTabbed.addTab("COM", Panel_COM);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("USB Port:");

        USBNameInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout Panel_USBLayout = new javax.swing.GroupLayout(Panel_USB);
        Panel_USB.setLayout(Panel_USBLayout);
        Panel_USBLayout.setHorizontalGroup(
            Panel_USBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_USBLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(USBNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        Panel_USBLayout.setVerticalGroup(
            Panel_USBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_USBLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(Panel_USBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(USBNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        CommInfoTabbed.addTab("USB", Panel_USB);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ServerIp:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PortNum:");

        javax.swing.GroupLayout Panel_TCPLayout = new javax.swing.GroupLayout(Panel_TCP);
        Panel_TCP.setLayout(Panel_TCPLayout);
        Panel_TCPLayout.setHorizontalGroup(
            Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TCPLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Ipaddr, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portNum, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_TCPLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Ipaddr, portNum});

        Panel_TCPLayout.setVerticalGroup(
            Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TCPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(Ipaddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_TCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        CommInfoTabbed.addTab("TCP", Panel_TCP);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cannel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CommInfoTabbed)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(CommInfoTabbed, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void oBButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oBButton1MouseClicked
        this.OKAction();        // TODO add your handling code here:
    }//GEN-LAST:event_oBButton1MouseClicked

    private void oBButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oBButton2MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_oBButton2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.OKAction();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();        // TODO 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void OKAction() {
        switch (this.CommInfoTabbed.getSelectedIndex()) {
            case 0:
                try {
                    String comname = this.CommNameInput.getSelectedItem().toString();
                    int baundrate = Integer.valueOf(this.BaundRateInput.getSelectedItem().toString());

                    this.par = new IOInfo(WindowsIOFactory.IOTYPE.COM.toString(), comname, String.valueOf(baundrate));
                } catch (Exception ex) {
                } finally {
                    break;
                }
            case 1:
                try {
                    String usbdev = this.USBNameInput.getSelectedItem().toString();

                    this.par = new IOInfo(WindowsIOFactory.IOTYPE.USB.toString(), usbdev);
                } catch (Exception ex) {
                } finally {
                    break;
                }
            case 2:
                try {
                    String ipaddr = this.Ipaddr.getText();
                    int portnum = Integer.valueOf(this.portNum.getText());

                    this.par = new IOInfo(WindowsIOFactory.IOTYPE.TCP.toString(), ipaddr, String.valueOf(portnum));
                } catch (Exception ex) {
                    this.Ipaddr.setText("127.0.0.1");
                    this.portNum.setText(String.valueOf(2000));
                } finally {
                    break;
                }
            default:
                break;
        }

        if (this.par != null) {
            this.dispose();
        }
    }
    private IOInfo par = null;

    public IOInfo GetIOInfo() {
        return this.par;
    }

    private final int[] Baundrates = new int[]{9600, 12800};

    private void InitUSBParameter() {

        if (!USBLib.IsInitLib()) {
            try {
                USBLib.InitLib();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }

        this.USBNameInput.removeAllItems();

        for (int i : USBLib.SearchUSBDev()) {
            this.USBNameInput.addItem(i);
        }
    }

    private void InitComParameter() {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        /* Clean USB and Comm Input */
        this.CommNameInput.removeAllItems();

        /* Foud Comm port */
        while (portList.hasMoreElements()) {
            CommPortIdentifier comportId = (CommPortIdentifier) portList.nextElement();

            /* If name is start with NT, it is an virtual USB comm port */
            this.CommNameInput.addItem(comportId.getName());

        }

        /* Init Baundrate */
        this.BaundRateInput.removeAllItems();

        for (int baundrate : Baundrates) {
            this.BaundRateInput.addItem(baundrate);
        }
    }

    private void InitTCPParameter() {
        try {
            this.Ipaddr.setText(InetAddress.getLocalHost().getHostAddress());
            this.portNum.setText("2000");
        } catch (UnknownHostException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox BaundRateInput;
    private javax.swing.JTabbedPane CommInfoTabbed;
    private javax.swing.JComboBox CommNameInput;
    private javax.swing.JTextField Ipaddr;
    private javax.swing.JPanel Panel_COM;
    private javax.swing.JPanel Panel_TCP;
    private javax.swing.JPanel Panel_USB;
    private javax.swing.JComboBox USBNameInput;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField portNum;
    // End of variables declaration//GEN-END:variables
}