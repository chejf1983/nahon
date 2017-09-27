/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author jiche
 */
public class WaveColorRenderer extends XYAreaRenderer2 {

    final XYPlot xyplot;

    public WaveColorRenderer(XYPlot xyplot) {
        this.xyplot = xyplot;
    }

    @Override
    public java.awt.Paint getItemPaint(int series, int item) {
        XYDataset dataset = xyplot.getDataset(0);

        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1)) {
            y1 = 0.0D;
        }

        double x0 = dataset.getXValue(series, Math.max(item - 2, 0));
        double y0 = dataset.getYValue(series, Math.max(item - 2, 0));
        if (Double.isNaN(y0)) {
            y0 = 0.0D;
        }

        int itemCount = dataset.getItemCount(series);

        double x2 = dataset.getXValue(series, Math.min(item + 2, itemCount - 1));
        double y2 = dataset.getYValue(series, Math.min(item + 2, itemCount - 1));
        if (Double.isNaN(y2)) {
            y2 = 0.0D;
        }

        GradientPaint gradientpaint = new GradientPaint((float) x0, (float) y0, WavelenghToRGB((int) x0),
                (float) x1, (float) y1, WavelenghToRGB((int) x1));

        return gradientpaint;
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
            XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        if (!getItemVisible(series, item)) {
            return;
        }

        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1)) {
            y1 = 0.0D;
        }
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, plot.getDomainAxisEdge());
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, plot.getRangeAxisEdge());

        double x0 = dataset.getXValue(series, Math.max(item - 2, 0));
        double y0 = dataset.getYValue(series, Math.max(item - 2, 0));
        if (Double.isNaN(y0)) {
            y0 = 0.0D;
        }
        double transX0 = domainAxis.valueToJava2D(x0, dataArea, plot.getDomainAxisEdge());
        double transY0 = rangeAxis.valueToJava2D(y0, dataArea, plot.getRangeAxisEdge());

        int itemCount = dataset.getItemCount(series);

        double x2 = dataset.getXValue(series, Math.min(item + 2, itemCount - 1));
        double y2 = dataset.getYValue(series, Math.min(item + 2, itemCount - 1));
        if (Double.isNaN(y2)) {
            y2 = 0.0D;
        }
        double transX2 = domainAxis.valueToJava2D(x2, dataArea, plot.getDomainAxisEdge());
        double transY2 = rangeAxis.valueToJava2D(y2, dataArea, plot.getRangeAxisEdge());

        double transZero = rangeAxis.valueToJava2D(0.0D, dataArea, plot.getRangeAxisEdge());
        Polygon hotspot = null;
        if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            hotspot = new Polygon();
            hotspot.addPoint((int) transZero, (int) ((transX0 + transX1) / 2D));
            hotspot.addPoint((int) ((transY0 + transY1) / 2D), (int) ((transX0 + transX1) / 2D));
            hotspot.addPoint((int) transY1, (int) transX1);
            hotspot.addPoint((int) ((transY1 + transY2) / 2D), (int) ((transX1 + transX2) / 2D));
            hotspot.addPoint((int) transZero, (int) ((transX1 + transX2) / 2D));
        } else {
            hotspot = new Polygon();
            hotspot.addPoint((int) ((transX0 + transX1) / 2D), (int) transZero);
            hotspot.addPoint((int) ((transX0 + transX1) / 2D), (int) ((transY0 + transY1) / 2D));
            hotspot.addPoint((int) transX1, (int) transY1);
            hotspot.addPoint((int) ((transX1 + transX2) / 2D), (int) ((transY1 + transY2) / 2D));
            hotspot.addPoint((int) ((transX1 + transX2) / 2D), (int) transZero);
        }
        PlotOrientation orientation = plot.getOrientation();
        java.awt.Paint paint = getItemPaint(series, item);
        java.awt.Stroke stroke = getItemStroke(series, item);
        g2.setPaint(paint);
        g2.setStroke(stroke);
        g2.fill(hotspot);
        if (isOutline()) {
            g2.setStroke(lookupSeriesOutlineStroke(series));
            g2.setPaint(lookupSeriesOutlinePaint(series));
            g2.draw(hotspot);
        }
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
        }
    }

    private Color WavelenghToRGB(int Wavelength){
        if(Wavelength<380)
		Wavelength=380;
	if(Wavelength>780)
		Wavelength=780;

	int iSPE1=450;
	int iSPE2=520;
	int iSPE3=620;
        double R;
        double G;
        double B;
	if(Wavelength<=iSPE1)
	{
		R=0;
		G=0;
		B=(int)(255*(Wavelength-380)/(iSPE1-380));
	}
	else if(Wavelength<iSPE2)
	{
		float k1=255*2.0f/(iSPE2-iSPE1);
		float b1=-iSPE1*k1;
		int m1=(int)(k1*Wavelength+b1);

		float k2=255*2.0f/(iSPE1-iSPE2);
		float b2=-iSPE2*k2;
		int m2=(int)(k2*Wavelength+b2);
		if(m1>=255)
			m1=255;
		if(m1<=0)
			m1=0;
		if(m2>=255)
			m2=255;
		if(m2<=0)
			m2=0;
		R=0;
		G=m1;
		B=m2;
	}
	else if(Wavelength<iSPE3)
	{
		float k1=255*2.0f/(iSPE3-iSPE2);
		float b1=-iSPE2*k1;
		int m1=(int)(k1*Wavelength+b1);

		float k2=255*2.0f/(iSPE2-iSPE3);
		float b2=-iSPE3*k2;
		int m2=(int)(k2*Wavelength+b2);
		if(m1>=255)
			m1=255;
		if(m1<=0)
			m1=0;
		if(m2>=255)
			m2=255;
		if(m2<=0)
			m2=0;
		R=m1;
		G=m2;
		B=0;
	}
	else
	{
		R=(int)((780-Wavelength)*255/(780-iSPE3));
		G=0;
		B=0;
	}
        int Red=(int) R;
        int Green=(int) G;
        int Blue=(int) B;
        return new Color(Red, Green, Blue);
}

}
    