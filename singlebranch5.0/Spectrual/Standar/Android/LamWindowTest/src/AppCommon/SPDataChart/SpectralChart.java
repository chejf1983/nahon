/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Paint;
import nahon.comm.event.Event;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;
import nahon.comm.tool.languange.LanguageHelper;
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

    public static int MainLineDataSetIndex = 0;
    public static int MaxLineDataSetIndex = 1;
    public static int SnapShotDataSetIndex = 2;
    public JFreeChart dataChart;
    public ChartPanel dataChartPane;
    public XYPlot xyplot;

    public MaxPointPaint maxPointPainter;
    public MainLineShapePaint mainLinePaint;
    public ChartRangePaint chartrangePaint;
    public CrossHairPaint crossHairPaint;
    public FreeTextPaint freeTextPaint;

    /**
     * Creates new form ChartPane
     *
     */
    public SpectralChart() {//, double AutoBound, double HighBoundLevel,double LowBoundLevel) {
        initComponents();
        /* Init Data Chart and data */
        this.InitChart();

        //初始化main line绘制器
        mainLinePaint = new MainLineShapePaint(this);
        //初始化main line 最大值绘制器
        maxPointPainter = new MaxPointPaint(this);
        //初始化图表上下限修改器
        chartrangePaint = new ChartRangePaint(this);

        //初始化快照背景数据
        this.InitSnapShotDataRender();

        this.crossHairPaint = new CrossHairPaint(this);
        this.freeTextPaint = new FreeTextPaint(this);
//        this.InitFreeText(chartpane);
    }

    // <editor-fold defaultstate="collapsed" desc="Init Pane Component">  
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
        dataChartPane.setPopupMenu(null);
        dataChartArea.setLayout(new CardLayout());
        dataChartArea.add(dataChartPane);
        
        /* 初始化背景颜色 */
//        SetBackgroundColor(ChartColorConfig.GetChartBackGroundColor());
//        SpectralPlatService.Instance.EventCenter.RegeditListener(new EventListener() {
//            @Override
//            public void recevieEvent(Event event) {
//                java.awt.EventQueue.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        SetBackgroundColor(ChartColorConfig.GetChartBackGroundColor());
//                        mainLinePaint.SetColor(ChartColorConfig.GetMainLineColor());
//                    }
//                });
//            }
//        });

        //注册语言
        //set tool text
        LanguageHelper.getIntance().EventCenter.RegeditListener(new EventListener() {
            @Override
            public void recevieEvent(Event event) {
                if (!"".equals(ChartName)) {
                    dataChart.setTitle(new TextTitle(LanguageHelper.getIntance().GetText(ChartName),
                            new Font("Microsoft YaHei", 0, 15)));
                }
            }
        });
    }

    public void SetBackgroundColor(Paint paint) {
        xyplot.setBackgroundPaint(paint);
    }

    /**
     *
     * @param title
     */
    String ChartName = "";

    public void SetTitle(String name) {
        this.ChartName = name;
    }
    // <editor-fold defaultstate="collapsed" desc="Chart Name"> 
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MainData Line">  
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

    // <editor-fold defaultstate="collapsed" desc="Snapshot Line"> 
    /**
     * display snap short, on xyplot dataset 1
     */
    public XYSeries[] snapshot = new XYSeries[0];
    private int snapshotDataSetIndex = 2;

    private void InitSnapShotDataRender() {
        this.xyplot.setRenderer(snapshotDataSetIndex, new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false));
    }

    public void DisplaySnapShot(XYSeries[] dataline) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        this.snapshot = dataline;
        if (dataline != null) {
            for (int i = 0; i < snapshot.length; i++) {
                xySeriesCollection.addSeries(snapshot[i]);
            }
        }
        this.xyplot.setDataset(snapshotDataSetIndex, xySeriesCollection);
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

        dataChartArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dataChartAreaMousePressed(evt);
            }
        });

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

    private void dataChartAreaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataChartAreaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dataChartAreaMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataChartArea;
    // End of variables declaration//GEN-END:variables
}
