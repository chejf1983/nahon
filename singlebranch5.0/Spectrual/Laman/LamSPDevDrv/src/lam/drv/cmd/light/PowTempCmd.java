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
public class PowTempCmd {    
    private static final MIGPCOMMEM.MDA Temperature = MIGPCOMMEM.instance.new MDA(0x1000, 0x04);
    private static final MIGPCOMMEM.MDA Power = MIGPCOMMEM.instance.new MDA(0x1004, 0x04);
    
    public float GetTemperature(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(Temperature.getMEM,
                Temperature.addr, Temperature.length, timeout);

        return NahonConvert.ByteArrayToFloat(tmpdata, 0);
    }
    
    public float GetBatteryPower(MIGPNahonDriver drv, int timeout) throws Exception {
        byte[] tmpdata = drv.GetMEM(Power.getMEM,
                Power.addr, Power.length, timeout);

        return NahonConvert.ByteArrayToFloat(tmpdata, 0);
    }
}
