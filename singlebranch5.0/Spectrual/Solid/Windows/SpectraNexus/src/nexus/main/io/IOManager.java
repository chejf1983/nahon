/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.main.io;

import USBDriver.USBLib;
import gnu.io.CommPortIdentifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.io.libs.WIOInfo;
import nahon.comm.io.libs.WindowsIO;
import nahon.comm.io.libs.WindowsIOFactory;
import nahon.drv.absractio.AbstractIO;
import nahon.drv.absractio.IOInfo;
import sps.dev.data.SSIOInfo;

/**
 *
 * @author Administrator
 */
public class IOManager {

    public static AbstractIO[] CreateAllIO() {
        // <editor-fold defaultstate="collapsed" desc="遍历所有物理口"> 
        ArrayList< AbstractIO> iolist = new ArrayList();
        //搜索所有串口信息
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier comportId = (CommPortIdentifier) portList.nextElement();
            iolist.add(
                    ConvertIO(
                            WindowsIOFactory.CreateIO(
                                    new WIOInfo(
                                            "COM", new String[]{comportId.getName(), String.valueOf(115200 * 4)}))));
        }

        //搜索所有USB口
        try {
            if (!USBLib.IsInitLib()) {
                USBLib.InitLib();
            }
            int[] devnums = USBLib.SearchUSBDev();
            for (int i = 0; i < devnums.length; i++) {
                iolist.add(
                    ConvertIO(
                            WindowsIOFactory.CreateIO(
                                    new WIOInfo("USB", new String[]{String.valueOf(i)}))));
            }
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        //搜索所有TCP口
        try {
            iolist.add(
                    ConvertIO(
                            WindowsIOFactory.CreateIO(
                                    new WIOInfo("TCP", new String[]{InetAddress.getLocalHost().getHostAddress(), "2000"}))));
        } catch (UnknownHostException ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }
        // </editor-fold>

        return iolist.toArray(new AbstractIO[0]);
    }

    private static AbstractIO ConvertIO(final WindowsIO io) {
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
}
