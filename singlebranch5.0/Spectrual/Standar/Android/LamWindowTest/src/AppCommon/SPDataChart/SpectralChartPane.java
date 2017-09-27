/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.tool.languange.LanguageHelper;
import org.jfree.data.xy.XYSeries;
import spdev.dev.data.SPData;

/**
 *
 * @author jiche
 */
public class SpectralChartPane extends javax.swing.JPanel {

    /**
     * Creates new form Wave_Pixel_AreaChartPane
     */
    public SpectralChartPane() {
        initComponents();

        ChartGroup.add(ToggleButton_EnableChartAtuoSize);
        ChartGroup.add(ToggleButton_EnableChartMaxSize);
        ChartGroup.add(ToggleButton_EnableChartManual);

        switch (this.datachart.chartrangePaint.GetRangeType()) {
            case AutoRange:
                this.ToggleButton_EnableChartAtuoSize.setSelected(true);
                break;
            case MaxRange:
                this.ToggleButton_EnableChartMaxSize.setSelected(true);
                break;
            case Manual:
                this.ToggleButton_EnableChartManual.setSelected(true);
                break;
            default:
        }
        this.UpdateLinePaint();
        this.RegisterLanguage();
    }

    private void RegisterLanguage() {
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener() {

            @Override
            public void recevieEvent(Event event) {
                ToggleButton_EnableChartAtuoSize.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_EnableChartAtuoSize_ToolTip"));
                ToggleButton_EnableChartMaxSize.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_EnableChartMaxSize_ToolTip"));
                ToggleButton_EnableChartManual.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_EnableChartManule_ToolTip"));
                ToggleButton_CrossHairEnable.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_CrossHairEnable_ToolTip"));
                ToggleButton_LineAreaEnable.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_CruveEnable_ToolTip"));
                ToggleButton_MaxPointEnable.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_SearchPlus_ToolTip"));
//                Button_ChartConfigure.setToolTipText(LanguageHelper.Instence.GetText("Button_ChartConfigure_ToolTip"));
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

        ChartGroup = new javax.swing.ButtonGroup();
        ToggleButton_EnableChartAtuoSize = new javax.swing.JToggleButton();
        ToggleButton_EnableChartMaxSize = new javax.swing.JToggleButton();
        ToggleButton_EnableChartManual = new javax.swing.JToggleButton();
        ToggleButton_CrossHairEnable = new javax.swing.JToggleButton();
        ToggleButton_LineAreaEnable = new javax.swing.JToggleButton();
        ToggleButton_MaxPointEnable = new javax.swing.JToggleButton();
        datachart = new AppCommon.SPDataChart.SpectralChart();
        ToggleButton_WaveEnable = new javax.swing.JToggleButton();

        ToggleButton_EnableChartAtuoSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/range_auto.png"))); // NOI18N
        ToggleButton_EnableChartAtuoSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartAtuoSizeActionPerformed(evt);
            }
        });

        ToggleButton_EnableChartMaxSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/range_max.png"))); // NOI18N
        ToggleButton_EnableChartMaxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartMaxSizeActionPerformed(evt);
            }
        });

        ToggleButton_EnableChartManual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/range_manual.png"))); // NOI18N
        ToggleButton_EnableChartManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartManualActionPerformed(evt);
            }
        });

        ToggleButton_CrossHairEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/cross_hair.png"))); // NOI18N
        ToggleButton_CrossHairEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_CrossHairEnableActionPerformed(evt);
            }
        });

        ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/line_simple.png"))); // NOI18N
        ToggleButton_LineAreaEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_LineAreaEnableActionPerformed(evt);
            }
        });

        ToggleButton_MaxPointEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/line_maxpoint.png"))); // NOI18N
        ToggleButton_MaxPointEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_MaxPointEnableActionPerformed(evt);
            }
        });

        ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/Pixel.png"))); // NOI18N
        ToggleButton_WaveEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_WaveEnableItemStateChanged(evt);
            }
        });
        ToggleButton_WaveEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_WaveEnableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ToggleButton_EnableChartAtuoSize, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_EnableChartMaxSize, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_EnableChartManual, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_CrossHairEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_LineAreaEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_WaveEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(ToggleButton_MaxPointEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datachart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ToggleButton_EnableChartAtuoSize, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_EnableChartManual, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_EnableChartMaxSize, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_CrossHairEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_LineAreaEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_MaxPointEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToggleButton_WaveEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datachart, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ToggleButton_EnableChartAtuoSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartAtuoSizeActionPerformed
        this.datachart.chartrangePaint.SetAutoRange();
//        this.datachart.RePaintMainLine();
    }//GEN-LAST:event_ToggleButton_EnableChartAtuoSizeActionPerformed

    private void ToggleButton_EnableChartMaxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartMaxSizeActionPerformed
        this.datachart.chartrangePaint.SetMaxRange(this.datachart.chartrangePaint.MaxRange,
                this.datachart.chartrangePaint.MinRange);
//        this.datachart.RePaintMainLine();
    }//GEN-LAST:event_ToggleButton_EnableChartMaxSizeActionPerformed

    private void ToggleButton_EnableChartManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartManualActionPerformed
        ManualRangeDialog dlg = new ManualRangeDialog(null, true);
        dlg.SetCurrentRange(this.datachart.xyplot.getRangeAxis().getRange(),
                this.datachart.xyplot.getDomainAxis().getRange());
        dlg.setVisible(true);
        if (dlg.IsOk()) {
            this.datachart.chartrangePaint.SetManualRange(
                    dlg.GetDomain().getLowerBound(), dlg.GetDomain().getUpperBound(),
                    dlg.GetRange().getLowerBound(), dlg.GetRange().getUpperBound());

        } else {
            this.datachart.chartrangePaint.ChangeType(ChartRangePaint.RangeType.Manual);
        }
