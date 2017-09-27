/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

/**
 *
 * @author Administrator
 */
public class DataCollectPar {

    public static int SoftMode = 0x00;
    public static int UpEdge = 0x04;
    public static int DowEdge = 0x05;
    public static int HighVolt = 0x08;
    public static int LowVolt = 0x09;

    public int WorkMode;
    public float integralTime;//ms
    public int averageTime;

    public DataCollectPar(float integralTime, int averageTime, int mode) {
        this.WorkMode = mode;
        this.integralTime = integralTime;
        this.averageTime = averageTime;
    }

    public DataCollectPar(float integralTime, int averageTime) {
        this.WorkMode = SoftMode;
        this.integralTime = integralTime;
        this.averageTime = averageTime;
    }

    public DataCollectPar(DataCollectPar par) {
        this.WorkMode = par.WorkMode;
        this.integralTime = par.integralTime;
        this.averageTime = par.averageTime;
    }

    public boolean EqualTo(DataCollectPar par) {
        return this.integralTime == par.integralTime
                && this.averageTime == par.averageTime
                && this.WorkMode == par.WorkMode;
    }
}
