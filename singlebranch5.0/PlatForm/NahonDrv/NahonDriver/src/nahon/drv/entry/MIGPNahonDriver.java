/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.entry;

import nahon.drv.data.ICaddr;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.data.SConnectInfo;
import nahon.drv.update.IProcess;
import nahon.drv.update.UpdateImple;
import static nahon.pro.migp.MIGPCOMMEM.FALSE;
import static nahon.pro.migp.MIGPCOMMEM.FLASH_CLEAN_CMD;
import static nahon.pro.migp.MIGPCOMMEM.FLASH_READ_CMD;
import static nahon.pro.migp.MIGPCOMMEM.FLASH_SPAN_LENGTH;
import static nahon.pro.migp.MIGPCOMMEM.FLASH_WRITE_CMD;
import static nahon.pro.migp.MIGPCOMMEM.HALT_BOOTMODE_CMD;
import static nahon.pro.migp.MIGPCOMMEM.IC_ADDR_QEURE_CMD;
import static nahon.pro.migp.MIGPCOMMEM.REBOOT_CMD;
import static nahon.pro.migp.MIGPCOMMEM.SYSTEM_JUMP_CMD;
import static nahon.pro.migp.MIGPCOMMEM.TRUE;
import nahon.pro.migp.MIGP_CmdSend;

/**
 *
 * @author Administrator
 */
public class MIGPNahonDriver {

    private MIGP_CmdSend migpsend;
    private SConnectInfo conpar;
    ReentrantLock drvlocker = new ReentrantLock();

    public MIGPNahonDriver(SConnectInfo conpar) {
        this.migpsend = new MIGP_CmdSend(conpar.io, conpar.srcaddr, conpar.dstaddr);
        this.conpar = conpar;
    }

    // <editor-fold defaultstate="collapsed" desc="公共接口"> 
    public void Cancel() {
        if (!this.IsClosed()) {
            this.migpsend.Cancel();
        }
    }

    public boolean IsCanceled() {
        return this.migpsend.IsCanceled();
    }

    public SConnectInfo GetConnectParameter() {
        return this.conpar;
    }

    public void Open() throws Exception {
        if (this.conpar.io.IsClosed()) {
            this.conpar.io.Open();
        }
    }

    public boolean IsClosed() {
        return this.conpar.io.IsClosed();
    }

    public void Close() {
        this.conpar.io.Close();
    }

    public void LockDrv() {
        this.drvlocker.lock();
    }

    public void UnLockDrv() {
        this.drvlocker.unlock();
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="内存读写"> 
    public byte[] GetMEM(byte MEM_ID, int MEM_ADDR, int MEM_Length, int timeout) throws Exception {
        try {
            this.LockDrv();
            return this.migpsend.GetMEM(MEM_ID, MEM_ADDR, MEM_Length, timeout);
        } finally {
            this.UnLockDrv();
        }
    }

    public boolean SetMEM(byte MEM_ID, int MEM_Addr, int MEM_Length, byte[] data, int timeout) throws Exception {
        try {
            this.LockDrv();
            return this.migpsend.SetMEM(MEM_ID, MEM_Addr, MEM_Length, data, timeout);
        } finally {
            this.UnLockDrv();
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="其它命令">
    public void UpdateFile(File updateFile, IProcess ret) throws Exception {
        try {
            this.LockDrv();
            new UpdateImple(this).UpdateFile(updateFile, ret);
        } finally {
            this.UnLockDrv();
        }
    }

    public Boolean HaltBoot(int timeout) throws Exception {
        try {
            this.LockDrv();
            byte[] data = this.migpsend.SendRPC(HALT_BOOTMODE_CMD, new byte[]{0x00}, timeout);
            return data[0] == TRUE;
        } finally {
            this.UnLockDrv();
        }
    }

    public ICaddr GetICInfomation(int timeout) throws Exception {
        try {
            this.LockDrv();
            byte[] data = this.migpsend.SendRPC(IC_ADDR_QEURE_CMD, new byte[]{0x00}, timeout);

            //根据返回长度，初始化icaddr，默认返回所有核的IC地址组
            ICaddr icaddr = new ICaddr(data.length / 8);

            for (int i = 0; i < icaddr.corenum; i++) {
                icaddr.startaddr[i] = NahonConvert.ByteArrayToInteger(data, 0 + i * 8);
                icaddr.endaddr[i] = NahonConvert.ByteArrayToInteger(data, 4 + i * 8);

                //如果当前地址组，开始，结束地址相同，表示从次核开始，都为空地址，不存在
                if (icaddr.startaddr == icaddr.endaddr) {
                    icaddr.corenum = i;
                    break;
                }
            }
            return icaddr;
        } finally {
            this.UnLockDrv();
        }
    }

    public Boolean ClearFlash(int blockNum, int timeout) throws Exception {
        try {
            this.LockDrv();
            byte[] data = this.migpsend.SendRPC(FLASH_CLEAN_CMD, NahonConvert.UShortToByteArray(blockNum), timeout);

            return data[0] == TRUE;
        } finally {
            this.UnLockDrv();
        }
    }

    public byte[] ReadFlash(int blockNum, int timeout) throws Exception {
        try {
            this.LockDrv();
            byte[] data = this.migpsend.SendRPC(FLASH_READ_CMD, NahonConvert.UShortToByteArray(blockNum), timeout);

            if (data.length == 1 && data[0] == FALSE) {
                throw new Exception("Read Flash block num: " + blockNum + " failed");
            } else if (data.length <= FLASH_SPAN_LENGTH) {
                return data;
            } else {
                throw new Exception("Read Flash block num: " + blockNum + " failed");
            }
        } finally {
            this.UnLockDrv();
        }
    }

    public Boolean WriteFlash(int blockNum, byte[] data, int timeout) throws Exception {
        try {
            this.LockDrv();
            byte[] tmp = new byte[2 + data.length];
            System.arraycopy(NahonConvert.UShortToByteArray((short) blockNum), 0, tmp, 0, 2);
            System.arraycopy(data, 0, tmp, 2, data.length);

            byte[] rcdata = this.migpsend.SendRPC(FLASH_WRITE_CMD, tmp, timeout);

            return rcdata[0] == TRUE;
        } finally {
            this.UnLockDrv();
        }
    }

    public void StartApp(int timeout) throws Exception {
        try {
            this.LockDrv();
            this.migpsend.SendCMD(SYSTEM_JUMP_CMD, new byte[]{0x00});
            TimeUnit.MILLISECONDS.sleep(timeout);
        } finally {
            this.UnLockDrv();
        }
    }

    public void ReBoot(int timeout) throws Exception {
        try {
            this.LockDrv();
            this.migpsend.SendCMD(REBOOT_CMD, new byte[]{0x00});
            TimeUnit.MILLISECONDS.sleep(timeout);
        } finally {
            this.UnLockDrv();
        }
    }
    // </editor-fold> 
}
