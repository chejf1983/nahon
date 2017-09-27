/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.pro.migp;

/**
 *
 * @author jiche
 */
public class MIGPPacket {

    public MIGPPacket(byte dstAddress, byte srcAddres, byte CMD, byte[] data) {
        this.dstAddress = dstAddress;
        this.srcAddress = srcAddres;
        this.CMD = CMD;
        this.data = data;
    }

    public final byte dstAddress;
    public final byte srcAddress;
    public final byte CMD;
    public final byte[] data;

    MIGPPacket(MIGPPacket rcPkt) {
        this.dstAddress = rcPkt.dstAddress;
        this.srcAddress = rcPkt.srcAddress;
        this.CMD = rcPkt.CMD;
        this.data = rcPkt.data;
    }

    @Override
    public String toString() {
        String ret = "dstDevNum:" + dstAddress;
        ret += " srcDevNum:" + srcAddress;
        ret += " CMD:" + CMD;
        ret += " length:" + data.length;

        ret += " data:";
        for (byte b : data) {
            ret += String.format("0x%02X ", b);
        }
        return ret;
    }
}
