/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.bill;

import nahon.comm.filter.Theleastsquaremethod;

/**
 *
 * @author Administrator
 */
public class SmoothFilter {

    // <editor-fold defaultstate="collapsed" desc="最小二乘法平滑">  
    private boolean isSmoothenable = false;

    public boolean IsSmoothEnable() {
        return this.isSmoothenable;
    }

    public void SetSmoothEnable(boolean value) {
        this.isSmoothenable = value;
    }

    public void Filter(double[] pixelIndex, double[] datavalue){
        Theleastsquaremethod.smooth(pixelIndex, datavalue, 5);
    }
    // </editor-fold> 
}
