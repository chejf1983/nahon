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
public class Newton {

    /*拷贝向量*/
    private static void copy_vector(double from[], double to[]) {
        int k = from.length;
        int k2 = to.length;
        if (k != k2) {
            System.out.println("the two vector's length is not equal!");
            System.exit(0);
        }
        for (int i = 0; i < k; i++) {
            to[i] = from[i];
        }

    }

    /*牛顿插值法*/
    private static double[] Newton_inter_method(double[] X, double[] Y, double X0[]) {
        int m = X.length;
        int n = X0.length;
        double[] Y0 = new double[n];
        double[] cp_Y = new double[m];
        for (int i1 = 0; i1 < n; i1++) {//遍历X0  
            double t = 0;
            int j = 0;
            copy_vector(Y, cp_Y);
            int kk = j;
            /*求各级均差*/
            while (kk < m - 1) {
                kk = kk + 1;
                for (int i2 = kk; i2 < m; i2++) {
                    cp_Y[i2] = (cp_Y[i2] - cp_Y[kk - 1]) / (X[i2] - X[kk - 1]);
                }
            }
            /*求插值结果*/
            double temp = cp_Y[0];
            for (int i = 1; i <= m - 1; i++) {
                double u = 1;
                int jj = 0;
                while (jj < i) {
                    u *= (X0[i1] - X[jj]);
                    jj++;
                }
                temp += cp_Y[i] * u;
            }

            Y0[i1] = temp;
        }

        return Y0;
    }

    public static double predict(double[] X, double[] Y, int halfwindow, double X0) {
        double[] xtmp = new double[]{X0};

        int centerindex = 0;

        if (X0 < X[0]) {
            centerindex = 0;
        } else if (X0 > X[X.length - 1]) {
            centerindex = X.length - 1;
        } else {
            for (int i = 0; i < X.length; i++) {
                if (X[i] == X0) {
                    return Y[i];
                }

                if (X[i] < X0 && X[i + 1] > X0) {
                    centerindex = i;
                }
            }
        }

        int start = 0;  //窗口起点坐标
        int end = 0;    //窗口终点

        //起点
        if (centerindex < halfwindow) {
            start = 0;
        } else {
            start = centerindex - halfwindow;
        }

        //终点
        if (Y.length - centerindex < halfwindow + 1) {
            end = Y.length - 1;
        } else {
            end = centerindex + halfwindow;
        }

        double[] ybuffer = new double[end - start + 1];
        double[] xbuffer = new double[ybuffer.length];

        //寻找数组窗口
        System.arraycopy(Y, start, ybuffer, 0, ybuffer.length);
        System.arraycopy(X, start, xbuffer, 0, xbuffer.length);

        return Newton_inter_method(xbuffer, ybuffer, xtmp)[0];
    }

    public static float predict(float[] X, float[] Y, int len, float X0) {
        double[] x = new double[len];
        double[] y = new double[len];

        for (int i = 0; i < x.length; i++) {
            x[i] = X[i];
            y[i] = Y[i];
        }
        return (float) predict(x, y, len, (double) X0);
    }
}
