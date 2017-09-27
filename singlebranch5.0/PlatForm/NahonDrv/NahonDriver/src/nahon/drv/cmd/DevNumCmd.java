/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.cmd;

import nahon.drv.entry.MIGPNahonDriver;
import static nahon.pro.migp.MIGPCOMMEM.DevNum;

/**
 *
 * @author Administrator
 */
public class DevNumCmd {
    public byte GetFromDevice(MIGPNahonDriver drv, int timeout) throws Exception {
        return drv.GetMEM(DevNum.getMEM, DevNum.addr, DevNum.length, timeout)[0];
    }
    
    public boolean SetToDevice(MIGPNahonDriver drv, byte devaddr, int timeout) throws Exception {
        return drv.SetMEM(DevNum.setMEM, DevNum.addr, DevNum.length, new byte[]{devaddr}, timeout);        
    }
}
