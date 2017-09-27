/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table.data;

/**
 *
 * @author Administrator
 */
public class TSPData {
    public double[] pixelIndex;
    public double[] waveIndex;
    public double[] datavalue;

    public TSPData() {
    }

    public TSPData(TSPData pkt) {
        this(pkt.waveIndex, pkt.datavalue);
    }

    public TSPData(double[] waveIndex, double[] datavalue) {
        this.pixelIndex = new double[waveIndex.length];
        this.waveIndex = new double[waveIndex.length];
        this.datavalue = new double[datavalue.length];
        for (int i = 0; i < datavalue.length; i++) {
            this.pixelIndex[i] = i + 1;
            this.waveIndex[i] = waveIndex[i];
            this.datavalue[i] = datavalue[i];
        }
    }
}
