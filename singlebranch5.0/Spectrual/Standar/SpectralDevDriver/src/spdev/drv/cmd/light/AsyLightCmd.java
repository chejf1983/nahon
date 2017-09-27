/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.light;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.MIGPSPMEM;
import spdev.drv.data.AsyLightPar;

/**
 *
 * @author Administrator
 */
public class AsyLightCmd {

    public boolean IsLightEnable(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(MIGPSPMEM.AsynLight.getMEM, MIGPSPMEM.AsynLight.addr, 0x01, timeout);
        return tmpdata[0] != 0;
    }

    public void SetLightEnable(MIGPNahonDriver drv,  boolean value, int timeout) throws Exception {
        byte[] tmpdata = new byte[]{0x00};
        if (value) {
            tmpdata = new byte[]{0x01};
        }

        drv.SetMEM(MIGPSPMEM.AsynLight.setMEM, MIGPSPMEM.AsynLight.addr, 0x01, tmpdata, timeout);
    }

    public AsyLightPar GetASynLightPar(MIGPNahonDriver drv, int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = drv.GetMEM(MIGPSPMEM.AsynLight.getMEM, MIGPSPMEM.AsynLight.addr, MIGPSPMEM.AsynLight.length, timeout);

        AsyLightPar par = new AsyLightPar();

        par.hightime = NahonConvert.ByteArrayToUShort(tmpdata, 1);
        par.lowtime = NahonConvert.ByteArrayToUShort(tmpdata, 3);

        return par;
    }

    public void SetASynLightPar(MIGPNahonDriver drv, AsyLightPar par,int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = new byte[MIGPSPMEM.AsynLight.length - 1];

        System.arraycopy(NahonConvert.UShortToByteArray(par.hightime), 0, tmpdata, 0, 2);
        System.arraycopy(NahonConvert.UShortToByteArray(par.lowtime), 0, tmpdata, 2, 2);
        drv.SetMEM(MIGPSPMEM.AsynLight.setMEM,
                MIGPSPMEM.AsynLight.addr + 0x01, tmpdata.length, tmpdata, timeout);
    }
}
