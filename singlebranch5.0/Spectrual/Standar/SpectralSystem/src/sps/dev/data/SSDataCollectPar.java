/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author Administrator
 */
public class SSDataCollectPar {

    public static int SoftMode = 0x00;
    public static int UpEdge = 0x04;
    public static int DowEdge = 0x05;
    public static int HighVolt = 0x08;
    public static int LowVolt = 0x09;

    public int WorkMode;
    public float integralTime;//ms
    public int averageTime;

    public SSDataCollectPar() {

    }

    public SSDataCollectPar(float GetIntegertime, int GetAverageTime, int GetCollectMode) {
        this.integralTime = GetIntegertime;
        this.averageTime = GetAverageTime;
        this.WorkMode = GetCollectMode;
    }

    public SSDataCollectPar(SSDataCollectPar collectpar) {
        this.integralTime = collectpar.integralTime;
        this.averageTime = collectpar.averageTime;
        this.WorkMode = collectpar.WorkMode;
    }
    
     public boolean EqualTo(SSDataCollectPar par) {
        return this.integralTime == par.integralTime
                && this.averageTime == par.averageTime
                && this.WorkMode == par.WorkMode;
    }
}
