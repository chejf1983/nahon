/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

/**
 *
 * @author jiche
 */
public class WaveCaculatePar {

    public double C0 = 0d;
    public double C1 = 0d;
    public double C2 = 0d;
    public double C3 = 0d;

    public WaveCaculatePar() {

    }

    public WaveCaculatePar(WaveCaculatePar wcpar) {
        this.C0 = wcpar.C0;
        this.C1 = wcpar.C1;
        this.C2 = wcpar.C2;
        this.C3 = wcpar.C3;
    }

    public boolean EuqalTo(WaveCaculatePar wpar) {
        return (Double.compare(wpar.C0, this.C0) == 0)
                && (Double.compare(wpar.C1, this.C1) == 0)
                && (Double.compare(wpar.C2, this.C2) == 0)
                && (Double.compare(wpar.C3, this.C3) == 0);
    }
}
