/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.solapp.data;

import java.math.BigDecimal;
import nahon.comm.filter.Newton;
import sps.dev.data.SSPData;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class SolTestData {

    public SSpectralDataPacket original_data;
    public SSPData absorbe_rate;
    public double Nd;
    public double Pd;
    public double Kd;

    public SolTestData(SSpectralDataPacket relativedata, SSPData rate) {
        this.original_data = relativedata;
        this.absorbe_rate = rate;
        if (this.absorbe_rate != null) {
            this.Nd = Newton.predict(rate.waveIndex, rate.datavalue, 10, 420);
            this.Nd =new BigDecimal(this.Nd).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            this.Pd = Newton.predict(rate.waveIndex, rate.datavalue, 10, 685);
            this.Pd =new BigDecimal(this.Pd).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            this.Kd = this.Pd;
        }
    }
}