//        this.datachart.chartrangePaint.SetRangeType(ChartRangePaint.RangeType.Manual);
    }//GEN-LAST:event_ToggleButton_EnableChartManualActionPerformed

    private void ToggleButton_CrossHairEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_CrossHairEnableActionPerformed
        this.datachart.crossHairPaint.EnableCrossHairPaint(this.ToggleButton_CrossHairEnable.isSelected());
    }//GEN-LAST:event_ToggleButton_CrossHairEnableActionPerformed

    private void ToggleButton_LineAreaEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_LineAreaEnableActionPerformed
        if (this.ToggleButton_LineAreaEnable.isSelected()) {
            ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/line_area.png")));
        } else {
            ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/line_simple.png")));
        }
        this.UpdateLinePaint();
    }//GEN-LAST:event_ToggleButton_LineAreaEnableActionPerformed

    private void ToggleButton_MaxPointEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_MaxPointEnableActionPerformed
        this.datachart.maxPointPainter.EnableMaxCheck(ToggleButton_MaxPointEnable.isSelected());
    }//GEN-LAST:event_ToggleButton_MaxPointEnableActionPerformed

    private void ToggleButton_WaveEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_WaveEnableActionPerformed

    }//GEN-LAST:event_ToggleButton_WaveEnableActionPerformed

    private void ToggleButton_WaveEnableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_WaveEnableItemStateChanged
        if (this.ToggleButton_WaveEnable.isSelected()) {
            ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/Wave.png")));
        } else {
            ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AppCommon/Resource/Pixel.png")));
        }
        this.UpdateLinePaint();
        this.ChangeWaveAndPixelData();
    }//GEN-LAST:event_ToggleButton_WaveEnableItemStateChanged

    public javax.swing.JToggleButton GetWaveEnableButton() {
        return this.ToggleButton_WaveEnable;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup ChartGroup;
    private javax.swing.JToggleButton ToggleButton_CrossHairEnable;
    private javax.swing.JToggleButton ToggleButton_EnableChartAtuoSize;
    private javax.swing.JToggleButton ToggleButton_EnableChartManual;
    private javax.swing.JToggleButton ToggleButton_EnableChartMaxSize;
    private javax.swing.JToggleButton ToggleButton_LineAreaEnable;
    private javax.swing.JToggleButton ToggleButton_MaxPointEnable;
    private javax.swing.JToggleButton ToggleButton_WaveEnable;
    private AppCommon.SPDataChart.SpectralChart datachart;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc="DisplayInterface Code">
    private XYSeries BuildXYSeries(String name, SPData data) {
        XYSeries line = new XYSeries(name);
        for (int i = 0; i < data.datavalue.length; i++) {
            if (this.ToggleButton_WaveEnable.isSelected()) {
                line.add(data.waveIndex[i], data.datavalue[i]);
            } else {
                line.add(data.pixelIndex[i], data.datavalue[i]);
            }
        }
        return line;

    }

    private SPData lastdata;
    private String maindataname;

    public void DisplaySPData(String name, SPData data) {
        if (name == null || data == null) {
            return;
        }
        this.lastdata = data;
        this.maindataname = name;

        datachart.DisplayMainData(this.BuildXYSeries(name, data));
    }
    private SPData[] snapshotdata = new SPData[0];
    private String[] snapdataname = new String[0];

    public void DisplaySnapShot(String[] name, SPData[] data) {
        if (name == null || data == null) {
            this.datachart.DisplaySnapShot(null);
            return;
        }
        this.snapdataname = name;
        this.snapshotdata = data;

        XYSeries[] spdata = new XYSeries[this.snapdataname.length];
        for (int i = 0;
                i < spdata.length;
                i++) {
            spdata[i] = this.BuildXYSeries(snapdataname[i], snapshotdata[i]);
        }
        this.datachart.DisplaySnapShot(spdata);
    }

    public void SetMaxRange(double maxRange, double minRange) {
        this.datachart.chartrangePaint.MaxRange = maxRange;
        this.datachart.chartrangePaint.MinRange = minRange;
    }

    public BufferedImage GetChartPanePNG() {
        BufferedImage bufferedImage = new BufferedImage(this.datachart.dataChartPane.getWidth(), this.datachart.dataChartPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.datachart.dataChartPane.paint(bufferedImage.createGraphics());
        return bufferedImage;
    }

    public FreeTextPaint GetFreeTextPaint() {
        return this.datachart.freeTextPaint;
    }

    public void SetTitle(String name) {
        this.datachart.SetTitle(name);
    }

    public JPanel GetChartPane() {
        return this.datachart.dataChartPane;
    }
    // </editor-fold> 

    private void ChangeWaveAndPixelData() {
        this.DisplaySPData(this.maindataname, lastdata);

        XYSeries[] spdata = new XYSeries[this.snapdataname.length];
        for (int i = 0; i < spdata.length; i++) {
            spdata[i] = this.BuildXYSeries(snapdataname[i], snapshotdata[i]);
        }
        this.datachart.DisplaySnapShot(spdata);
    }

    private void UpdateLinePaint() {
        boolean areaenable = this.ToggleButton_LineAreaEnable.isSelected();
        boolean waveenable = this.ToggleButton_WaveEnable.isSelected();

        if (areaenable) {
            if (waveenable) {
                this.datachart.mainLinePaint.SetLineType(MainLineShapePaint.MainLineType.Wave_Area);
            } else {
                this.datachart.mainLinePaint.SetLineType(MainLineShapePaint.MainLineType.Pixel_Area);
            }
        } else {
            this.datachart.mainLinePaint.SetLineType(MainLineShapePaint.MainLineType.Line);
        }
    }

}