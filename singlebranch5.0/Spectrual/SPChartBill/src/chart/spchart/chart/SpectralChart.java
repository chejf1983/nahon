/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import chart.spchart.chart.ChartRangePaint;
import chart.spchart.chart.CrossHairPaint;
import chart.spchart.chart.FloatTextBuilder;
import chart.spchart.chart.MainLineShapePaint;
import chart.spchart.chart.MaxPointPaint;
import java.awt.Paint;
import java.awt.CardLayout;
import java.awt.Font;
import nahon.comm.event.EventCenter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author jiche
 */
public final class SpectralChart extends javax.swing.JPanel {

    /* Init temperature chart */

    public JFreeChart dataChart;
    public ChartPanel dataChartPane;
    public XYPlot xyplot;

    public MaxPointPaint maxPointPainter;
    public MainLineShapePaint mainLinePaint;
    public ChartRangePaint chartrangePaint;
    public CrossHairPaint crossHairPaint;
    public FloatTextBuilder freeTextPaint;

    /**
     * Creates new form ChartPane
     *
     */
    public SpectralChart() {//, double AutoBound, double HighBoundLevel,double LowBoundLevel) {
        initComponents();
        /* Init Data Chart and data */
        this.InitChart();

        //初始化main line绘制器
        this.mainLinePaint = new MainLineShapePaint(this);
        //初始化main line 最峰值绘制器
        this.maxPointPainter = new MaxPointPaint(this);
        //初始化图表上下限修改器
        this.chartrangePaint = new ChartRangePaint(this);

        //初始化快照背景数据
        this.InitSnapShotDataRender();

        //初始化标线绘制器
        this.crossHairPaint = new CrossHairPaint(this);
        //初始化浮动文字生成器
        this.freeTextPaint = new FloatTextBuilder(this);

    }

    // <editor-fold defaultstate="collapsed" desc="初始化">  
    /**
     * Init data chart
     */
    private void InitChart() {
        /* 创建线性图表 */
        dataChart = ChartFactory.createXYLineChart(
                "",
                "",
                "",
                null,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);
        xyplot = (XYPlot) this.dataChart.getPlot();

        this.dataChartPane = new ChartPanel(dataChart);
        //关闭弹出窗口
        dataChartPane.setPopupMenu(null);
        dataChartArea.setLayout(new CardLayout());
        dataChartArea.add(dataChartPane);
    }

    //表格背景
    public void SetBackgroundColor(Paint paint) {
        xyplot.setBackgroundPaint(paint);
    }

    /**
     *
     * @param name
     */
    public void SetTitle(String name) {
        this.dataChart.setTitle(new TextTitle(name, new Font("Microsoft YaHei", 0, 15)));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="主曲线">  
    public static int MainLineDataSetIndex = 0;
    public static int MaxLineDataSetIndex = 1;
    public XYSeries mainData = new XYSeries(0);
    public EventCenter<XYSeries> UpdateMainDataEvent = new EventCenter();

    public void DisplayMainData(XYSeries mainData) {
        this.mainData = mainData;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        if (mainData != null) {
            xySeriesCollection.addSeries(mainData);
        }
        xyplot.setDataset(0, xySeriesCollection);
        this.UpdateMainDataEvent.CreateEvent(mainData);
    }

    void RePaintMainLine() {
        this.UpdateMainDataEvent.CreateEvent(mainData);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="辅曲线"> 
    /**
     * display snap short, on xyplot dataset 1
     */
    public XYSeries[] snapshot = new XYSeries[0];
    public static int SnapShotDataSetIndex = 2;

    private void InitSnapShotDataRender() {
        this.xyplot.setRenderer(SnapShotDataSetIndex, new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false));
    }

    public void DisplaySnapShot(XYSeries[] dataline) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        this.snapshot = dataline;
        if (dataline != null) {
            for (int i = 0; i < snapshot.length; i++) {
                xySeriesCollection.addSeries(snapshot[i]);
            }
        }
        this.xyplot.setDataset(SnapShotDataSetIndex, xySeriesCollection);
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

        dataChartArea = new javax.swing.JPanel();

        javax.swing.GroupLayout dataChartAreaLayout = new javax.swing.GroupLayout(dataChartArea);
        dataChartArea.setLayout(dataChartAreaLayout);
        dataChartAreaLayout.setHorizontalGroup(
            dataChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        dataChartAreaLayout.setVerticalGroup(
            dataChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dataChartArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dataChartArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataChartArea;
    // End of variables declaration//GEN-END:variables
}