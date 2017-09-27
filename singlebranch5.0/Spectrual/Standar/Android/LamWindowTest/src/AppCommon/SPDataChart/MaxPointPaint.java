/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import app.common.chartmath.MaxWavePointHelper;
import java.awt.Color;
import java.awt.Font;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author jiche
 */
public class MaxPointPaint {

    private org.jfree.chart.renderer.xy.XYLineAndShapeRenderer maxPointrender;
    private boolean isMaxEnable = false;
    private SpectralChart parent;

    public MaxPointPaint(SpectralChart parent) {
        this.parent = parent;
        this.InitMaxPointRender();

        this.parent.UpdateMainDataEvent.RegeditListener(new EventListener<XYSeries>() {
            @Override
            public void recevieEvent(Event<XYSeries> event) {
                if (IsEnableMaxCheck()) {
                    drawMaxPointLine();
                }
            }
        });
    }

    public void EnableMaxCheck(boolean value) {
        if (this.isMaxEnable != value) {
            isMaxEnable = value;
            if (this.isMaxEnable) {
                this.drawMaxPointLine();
            } else {
                this.clearMaxPointLine();
            }
        }
    }

    public boolean IsEnableMaxCheck() {
        return this.isMaxEnable;
    }

    private void InitMaxPointRender() {
        maxPointrender = new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false);
        maxPointrender.setBaseItemLabelsVisible(true);
        maxPointrender.setBaseLinesVisible(false);
        maxPointrender.setBaseShapesVisible(true);

        maxPointrender.setBaseItemLabelGenerator(new org.jfree.chart.labels.XYItemLabelGenerator() {
            @Override
            public String generateLabel(XYDataset xyd, int i, int i1) {
                return String.format("(%.1f ,  %.1f)", xyd.getXValue(i, i1), xyd.getYValue(i, i1));
            }
        });
        maxPointrender.setBaseItemLabelPaint(Color.WHITE);
        maxPointrender.setBaseItemLabelFont(new Font("Dialog", 1, 12));
        this.parent.xyplot.setRenderer(SpectralChart.MaxLineDataSetIndex, maxPointrender);
    }

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

            Integer[] MaxPointIndex = MaxWavePointHelper.FindMaxWavePoint(dataArray, dataIndex);
            XYSeries line = new XYSeries("");
            for (int i = 0; i < MaxPointIndex.length; i++) {
                line.add(mainData.getX(MaxPointIndex[i]), mainData.getY(MaxPointIndex[i]));
            }
            xySeriesCollection.addSeries(line);
        }
        this.parent.xyplot.setDataset(SpectralChart.MaxLineDataSetIndex, xySeriesCollection);
    }

    private void clearMaxPointLine() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        this.parent.xyplot.setDataset(SpectralChart.MaxLineDataSetIndex, xySeriesCollection);
    }
}
