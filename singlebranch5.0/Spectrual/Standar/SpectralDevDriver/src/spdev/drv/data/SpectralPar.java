/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

/**
 *
 * @author jiche
 */
public class SpectralPar {

    public int maxWaveLength = 0;
    public int minWaveLength = 0;
    public int maxIntegralTime = 0;
    public int minIntegralTime = 0;
    public int nodeNumber = 0;

    public SpectralPar(int minWaveLength,
            int maxWaveLength,
            int minIntegralTime,            
            int maxIntegralTime,
            int nodeNumber) {

        this.maxWaveLength = maxWaveLength;
        this.minWaveLength = minWaveLength;
        this.maxIntegralTime = maxIntegralTime;
        this.minIntegralTime = minIntegralTime;
        this.nodeNumber = nodeNumber;
    }

    public SpectralPar(SpectralPar spar) {
        this.maxWaveLength = spar.maxWaveLength;
        this.minWaveLength = spar.minWaveLength;
        this.maxIntegralTime = spar.maxIntegralTime;
        this.minIntegralTime = spar.minIntegralTime;
        this.nodeNumber = spar.nodeNumber;
    }

    public SpectralPar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
