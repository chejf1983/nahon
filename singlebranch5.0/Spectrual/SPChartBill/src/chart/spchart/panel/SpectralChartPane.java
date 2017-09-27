/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.panel;

import java.awt.CardLayout;
import chart.spchart.chart.ChartRangePaint;
import chart.spchart.chart.FloatTextBuilder;
import chart.spchart.chart.MainLineShapePaint;
import chart.spchart.chart.ManualRangeDialog;
import java.awt.image.BufferedImage;
import java.util.Properties;
import javax.swing.JPanel;
import nahon.comm.event.Event;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.comm.tool.languange.LanguageHelper;
import chart.data.CSPData;
import chart.spchart.chart.SpectralChart;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author jiche
 */
public class SpectralChartPane extends javax.swing.JPanel {

    //全局颜色显示事件
    public static EventCenter GlobalColorEvent = new EventCenter();
    public static Properties Config = new Properties();
    public static void SetProperties (Properties input){
        Config = input;
        GlobalColorEvent.CreateEvent(null);
    }
    
    private SpectralChart datachart = new SpectralChart();
    /**
     * Creates new form Wave_Pixel_AreaChartPane
     */
    public SpectralChartPane() {
        initComponents();

        this.chart_area.setLayout(new CardLayout());
        this.chart_area.add(this.datachart);
        
        this.InitButtonGroup();
        
        this.UpdateLinePaint();
        
        this.RegisterLanguage();
    }

