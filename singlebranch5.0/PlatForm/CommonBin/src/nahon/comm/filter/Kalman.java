/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.filter;

/**
 * 卡尔曼滤波
 *
 * @author Administrator
 */
public class Kalman {

    private int nodenum;      //点个数
    private double nose = 0;  //噪声大小
    private double stddata[]; //基础值（开始做kalman计算的值）
    private double x_last[];  //预判值
    private double p_last[];  //预判值得误差
    private double Q = 0; //过程噪声，假设没有
    private double R;     //测量噪声

    public Kalman(int nodenum, double noise) {
        this.nodenum = nodenum;
        this.nose = noise;
        this.x_last = new double[nodenum];
        this.p_last = new double[nodenum];
        this.stddata = new double[nodenum];
        this.R = noise; //测量噪声，等于暗电流波动范围。
        this.Q = noise / 20;//0;     //过程噪声，假设没有
        for (int i = 0; i < nodenum; i++) {
            x_last[i] = 0;
            stddata[i] = 0;
            p_last[i] = noise;
        }
    }

    //滤波
    public double[] Filter(double[] data) {
        if (data.length != nodenum) {
            return data;
        }

        //判断信号相似
//        boolean isSimilar = SingleCompare.Compara(data, stddata);
        double max = 0;
        for (int nid = 0; nid < nodenum; nid++) {
            if(max < data[nid]){
                max = data[nid];
            }
        }
        //如果不相似，认为是新信号，重新开始卡尔曼收敛
//        if (!isSimilar) {
//        if (false) {
        for (int nid = 0; nid < nodenum; nid++) {
            if (Math.abs(data[nid] - x_last[nid]) > (this.R + max / this.R)) {
                stddata[nid] = data[nid];
                x_last[nid] = data[nid];
                p_last[nid] = this.nose;
            } else {
                double x_mid = x_last[nid];

                //计算误差权重（如果没有Q,过程误差，权重值将趋于0
                //如果有过程误差Q，权重值趋于 Q/R）
                double p_mid = p_last[nid] + Q;
                double kg = p_mid / (p_mid + R);

                x_last[nid] = x_mid + kg * (data[nid] - x_mid); //估计出的最有值  
                p_last[nid] = (1 - kg) * p_mid;                 //最优值对应的协方差  
                data[nid] = x_last[nid];
            }
        }
//        } else {
//            for (int nid = 0; nid < nodenum; nid++) {
//                double x_mid = x_last[nid];
//
//                //计算误差权重（如果没有Q,过程误差，权重值将趋于0
//                //如果有过程误差Q，权重值趋于 Q/R）
//                double p_mid = p_last[nid] + Q;
//                double kg = p_mid / (p_mid + R);
//
//                x_last[nid] = x_mid + kg * (data[nid] - x_mid); //估计出的最有值  
//                p_last[nid] = (1 - kg) * p_mid;                 //最优值对应的协方差  
//                data[nid] = x_last[nid];
//            }
//        }

        return data;
    }
}
