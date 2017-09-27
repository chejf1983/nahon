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
public class DarkDataFilter {

    // <editor-fold defaultstate="collapsed" desc="暗电流控制"> 
    private boolean isdkEnable = false;
    private double[] dkdata = null;

    public boolean IsDKEnable() {
        return this.isdkEnable && this.dkdata != null;
    }

    public void SetDkEnable(boolean value){
        this.isdkEnable = value;
    }

    public void UpdateDKData(double[] oradata) {
        this.dkdata = new double[oradata.length];
        System.arraycopy(oradata, 0, this.dkdata, 0, oradata.length);

    }

    public void Filter(double[] spdata) {
        if (this.IsDKEnable()) {
            //暗电流修正
            for (int i = 0; i < spdata.length; i++) {
                spdata[i] -= dkdata[i];
            }
        }
    }
    // </editor-fold> 
}
