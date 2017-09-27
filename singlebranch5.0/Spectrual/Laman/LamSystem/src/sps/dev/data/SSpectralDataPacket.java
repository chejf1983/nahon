/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

import java.util.Date;
import jxl.write.DateTime;

/**
 *
 * @author Administrator
 */
public class SSpectralDataPacket {

    public Date time;
    public double IP;
    public SSPData data;

    public SSpectralDataPacket() {
    }

    public SSpectralDataPacket(SSpectralDataPacket data) {
        this.time = data.time;
        this.IP = data.IP;
        this.data = new SSPData(data.data);
    }

    public SSpectralDataPacket(SSPData ADValue, double IP) {
        this.IP = IP;
        this.data = ADValue;
        this.time = new Date();
    }
}
