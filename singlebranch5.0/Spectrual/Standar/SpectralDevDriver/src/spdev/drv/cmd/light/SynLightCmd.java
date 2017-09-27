/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.light;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.MIGPSPMEM;
import spdev.drv.data.SynLightPar;

/**
 *
 * @author Administrator
 */
public class SynLightCmd {
     public boolean IsLightEnable(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(MIGPSPMEM.SynLight.getMEM, MIGPSPMEM.SynLight.addr, 0x01, timeout);
        return tmpdata[0] != 0;
    }

    public void SetLightEnable(MIGPNahonDriver drv,  boolean value, int timeout) throws Exception {
        byte[] tmpdata = new byte[]{0x00};
        if (value) {
            tmpdata = new byte[]{0x01};
        }

        drv.SetMEM(MIGPSPMEM.SynLight.setMEM, MIGPSPMEM.SynLight.addr, 0x01, tmpdata, timeout);
    }
    
    public SynLightPar GetSynLightPar(MIGPNahonDriver drv,int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = drv.GetMEM(MIGPSPMEM.SynLight.getMEM, MIGPSPMEM.SynLight.addr, MIGPSPMEM.SynLight.length, timeout);

        SynLightPar par = new SynLightPar();
        par.plustype = tmpdata[1];
        par.pluswidth = NahonConvert.ByteArrayToUShort(tmpdata, 2);
        par.plusinterval = NahonConvert.ByteArrayToUShort(tmpdata, 4);
        par.pluslag = NahonConvert.ByteArrayToInteger(tmpdata, 6);
        par.plusnum = NahonConvert.ByteArrayToInteger(tmpdata, 10);
        return par;
    }


    public void SetSynLightPar(MIGPNahonDriver drv, SynLightPar par,int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = new byte[MIGPSPMEM.SynLight.length - 1];

        tmpdata[0] = (byte) par.plustype;
        System.arraycopy(NahonConvert.UShortToByteArray(par.pluswidth), 0, tmpdata, 1, 2);
        System.arraycopy(NahonConvert.UShortToByteArray(par.plusinterval), 0, tmpdata, 3, 2);
        System.arraycopy(NahonConvert.IntegerToByteArray(par.pluslag), 0, tmpdata, 5, 4);
        System.arraycopy(NahonConvert.IntegerToByteArray(par.plusnum), 0, tmpdata, 9, 4);
        drv.SetMEM(MIGPSPMEM.SynLight.setMEM,
                MIGPSPMEM.SynLight.addr + 0x01, tmpdata.length, tmpdata, timeout);
    }
}
