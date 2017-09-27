/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.drv.cmd.light;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import nahon.pro.migp.MIGPCOMMEM;

/**
 *
 * @author Administrator
 */
public class LaserLightCmd {
    
    // <editor-fold defaultstate="collapsed" desc="协议层接口"> 
    private static MIGPCOMMEM.NVPA LLight = MIGPCOMMEM.instance.new NVPA(0x10, 0x02);
    private static MIGPCOMMEM.VPA LLightSwitch  = MIGPCOMMEM.instance.new VPA(0x16, 0x01);

    public int GetLLIr(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(LLight.getMEM,
                LLight.addr, LLight.length, timeout);

        return NahonConvert.ByteArrayToUShort(tmpdata, 0);
    }

    public void SetLLIr(MIGPNahonDriver drv, int Ir, int timeout) throws Exception {
        drv.SetMEM(LLight.setMEM, LLight.addr, LLight.length, NahonConvert.UShortToByteArray(Ir), timeout);
    }
    
    public boolean IsLightEnable(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(LLightSwitch.getMEM, LLightSwitch.addr, LLightSwitch.length, timeout);
        return tmpdata[0] != 0;
    }

    public void SetLightEnable(MIGPNahonDriver drv,  boolean value, int timeout) throws Exception {
        byte[] tmpdata = new byte[]{0x00};
        if (value) {
            tmpdata = new byte[]{0x01};
        }

        drv.SetMEM(LLightSwitch.setMEM, LLightSwitch.addr, LLightSwitch.length, tmpdata, timeout);
    }
    // </editor-fold>

}
