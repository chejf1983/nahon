package com.nahon.sobs.LamApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiche on 2016/12/2.
 */

public class ColorLineChart extends LineChart {
    public ColorLineChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
    }

    public static final GraphicalView getLineChartView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        LineChart chart = new ColorLineChart(dataset, renderer);
        GraphicalView chart_view = new GraphicalView(context, chart);
        return chart_view;
    }

    private void drawColorRender1(XYSeries series, Canvas canvas, List<Float> points, float yAxiValue, int startIndex) {
        if (points.size() > 2) {
            int xvalue_start = (int) series.getX(startIndex);
            List<Float> tmppath = new ArrayList<Float>();
            float x_left = points.get(0).floatValue();
            float y_left = points.get(1).floatValue();
            tmppath.add(x_left);
            tmppath.add(y_left);


            //画圆角矩形
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);//充满
            paint.setAntiAlias(true);// 设置画笔的锯齿效果

            for (int k1 = 2; k1 < points.size(); k1 += 2) {
                int xvalue = (int) series.getX(startIndex + (k1 / 2));
                float x_new = points.get(k1).floatValue();
                float y_new = points.get(k1 + 1).floatValue();
                tmppath.add(x_new);
                tmppath.add(y_new);

                if (x_new - x_left > 30 || k1 == points.size() - 2) {
                    //draw path
                    Path path = new Path();
                    path.moveTo(x_new + 1, y_new);
                    path.lineTo(x_new + 1, yAxiValue);
                    path.lineTo(x_left - 1, yAxiValue);
                    path.lineTo(x_left - 1, y_left);
                    for (int k2 = 0; k2 < tmppath.size(); k2 += 2) {
                        path.lineTo(tmppath.get(k2).floatValue(), tmppath.get(k2 + 1).floatValue());
                    }
                    tmppath.clear();

                    path.close();
                    paint.setColor(WavelenghToRGB(xvalue));
                    canvas.drawPath(path, paint);


                    x_left = x_new;
                    y_left = y_new;
                    tmppath.add(x_left);
                    tmppath.add(y_left);
                }
            }
        }
    }

    //画彩色光谱
    private void drawColorRender(XYSeries series, Canvas canvas, List<Float> points, float yAxiValue, int startIndex) {
        //两个值以上才有效，points当中是x,y 并列的一组值，一个点至少有两个值
        if (points.size() > 2) {

            //获取y的最小值和最大值
            if(yAxiValue < 0){
                yAxiValue = 0;
            }else if (yAxiValue > canvas.getHeight()){
                yAxiValue = canvas.getHeight();
            }

            //填充画笔
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);//充满样式

            //获取第一个点的值（也就是开始nm波长）
            int nm_start = (int) series.getX(startIndex);
            //获取最后一个点的值（最后一个nm波长）
            int nm_end = (int)series.getX(startIndex + (points.size() / 2) - 1);
            //创建不同nm对应不同的颜色数组
            int[] colors = new int[nm_end - nm_start + 1];
            for(int i = 0; i <= nm_end - nm_start; i++){
                colors[i] = this.WavelenghToRGB(nm_start + i);
            }
            //创建线性渐变画笔
            LinearGradient lg = new LinearGradient(points.get(0).floatValue(),yAxiValue,
                    points.get(points.size() - 2).floatValue(),yAxiValue,
                    colors, null, Shader.TileMode.REPEAT);
            paint.setShader(lg);

            //绘制路径，把看的见得图形画的边界画一遍，然后填充颜色，标准方式
            Path path = new Path();
            path.moveTo(points.get(0).floatValue(), yAxiValue);
            for (int k1 = 0; k1 < points.size(); k1 += 2) {
                path.lineTo(points.get(k1).floatValue(), points.get(k1 + 1).floatValue());
            }
            path.lineTo(points.get(points.size() - 2).floatValue(), yAxiValue);
            path.close();

            canvas.drawPath(path, paint);
        }

    }

    protected void drawSeries(XYSeries series, Canvas canvas, Paint paint, List<Float> pointsList, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, XYMultipleSeriesRenderer.Orientation or, int startIndex) {

        super.drawSeries(series, canvas, paint, pointsList, seriesRenderer, yAxisValue, seriesIndex, or, startIndex);

        //填充颜色
        this.drawColorRender(series, canvas, pointsList, yAxisValue, startIndex);

    }

    //波长转颜色
    private int WavelenghToRGB(int Wavelength) {
        //可见光从380nm~780nm
        if (Wavelength < 380)
            Wavelength = 380;
        if (Wavelength > 780)
            Wavelength = 780;

        int iSPE1 = 450;
        int iSPE2 = 520;
        int iSPE3 = 620;
        double R;
        double G;
        double B;
        //小于450nm，只有蓝色值
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
            if (m1 >= 255)
                m1 = 255;
            if (m1 <= 0)
                m1 = 0;
            if (m2 >= 255)
                m2 = 255;
            if (m2 <= 0)
                m2 = 0;
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
            if (m1 >= 255)
                m1 = 255;
            if (m1 <= 0)
                m1 = 0;
            if (m2 >= 255)
                m2 = 255;
            if (m2 <= 0)
                m2 = 0;
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
        return Color.rgb(Red, Green, Blue);
    }
}
