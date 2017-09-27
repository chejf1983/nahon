/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.parameter;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.WaveCaculatePar;
import static spdev.drv.data.MIGPSPMEM.*;

/**
 *
 * @author Administrator
 */
public class WaveCaculateParCmd {

    /**
     *
     * @return WaveCaculatePar
     * @throws Exception
     */
    public WaveCaculatePar GetWaveCaculateParameter(MIGPNahonDriver drv, int timeout) throws Exception {
//        int timeout = 100;
        WaveCaculatePar pc = new WaveCaculatePar();

        byte[] tmpdata = drv.GetMEM(C0.getMEM, C0.addr, 32, timeout);

        pc.C0 = NahonConvert.ByteArrayToDouble(tmpdata, 0);
        pc.C1 = NahonConvert.ByteArrayToDouble(tmpdata, 8);
        pc.C2 = NahonConvert.ByteArrayToDouble(tmpdata, 16);
        pc.C3 = NahonConvert.ByteArrayToDouble(tmpdata, 24);

        return pc;
    }

    /**
     *
     * @param par set WaveCaculatePar
     * @throws Exception
     */
    public void SetWaveCaculateParameter(MIGPNahonDriver drv, WaveCaculatePar par, int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = new byte[32];

        System.arraycopy(NahonConvert.DoubleToByteArray(par.C0), 0, tmpdata, 0, C0.length);
        System.arraycopy(NahonConvert.DoubleToByteArray(par.C1), 0, tmpdata, 8, C1.length);
        System.arraycopy(NahonConvert.DoubleToByteArray(par.C2), 0, tmpdata, 16, C2.length);
        System.arraycopy(NahonConvert.DoubleToByteArray(par.C3), 0, tmpdata, 24, C3.length);

        drv.SetMEM(C0.setMEM, C0.addr, tmpdata.length, tmpdata, timeout);
    }
}
