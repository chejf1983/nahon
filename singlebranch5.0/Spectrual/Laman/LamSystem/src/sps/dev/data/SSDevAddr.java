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
public class SSDevAddr {

    public byte host_laddr;
    public byte devaddr;

    public boolean SameAs(SSDevAddr address) {
        return this.host_laddr == address.host_laddr && this.devaddr == address.devaddr;
    }
}
