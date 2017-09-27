/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.bill;

import nahon.comm.filter.Kalman;
import sps.dev.data.SSPData;

/**
 *
 * @author Administrator
 */
public class KalManFilter {

    // <editor-fold defaultstate="collapsed" desc="卡尔曼滤波"> 
    private boolean kalenable = false;
    private Kalman kalmanfilter;

    public void InitKalman(int num) {
        //初始化卡尔曼滤波器
        this.kalmanfilter = new Kalman(num, 50);
    }

    public boolean IsKalmanFilterEnable() {
        return kalenable && this.kalmanfilter != null;
    }

    public void SetKalmanFilterEnable(boolean value) {
        this.kalenable = value;
    }

    public double[] Filter(double[] spdata){
        return this.kalmanfilter.Filter(spdata);
    }
    // </editor-fold> 
}
