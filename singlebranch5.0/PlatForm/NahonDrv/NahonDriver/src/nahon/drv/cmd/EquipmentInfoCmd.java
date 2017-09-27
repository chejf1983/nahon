/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.cmd;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.data.EquipmentInfo;
import nahon.drv.entry.MIGPNahonDriver;
import static nahon.pro.migp.MIGPCOMMEM.BuildDate;
import static nahon.pro.migp.MIGPCOMMEM.BuildSerialNum;
import static nahon.pro.migp.MIGPCOMMEM.DeviceName;
import static nahon.pro.migp.MIGPCOMMEM.Hardversion;
import static nahon.pro.migp.MIGPCOMMEM.SoftwareVersion;

/**
 *
 * @author Administrator
 */
public class EquipmentInfoCmd {
    
    public EquipmentInfo GetFromDevice(MIGPNahonDriver drv, int timeout) throws Exception {
        int EIAInfo_length = 0x44;
        byte[] tmpdata = drv.GetMEM(DeviceName.getMEM, DeviceName.addr, EIAInfo_length, timeout);
        EquipmentInfo EIA = new EquipmentInfo();

        EIA.DeviceName = NahonConvert.ByteArrayToString(tmpdata, DeviceName.addr, DeviceName.length);
        EIA.BuildDate = NahonConvert.ByteArrayToString(tmpdata, BuildDate.addr, BuildDate.length);
        EIA.BuildSerialNum = NahonConvert.ByteArrayToString(tmpdata, BuildSerialNum.addr, BuildSerialNum.length);
        EIA.Hardversion = NahonConvert.ByteArrayToString(tmpdata, Hardversion.addr, Hardversion.length);
        EIA.SoftwareVersion = NahonConvert.ByteArrayToString(tmpdata, SoftwareVersion.addr, SoftwareVersion.length);
        return EIA;
    }

    public boolean SetToDevice(MIGPNahonDriver drv, EquipmentInfo eiaInfo, int timeout) throws Exception {
        int EIAInfo_length = 0x44;
        byte[] tmpdata = new byte[EIAInfo_length];

        byte[] tmp = NahonConvert.StringToByte(eiaInfo.DeviceName, DeviceName.length);
        System.arraycopy(tmp, 0, tmpdata, DeviceName.addr, tmp.length);

        tmp = NahonConvert.StringToByte(eiaInfo.BuildDate, BuildDate.length);
        System.arraycopy(tmp, 0, tmpdata, BuildDate.addr, tmp.length);

        tmp = NahonConvert.StringToByte(eiaInfo.BuildSerialNum, BuildSerialNum.length);
        System.arraycopy(tmp, 0, tmpdata, BuildSerialNum.addr, tmp.length);

        tmp = NahonConvert.StringToByte(eiaInfo.Hardversion, Hardversion.length);
        System.arraycopy(tmp, 0, tmpdata, Hardversion.addr, tmp.length);

        tmp = NahonConvert.StringToByte(eiaInfo.SoftwareVersion, SoftwareVersion.length);
        System.arraycopy(tmp, 0, tmpdata, SoftwareVersion.addr, tmp.length);

        return drv.SetMEM(DeviceName.setMEM, DeviceName.addr, EIAInfo_length, tmpdata, timeout);
    }

}
