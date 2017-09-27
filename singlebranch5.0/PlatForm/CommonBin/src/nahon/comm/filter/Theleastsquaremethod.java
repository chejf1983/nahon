/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.filter;

/**
 *
 * @author Administrator
 */
public class Theleastsquaremethod {

    /**
     * 最小二乘法 y=ax+b
     *
     * @author Administrator
     *
     */
    private static double a;

    private static double b;

    /**
     * Ax+By+C = 0; distance = |Ax0+ By0 + C| / sqrt(A^2 + B^2)
     *
     * @param x
     * @param y
     * @return
     */
    private static double calPointDistance(double x, double y) {
        return Math.abs(a * x - y + b) / Math.sqrt(a * a + 1);
    }

    /**
     * a=(NΣxy-ΣxΣy)/(NΣx^2-(Σx)^2) b=y(平均)-a*x（平均）
     *
     * @param x
     * @param y
     * @return
     */
    private static void train(double x[], double y[], int len) {
        int num = len;
        double xy = 0.0, xT = 0.0, yT = 0.0, xS = 0.0;
        for (int i = 0; i < num; i++) {
            xy += x[i] * y[i];
            xT += x[i];
            yT += y[i];
            xS += Math.pow(x[i], 2.0);
        }
        a = (num * xy - xT * yT) / (num * xS - Math.pow(xT, 2.0));
        b = yT / num - a * xT / num;
    }

    /**
     * 预测
     *
     * @param xValue
     * @return
     */
    private static double predict(double xValue) {
        //System.out.println("a=" + a);
        //System.out.println("b=" + b);
        return a * xValue + b;
    }

    private static double predict(double[] xvalue, double[] yvalue, int i, int halfwindow, double newx) {
        int start = 0;  //窗口起点坐标
        int end = 0;    //窗口终点

        //起点
        if (i < halfwindow) {
            start = 0;
        } else {
            start = i - halfwindow;
        }

        //终点
        if (yvalue.length - i < halfwindow + 1) {
            end = yvalue.length - 1;
        } else {
            end = i + halfwindow;
        }

        double[] ybuffer = new double[end - start + 1];
        double[] xbuffer = new double[ybuffer.length];

        //寻找数组窗口
        System.arraycopy(yvalue, start, ybuffer, 0, ybuffer.length);
        System.arraycopy(xvalue, start, xbuffer, 0, xbuffer.length);

        //最小二乘法，计算预测值
        Theleastsquaremethod.train(xbuffer, ybuffer, ybuffer.length);
        return Theleastsquaremethod.predict(newx);
    }

    public static double predict(double[] xvalue, double[] yvalue, int halfwindow, double newx) {
        for (int i = 0; i < xvalue.length; i++) {
            if (newx == xvalue[i]) {
                return yvalue[i];
            }
        }

        int centerindex = 0;

        if (newx <= xvalue[0]) {
            centerindex = 0;
        } else if (newx >= xvalue[xvalue.length - 1]) {
            centerindex = xvalue.length - 1;
        } else {
            for (int i = 0; i < xvalue.length; i++) {
                if (xvalue[i] < newx && xvalue[i + 1] > newx) {
                    centerindex = i;
                }
            }
        }

        return predict(xvalue, yvalue, centerindex, halfwindow, newx);
    }

    private static void smooth_onearea(double[] xvalue, double[] yvalue, int halfwindow) {
        double[] tmpy = new double[yvalue.length];
        for (int i = 0; i < yvalue.length; i++) {
            tmpy[i] = yvalue[i];
        }

        for (int i = 0; i < yvalue.length; i++) {
            yvalue[i] = predict(xvalue, tmpy, i, halfwindow, xvalue[i]);
        }
    }

    public static void smooth(double[] xvalue, double[] yvalue, int halfwindow) {
        double[] dist = new double[yvalue.length];

        for (int i = 1; i < yvalue.length; i++) {
            dist[i] = Math.abs(yvalue[i] - yvalue[i - 1]);
        }
        dist[0] = dist[1];

        int start = 0;
        //double avrdelta = dist[0];
        for (int i = 0; i < yvalue.length; i++) {
            //变化率，超过平均3倍，表示不连续了
            if (dist[i] - 50 > 100 * 3 || dist[i] * 3 + 100 < 100) {
                if (i - start > 1) {
                    double[] tmpx = new double[i - start];
                    double[] tmpy = new double[i - start];

                    System.arraycopy(xvalue, start, tmpx, 0, tmpx.length);
                    System.arraycopy(yvalue, start, tmpy, 0, tmpy.length);

                    smooth_onearea(tmpx, tmpy, halfwindow);
                    System.arraycopy(tmpy, 0, yvalue, start, tmpy.length);
                }
                start = i;
            }
        }

        if (yvalue.length - start > 1) {
            double[] tmpx = new double[yvalue.length - start];
            double[] tmpy = new double[yvalue.length - start];

            System.arraycopy(xvalue, start, tmpx, 0, tmpx.length);
            System.arraycopy(yvalue, start, tmpy, 0, tmpy.length);

            smooth_onearea(tmpx, tmpy, halfwindow);
            System.arraycopy(tmpy, 0, yvalue, start, tmpy.length);
        }
    }

    public static double predict(float[] xvalue, float[] yvalue, int halfwindow, float newx) {
        double[] xtmp = new double[xvalue.length];
        double[] ytmp = new double[yvalue.length];

        for (int i = 0; i < xtmp.length; i++) {
            xtmp[i] = (double) xvalue[i];
            ytmp[i] = (double) yvalue[i];
        }

        return predict(xtmp, ytmp, halfwindow, (double) newx);
    }

    public static void smooth(float[] xvalue, float[] yvalue, int halfwindow) {
        double[] xtmp = new double[xvalue.length];
        double[] ytmp = new double[yvalue.length];

        for (int i = 0; i < xtmp.length; i++) {
            xtmp[i] = (double) xvalue[i];
            ytmp[i] = (double) yvalue[i];
        }

        smooth(xtmp, ytmp, halfwindow);
    }

    public static void main(String args[]) {
        double[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] y = {23, 44, 32, 56, 33, 34, 55, 65, 45, 55};
        Theleastsquaremethod.train(x, y, y.length);
        System.out.println(Theleastsquaremethod.predict(10.0));
    }

}
