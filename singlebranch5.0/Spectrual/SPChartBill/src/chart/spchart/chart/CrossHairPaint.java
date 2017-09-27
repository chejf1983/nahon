/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

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
 * 标线绘制
 *
 * @author jiche
 */
public class CrossHairPaint {

    private CrosshairOverlay crosshairoverlay = new CrosshairOverlay();
    private SpectralChart parent;

    public CrossHairPaint(SpectralChart dataChartPane) {
        this.parent = dataChartPane;
        //添加标线图层
        this.parent.dataChartPane.addOverlay(crosshairoverlay);

        //鼠标移动事件
        this.parent.dataChartPane.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    //有选中的标线，移动到鼠标移动的位置
                    if (selectedhair != null) {
                        MoveSelectCrossHairTo(GetMouseSlectedDomainValue(e));
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        //鼠标点击事件
        this.parent.dataChartPane.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    //双击左键，在鼠标选择的坐标，创建新标线
                    if ((e.getButton() == MouseEvent.BUTTON1)) {
                        if (e.getClickCount() == 2) {
                            CreateCrossHair(GetMouseSlectedDomainValue(e));
                        }

                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        //鼠标右键双击，删除最后一条标线
                        if (e.getClickCount() == 2) {
                            DeleteCrossHair();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    //鼠标左键单击，寻找最近的标线
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        selectedhair = GetNearestCrossHair(GetMouseSlectedDomainValue(e));
                        if (selectedhair != null) {
                            //选中的表面，颜色变成橘黄色
                            selectedhair.setPaint(Color.ORANGE);
                            //关闭chart图表自动缩放的功能，避免冲突
                            parent.chartrangePaint.zoomHelper.Enable(false);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (IsCrossHairPaintEnable()) {
                    //鼠标左键释放
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (selectedhair != null) {
                            //恢复选中标线颜色
                            selectedhair.setPaint(Color.RED);
                            //恢复chart图表自动缩放的功能
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

    //计算当前鼠标对应的X轴的值
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

    // <editor-fold defaultstate="collapsed" desc="标线创建删除">
    private ArrayList<Crosshair> crosshairs = new ArrayList(); //标线组
    //创建标线

    public void CreateCrossHair(double bornPoint) {
        Crosshair crosshair = new Crosshair(bornPoint);

        /* Init corsshair */
        //标线颜色
        crosshair.setPaint(Color.red);

        //标线旗帜设置
        crosshair.setLabelVisible(true);
        //标线旗帜位置
        crosshair.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        //旗帜背景色
        crosshair.setLabelBackgroundPaint(new Color(255, 255, 0, 100));

        //是否显示标线
        crosshair.setVisible(this.iscorssHairPaintEnable);

        //设置标线旗帜内容计算方法
        crosshair.setLabelGenerator(new org.jfree.chart.labels.CrosshairLabelGenerator() {
            @Override
            public String generateLabel(Crosshair crshr) {
                //显示标线旗帜的值
                XYSeries mainData = parent.mainData;
                double newx = crshr.getValue();
                int centerindex = 0;

                if (mainData.getItemCount() == 0) {
                    return "x: 0.00" + "y: 0.00 ";
                }
                                
                //寻找标线当前图像值，对应的数据X轴数组的位置
                if (newx <= mainData.getX(0).doubleValue()) {
                    centerindex = 0;
                    //小于最小值，标线直接停在第一个点
                    crshr.setValue(mainData.getX(0).doubleValue());
                } else if (newx >= mainData.getX(mainData.getItemCount() - 1).doubleValue()) {
                    centerindex = mainData.getItemCount() - 1;
                    //超过X最大值，标线停在最后一个点
                    crshr.setValue(mainData.getX(mainData.getItemCount() - 1).doubleValue());
                } else {
                    //寻找比当前值小一点的最近的值
                    for (int i = 0; i < mainData.getItemCount(); i++) {
                        if (mainData.getX(i).doubleValue() < newx
                                && mainData.getX(i + 1).doubleValue() > newx) {
                            centerindex = i;
                        }
                    }
                }

                return "x: " + new DecimalFormat("0.00").format(mainData.getX(centerindex).doubleValue())
                        + "y: " + new DecimalFormat("0.0000").format(mainData.getY(centerindex).doubleValue());
                /*
                double x[] = new double[mainData.getItemCount()];
                double y[] = new double[mainData.getItemCount()];
                
                for (int i = 0; i < x.length; i++) {
                    x[i] = mainData.getX(i).doubleValue();
                    y[i] = mainData.getY(i).doubleValue();
                }
                double labley = 0;
                if (y.length != 0) {
                    labley = Theleastsquaremethod.predict(x, y, x.length / 200, crshr.getValue());
                }                
                
//                return "<html>nihao<br>again</html>" ;
                return "x: " + new DecimalFormat("0.00").format(crshr.getValue())
                        + "y: " + new DecimalFormat("0.0000").format(labley);
                 */
            }
        });

        //添加标线
        this.crosshairoverlay.addDomainCrosshair(crosshair);
        this.crosshairs.add(crosshair);
    }

    //删除标线
    public void DeleteCrossHair() {
        if (this.crosshairs.size() > 0) {
            Crosshair remove = this.crosshairs.remove(this.crosshairs.size() - 1);
            this.crosshairoverlay.removeDomainCrosshair(remove);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="标线是否开启">
    //标线是否开启
    private boolean iscorssHairPaintEnable = false;

    public boolean IsCrossHairPaintEnable() {
        return this.iscorssHairPaintEnable;
    }

    public void EnableCrossHairPaint(boolean value) {
        this.iscorssHairPaintEnable = value;

        for (Crosshair crosshair : this.crosshairs) {
            crosshair.setVisible(value);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="选中的标线控制">
    private Crosshair selectedhair;  //选中的标线
    //选择最近的一个标线，做选中的标线

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
    }
    // </editor-fold>

}
