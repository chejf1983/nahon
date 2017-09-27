/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.pro.migp;

import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.absractio.AbstractIO;
import static nahon.pro.migp.MIGPCOMMEM.TRUE;

/**
 *
 * @author jiche
 */
public class MIGP_CmdSend extends MIGPNode {

    public MIGP_CmdSend(AbstractIO physicalInterface, byte localAddr, byte dstAddr) {
        super(physicalInterface, localAddr);
        this.dstAddr = dstAddr;
        this.usbmaxslicelen = physicalInterface.MaxBuffersize();
    }

    // <editor-fold defaultstate="collapsed" desc="MIGP Information">
    private byte dstAddr;

    public byte GetDstAddr() {
        return this.dstAddr;
    }

    private int usbmaxslicelen = 65535;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="migp data send interface"> 
    /**
     * check packet cmd
     *
     * @param CMD
     * @param packet
     * @return
     * @throws Exception
     */
    private boolean CheckrPktCMD(int CMD, MIGPPacket packet) throws Exception {
        if (packet == null) {
            return false;
        }
        //receive cmd must equal to send cmd | 0x80
        return packet.CMD == (byte) (CMD | 0x80);
    }

    public synchronized byte[] SendRPC(byte cmd, byte[] data, int timeout) throws Exception {
        this.nodelocker.lock();
        try {
            this.SendCMD(cmd, data);
            return this.ReceiveCMD(cmd, timeout);
        } finally {
            this.nodelocker.unlock();
        }
    }

    public synchronized void SendCMD(byte cmd, byte[] data) throws Exception {
        this.SendMIGPPacket(dstAddr, cmd, data);
    }

    public synchronized byte[] ReceiveCMD(final int cmd, int timeout) throws Exception {
        //记录返回值
        byte[] redata = null;
        //if receive an error migp packet, retry 3 times
        for (int i = 0; i < 3; i++) {
            //if receive timout, it will break here, 
            MIGPPacket rePkt = ReceiveMIGPPacket(timeout);

            if (rePkt == null) {
                //如果没有数据，超时
                throw new Exception("MIGP packet Timeout!");
            }

            //check cmd
            if (CheckrPktCMD(cmd, rePkt) && this.checkPktSrcAddr(rePkt)) {
                //找到数据，break
                return rePkt.data;
            }
        }
        //如果没有数据，超时
        throw new Exception("MIGP packet Timeout!");
    }

    private boolean checkPktSrcAddr(MIGPPacket pkt) {
        if (pkt == null) {
            return false;
        }

        if (pkt.srcAddress == this.dstAddr || this.dstAddr == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param MEM_ID
     * @param MEM_Addr
     * @param MEM_Length
     * @param data
     * @return
     * @throws java.lang.Exception
     * @throws Device.Data.Exception
     */
    public synchronized boolean SetMEM(byte MEM_ID, int MEM_Addr, int MEM_Length, byte[] data, int timeout) throws Exception {
        for (int sendlen = 0; sendlen < MEM_Length; sendlen += usbmaxslicelen) {
            int slicelen = 0;

            if (MEM_Length - sendlen > usbmaxslicelen) {
                //if left buffer is bigger than one max usb slice send a complete slice
                slicelen = usbmaxslicelen;
            } else {
                //send left buffer
                slicelen = MEM_Length - sendlen;
            }

            byte[] slicedata = new byte[slicelen];
            System.arraycopy(data, sendlen, slicedata, 0, slicelen);

            //if one slice failed, all failed
            if (!this.SetSingelMemory(MEM_ID, MEM_Addr + sendlen, slicelen, slicedata, timeout)) {
                return false;
            }
        }
        return true;
    }

    private boolean SetSingelMemory(byte MEM_ID, int MEM_Addr, int MEM_Length, byte[] data, int timeout) throws Exception {
        byte[] sbuffer = new byte[8 + MEM_Length];
        System.arraycopy(NahonConvert.IntegerToByteArray(MEM_Addr), 0, sbuffer, 0, 4);
        System.arraycopy(NahonConvert.IntegerToByteArray(MEM_Length), 0, sbuffer, 4, 4);

        System.arraycopy(data, 0, sbuffer, 8, MEM_Length);

        byte[] rcdata = this.SendRPC(MEM_ID, sbuffer, timeout);

        return rcdata[0] == TRUE;
    }

    public synchronized byte[] GetMEM(byte MEM_ID, int MEM_ADDR, int MEM_Length, int timeout) throws Exception {
        byte[] sbuffer = new byte[8];
        System.arraycopy(NahonConvert.IntegerToByteArray(MEM_ADDR), 0, sbuffer, 0, 4);
        System.arraycopy(NahonConvert.IntegerToByteArray(MEM_Length), 0, sbuffer, 4, 4);

        byte[] data = this.SendRPC(MEM_ID, sbuffer, timeout);

        int memaddr = NahonConvert.ByteArrayToInteger(data, 0);

        //check memaddr and length
        if (memaddr == MEM_ADDR && data.length == MEM_Length + 4) {
            byte[] tmp = new byte[MEM_Length];
            System.arraycopy(data, 4, tmp, 0, MEM_Length);
            return tmp;
        } else {
            throw new Exception("Get Mem Failed");
        }
    }
    // </editor-fold> 
}
