/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public interface ISPDataReceive {
    public void ReceiveData(SSpectralDataPacket data) throws Exception;
}
