/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import java.awt.Color;
import java.awt.Font;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import chart.math.MaxWavePointHelper;

/**
 *
 * @author jiche
 */
public class MaxPointPaint {

    private final SpectralChart parent;

    public MaxPointPaint(SpectralChart parent) {
        this.parent = parent;
        //初始化峰值显示参数
        this.InitMaxPointRender();

        //主数据刷新后，同时刷新峰值的曲线
        this.parent.UpdateMainDataEvent.RegeditListener(new EventListener<XYSeries>() {
            @Override
            public void recevieEvent(Event<XYSeries> event) {
                if (IsEnableMaxCheck()) {
                    drawMaxPointLine();
                }
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="峰值使能开关">  
    //是否显示峰值
    private boolean isMaxEnable = false;

    //使能峰值
    public void EnableMaxCheck(boolean value) {
        if (this.isMaxEnable != value) {
            isMaxEnable = value;
            if (this.isMaxEnable) {
                //画峰值曲线
                this.drawMaxPointLine();
            } else {
                //清除峰值曲线
                this.clearMaxPointLine();
            }
        }
    }

    //是否显示峰值
    public boolean IsEnableMaxCheck() {
        return this.isMaxEnable;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="初始化峰值曲线">  
    //峰值显示样式
    private org.jfree.chart.renderer.xy.XYLineAndShapeRenderer maxPointrender;
    //初始化峰值显示
    private void InitMaxPointRender() {
        maxPointrender = new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false);
        maxPointrender.setBaseItemLabelsVisible(true);
        maxPointrender.setBaseLinesVisible(false);
        maxPointrender.setBaseShapesVisible(true);

        //峰值点值显示
        maxPointrender.setBaseItemLabelGenerator(new org.jfree.chart.labels.XYItemLabelGenerator() {
            @Override
            public String generateLabel(XYDataset xyd, int i, int i1) {
                return String.format("(%.1f ,  %.1f)", xyd.getXValue(i, i1), xyd.getYValue(i, i1));
            }
        });

        //字体设置
        maxPointrender.setBaseItemLabelPaint(Color.WHITE);
        maxPointrender.setBaseItemLabelFont(new Font("Dialog", 1, 12));
        //设置峰值样式
        this.parent.xyplot.setRenderer(SpectralChart.MaxLineDataSetIndex, maxPointrender);
    }
    // </editor-fold>

    //画峰值曲线
    private void drawMaxPointLine() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries mainData = this.parent.mainData;
        if (this.parent.mainData != null) {
            double[] dataArray = new double[mainData.getItemCount()];
            double[] dataIndex = new double[mainData.getItemCount()];

            for (int i = 0; i < mainData.getItemCount(); i++) {
                dataArray[i] = mainData.getY(i).doubleValue();
                dataIndex[i] = mainData.getX(i).doubleValue();
            }

            //寻峰
            Integer[] MaxPointIndex = MaxWavePointHelper.FindMaxWavePoint(dataArray, dataIndex);
            //构建峰值曲线
            XYSeries line = new XYSeries("");
            for (int i = 0; i < MaxPointIndex.length; i++) {
                line.add(mainData.getX(MaxPointIndex[i]), mainData.getY(MaxPointIndex[i]));
            }
            xySeriesCollection.addSeries(line);
        }
        //显示峰值曲线
        this.parent.xyplot.setDataset(SpectralChart.MaxLineDataSetIndex, xySeriesCollection);
    }

    //清除峰值曲线
    private void clearMaxPointLine() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        this.parent.xyplot.setDataset(SpectralChart.MaxLineDataSetIndex, xySeriesCollection);
    }
}
