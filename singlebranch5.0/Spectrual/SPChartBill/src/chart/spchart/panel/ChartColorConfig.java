/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.panel;

import java.awt.Color;
import java.awt.Paint;
import javax.swing.JColorChooser;

/** 
 *
 * @author Administrator
 */

public final class ChartColorConfig extends javax.swing.JPanel {
    /**
     * Creates new form ColorSet
     */
    public ChartColorConfig() {
        initComponents();
        this.chartLine_ColorSample.setBackground((Color) GetMainLineColor());
        this.chartBackgound_ColorSample.setBackground((Color) GetChartBackGroundColor());
    }
    

    public static String LineColorKey = "SpColor";
    public static String DefaultLineColor = "#00FF00";
    public static Paint GetMainLineColor(){        
        return Color.decode(SpectralChartPane.Config.getProperty(LineColorKey, DefaultLineColor));
    }
    
    public static String BKColorKey = "BGroundColor";
    public static String DefaultBKColor = "#808080";
    public static Paint GetChartBackGroundColor(){        
        return Color.decode(SpectralChartPane.Config.getProperty(BKColorKey, DefaultBKColor));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton_SpColorSet = new javax.swing.JButton();
        jButton_BGroundColorSet = new javax.swing.JButton();
        chartLine_ColorSample = new javax.swing.JTextField();
        chartBackgound_ColorSample = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPasswordField1.setText("jPasswordField1");

        jButton_SpColorSet.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jButton_SpColorSet.setText("光谱颜色设置");
        jButton_SpColorSet.setBorder(null);
        jButton_SpColorSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SpColorSetActionPerformed(evt);
            }
        });

        jButton_BGroundColorSet.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        jButton_BGroundColorSet.setText("背景颜色设置");
        jButton_BGroundColorSet.setBorder(null);
        jButton_BGroundColorSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BGroundColorSetActionPerformed(evt);
            }
        });

        chartLine_ColorSample.setEditable(false);
        chartLine_ColorSample.setBackground(new java.awt.Color(51, 255, 0));

        chartBackgound_ColorSample.setEditable(false);
        chartBackgound_ColorSample.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setForeground(new java.awt.Color(255, 0, 51));

        jLabel2.setBackground(new java.awt.Color(204, 0, 0));
        jLabel2.setForeground(new java.awt.Color(204, 0, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_SpColorSet, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(jButton_BGroundColorSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chartLine_ColorSample, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chartBackgound_ColorSample, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(43, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(114, 114, 114))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(124, 124, 124))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_SpColorSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chartLine_ColorSample, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_BGroundColorSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chartBackgound_ColorSample, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private String ColorToString(Color color) {
        String R = Integer.toHexString(color.getRed());
        R = R.length() < 2 ? ('0' + R) : R;
        String B = Integer.toHexString(color.getBlue());
        B = B.length() < 2 ? ('0' + B) : B;
        String G = Integer.toHexString(color.getGreen());
        G = G.length() < 2 ? ('0' + G) : G;
        return '#' + R + G + B;
    }

    private void setColor(String key, String value) {
        SpectralChartPane.Config.setProperty(key, value);
        SpectralChartPane.GlobalColorEvent.CreateEvent(value);
    }

    private void jButton_SpColorSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SpColorSetActionPerformed
        Color color = JColorChooser.showDialog(null, "选择需要设定的光谱颜色", null);
        if (color == null) {
            color = Color.GREEN;
        }
        
        setColor(ChartColorConfig.LineColorKey, ColorToString(color));
        this.chartLine_ColorSample.setBackground(color);
    }//GEN-LAST:event_jButton_SpColorSetActionPerformed

    private void jButton_BGroundColorSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BGroundColorSetActionPerformed
        Color color = JColorChooser.showDialog(null, "选择需要设定的背景颜色", null);
        if (color == null) {
            color = Color.GRAY;
        }
        setColor(ChartColorConfig.BKColorKey, ColorToString(color));
        this.chartBackgound_ColorSample.setBackground(color);
    }//GEN-LAST:event_jButton_BGroundColorSetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField chartBackgound_ColorSample;
    public javax.swing.JTextField chartLine_ColorSample;
    private javax.swing.JButton jButton_BGroundColorSet;
    private javax.swing.JButton jButton_SpColorSet;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    // End of variables declaration//GEN-END:variables
}