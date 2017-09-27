/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.data;

import nahon.drv.absractio.AbstractIO;

/**
 *
 * @author jiche
 */
public class SConnectInfo {
    public AbstractIO io;
    public byte dstaddr;
    public byte srcaddr;

    public SConnectInfo(AbstractIO io, byte srcaddr, byte dstaddr) {
        this.io = io;
        this.dstaddr = dstaddr;
        this.srcaddr = srcaddr;
    }

    public SConnectInfo(SConnectInfo par) {
        this.io = par.io;
        this.dstaddr = par.dstaddr;
        this.srcaddr = par.srcaddr;
    }

    public boolean Equalto(SConnectInfo par) {
        return this.dstaddr == par.dstaddr
                && this.io == par.io
                && this.srcaddr == par.srcaddr;
    }
}
