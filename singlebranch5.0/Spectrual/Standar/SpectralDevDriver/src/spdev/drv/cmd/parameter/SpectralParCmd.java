/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.parameter;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.SpectralPar;
import static spdev.drv.data.MIGPSPMEM.MinwaveLength;

/**
 *
 * @author Administrator
 */
public class SpectralParCmd {
        public SpectralPar GetSpectralParameter(MIGPNahonDriver drv,int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmp = drv.GetMEM(MinwaveLength.getMEM, MinwaveLength.addr, 14, timeout);
        SpectralPar ret = new SpectralPar(
                NahonConvert.ByteArrayToUShort(tmp, 0),
                NahonConvert.ByteArrayToUShort(tmp, 2),
                NahonConvert.ByteArrayToInteger(tmp, 4),
                NahonConvert.ByteArrayToInteger(tmp, 8),
                NahonConvert.ByteArrayToUShort(tmp, 12));
        return ret;
    }
}
