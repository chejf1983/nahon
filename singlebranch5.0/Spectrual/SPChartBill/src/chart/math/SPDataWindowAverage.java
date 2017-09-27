/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.math;

/**
 *
 * @author jiche
 */
public class SPDataWindowAverage {

    public static double[] WindowAverage(double[] value, int window) {
        if (value == null) {
            return null;
        }

        //复制返回值数组
        double[] ret = new double[value.length];

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
                if (index >= 0 && index < value.length) {
                    ret[i] += value[index];
                    windownum ++;
                }
            }
            //取平均
            ret[i] /= windownum;
        }

        return ret;
    }
}
