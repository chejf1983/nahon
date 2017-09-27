/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import app.common.chartmath.NewTonHelper;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author jiche
 */
public class CrossHairPaint {
    
    private CrosshairOverlay crosshairoverlay = new CrosshairOverlay();
    private ArrayList<Crosshair> crosshairs = new ArrayList();
    private boolean iscorssHairPaintEnable = false;
    private SpectralChart parent;
    private Crosshair selectedhair;
    
    public CrossHairPaint(SpectralChart dataChartPane) {
        this.parent = dataChartPane;
        this.parent.dataChartPane.addOverlay(crosshairoverlay);
        
        this.parent.dataChartPane.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    if (selectedhair != null) {
                        MoveSelectCrossHairTo(GetMouseSlectedDomainValue(e));
                    }
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        });
        this.parent.dataChartPane.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    if ((e.getButton() == MouseEvent.BUTTON1)) {
                        if (e.getClickCount() == 2) {
                            CreateCrossHair(GetMouseSlectedDomainValue(e));
                        }
                        
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 2) {
                            DeleteCrossHair();
                        }
                    }
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        selectedhair = GetNearestCrossHair(GetMouseSlectedDomainValue(e));
                        if (selectedhair != null) {
                            selectedhair.setPaint(Color.ORANGE);
                            parent.chartrangePaint.zoomHelper.Enable(false);
                        }
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (selectedhair != null) {
                            selectedhair.setPaint(Color.RED);
                            parent.chartrangePaint.zoomHelper.Enable(true);
                        }
                        selectedhair = null;
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void CreateCrossHair(double bornPoint) {
        Crosshair crosshair = new Crosshair(bornPoint);

        /* Init corsshair */
        crosshair.setPaint(Color.red);
        crosshair.setLabelVisible(true);
        crosshair.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
//        crosshair.setLabelAnchor(new RectangleAnchor(""));
        crosshair.setLabelBackgroundPaint(new Color(255, 255, 0, 100));
        crosshair.setVisible(this.iscorssHairPaintEnable);
        
        crosshair.setLabelGenerator(new org.jfree.chart.labels.CrosshairLabelGenerator() {
            @Override
            public String generateLabel(Crosshair crshr) {
                XYSeries mainData = parent.mainData;
                double x[] = new double[mainData.getItemCount()];
                double y[] = new double[mainData.getItemCount()];
                
                for (int i = 0; i < x.length; i++) {
                    x[i] = mainData.getX(i).doubleValue();
                    y[i] = mainData.getY(i).doubleValue();
                }
                
                double labley = 0;
                if (y.length != 0) {
                    labley = NewTonHelper.NewInt(x, y, x.length, crshr.getValue());
                }
//                return "<html>nihao<br>again</html>" ;
                return "x: " + new DecimalFormat("0.00").format(crshr.getValue())
                        + "y: " + new DecimalFormat("0.0000").format(labley);
            }
        });
        this.crosshairoverlay.addDomainCrosshair(crosshair);
        this.crosshairs.add(crosshair);
    }
    
    public void DeleteCrossHair() {
        if (this.crosshairs.size() > 0) {
            Crosshair remove = this.crosshairs.remove(this.crosshairs.size() - 1);
            this.crosshairoverlay.removeDomainCrosshair(remove);
        }
    }
    
    public boolean IsCrossHairPaintEnable() {
        return this.iscorssHairPaintEnable;
    }
    
    public void EnableCrossHairPaint(boolean value) {
        this.iscorssHairPaintEnable = value;
        
        for (Crosshair crosshair : this.crosshairs) {
            crosshair.setVisible(value);
        }
    }
    
    public Crosshair GetNearestCrossHair(double mouse_xvalue) {
        Crosshair ret = null;
        double distance = (this.parent.xyplot.getDomainAxis().getUpperBound()
                - this.parent.xyplot.getDomainAxis().getLowerBound()) / 50;
        for (Crosshair crosshair : this.crosshairs) {
            double tmpdistance = Math.abs(crosshair.getValue() - mouse_xvalue);
            if (tmpdistance < distance) {
                ret = crosshair;
                distance = tmpdistance;
            }
        }
        return ret;
    }
    
    public void MoveSelectCrossHairTo(double mouse_xvalue) {
        if (this.selectedhair != null) {
            selectedhair.setValue(mouse_xvalue);
        }
//        this.UpdateAllCrossHairLable();
    }
    
    private double GetMouseSlectedDomainValue(java.awt.event.MouseEvent evt) {
        int xPos = evt.getX();
        int yPos = evt.getY();
//        System.out.println("x = " + xPos + ", y = " + yPos);
        Point2D point2D = this.parent.dataChartPane.translateScreenToJava2D(new Point(xPos, yPos));
//        XYPlot xyPlot = (XYPlot)this.dataChart.getPlot();
        ChartRenderingInfo chartRenderingInfo = this.parent.dataChartPane.getChartRenderingInfo();
        Rectangle2D rectangle2D = chartRenderingInfo.getPlotInfo().getDataArea();
        ValueAxis valueAxis1 = this.parent.xyplot.getDomainAxis();
        RectangleEdge rectangleEdge1 = this.parent.xyplot.getDomainAxisEdge();
//        ValueAxis valueAxis2 = xyplot.getRangeAxis();
//        RectangleEdge rectangleEdge2 = xyplot.getRangeAxisEdge();
        double d1 = valueAxis1.java2DToValue(point2D.getX(), rectangle2D, rectangleEdge1);
//        double d2 = valueAxis2.java2DToValue(point2D.getY(), rectangle2D, rectangleEdge2);
//        System.out.println("Chart: x = " + d1 + ", y = " + d2);

        if (d1 < this.parent.xyplot.getDomainAxis().getLowerBound()) {
            d1 = this.parent.xyplot.getDomainAxis().getLowerBound();
        } else if (d1 > this.parent.xyplot.getDomainAxis().getUpperBound()) {
            d1 = this.parent.xyplot.getDomainAxis().getUpperBound();
        }
        
        return d1;
    }
}
