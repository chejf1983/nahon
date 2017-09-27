/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dev.window;

import nahon.comm.tool.convert.MyConvert;
import test.dev.basedev.MockDev;
import nahon.comm.event.EventListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import nahon.drv.data.EquipmentInfo;
import static nahon.pro.migp.MIGPCOMMEM.*;
import test.dev.basedev.TCPHost;

/**
 *
 * @author jiche
 */
public final class MockeDevForm extends javax.swing.JFrame {

    private MockDev mockdev;
    private TCPHost hosttcp = new TCPHost();

    /**
     * Creates new form MockerDevice
     */
    public MockeDevForm() {
        initComponents();

        this.InitMockeDevForm();
    }

    private void InitMockeDevForm() {
        this.SetMockDev(new MockDev((byte) 0x03));

        this.ComboBox_MEM.removeAllItems();
        for (String it : MemoryConvert.GetCMDList()) {
            this.ComboBox_MEM.addItem(it);
        }
        this.ComboBox_INPUTTYPE.removeAllItems();
        for (String it : MemoryConvert.GetInputType()) {
            this.ComboBox_INPUTTYPE.addItem(it);
        }

        try {
            this.HostIP.setText(InetAddress.getLocalHost().getHostAddress());
            this.PortNum.setText("2000");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MockeDevForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetMockDev(MockDev dev) {
        this.mockdev = dev;
    }

    public void Open() throws Exception {
        if (this.hosttcp.IsClosed()) {
            this.hosttcp.Listen(this.HostIP.getText(),
                    Integer.valueOf(this.PortNum.getText()),
                    mockdev);

            this.State.setText("Open");

            this.InitEIAInfo();
        }
    }

    public void Close() {
        if (!this.hosttcp.IsClosed()) {
            this.hosttcp.Close();
        }
        this.State.setText("Close");
    }

    private EquipmentInfo eia;

    public void InitEIAInfo() {

        this.eia = this.ReadEia();

        this.EIA_Table.setModel(new AbstractTableModel() {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public int getRowCount() {
                return 5;
            }

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    byte[] tmp;
                    switch (rowIndex) {
                        case 0:
                            return eia.DeviceName;
                        case 1:
                            return eia.BuildDate;
                        case 2:
                            return eia.BuildSerialNum;
                        case 3:
                            return eia.Hardversion;
                        case 4:
                            return eia.SoftwareVersion;

                        case 5:
                            break;
                    }
                }
                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    try {
                        switch (rowIndex) {
                            case 0:
                                mockdev.memory.SetValue(mockdev.memory.EIA, DeviceName.addr, DeviceName.length,
                                        MyConvert.StringToByte(aValue.toString(), DeviceName.length));
                                break;
                            case 1:
                                mockdev.memory.SetValue(mockdev.memory.EIA, BuildDate.addr, BuildDate.length,
                                        MyConvert.StringToByte(aValue.toString(), BuildDate.length));
                                break;
                            case 2:
                                mockdev.memory.SetValue(mockdev.memory.EIA, BuildSerialNum.addr, BuildSerialNum.length,
                                        MyConvert.StringToByte(aValue.toString(), BuildSerialNum.length));
                                break;
                            case 3:
                                mockdev.memory.SetValue(mockdev.memory.EIA, Hardversion.addr, Hardversion.length,
                                        MyConvert.StringToByte(aValue.toString(), Hardversion.length));
                                break;
                            case 4:
                                mockdev.memory.SetValue(mockdev.memory.EIA, SoftwareVersion.addr, SoftwareVersion.length,
                                        MyConvert.StringToByte(aValue.toString(), SoftwareVersion.length));
                                break;
                            case 5:
                                break;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(MockeDevForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });

        mockdev.memory.UpdateMem.RegeditListener(new EventListener() {

            @Override
            public void recevieEvent(nahon.comm.event.Event event) {
                if (event.GetEvent() == mockdev.memory.EIA) {

                }
            }
        });
    }

    private EquipmentInfo ReadEia() {
        EquipmentInfo teia = new EquipmentInfo();
        try {
            byte[] tmp;
            tmp = mockdev.memory.GetValue(mockdev.memory.EIA, DeviceName.addr, DeviceName.length);
            teia.DeviceName = MyConvert.ByteArrayToString(tmp, 0, tmp.length);
            tmp = mockdev.memory.GetValue(mockdev.memory.EIA, BuildDate.addr, BuildDate.length);
            teia.BuildDate = MyConvert.ByteArrayToString(tmp, 0, tmp.length);

            tmp = mockdev.memory.GetValue(mockdev.memory.EIA, BuildSerialNum.addr, BuildSerialNum.length);
            teia.BuildSerialNum = MyConvert.ByteArrayToString(tmp, 0, tmp.length);

            tmp = mockdev.memory.GetValue(mockdev.memory.EIA, Hardversion.addr, Hardversion.length);
            teia.Hardversion = MyConvert.ByteArrayToString(tmp, 0, tmp.length);

            tmp = mockdev.memory.GetValue(mockdev.memory.EIA, SoftwareVersion.addr, SoftwareVersion.length);
            teia.SoftwareVersion = MyConvert.ByteArrayToString(tmp, 0, tmp.length);
        } catch (Exception ex) {

        }

        return teia;
    }

    private byte[] ConvertCMD(String cmd) {
        switch (cmd) {
            case "EIA":
                return this.mockdev.memory.EIA;
            case "VPA":
                return this.mockdev.memory.VPA;
            case "NVPA":
                return this.mockdev.memory.NVPA;
            case "MDA":
                return this.mockdev.memory.MDA;
            case "SRA":
                return this.mockdev.memory.SRA;
        }
        return this.mockdev.memory.EIA;
    }

    private void SetValue() {
        try {
            mockdev.memory.SetValue(ConvertCMD(ComboBox_MEM.getSelectedItem().toString()),
                    Integer.valueOf(MEM_ADDR.getText()), Integer.valueOf(MEM_LEN.getText()),
                    new MemoryConvert().ConvertMemToByte(ComboBox_INPUTTYPE.getSelectedItem().toString(), InputMem.getText()));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            Logger.getLogger(MockeDevForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Start = new javax.swing.JButton();
        Close = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Button_SetEIA = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ComboBox_MEM = new javax.swing.JComboBox();
        MEM_ADDR = new javax.swing.JTextField();
        MEM_LEN = new javax.swing.JTextField();
        ComboBox_INPUTTYPE = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InputMem = new javax.swing.JTextArea();
        SET = new javax.swing.JButton();
        ToggleButtonRadom = new javax.swing.JToggleButton();
        HostIP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        PortNum = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        State = new javax.swing.JLabel();
        GET = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        EIA_Table = new javax.swing.JTable();
        SET1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Start.setText("Start");
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartActionPerformed(evt);
            }
        });

        Close.setText("Close");
        Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseActionPerformed(evt);
            }
        });

        jLabel1.setText("EIA Info");

        Button_SetEIA.setText("SetEIA");
        Button_SetEIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SetEIAActionPerformed(evt);
            }
        });

        jLabel2.setText("SetMem");

        ComboBox_MEM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        MEM_ADDR.setText("2");

        MEM_LEN.setText("4");

        ComboBox_INPUTTYPE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("MEM");

        jLabel4.setText("Addr");

        jLabel5.setText("type");

        jLabel6.setText("len");

        InputMem.setColumns(20);
        InputMem.setRows(5);
        jScrollPane1.setViewportView(InputMem);

        SET.setText("Set");
        SET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SETActionPerformed(evt);
            }
        });

        ToggleButtonRadom.setText("RadomInput");
        ToggleButtonRadom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButtonRadomActionPerformed(evt);
            }
        });

        HostIP.setText("jTextField1");

        jLabel7.setText("HostIp:");

        jLabel8.setText("PortNum:");

        PortNum.setText("jTextField1");

        jLabel9.setText("State:");

        State.setText("State:");

        GET.setText("Get");
        GET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GETActionPerformed(evt);
            }
        });

        EIA_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(EIA_Table);

        SET1.setText("Set");
        SET1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SET1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Hex MemControl:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Button_SetEIA)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane1)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(ToggleButtonRadom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(SET1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(GET, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(SET, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(ComboBox_MEM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel3))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addComponent(MEM_ADDR, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(51, 51, 51))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(MEM_LEN, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(ComboBox_INPUTTYPE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel5)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(State))
                                            .addComponent(jLabel2)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8)
                                                    .addComponent(jLabel7))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(HostIP, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                                    .addComponent(PortNum))))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Start)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Close)
                        .addGap(9, 9, 9))))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {HostIP, PortNum});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_SetEIA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBox_MEM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MEM_ADDR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MEM_LEN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboBox_INPUTTYPE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SET)
                    .addComponent(ToggleButtonRadom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SET1)
                    .addComponent(GET)
                    .addComponent(jLabel10))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(HostIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(PortNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Start)
                    .addComponent(Close)
                    .addComponent(jLabel9)
                    .addComponent(State))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartActionPerformed
        try {
            Open();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_StartActionPerformed

    private void CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseActionPerformed
        this.Close();
    }//GEN-LAST:event_CloseActionPerformed

    private void Button_SetEIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SetEIAActionPerformed

    }//GEN-LAST:event_Button_SetEIAActionPerformed

    private void SETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SETActionPerformed
        SetValue();
    }//GEN-LAST:event_SETActionPerformed

    private boolean state = false;
    private void ToggleButtonRadomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButtonRadomActionPerformed
        if (this.ToggleButtonRadom.isSelected() != state) {
            state = this.ToggleButtonRadom.isSelected();
            if (state == true) {
                ExecutorService thread = Executors.newSingleThreadExecutor();
                thread.submit(new Runnable() {

                    @Override
                    public void run() {
                        Random R = new Random();
                        while (state) {
                            float input = R.nextFloat() * 100;
                            InputMem.setText(new DecimalFormat("0.00").format(input));
                            SetValue();
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (Exception ex) {
                            }
                        }
                    }
                });
                thread.shutdown();
            }
        }
    }//GEN-LAST:event_ToggleButtonRadomActionPerformed

    private void GETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GETActionPerformed
        try {
            byte[] value = mockdev.memory.GetValue(ConvertCMD(ComboBox_MEM.getSelectedItem().toString()),
                    Integer.valueOf(MEM_ADDR.getText()), Integer.valueOf(MEM_LEN.getText()));

            String output = "";
            int i = 0;
            for (byte b : value) {
                output += String.format(" 0x%02x", b);
                i++;
                if (i % 10 == 0) {
                    output += "\r\n";
                }
            }

            this.InputMem.setText(output);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            Logger.getLogger(MockeDevForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_GETActionPerformed

    private void SET1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SET1ActionPerformed
        try {
            mockdev.memory.SetValue(ConvertCMD(ComboBox_MEM.getSelectedItem().toString()),
                    Integer.valueOf(MEM_ADDR.getText()), Integer.valueOf(MEM_LEN.getText()),
                    new MemoryConvert().ConvertMemToByte("byte[]", InputMem.getText()));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            Logger.getLogger(MockeDevForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SET1ActionPerformed

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
            java.util.logging.Logger.getLogger(MockeDevForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MockeDevForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MockeDevForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MockeDevForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockeDevForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_SetEIA;
    private javax.swing.JButton Close;
    private javax.swing.JComboBox ComboBox_INPUTTYPE;
    private javax.swing.JComboBox ComboBox_MEM;
    private javax.swing.JTable EIA_Table;
    private javax.swing.JButton GET;
    private javax.swing.JTextField HostIP;
    private javax.swing.JTextArea InputMem;
    private javax.swing.JTextField MEM_ADDR;
    private javax.swing.JTextField MEM_LEN;
    private javax.swing.JTextField PortNum;
    private javax.swing.JButton SET;
    private javax.swing.JButton SET1;
    private javax.swing.JButton Start;
    private javax.swing.JLabel State;
    private javax.swing.JToggleButton ToggleButtonRadom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
