/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.collect;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import static spdev.drv.data.MIGPSPMEM.TestData;

/**
 *
 * @author Administrator
 */
public class DataCollectCmd {

    public double[] GetSPData(MIGPNahonDriver drv, int nodeNum, int timeout) throws Exception {
        double[] rcdata = new double[nodeNum];
        byte[] ret = drv.GetMEM(TestData.getMEM, TestData.addr, nodeNum * 2, timeout);

        for (int i = 0; i < rcdata.length; i++) {
            rcdata[i] = (double) NahonConvert.ByteArrayToUShort(ret, i * 2);
        }

        return rcdata;
    }
    
    /*
    public double [] WaiteFromDevice(MIGPNahonDriver drv, int nodeNum, int timeout) throws Exception{
        double[] rcdata = new double[nodeNum];
        int memlength = nodeNum * 2;
        byte[] data = drv.ReceiveCMD(TestData.getMEM, timeout);

        int address = (data[0] << 24)
                | (data[1] << 16)
                | (data[2] << 8)
                | data[3];

        if (address == TestData.addr && data.length == memlength + 4) {
            byte[] ret = new byte[memlength];
            System.arraycopy(data, 4, ret, 0, memlength);
            for (int i = 0; i < rcdata.length; i++) {
                rcdata[i] = (double) NahonConvert.ByteArrayToUShort(ret, i * 2);
            }
            return rcdata;
        } else {
            throw new Exception("Get MEM Failed ");
        }
    }*/
}
