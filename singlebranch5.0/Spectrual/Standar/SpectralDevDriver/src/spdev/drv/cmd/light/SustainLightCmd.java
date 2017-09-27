/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.light;

import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.MIGPSPMEM;

/**
 *
 * @author Administrator
 */
public class SustainLightCmd {
     public boolean IsLightEnable(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(MIGPSPMEM.SustainLight.getMEM, MIGPSPMEM.SustainLight.addr, 0x01, timeout);
        return tmpdata[0] != 0;
    }

    public void SetLightEnable(MIGPNahonDriver drv,  boolean value, int timeout) throws Exception {
        byte[] tmpdata = new byte[]{0x00};
        if (value) {
            tmpdata = new byte[]{0x01};
        }

        drv.SetMEM(MIGPSPMEM.SustainLight.setMEM, MIGPSPMEM.SustainLight.addr, 0x01, tmpdata, timeout);
    }
    
    public boolean[] GetLightPar(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] ret = drv.GetMEM(MIGPSPMEM.LightPar.getMEM, MIGPSPMEM.LightPar.addr, MIGPSPMEM.LightPar.length, timeout);
        byte lightstate = ret[0];
        boolean[] bret = new boolean[8];

        for (int i = 0; i < bret.length; i++) {
            bret[i] = ((lightstate >> i) & 0x01) != 0;
        }

        return bret;
    }

    public void SetLightPar(MIGPNahonDriver drv, boolean[] value, int timeout) throws Exception {
        if (value == null || value.length != 8) {
            throw new Exception("Invalude input value");
        }

        byte state = 0;
        for (int i = 0; i < value.length; i++) {
            if (value[i]) {
                state |= 0x01 << i;
            }
        }
       drv.SetMEM(MIGPSPMEM.LightPar.setMEM, MIGPSPMEM.LightPar.addr, MIGPSPMEM.LightPar.length, new byte[]{state}, timeout);
    }
}
