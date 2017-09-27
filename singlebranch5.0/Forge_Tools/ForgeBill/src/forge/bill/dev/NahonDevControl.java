/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.dev;

import forge.bill.dev.adapter.NahonDevice;
import forge.bill.dev.adapter.NahonDevice.MEMTYPE;
import forge.bill.eia.builder.SEiaRecord;
import forge.bill.platform.ForgeSystem;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.event.EventCenter;
import nahon.comm.faultsystem.FaultCenter;
import nahon.comm.io.libs.WindowsIOFactory;
import nahon.drv.absractio.AbstractIO;
import nahon.drv.absractio.IOInfo;
import nahon.drv.data.EquipmentInfo;
import nahon.drv.data.SConnectInfo;
import nahon.drv.update.IProcess;

/**
 *
 * @author Administrator
 */
public class NahonDevControl {

    // <editor-fold defaultstate="collapsed" desc="控制器状态"> 
    public enum ControlState {
        CONNECT,
        DISCONNECT,
        UPDATE,
        SETTING
    }

    private ControlState state = ControlState.DISCONNECT;
    public EventCenter<ControlState> StateCenter = new EventCenter();

    public ControlState State() {
        return this.state;
    }

    private void SetState(ControlState state) {
        ControlState laststate = this.state;
        this.state = state;
        this.StateCenter.CreateEventAsync(state, laststate);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="设备连接"> 
    private NahonDevice device;

    private AbstractIO ConvertIO(final nahon.comm.io.libs.AbstractIO io) {
        return new AbstractIO() {
            @Override
            public boolean IsClosed() {
                return io.IsClosed();
            }

            @Override
            public void Open() throws Exception {
                io.Open();
            }

            @Override
            public void Close() {
                io.Close();
            }

            @Override
            public void SendData(byte[] bytes) throws Exception {
                io.SendData(bytes);
            }

            @Override
            public int ReceiveData(byte[] bytes, int i) throws Exception {
                return io.ReceiveData(bytes, i);
            }

            @Override
            public IOInfo GetConnectInfo() {
                return new IOInfo(io.GetConnectInfo().iotype, io.GetConnectInfo().par);
            }

            @Override
            public int MaxBuffersize() {
                return io.MaxBuffersize();
            }
        };
    }

    public void Connect(IOInfo ioinfo, byte devaddr) {
        if (this.State() == ControlState.DISCONNECT) {
            device = null;
            //创建物理口
            nahon.comm.io.libs.AbstractIO io_instance = 
                    WindowsIOFactory.CreateIO(
                            new nahon.comm.io.libs.IOInfo(ioinfo.iotype, ioinfo.par));


            //创建设备
            device = new NahonDevice(new SConnectInfo(ConvertIO(io_instance), (byte) 0xEF, devaddr));
            if (device != null) {
                //初始化设备
                try {
                    device.Connect();
                    //保存IO配置
                    ForgeSystem.GetInstance().systemConfig.SaveDefaultIO(ioinfo);
                    ForgeSystem.GetInstance().systemConfig.SaveAddr(devaddr);

                    this.SetState(ControlState.CONNECT);
                } catch (Exception ex) {
                    FaultCenter.Instance().SendFaultReport(Level.SEVERE, "设备初始化失败");
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                    return;
                }

                //如果是内部版本，需要记录出厂信息
                if (ForgeSystem.GetInstance().systemConfig.IsInternal()) //设备连接成功后，初始化附加信息
                {
                    if (this.State() == ControlState.CONNECT) {
                        if (!this.device.GetEquipmentInfo().EqualTo(new EquipmentInfo())) {
                            //初始化设备附加信息
                            try {
                                this.devInfo = ForgeSystem.GetInstance().eiaBuilder.ReadRecord(device.GetEquipmentInfo().BuildSerialNum);
                            } catch (Exception ex) {
                                FaultCenter.Instance().SendFaultReport(Level.SEVERE, "连接数据库失败");
                                Logger.getGlobal().log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }

    public void Connect() {
        IOInfo ioinfo = ForgeSystem.GetInstance().systemConfig.GetDefaultIO();
        byte addr = ForgeSystem.GetInstance().systemConfig.GetAddr();
        this.Connect(ioinfo, addr);
    }

    public void DisConnect() {
        if (this.State() == ControlState.DISCONNECT) {
            return;
        }

        if (this.State() == ControlState.CONNECT) {
            this.device.DisConnect();
            this.device = null;
            this.SetState(ControlState.DISCONNECT);
        } else {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "设备忙"
                    + this.State() + "，无法关闭，请先停止其它操作");
        }
    }
    // </editor-fold> 

    public EquipmentInfo GetEquipmentInfo() {
        if (this.state == ControlState.DISCONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return null;
        }

        return this.device.GetEquipmentInfo();
    }

    public boolean SetEquipmentInfo(EquipmentInfo neweia) {
        if (this.state == ControlState.DISCONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return false;
        }
        //检查状态
        if (ForgeSystem.GetInstance().devControl.State() != NahonDevControl.ControlState.CONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "无法设备EIA信息!");
            return false;
        } else {
            this.SetState(ControlState.SETTING);
        }

        try {
            //连接状态下才可以下发设备信息
            this.device.SetEquipmentInfo(neweia);
        } catch (Exception ex) {
            //设置异常，记录到log并显示到界面
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "设置EIA异常");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            this.SetState(ControlState.CONNECT);
            return false;
        }

        this.SetState(ControlState.CONNECT);
        return true;
    }

    private SEiaRecord devInfo;

    public SEiaRecord GetInfomation() {
        if (this.state != ControlState.DISCONNECT) {
            return this.devInfo;
        } else {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return null;
        }
    }

    public boolean SetInformation(SEiaRecord rec) {
        //如果是内部版本，需要记录出厂信息
        if (ForgeSystem.GetInstance().systemConfig.IsInternal()) {
            //记录出厂信息
            try {
                //如果记录相同，不用修改数据库
                if (!rec.Equalto(this.devInfo)) {
                    if (this.devInfo == null) {
                        //新增新记录
                        ForgeSystem.GetInstance().eiaBuilder.AddNewRecord(rec);
                    } else {
                        //记录到数据库，更具老的序列号，自动回检查是否是更新记录还是增加新记录
                        ForgeSystem.GetInstance().eiaBuilder.UpdateRecord(this.devInfo.devserialID, rec);
                    }

                    this.devInfo = rec;
                }

                return true;
            } catch (Exception ex) {
                FaultCenter.Instance().SendFaultReport(Level.SEVERE, "记录出厂信息异常");
                Logger.getGlobal().log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
    }

    private UpdateProcess ret;

    public IProcess UpdateDevice(final File updateFile) {
        if (this.state == ControlState.DISCONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return null;
        }

        if (this.state == ControlState.CONNECT) {
            this.SetState(ControlState.UPDATE);
            ret = new UpdateProcess();

            ForgeSystem.GetInstance().systemthreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        device.UpdateFile(updateFile, ret);
                        ret.SetSuccessFlag(true);
                    } catch (Exception ex) {
                        ret.SetSuccessFlag(false);
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE, "升级失败");
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    } finally {
                        SetState(ControlState.CONNECT);
                    }
                }
            });

            return ret;
        }

        if (this.state == ControlState.UPDATE) {
            return ret;
        } else {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "无法升级");
            return null;
        }
    }

    public boolean SetDevAddr(byte addr) {
        //检查状态
        if (this.state == ControlState.DISCONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return false;
        }

        if (ForgeSystem.GetInstance().devControl.State() != NahonDevControl.ControlState.CONNECT) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "无法设备设备地址!");
            return false;
        }

        try {
            this.device.SetDevAddrNum(addr);
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "设置地址失败");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public byte GetDevAddr() {
        if (this.state != ControlState.DISCONNECT) {
            return this.device.GetDevAddr();
        } else {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "未连接设备");
            return -1;
        }
    }

    public byte[] GetMEM(MEMTYPE MEM_ID, int MEM_ADDR, int MEM_Length) throws Exception {
        if (this.state == ControlState.CONNECT) {
            return this.device.GetMEM(MEM_ID, MEM_ADDR, MEM_Length);
        } else {
            throw new Exception("未连接设备");
        }
    }

    public void SetMem(MEMTYPE MEM_ID, int MEM_ADDR, int MEM_Length, byte[] data) throws Exception {
        if (this.state == ControlState.CONNECT) {
            this.device.SetMem(MEM_ID, MEM_ADDR, MEM_Length, data);
        } else {
            throw new Exception("未连接设备");
        }
    }
}
