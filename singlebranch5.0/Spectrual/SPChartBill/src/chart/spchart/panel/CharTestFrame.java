/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.panel;

import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.UIManager;
import nahon.comm.tool.languange.LanguageHelper;
import nahon.comm.tool.languange.LanguageNode;
import chart.data.CSPData;
import org.jfree.chart.ChartUtilities;

/**
 *
 * @author jiche
 */
public class CharTestFrame extends javax.swing.JFrame {

    private SpectralChartPane spectralChartPane2;

    /**
     * Creates new form TestFrame
     */
    public CharTestFrame() {
        initComponents();
        
//        LanguageHelper.SetLanguangeFile("./");

        spectralChartPane2 = new SpectralChartPane();
//        ((JPanel)jTabbedPane1.getComponentAt(1)).setOpaque(true);
//        jTabbedPane1.setOpaque(true);
//        spectralChartPane2.setOpaque(false);

        /* Add Micorvolumne Floating Panel */
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.spectralChartPane2.GetChartPane().setLayout(gridBagLayout);
//        MvFloatPane mvFloatPane = new MvFloatPane();
//        this.spectralChartPane2.GetChartPane().add(mvFloatPane, new GBC(1,0,1,1)
//                .setWeight(1, 1)
//                .setAnchor(GridBagConstraints.NORTHEAST)
//                .setInsets(40, 0, 0, 40)
//        );

        jTabbedPane1.add(spectralChartPane2);
//        jTabbedPane1.add(new MvFloatPane());

        System.out.println(UIManager.get("TabbedPane.contentOpaque"));
//        jTabbedPane1.getUI

        if (jTabbedPane1.isOpaque()) {
            System.out.println("true");
        }
        LanguageHelper.getIntance().SetLanguage(LanguageNode.Language.Chinese);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("create data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("setcolor");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("+S");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("-S");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("savepng");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("inputtext");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("removeinput");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addGap(0, 46, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private double[] BuidSinData() {
//        id++;

        double node[] = new double[1024];

        if (isincrease) {
            this.id++;
        } else {
            this.id--;
        }

        if (this.id >= 50) {
            isincrease = false;
        } else if (this.id <= 0) {
            isincrease = true;
        }

        for (int i = 0; i < node.length; i++) {
            node[i] = i;
            node[i] = Math.sin((float) (i + (10 * (this.id % 256))) / (float) 60);
            node[i] += 1;
            node[i] *= (500 * (this.id));
        }

        return node;
    }
    private boolean isincrease = true;
    int id = 0;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        double[] data = this.BuidSinData();
        double[] index = new double[data.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = i + 100;
        }
        this.spectralChartPane2.DisplaySPData("testmain", new CSPData(index, data));

        if (jToggleButton1.isSelected()) {
            jButton1.setEnabled(false);
            Executors.newSingleThreadExecutor().submit(new Runnable() {

                @Override
                public void run() {
                    while (jToggleButton1.isSelected()) {
                        double[] data = BuidSinData();
                        double[] index = new double[data.length];
                        for (int i = 0; i < index.length; i++) {
                            index[i] = i + 100;
                        }
                        spectralChartPane2.DisplaySPData("testmain", new CSPData(index, data));
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CharTestFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    jButton1.setEnabled(true);
                }
            });
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new ChartConfigDialog(this, true).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    ArrayList<CSPData> spdata = new ArrayList();
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        double[] data = this.BuidSinData();
        double[] index = new double[data.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = i + 100;
        }

        spdata.add(new CSPData(index, data));
        String name[] = new String[spdata.size()];
        for (int i = 0; i < name.length; i++) {
            name[i] = "HS-" + i;
        }
        this.spectralChartPane2.DisplaySnapShot(name, spdata.toArray(new CSPData[0]));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (spdata.size() > 0) {
            spdata.remove(0);
        }
        String name[] = new String[spdata.size()];
        for (int i = 0; i < name.length; i++) {
            name[i] = "HS-" + i;
        }
        this.spectralChartPane2.DisplaySnapShot(name, spdata.toArray(new CSPData[0]));
    }//GEN-LAST:event_jButton4ActionPerformed

    private BufferedImage mergeImage(BufferedImage img1,
            BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

        // 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            DestImage = new BufferedImage(w1, h1 + h2,
                    BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }

        return DestImage;
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            File file = FileDialogHelp.GetFilePath(".png");
            if (file != null) {
                try (FileOutputStream bout = new java.io.FileOutputStream(file)) {
                    ChartUtilities.writeBufferedImageAsPNG(bout,
                            this.mergeImage(this.spectralChartPane2.GetChartPanePNG(),
                                    this.spectralChartPane2.GetChartPanePNG(), true));
                    bout.flush();
                }
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private ArrayList<JLabel> ret = new ArrayList();
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        ret.add(this.spectralChartPane2.GetFreeTextPaint().CreateNewLable("test input"));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.spectralChartPane2.GetFreeTextPaint().RemoveLable(ret.remove(0));
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
 /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
//                System.out.println(UIManager.get("TabbedPane.contentOpaque"));
//            javax.swing.UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel");
//            ().
//                UIManager.put("TabbedPane.contentOpaque", true);
//                System.out.println(UIManager.get("TabbedPane.contentOpaque"));
//                UIManager.put("TabbedPane.contentOpaque", true);
//                System.out.println(UIManager.get("TabbedPane.contentOpaque"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CharTestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CharTestFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
