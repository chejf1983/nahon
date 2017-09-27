/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.dev.basedev;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import nahon.comm.event.EventCenter;
import nahon.comm.tool.convert.MyConvert;
import static nahon.pro.migp.MIGPCOMMEM.TRUE;
import nahon.pro.migp.MIGPPacket;

/**
 *
 * @author jiche
 */
public class MockDevMem {
    private MockDev dev;
    private Lock memloc = new ReentrantLock();
    public MockDevMem(MockDev dev){
        this.dev = dev;
    }
    
    public EventCenter<byte[]> UpdateMem = new EventCenter();

    public byte[] EIA = new byte[10240];
    public byte[] MDA = new byte[10240];
    public byte[] NVPA = new byte[10240];
    public byte[] VPA = new byte[10240];
    public byte[] SRA = new byte[10240];

    public void SetValue(byte[] mem, int pos, int len, byte[] data) {
        memloc.lock();
        System.arraycopy(data, 0, mem, pos, len);
        memloc.unlock();
        this.UpdateMem.CreateEventAsync(mem);
    }

    public byte[] GetValue(byte[] mem, int pos, int len) {
        if (pos + len > mem.length) {
            return null;
        }

        byte[] tmp = new byte[len];
        memloc.lock();
        System.arraycopy(mem, pos, tmp, 0, len);
        memloc.unlock();
        return tmp;
    }
        
    public MIGPPacket SetMem(MIGPPacket cmd, byte[] mem) throws Exception {
        int len;
        int addr;

        len = MyConvert.ByteArrayToInteger(cmd.data, 4);
        addr = MyConvert.ByteArrayToInteger(cmd.data, 0);
        memloc.lock();
        System.arraycopy(cmd.data, 8, mem, addr, len);
        memloc.unlock();

        this.UpdateMem.CreateEventAsync(mem);
        return new MIGPPacket(cmd.srcAddress, this.dev.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{TRUE});
    }

    public MIGPPacket GetMem(MIGPPacket cmd, byte[] mem) throws Exception {
        int len;
        int addr;
        byte[] ret;

        addr = MyConvert.ByteArrayToInteger(cmd.data, 0);
        len = MyConvert.ByteArrayToInteger(cmd.data, 4);

        ret = new byte[len + 4];
        memloc.lock();
        System.arraycopy(cmd.data, 0, ret, 0, 4);
        System.arraycopy(mem, addr, ret, 4, len);
        memloc.unlock();

        return new MIGPPacket(cmd.srcAddress, this.dev.devAddr, (byte) (cmd.CMD | 0x80), ret);
    }
}
