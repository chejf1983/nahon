/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

import java.util.Date;

/**
 *
 * @author jiche
 */
public class SpectralDataPacket {
    public static int MaxValue = 65535;
    public static int MinValue = 0;

    public SpectralDataPacket(SpectralDataPacket pkt) {
        this.ADValue = new SPData(pkt.ADValue);
        this.time = pkt.time;
        this.IP = pkt.IP;
    }

    public SpectralDataPacket(
            SPData ADValue,
            double IP) {
        this.ADValue = (ADValue);
        this.time = new Date();
        this.IP = IP;
    }

    public final Date time;
    public final SPData ADValue;
    public final double IP;
    //public final SPData RelativeData;
}
