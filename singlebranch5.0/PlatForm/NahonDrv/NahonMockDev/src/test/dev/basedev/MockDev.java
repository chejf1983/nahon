/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dev.basedev;

import nahon.pro.migp.MIGPPacket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.tool.convert.MyConvert;
import nahon.comm.tool.convert.MyConvertException;
import static nahon.pro.migp.MIGPCOMMEM.*;

/**
 *
 * @author jiche
 */
public class MockDev {

    public MockDevMem memory;

    public MockDev(byte devAddress) {
        //设置通信地址
        this.devAddr = devAddress;
        //初始化虚拟内存
        this.memory = new MockDevMem(this);

        //初始化默认EIA信息
        try {
            this.memory.SetValue(this.memory.EIA, DeviceName.addr, DeviceName.length, MyConvert.StringToByte("TestNahonDev", DeviceName.length));
            this.memory.SetValue(this.memory.EIA, SoftwareVersion.addr, SoftwareVersion.length, MyConvert.StringToByte("NS001", SoftwareVersion.length));
            this.memory.SetValue(this.memory.EIA, Hardversion.addr, Hardversion.length, MyConvert.StringToByte("NH002", Hardversion.length));
        } catch (MyConvertException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="MIGP CMD Executer">
    public byte devAddr = 0;
    private byte tmpaddr = 0;
    public final int FlashStartAddress = 0;
    public final int FlashNum = 3000;
    public final int FlashEndAddress = FlashNum * FLASH_SPAN_LENGTH;
    public byte[][] FlashMem = new byte[FlashNum][FLASH_SPAN_LENGTH];

    public MIGPPacket[] ExecutorMIGPCMD(MIGPPacket cmd) throws Exception {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
            switch (cmd.CMD) {
                //进入boot模式
                case HALT_BOOTMODE_CMD:
                    return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{TRUE})};
                case SYSTEM_JUMP_CMD:
//                mem.StartAppResult = true;
                    break;
                //查询IC地址
                case IC_ADDR_QEURE_CMD:
                    byte[] tmp = new byte[8];
                    System.arraycopy(MyConvert.IntegerToByteArray(FlashStartAddress), 0, tmp, 0, 4);
                    System.arraycopy(MyConvert.IntegerToByteArray(FlashEndAddress), 0, tmp, 4, 4);

                    return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), tmp)};
                //擦除falsh
                case FLASH_CLEAN_CMD:
                    int cbnum = MyConvert.ByteArrayToUShort(cmd.data, 0);
                    if (cbnum > (FlashStartAddress / FLASH_SPAN_LENGTH) && cbnum < this.FlashNum) {
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{TRUE})};
                    } else {
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{FALSE})};
                    }
                //读取FLASH
                case FLASH_READ_CMD:
                    int rbnum = MyConvert.ByteArrayToUShort(cmd.data, 0);
                    if (rbnum > (FlashStartAddress / FLASH_SPAN_LENGTH) && rbnum < this.FlashNum) {
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), FlashMem[rbnum])};
                    } else {
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{FALSE})};
                    }
                //写FLASH
                case FLASH_WRITE_CMD:
                    int wbnum = MyConvert.ByteArrayToUShort(cmd.data, 0);
                    if (wbnum < this.FlashNum && cmd.data.length - 2 <= FLASH_SPAN_LENGTH) {
                        System.arraycopy(cmd.data, 2, FlashMem[wbnum], 0, cmd.data.length - 2);
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{TRUE})};
                    } else {
                        return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{FALSE})};
                    }
                //重启
                case REBOOT_CMD:
                    //重启后，设置设备地址
                    if (this.tmpaddr != 0) {
                        this.devAddr = this.tmpaddr;
                        this.tmpaddr = 0;
                    }
                    break;
                //设置DEV
                case SET_DEVNUM:
                    //设置临时addr，重启后才生效
                    this.tmpaddr = cmd.data[0];
                    return new MIGPPacket[]{new MIGPPacket(cmd.srcAddress, this.devAddr, (byte) (cmd.CMD | 0x80), new byte[]{TRUE})};

                //内存读写命令
                case GETEIA:
                    return new MIGPPacket[]{this.memory.GetMem(cmd, this.memory.EIA)};
                case SETEIA:
                    return new MIGPPacket[]{this.memory.SetMem(cmd, this.memory.EIA)};
                case GETMDA:
                    return new MIGPPacket[]{this.memory.GetMem(cmd, this.memory.MDA)};
                case SETMDA:
                    return new MIGPPacket[]{this.memory.SetMem(cmd, this.memory.MDA)};
                case GETNVPA:
                    return new MIGPPacket[]{this.memory.GetMem(cmd, this.memory.NVPA)};
                case SETNVPA:
                    return new MIGPPacket[]{this.memory.SetMem(cmd, this.memory.NVPA)};
                case GETVPA:
                    return new MIGPPacket[]{this.memory.GetMem(cmd, this.memory.VPA)};
                case SETVPA:
                    return new MIGPPacket[]{this.memory.SetMem(cmd, this.memory.VPA)};
                case GETSRA:
                    return new MIGPPacket[]{this.memory.GetMem(cmd, this.memory.SRA)};
                case SETSRA:
                    return new MIGPPacket[]{this.memory.SetMem(cmd, this.memory.SRA)};

                default:
                    throw new Exception("Unkown CMD: " + cmd.CMD);
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return null;
    }
    // </editor-fold> 
}
