/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.bill;

/**
 *
 * @author Administrator
 */
public class WindowSmoothFilter {

    //窗口平滑计算模块
    public double[] Filter(double[] datavalue, int window) {
        if (datavalue == null) {
            return datavalue;
        }

        if (window > datavalue.length / 10) {
            //窗口太大，不做平滑
            return datavalue;
        }

        //复制返回值数组
        double[] ret = new double[datavalue.length];

        //计算窗口启使偏移
        int wleftoffer = 0 - window / 2;

        for (int i = 0; i < ret.length; i++) {
            //初值赋0
            ret[i] = 0;

            //有效窗内值个数
            int windownum = 0;
            for (int w = 0; w < window; w++) {
                //计算窗口坐标
                int index = i + wleftoffer + w;
                //如果窗口坐标有效，添加基础值，否则添0
                if (index >= 0 && index < datavalue.length) {
                    ret[i] += datavalue[index];
                    windownum++;
                }
            }
            //取平均
            ret[i] /= windownum;
        }

        return ret;
    }

}
