/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

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
    private Paint lineColor = Color.BLACK;
    private SpectralChart parent;
    public static int mainLineDataSetIndex = 0;

    public MainLineShapePaint(SpectralChart parent) {
        this.parent = parent;
    }

    public void SetColor(Paint paint) {
        if (this.lineColor != paint) {
            this.lineColor = paint;
            if(this.colorType != MainLineType.Wave_Area ){
                this.parent.xyplot.getRenderer(SpectralChart.MainLineDataSetIndex).
                        setSeriesPaint(mainLineDataSetIndex, lineColor);
            }
        }
    }

    public Paint GetColor() {
        return this.lineColor;
    }

    public enum MainLineType {
        Line,
        Pixel_Area,
        Wave_Area,
        Null
    }

    public void SetLineType(MainLineType type) {
        if (!this.colorType.equals(type)) {
            this.colorType = type;
            this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, null);
            switch (this.colorType) {
                case Line:
                    XYLineAndShapeRenderer linerender = new XYLineAndShapeRenderer(true, false);
                    linerender.setSeriesPaint(mainLineDataSetIndex, lineColor);
                    this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, linerender);
                    break;
                case Pixel_Area:
                    XYAreaRenderer xyarearenderer = new XYAreaRenderer();
                    xyarearenderer.setSeriesPaint(mainLineDataSetIndex, lineColor);
                    this.parent.xyplot.setRenderer(SpectralChart.MainLineDataSetIndex, xyarearenderer);
                    break;
                case Wave_Area:
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
}
