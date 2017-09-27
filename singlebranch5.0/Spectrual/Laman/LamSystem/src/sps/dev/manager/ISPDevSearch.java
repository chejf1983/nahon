/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import nahon.drv.absractio.AbstractIO;

/**
 *
 * @author Administrator
 */
public interface ISPDevSearch {
    public ISPDevDriver SearchDevice(AbstractIO io, byte hostaddr, byte dstaddr, int timeout);
}
