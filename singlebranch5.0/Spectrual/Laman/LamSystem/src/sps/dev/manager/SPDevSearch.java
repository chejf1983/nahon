/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import nahon.drv.absractio.AbstractIO;
import nahon.drv.cmd.EquipmentInfoCmd;
import nahon.drv.data.EquipmentInfo;
import nahon.drv.data.SConnectInfo;
import nahon.drv.entry.MIGPNahonDriver;

/**
 *
 * @author Administrator
 */
public class SPDevSearch implements ISPDevSearch{
    @Override
    public ISPDevDriver SearchDevice(AbstractIO io, byte hostaddr, byte dstaddr, int timeout) {
        try {
            SConnectInfo confino = new SConnectInfo(io, hostaddr, dstaddr);
            MIGPNahonDriver drv = new MIGPNahonDriver(confino);
            EquipmentInfo eia = new EquipmentInfoCmd().GetFromDevice(drv, timeout);
            return new SPDevDriver(eia, confino);
        } catch (Exception ex) {
            return null;
        }
    }
}
