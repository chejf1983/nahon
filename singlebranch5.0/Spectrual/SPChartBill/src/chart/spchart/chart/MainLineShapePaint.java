/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import chart.spchart.panel.ChartColorConfig;
import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author jiche
 */
public class MainLineShapePaint {
    private MainLineType colorType = MainLineType.Null;
    private final SpectralChart parent;

    public MainLineShapePaint(SpectralChart parent) {
        this.parent = parent;
        this.SetColor(ChartColorConfig.GetMainLineColor());
    }

    // <editor-fold defaultstate="collapsed" desc="主曲线颜色设置">
    private Paint lineColor = Color.BLACK;
    //设置主线的颜色
    public void SetColor(Paint paint) {
        if (this.lineColor != paint) {
            this.lineColor = paint;
            //波长曲线显示的时候，曲线颜色无效
            if(this.colorType != MainLineType.Wave_Area ){
                this.parent.xyplot.getRenderer(SpectralChart.MainLineDataSetIndex).
                        setSeriesPaint(SpectralChart.MainLineDataSetIndex, lineColor);
            }
        }
    }

    //获取主线的颜色
    public Paint GetColor() {
        return this.lineColor;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="主曲样式设置">
    public enum MainLineType {
        Line,    //线形
        Pixel_Area,  //像素形，单色填充
        Wave_Area,   //波长颜色填充
        Null
    }

    public void SetLineType(MainLineType type) {
        if (!this.colorType.equals(type)) {
            this.colorType = type;
            this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, null);
            switch (this.colorType) {
                case Line: //线形
                    XYLineAndShapeRenderer linerender = new XYLineAndShapeRenderer(true, false);
                    linerender.setSeriesPaint(SpectralChart.MainLineDataSetIndex, lineColor);
                    this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, linerender);
                    break;
                case Pixel_Area: //像素形，单色填充
                    XYAreaRenderer xyarearenderer = new XYAreaRenderer();
                    xyarearenderer.setSeriesPaint(SpectralChart.MainLineDataSetIndex, lineColor);
                    this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, xyarearenderer);
                    break;
                case Wave_Area:  //波长颜色填充
                    org.jfree.chart.renderer.xy.XYAreaRenderer2 xyarearenderer2 = new WaveColorRenderer(this.parent.xyplot);
                    this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, xyarearenderer2);
                    break;
                default:
                    break;
            }
        }
    }

    public MainLineType GetLineType() {
        return this.colorType;
    }
    // </editor-fold>
}
