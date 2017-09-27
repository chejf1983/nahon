/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

/**
 * 彩色显示可见光
 *
 * @author jiche
 */
public class WaveColorRenderer extends XYAreaRenderer2 {

    final XYPlot xyplot;

    public WaveColorRenderer(XYPlot xyplot) {
        this.xyplot = xyplot;
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
            XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        if (!getItemVisible(series, item)) {
            return;
        }

        int range = (int) (domainAxis.getRange().getUpperBound() - domainAxis.getRange().getLowerBound());
        int itemCount = dataset.getItemCount(series);

        //计算过滤窗口
        int window = range / 50 + 1;
        //减少计算点个数，增加4个像素的重叠，保证颜色连续  
        if (item % (window - 4 > 0 ? window - 4 : 1) != 0) {
            return;
        }
//        if (item % window != 0) {
//            return;
//        }

        //y = 0的像素值
        double zeroY = rangeAxis.valueToJava2D(0.0D, dataArea, plot.getRangeAxisEdge());

        //起始点x0值
        double x0 = dataset.getXValue(series, item);
        double TransX0 = domainAxis.valueToJava2D(x0, dataArea, plot.getDomainAxisEdge());
        //重点xn值
        double xn = 0;
        double TransXn = 0;

        Polygon hotspot = null;
        hotspot = new Polygon();
        //添加左下角第一个点(x0,0)
        hotspot.addPoint((int) (TransX0), (int) zeroY);

        //循环添加曲线点
        for (int i = 0; i < window + 1; i++) {
            //超过数据上限，退出
            if (item + i >= itemCount) {
                break;
            }

            double x = dataset.getXValue(series, item + i);
            double y = dataset.getYValue(series, item + i);
            if (Double.isNaN(y)) {
                y = 0.0D;
            }

            double transX = domainAxis.valueToJava2D(x, dataArea, plot.getDomainAxisEdge());
            double transY = rangeAxis.valueToJava2D(y, dataArea, plot.getRangeAxisEdge());
            //更新最后一个点的xn值
            xn = x;
            TransXn = transX;
            //添加动态点(x,y)
            hotspot.addPoint((int) (transX), (int) transY);
        }

        //添加最后一个点(xn, 0)
        hotspot.addPoint((int) TransXn, (int) zeroY);

        //计算渐变色，从x0->xn
        GradientPaint gradientpaint = new GradientPaint((float) TransX0, (float) zeroY, WavelenghToRGB((int) x0),
                (float) TransXn, (float) zeroY, WavelenghToRGB((int) xn));

        //赋值颜色
        java.awt.Paint paint = gradientpaint;
//        java.awt.Paint paint = WavelenghToRGB((int) x0);
        java.awt.Stroke stroke = getItemStroke(series, item);
        g2.setPaint(paint);
        g2.setStroke(stroke);
        g2.fill(hotspot); //填充多边形
//        if (isOutline()) {
//            g2.setStroke(lookupSeriesOutlineStroke(series));
//            g2.draw(hotspot);
//            g2.setPaint(lookupSeriesOutlinePaint(series));
//        }

        /*
        int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
        int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
        updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex, rangeAxisIndex, transX1, transY1, orientation);
        if (state.getInfo() != null) {
            EntityCollection entities = state.getEntityCollection();
            if (entities != null && hotspot != null) {
                String tip = null;
                XYToolTipGenerator generator = getToolTipGenerator(series, item);
                if (generator != null) {
                    tip = generator.generateToolTip(dataset, series, item);
                }
                String url = null;
                if (getURLGenerator() != null) {
                    url = getURLGenerator().generateURL(dataset, series, item);
                }
                XYItemEntity entity = new XYItemEntity(hotspot, dataset, series, item, tip, url);
                entities.add(entity);
            }
        }*/
    }

    //波长RGB 颜色换算
    private Color WavelenghToRGB(int Wavelength) {
        if (Wavelength < 380) {
            Wavelength = 380;
        }
        if (Wavelength > 780) {
            Wavelength = 780;
        }

        int iSPE1 = 450;
        int iSPE2 = 520;
        int iSPE3 = 620;
        double R;
        double G;
        double B;
        if (Wavelength <= iSPE1) {
            R = 0;
            G = 0;
            B = (int) (255 * (Wavelength - 380) / (iSPE1 - 380));
        } else if (Wavelength < iSPE2) {
            float k1 = 255 * 2.0f / (iSPE2 - iSPE1);
            float b1 = -iSPE1 * k1;
            int m1 = (int) (k1 * Wavelength + b1);

            float k2 = 255 * 2.0f / (iSPE1 - iSPE2);
            float b2 = -iSPE2 * k2;
            int m2 = (int) (k2 * Wavelength + b2);
            if (m1 >= 255) {
                m1 = 255;
            }
            if (m1 <= 0) {
                m1 = 0;
            }
            if (m2 >= 255) {
                m2 = 255;
            }
            if (m2 <= 0) {
                m2 = 0;
            }
            R = 0;
            G = m1;
            B = m2;
        } else if (Wavelength < iSPE3) {
            float k1 = 255 * 2.0f / (iSPE3 - iSPE2);
            float b1 = -iSPE2 * k1;
            int m1 = (int) (k1 * Wavelength + b1);

            float k2 = 255 * 2.0f / (iSPE2 - iSPE3);
            float b2 = -iSPE3 * k2;
            int m2 = (int) (k2 * Wavelength + b2);
            if (m1 >= 255) {
                m1 = 255;
            }
            if (m1 <= 0) {
                m1 = 0;
            }
            if (m2 >= 255) {
                m2 = 255;
            }
            if (m2 <= 0) {
                m2 = 0;
            }
            R = m1;
            G = m2;
            B = 0;
        } else {
            R = (int) ((780 - Wavelength) * 255 / (780 - iSPE3));
            G = 0;
            B = 0;
        }
        int Red = (int) R;
        int Green = (int) G;
        int Blue = (int) B;
        return new Color(Red, Green, Blue);
    }

}