    //初始化按键组
    private void InitButtonGroup() {
        //图标坐标缩放三按钮
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

        /* 初始化背景颜色 */
        this.datachart.SetBackgroundColor(ChartColorConfig.GetChartBackGroundColor());
        GlobalColorEvent.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        datachart.SetBackgroundColor(ChartColorConfig.GetChartBackGroundColor());
                        datachart.mainLinePaint.SetColor(ChartColorConfig.GetMainLineColor());
                    }
                });
            }
        });
    }

    //刷新曲线颜色设置按钮
    private void UpdateLinePaint() {
        //波长颜色显示
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

    //注册语言包
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
                ToggleButton_WaveEnable.setToolTipText(LanguageHelper.getIntance().GetText("ToggleButton_WaveEnable_ToolTip"));
                Button_ChartConfigure.setToolTipText(LanguageHelper.getIntance().GetText("Button_ChartConfigure_ToolTip"));

                SetTitle(chartname);
//                    dataChart.setTitle(new TextTitle(LanguageHelper.getIntance().GetText(ChartName),
//                            new Font("Microsoft YaHei", 0, 15)));

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
        ToggleButton_WaveEnable = new javax.swing.JToggleButton();
        Button_ChartConfigure = new javax.swing.JButton();
        chart_area = new javax.swing.JPanel();

        ToggleButton_EnableChartAtuoSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/range_auto.png"))); // NOI18N
        ToggleButton_EnableChartAtuoSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartAtuoSizeActionPerformed(evt);
            }
        });

        ToggleButton_EnableChartMaxSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/range_max.png"))); // NOI18N
        ToggleButton_EnableChartMaxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartMaxSizeActionPerformed(evt);
            }
        });

        ToggleButton_EnableChartManual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/range_manual.png"))); // NOI18N
        ToggleButton_EnableChartManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_EnableChartManualActionPerformed(evt);
            }
        });

        ToggleButton_CrossHairEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/cross_hair.png"))); // NOI18N
        ToggleButton_CrossHairEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_CrossHairEnableActionPerformed(evt);
            }
        });

        ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/line_simple.png"))); // NOI18N
        ToggleButton_LineAreaEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_LineAreaEnableActionPerformed(evt);
            }
        });

        ToggleButton_MaxPointEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/line_maxpoint.png"))); // NOI18N
        ToggleButton_MaxPointEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButton_MaxPointEnableActionPerformed(evt);
            }
        });

        ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/Pixel.png"))); // NOI18N
        ToggleButton_WaveEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ToggleButton_WaveEnableItemStateChanged(evt);
            }
        });

        Button_ChartConfigure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/configuration.png"))); // NOI18N
        Button_ChartConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_ChartConfigureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chart_areaLayout = new javax.swing.GroupLayout(chart_area);
        chart_area.setLayout(chart_areaLayout);
        chart_areaLayout.setHorizontalGroup(
            chart_areaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chart_areaLayout.setVerticalGroup(
            chart_areaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addComponent(ToggleButton_MaxPointEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Button_ChartConfigure, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(chart_area, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Button_ChartConfigure, ToggleButton_MaxPointEnable});

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
                    .addComponent(ToggleButton_WaveEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_ChartConfigure, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart_area, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_ChartConfigure, ToggleButton_MaxPointEnable});

    }// </editor-fold>//GEN-END:initComponents

    private void ToggleButton_EnableChartAtuoSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartAtuoSizeActionPerformed
        this.datachart.chartrangePaint.ChangeType(ChartRangePaint.RangeType.AutoRange);
    }//GEN-LAST:event_ToggleButton_EnableChartAtuoSizeActionPerformed

    private void ToggleButton_EnableChartMaxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartMaxSizeActionPerformed
        this.datachart.chartrangePaint.SetFixRangeMaxValue(this.datachart.chartrangePaint.MaxRange,
                this.datachart.chartrangePaint.MinRange);
    }//GEN-LAST:event_ToggleButton_EnableChartMaxSizeActionPerformed

    private void ToggleButton_EnableChartManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_EnableChartManualActionPerformed
        ManualRangeDialog dlg = new ManualRangeDialog(null, true);
        dlg.setLocationRelativeTo(ToggleButton_EnableChartManual);
        dlg.SetCurrentRange(this.datachart.xyplot.getRangeAxis().getRange(),
                this.datachart.xyplot.getDomainAxis().getRange());
        dlg.setVisible(true);
        if (dlg.IsOk()) {
            this.datachart.chartrangePaint.SetManualRange(
                    dlg.GetXRange().getLowerBound(), dlg.GetXRange().getUpperBound(),
                    dlg.GetYRange().getLowerBound(), dlg.GetYRange().getUpperBound());

        }
    }//GEN-LAST:event_ToggleButton_EnableChartManualActionPerformed

    private void ToggleButton_CrossHairEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_CrossHairEnableActionPerformed
        this.datachart.crossHairPaint.EnableCrossHairPaint(this.ToggleButton_CrossHairEnable.isSelected());
    }//GEN-LAST:event_ToggleButton_CrossHairEnableActionPerformed

    private void ToggleButton_LineAreaEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_LineAreaEnableActionPerformed
        if (this.ToggleButton_LineAreaEnable.isSelected()) {
            ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/line_area.png")));
        } else {
            ToggleButton_LineAreaEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/line_simple.png")));
        }
        this.UpdateLinePaint();
    }//GEN-LAST:event_ToggleButton_LineAreaEnableActionPerformed

    private void ToggleButton_MaxPointEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButton_MaxPointEnableActionPerformed
        this.datachart.maxPointPainter.EnableMaxCheck(ToggleButton_MaxPointEnable.isSelected());
    }//GEN-LAST:event_ToggleButton_MaxPointEnableActionPerformed

    private void ToggleButton_WaveEnableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ToggleButton_WaveEnableItemStateChanged
        if (this.ToggleButton_WaveEnable.isSelected()) {
            ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/Wave.png")));
        } else {
            ToggleButton_WaveEnable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nexus/chart/resource/Pixel.png")));
        }
        this.UpdateLinePaint();
        this.RePaintMainAndSnapShot();
    }//GEN-LAST:event_ToggleButton_WaveEnableItemStateChanged

    private void Button_ChartConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_ChartConfigureActionPerformed
        new ChartConfigDialog(null, true).setVisible(true);
    }//GEN-LAST:event_Button_ChartConfigureActionPerformed

    // <editor-fold defaultstate="collapsed" desc="公共对外接口">
    public javax.swing.JToggleButton GetWaveEnableButton() {
        return this.ToggleButton_WaveEnable;
    }

    // <editor-fold defaultstate="collapsed" desc="显示曲线接口">
    private XYSeries BuildXYSeries(String name, CSPData data) {
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

    //重绘曲线
    private void RePaintMainAndSnapShot() {
        if (this.maindataname != null) {
            this.DisplaySPData(this.maindataname, lastdata);
        }

        XYSeries[] spdata = new XYSeries[this.snapdataname.length];
        for (int i = 0; i < spdata.length; i++) {
            spdata[i] = this.BuildXYSeries(snapdataname[i], snapshotdata[i]);
        }
        this.datachart.DisplaySnapShot(spdata);
    }

    // <editor-fold defaultstate="collapsed" desc="显示主曲线">
    private CSPData lastdata;
    private String maindataname;

    public void DisplaySPData(String name, CSPData data) {
        if (name == null || data == null) {
            return;
        }
        this.lastdata = data;
        this.maindataname = name;

        datachart.DisplayMainData(this.BuildXYSeries(name, data));
    }
    // </editor-fold>     

    // <editor-fold defaultstate="collapsed" desc="显示副曲线">
    private CSPData[] snapshotdata = new CSPData[0];
    private String[] snapdataname = new String[0];

    public void DisplaySnapShot(String[] name, CSPData[] data) {
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
    // </editor-fold> 

    public BufferedImage GetChartPanePNG() {
        BufferedImage bufferedImage = new BufferedImage(this.datachart.dataChartPane.getWidth(), this.datachart.dataChartPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.datachart.dataChartPane.paint(bufferedImage.createGraphics());
        return bufferedImage;
    }
    // </editor-fold> 

    //获取浮动文字设置器
    public FloatTextBuilder GetFreeTextPaint() {
        return this.datachart.freeTextPaint;
    }

    String chartname = "";

    //设置标题
    public void SetTitle(String name) {
        if (!"".equals(name)) {
            chartname = name;
            this.datachart.SetTitle(LanguageHelper.getIntance().GetText(name));
        }
    }

    public JPanel GetChartPane() {
        return this.datachart.dataChartPane;
    }
    // </editor-fold> 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_ChartConfigure;
    private javax.swing.ButtonGroup ChartGroup;
    private javax.swing.JToggleButton ToggleButton_CrossHairEnable;
    private javax.swing.JToggleButton ToggleButton_EnableChartAtuoSize;
    private javax.swing.JToggleButton ToggleButton_EnableChartManual;
    private javax.swing.JToggleButton ToggleButton_EnableChartMaxSize;
    private javax.swing.JToggleButton ToggleButton_LineAreaEnable;
    private javax.swing.JToggleButton ToggleButton_MaxPointEnable;
    private javax.swing.JToggleButton ToggleButton_WaveEnable;
    private javax.swing.JPanel chart_area;
    // End of variables declaration//GEN-END:variables

}
