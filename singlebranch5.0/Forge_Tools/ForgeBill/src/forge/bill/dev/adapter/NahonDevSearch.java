/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.dev.adapter;

import java.util.ArrayList;
import nahon.drv.absractio.*;
import nahon.drv.cmd.EquipmentInfoCmd;
import nahon.drv.data.SConnectInfo;
import nahon.drv.entry.MIGPNahonDriver;

/**
 *
 * @author jiche
 */
public class NahonDevSearch {

    //根据获取eia来判断能否连接上设备
    private static NahonDevice SearchNDev(AbstractIO io, byte dstaddr, int timeout) {
        try {
            SConnectInfo confino = new SConnectInfo(io, (byte) 0xEF, (byte) dstaddr);
            MIGPNahonDriver drv = new MIGPNahonDriver(confino);
            new EquipmentInfoCmd().GetFromDevice(drv, timeout);
            return new NahonDevice(confino);
        } catch (Exception ex) {
            return null;
        }
    }

    //搜索指定地址
    public static NahonDevice SearchDevByAddr(AbstractIO io, byte dstaddr, int timeout) {
        try {
            io.Open();
        } catch (Exception ex) {
            return null;
        }

        NahonDevice tmpdev = SearchNDev(io, (byte) dstaddr, timeout);
        io.Close();
        return tmpdev;
    }

    //搜索一个设备
    public static ArrayList<NahonDevice> SearchNDevs(AbstractIO io, byte minaddr, byte maxaddr, int timeout) {
        ArrayList<NahonDevice> result = new ArrayList();
        if (minaddr < 0 || minaddr > maxaddr) {
            //参数异常，最小值不能小于0，最小值也必须小于最大值
            return result;
        }

        try {
            io.Open();
        } catch (Exception ex) {
            return result;
        }

        //搜索所有设备地址
        for (byte i = minaddr; i < maxaddr; i++) {
            //跳过广播地址
            if (i != 0) {
                NahonDevice tmpdev = SearchNDev(io, (byte) i, timeout);
                if (tmpdev != null) {
                    result.add(tmpdev);
                }
            }
        }

        //如果需要检查广播地址，同时没有搜索到任何设备，尝试搜索广播地址
        if (minaddr == 0 && result.isEmpty()) {
            NahonDevice tmpdev = SearchNDev(io, (byte) 0, timeout);
            if (tmpdev != null) {
                result.add(tmpdev);
            }
        }

        //关闭IO
        io.Close();
        return result;
    }
}
