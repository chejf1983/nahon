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
public class BattwoseFilter {

    private FFT fft;
    private double[] bat_window = new double[0];
    private boolean batenable = false;

    public BattwoseFilter() {
        fft = new FFT();
    }

    public boolean IsBatFilterEnable() {
        return batenable && bat_window.length > 0;
    }

    public void SetBatFilterEnable(boolean value) {
        this.batenable = value;
    }

    public void InitBattwoseWindow(int PointNum, int start, int N) {
        this.fft.initTable(PointNum);

        this.bat_window = new double[PointNum];
        for (int i = 0; i < PointNum / 2; i++) {
            double value = 1 / (1 + Math.pow(((float) i / (float) start), N));
            this.bat_window[i] = value;
            this.bat_window[PointNum - i - 1] = value;
        }
    }

    private Complex[] Convert(double[] input) {
        Complex[] smdata = new Complex[input.length];
        for (int i = 0; i < smdata.length; i++) {
            //虚部直接取0
            smdata[i] = new Complex(input[i], 0);
        }

        return smdata;
    }

    private double[] IConvert(Complex[] soutput) {
        double[] out = new double[soutput.length];
        //
        for (int i = 0; i < out.length; i++) {
            //可以直接使用实数,因为虚部通常非常的小
            out[i] = soutput[i].re();
            //标准的情况需要用模长，以及判断实部正负
            //out[i] = soutput[i].abs();
        }

        return out;
    }

    //滤波
    public double[] Filter(double[] input) {
        if (this.bat_window.length == input.length) {
            //转换复坐标
            Complex[] sinput = Convert(input);

            //傅里叶变换获取频域
            Complex[] fpdata = fft.fft(sinput);

            //信号频谱乘以窗口系数
            for (int i = 0; i < fpdata.length; i++) {
                fpdata[i] = fpdata[i].scale(this.bat_window[i]);
            }

            //傅里叶逆变换
            Complex[] sout = fft.ifft(fpdata);

            //复数转实数
            double[] out = IConvert(sout);

            for (int i = 0; i < out.length / 400; i++) {
                out[out.length - i - 1] = input[input.length - i - 1];
                out[i] = input[i];
            }
            return out;
        } else {
            return input;
        }
    }
}
