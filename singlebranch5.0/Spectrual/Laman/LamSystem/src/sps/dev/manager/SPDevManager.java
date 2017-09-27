/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import java.util.ArrayList;
import nahon.comm.event.EventCenter;
import nahon.drv.absractio.AbstractIO;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.inst.SPDevControl;

/**
 *
 * @author jiche
 */
public class SPDevManager {

    private ArrayList<ISPDevDriver> devlist = new ArrayList();
    public EventCenter EventPod = new EventCenter();

    public ArrayList<ISPDevDriver> GetDeviceList() {
        return this.devlist;
    }

    // <editor-fold defaultstate="collapsed" desc="设备控制器">  
    //设备控制器
    private SPDevControl spcontrol = new SPDevControl();

    public ISPDevControl GetDevControl() {
        return this.spcontrol;
    }

    // </editor-fold>   
    
    // <editor-fold defaultstate="collapsed" desc="搜索设备">  
    private final int MaxDevAddr = 0x1A;

    public void StartSearchIO(AbstractIO[] iolist) {
        this.spcontrol.Close();

        this.devlist.clear();

        for (AbstractIO io : iolist) {
            for (ISPDevDriver drv : SearchNDevs(io, (byte) 0, (byte) 0, 300)) {
                this.devlist.add(drv);
            }
        }
    }

    //搜索一个设备
    private ISPDevDriver[] SearchNDevs(AbstractIO io, byte minaddr, byte maxaddr, int timeout) {
        ArrayList<ISPDevDriver> result = new ArrayList();
        if (minaddr < 0 || minaddr > maxaddr) {
            //参数异常，最小值不能小于0，最小值也必须小于最大值
            return result.toArray(new ISPDevDriver[0]);
        }

        try {
            io.Open();
        } catch (Exception ex) {
            return result.toArray(new ISPDevDriver[0]);
        }

        //搜索所有设备地址
        for (byte i = minaddr; i <= maxaddr; i++) {
            //跳过广播地址
            if (i != 0) {
                ISPDevDriver tmpdev = new SPDevSearch().SearchDevice(io, (byte) 0xFE, (byte) i, timeout);
                if (tmpdev != null) {
                    result.add(tmpdev);
                }
            }
        }

        //如果需要检查广播地址，同时没有搜索到任何设备，尝试搜索广播地址
        if (minaddr == 0 && result.isEmpty()) {
            ISPDevDriver tmpdev = new SPDevSearch().SearchDevice(io, (byte) 0xFE, (byte) 0, timeout);
            if (tmpdev != null) {
                result.add(tmpdev);
            }
        }

        //关闭IO
        io.Close();
        return result.toArray(new ISPDevDriver[0]);
    }

    // </editor-fold>         
}
