/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.collect;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.DataCollectPar;
import static spdev.drv.data.MIGPSPMEM.AverageTime;
import static spdev.drv.data.MIGPSPMEM.CollectMode;
import static spdev.drv.data.MIGPSPMEM.IntegralTime;

/**
 *
 * @author Administrator
 */
public class DataCollectParCmd {
    
    public DataCollectPar GetCollectPar(MIGPNahonDriver drv, int timeout) throws Exception {
         int datalen = CollectMode.length + IntegralTime.length + AverageTime.length;
        byte[] data = drv.GetMEM(CollectMode.getMEM, CollectMode.addr, datalen, timeout);

        DataCollectPar tmp = new DataCollectPar(
                (float) NahonConvert.ByteArrayToInteger(data, 1) / 1000,
                NahonConvert.ByteArrayToUShort(data, 5),
                data[0]);
        return tmp;
    }
    
    public boolean SetCollectPar(MIGPNahonDriver drv, DataCollectPar time, int timeout) throws Exception {
        int datalen = CollectMode.length + IntegralTime.length + AverageTime.length;
        byte[] data = new byte[datalen];

        data[0] = (byte) time.WorkMode;
        System.arraycopy(NahonConvert.IntegerToByteArray((int) (time.integralTime * 1000)),
                0, data, 1, IntegralTime.length);
        System.arraycopy(NahonConvert.UShortToByteArray(time.averageTime),
                0, data, 5, AverageTime.length);

//            this.drv.migpsend.SetMEM(SETVPA, IntegralTime_Addr,
//                    IntegralTime_Length, NahonConvert.IntegerToByteArray((int) (time.integralTime * 1000)), timeout);
//            this.drv.migpsend.SetMEM(SETVPA, AverageTime_Addr,
//                    AverageTime_Length, NahonConvert.UShortToByteArray(time.averageTime), timeout);
//            this.drv.migpsend.SetMEM(SETVPA, CollectMode_Addr, CollectMode_Length, new byte[]{(byte) time.WorkMode}, timeout);
        return drv.SetMEM(CollectMode.setMEM, CollectMode.addr, datalen, data, timeout);     
    }
}
