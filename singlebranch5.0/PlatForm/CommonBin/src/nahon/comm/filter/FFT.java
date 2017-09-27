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
/**
 * ***********************************************************************
 * Compilation: javac FFT.java Execution: java FFT N Dependencies: Complex.java
 *
 * Compute the FFT and inverse FFT of a length N complex sequence. Bare bones
 * implementation that runs in O(N log N) time. Our goal is to optimize the
 * clarity of the code, rather than performance.
 *
 * Limitations ----------- - assumes N is a power of 2
 *
 * - not the most memory efficient algorithm (because it uses an object type for
 * representing complex numbers and because it re-allocates memory for the
 * subarray, instead of doing in-place or reusing a single temporary array)
 *
 ************************************************************************
 */
public class FFT {

    public FFT() {

    }

    private double[] sin_table = new double[0];
    private double[] cos_table = new double[0];
    
    public int NodeNum(){
        return this.sin_table.length;
    }

    public void initTable(int N) {
        this.sin_table = new double[N / 2 + 1];
        this.cos_table = new double[N / 2 + 1];

        for (int k = 0; k < sin_table.length; k++) {
            double kth = -2 * k * Math.PI / N;
            this.sin_table[k] = Math.sin(kth);
            this.cos_table[k] = Math.cos(kth);
        }
    }

    private double getCos(int k, int N) {
        if (cos_table != null && cos_table.length == N) {
            return cos_table[k];
        } else {
            return Math.cos(-2 * k * Math.PI / N);
        }
    }

    private double getSin(int k, int N) {
        if (cos_table != null && cos_table.length == N) {
            return sin_table[k];
        } else {
            return Math.sin(-2 * k * Math.PI / N);
        }
    }

    // compute the FFT of x[], assuming its length is a power of 2  
    public Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case  
        if (N == 1) {
            return new Complex[]{x[0]};
        }

        // radix 2 Cooley-Tukey FFT  
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms  
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms  
        Complex[] odd = even;  // reuse the array  
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine  
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            //double kth = -2 * k * Math.PI / N;
            //Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            Complex wk = new Complex(this.getCos(k, N), this.getSin(k, N));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // compute the inverse FFT of x[], assuming its length is a power of 2  
    public Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate  
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT  
        y = fft(y);

        // take conjugate again  
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N  
        for (int i = 0; i < N; i++) {
            y[i] = y[i].scale(1.0 / N);
        }

        return y;

    }

    // compute the circular convolution of x and y  
    public Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length  
        // and are powers of 2  
        if (x.length != y.length) {
            throw new RuntimeException("Dimensions don't agree");
        }

        int N = x.length;

        // compute FFT of each sequence，求值  
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply，点值乘法  
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT，插值  
        return ifft(c);
    }

    // compute the linear convolution of x and y  
    public Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2 * x.length];//2n次数界，高阶系数为0.  
        for (int i = 0; i < x.length; i++) {
            a[i] = x[i];
        }
        for (int i = x.length; i < 2 * x.length; i++) {
            a[i] = ZERO;
        }

        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++) {
            b[i] = y[i];
        }
        for (int i = y.length; i < 2 * y.length; i++) {
            b[i] = ZERO;
        }

        return cconvolve(a, b);
    }

}
