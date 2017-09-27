/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import chart.spchart.chart.ZoomUpHelper.ZoomState;
import java.awt.Point;
import java.awt.event.MouseEvent;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author jiche
 */
public class ChartRangePaint {
    
    SpectralChart parent;
    public ZoomUpHelper zoomHelper;
    
    public ChartRangePaint(SpectralChart parent) {
        this.parent = parent;
        
        //注册数据更新callback
        this.parent.UpdateMainDataEvent.RegeditListener(new EventListener<XYSeries>() {
            @Override
            public void recevieEvent(Event<XYSeries> event) {
                //如果是自动调整界面，当曲线没有被放大的情况下，重新调整上下线
                if (rangetype == RangeType.AutoRange) {
                    if (zoomHelper.state == ZoomState.ZoomUp) {
                        return;
                    } else {
                        AutoSetRange();
                    }
                }
            }
        });
        
        //初始化放大控制器
        zoomHelper = new ZoomUpHelper(this);
        zoomHelper.Enable(true);
        //设置当前为自动调节
        this.ChangeType(RangeType.AutoRange);
    }
    
    public enum RangeType {
        
        AutoRange,
        MaxRange,
        Manual
    }
    
    private RangeType rangetype;
    
    //修改chart范围类型
    public void ChangeType(RangeType type) {
        this.rangetype = type;
        
        switch (this.rangetype) {
            case AutoRange:
                //自动调节状态
                this.parent.xyplot.getRangeAxis().setAutoRange(false);//y
                this.parent.xyplot.getDomainAxis().setAutoRange(true);//x
//                this.parent.xyplot.getRangeAxis().setAutoRangeMinimumSize((MaxRange - MinRange / 100));
                this.AutoSetRange();
                break;
            case MaxRange:
                //设置成最大值
                this.parent.xyplot.getRangeAxis().setAutoRange(false);//y
                this.parent.xyplot.getDomainAxis().setAutoRange(true);//x
                this.parent.xyplot.getRangeAxis().setRange(new Range(this.MinRange, this.MaxRange));
                break;
            case Manual:
                //手动调节
                this.parent.xyplot.getRangeAxis().setAutoRange(false);//y
                this.parent.xyplot.getDomainAxis().setAutoRange(false);//x
                this.parent.xyplot.getRangeAxis().setRange(new Range(this.ManualYMin, this.ManualYMax));
                this.parent.xyplot.getDomainAxis().setRange(new Range(this.ManualXMin, this.ManualXMax));
                break;
            default:
                throw new AssertionError(this.rangetype.name());
            
        }
        //修改范围时，取消放大状态
        this.zoomHelper.state = ZoomUpHelper.ZoomState.Stand;
    }

    /**
     * 获取当前range模式
     *
     * @return
     */
    public RangeType GetRangeType() {
        return this.rangetype;
    }    
    
    // <editor-fold defaultstate="collapsed" desc="固定最大上下限">
    public double MaxRange = 65535;
    public double MinRange = -1000;

    /**
     * 设置最大，最小量程
     *
     * @param maxRange
     * @param minRange
     */
    public void SetFixRangeMaxValue(double maxRange, double minRange) {
        this.MaxRange = maxRange;
        this.MinRange = minRange;
        ChangeType(RangeType.MaxRange);
    }
    // </editor-fold>
         
    // <editor-fold defaultstate="collapsed" desc="手动设置上下限">
    public double ManualYMax = 65535;
    public double ManualYMin = -1000;
    public double ManualXMax = 1024;
    public double ManualXMin = 0;

    /**
     * 设置手动量程范围
     *
     * @param Xstart
     * @param Xend
     * @param Ystart
     * @param Yend
     */
    public void SetManualRange(double Xstart, double Xend, double Ystart, double Yend) {
        this.ManualYMax = Yend;
        this.ManualYMin = Ystart;
        this.ManualXMax = Xend;
        this.ManualXMin = Xstart;
        
        ChangeType(RangeType.Manual);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="自动调节设置上下限">
    //刷新上下限
    private void AutoSetRange() {
        double MaxNodeValue;//获取maindata 和 snapshot　中最大的Ｙ值
        double MinNodeValue;//获取maindata 和 snapshot　中最小的Ｙ值
        
        //获取主数据的最大最小值，以主数据最大最小值为参考
        if (this.parent.mainData != null) {
            MaxNodeValue = this.parent.mainData.getMaxY();
            MinNodeValue = this.parent.mainData.getMinY();
        } else {
            MaxNodeValue = 0;
            MinNodeValue = 0;
        }
        
        //如果最小值大于等于最大值
        if (MinNodeValue >= MaxNodeValue) {
            MaxNodeValue = MinNodeValue + 100;
        }
        
        //获取当前range最大最小值
        double CurrentMaxRange = this.parent.xyplot.getRangeAxis().getRange().getUpperBound();
        double CurrentMinRange = this.parent.xyplot.getRangeAxis().getRange().getLowerBound();
        
        //获取数据宽度
        double datarange = (MaxNodeValue - MinNodeValue);
        //获取range宽度
        double axisrange = (CurrentMaxRange - CurrentMinRange);
        
        //如果数据最大值超过上限，或者最小值小于下线，或者数据宽度小于range宽度的50% 调整range范围
        if (MaxNodeValue > CurrentMaxRange
                || MinNodeValue < CurrentMinRange
                || datarange < axisrange * 0.5) {
            //设置range范围是 数据最小值-数据宽度的0.05， 最大值加上数据宽度的0.3（最大值出在全局的75%左右）
            //使数据宽度在range宽度的50%~99%之间波动
            this.parent.xyplot.getRangeAxis().setRange(
                    new Range(MinNodeValue - datarange * 0.05, MaxNodeValue + datarange * 0.3));
        }
    }
    // </editor-fold>
}

//鼠标滑动放大
class ZoomUpHelper {
    
    public enum ZoomState {        
        ZoomUp,
        Stand
    }
    
    public ZoomState state = ZoomState.Stand;
    
    Point lastpoint = new Point();
    private boolean isEnable = true;
    private ChartRangePaint pane;
    
    public void Enable(boolean value) {
        this.isEnable = value;
        this.pane.parent.dataChartPane.setMouseZoomable(value);
    }
    
    public boolean isEnable(){
        return this.isEnable;
    }
    
    public ZoomUpHelper(final ChartRangePaint pane) {
        this.pane = pane;
        
        //注册鼠标事件
        this.pane.parent.dataChartPane.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                //鼠标按下，记录当前位置
                if (isEnable) {
                    lastpoint.x = e.getX();
                    lastpoint.y = e.getY();
                    
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnable) {
                    //鼠标提起，如果是正向，表明当前处于放大状态
                    boolean isZoomout = e.getX() - lastpoint.x > 1 && e.getY() - lastpoint.y > 1;
                    if (isZoomout) {
                        state = ZoomState.ZoomUp;
                    } else {
                        //否则处于标准状态，重新刷新range
                        pane.ChangeType(pane.GetRangeType());
                        state = ZoomState.Stand;
                    }
                }
            }
        });
    }
}
