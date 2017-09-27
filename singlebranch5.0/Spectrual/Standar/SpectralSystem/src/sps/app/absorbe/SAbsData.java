/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.absorbe;

import sps.dev.data.SSPData;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class SAbsData {
    public SSpectralDataPacket original_data;
    public SSPData absorbe_rate;
    
    public SAbsData(SSpectralDataPacket relativedata, SSPData rate) {
        this.original_data = relativedata;
        this.absorbe_rate = rate;
    }
}
