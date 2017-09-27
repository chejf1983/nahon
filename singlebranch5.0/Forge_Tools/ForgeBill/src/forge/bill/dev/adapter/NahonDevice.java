/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.dev.adapter;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import nahon.drv.cmd.DevNumCmd;
import nahon.drv.cmd.EquipmentInfoCmd;

import nahon.drv.data.EquipmentInfo;
import nahon.drv.data.SConnectInfo;
import nahon.drv.entry.MIGPNahonDriver;
import nahon.drv.update.IProcess;
import nahon.pro.migp.MIGPCOMMEM;

/**
 *
 * @author Administrator
 */
public class NahonDevice {

    private final SConnectInfo devinfo;
    private MIGPNahonDriver nahondrv;
    private final ReentrantLock devlock = new ReentrantLock();

    public NahonDevice(SConnectInfo devinfo) {
        this.devinfo = devinfo;
    }

    // <editor-fold defaultstate="collapsed" desc="设备连接"> 
    private boolean isConnect = false;

    public boolean IsConnect() {
        return this.isConnect;
    }

    public void Connect() throws Exception {
        if (!IsConnect()) {
            this.devlock.lock();
            try {
                //初始化驱动
                this.nahondrv = new MIGPNahonDriver(devinfo);

                this.nahondrv.Open();

                //初始化设备信息
                this.eia = new EquipmentInfoCmd().GetFromDevice(nahondrv, 1000);
                //初始化设备地址
                this.devaddr = new DevNumCmd().GetFromDevice(nahondrv, 1000);

                this.isConnect = true;
            } finally {
                this.devlock.unlock();
                this.nahondrv.Close();
            }
        }
    }

    public void DisConnect() {
        //IO关闭了，打开IO
        if (IsConnect()) {
            this.devlock.lock();
            try {
                this.isConnect = false;
                //清除缓存
                this.eia = new EquipmentInfo();
                this.devaddr = -1;
            } finally {
                this.devlock.unlock();
            }
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="设备接口"> 
    //升级设备
    public void UpdateFile(final File updateFile, IProcess ret) throws Exception {
        this.nahondrv.Open();
        try {
            //升级文件
            this.nahondrv.UpdateFile(updateFile, ret);
        } finally {
            this.nahondrv.Close();
        }
    }

    //本地缓存
    private EquipmentInfo eia;

    //获取EIA信息
    public EquipmentInfo GetEquipmentInfo() {
        return new EquipmentInfo(this.eia);
    }

    //获取EIA信息
    public void SetEquipmentInfo(EquipmentInfo eia) throws Exception {
        //如果和本地缓存相同，直接返回
        //       if (this.eia != null && this.eia.EqualTo(eia)) {
        //          return;
        //       }
        this.nahondrv.Open();
        try {
            //下发EIA
            new EquipmentInfoCmd().SetToDevice(nahondrv, eia, 1000);

            //更新本地缓存
            this.eia = new EquipmentInfo(eia);
        } finally {
            this.nahondrv.Close();
        }
    }

    private byte devaddr;

    //设置设备地址
    public void SetDevAddrNum(byte addr) throws Exception {
        //如果和本地缓存相同，直接返回
        //    if (this.devaddr == addr) {
        //        return;
        //    }
        this.nahondrv.Open();
        try {
            //设置设备地址
            //下发设备地址
            new DevNumCmd().SetToDevice(nahondrv, addr, 1000);
            //设备地址重启生效
            this.devaddr = addr;
        } finally {
            this.nahondrv.Close();
        }
    }

    public byte GetDevAddr() {
        return this.devaddr;
    }

    public enum MEMTYPE {
        EIA,
        VPA,
        NVPA,
        MDA,
        SRA
    }

    public byte[] GetMEM(MEMTYPE MEM_ID, int MEM_ADDR, int MEM_Length) throws Exception {
        this.nahondrv.Open();
        byte id = 0;
        switch (MEM_ID) {
            case EIA:
                id = MIGPCOMMEM.GETEIA;
                break;
            case VPA:
                id = MIGPCOMMEM.GETVPA;
                break;
            case NVPA:
                id = MIGPCOMMEM.GETNVPA;
                break;
            case MDA:
                id = MIGPCOMMEM.GETMDA;
                break;
            case SRA:
                id = MIGPCOMMEM.GETSRA;
                break;
        }
        try {
            return this.nahondrv.GetMEM(id, MEM_ADDR, MEM_Length, 1000);
        } finally {
            this.nahondrv.Close();
        }
    }

    public void SetMem(MEMTYPE MEM_ID, int MEM_ADDR, int MEM_Length, byte[] data) throws Exception {
        this.nahondrv.Open();
        byte id = 0;
        switch (MEM_ID) {
            case EIA:
                id = MIGPCOMMEM.SETEIA;
                break;
            case VPA:
                id = MIGPCOMMEM.SETVPA;
                break;
            case NVPA:
                id = MIGPCOMMEM.SETNVPA;
                break;
            case MDA:
                id = MIGPCOMMEM.SETMDA;
                break;
            case SRA:
                id = MIGPCOMMEM.SETSRA;
                break;
        }
        try {
            this.nahondrv.SetMEM(id, MEM_ADDR, MEM_Length, data, 1000);
        } finally {
            this.nahondrv.Close();
        }
    }
    // </editor-fold> 
}
