package com.nahon.usbdrv;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

/**
 * Created by Administrator on 2017/8/29.
 */

public class IO_USB {
    private boolean isClosed = true;
    private UsbEndpoint out_point;
    private UsbEndpoint in_point;
    private int pid, vid;

    public IO_USB(int pid, int vid) {
        this.pid = pid;
        this.vid = vid;
    }

    public boolean IsClosed() {
        return this.isClosed;
    }

    public UsbDevice getdevice()throws Exception{
        UsbDevice dev = USBManager.FindDevice(this.pid, this.vid);
        if (dev == null) {
            throw new Exception("USB接口不存在");
        }

        if(!USBManager.GetManager().hasPermission(dev)){
            throw new Exception("没有权限");
        }
        return dev;
    }

    public void Open() throws Exception {
        if (!this.isClosed) {
            return;
        }

        UsbDevice dev = this.getdevice();
        for (int i = 0; i < dev.getInterfaceCount(); i++) {
            UsbInterface inter = dev.getInterface(i);
            if (inter.getEndpointCount() > 1) {
                for (int j = 0; j < inter.getEndpointCount(); j++) {
                    if (inter.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT) {
                        out_point = inter.getEndpoint(j);
                    } else {
                        in_point = inter.getEndpoint(j);
                    }
                }
            }
        }
        this.isClosed = false;
    }

    public void Close() {
        if (this.isClosed) {
            return;
        }

        this.out_point = null;
        this.in_point = null;
        this.isClosed = true;
    }

    public void SendData(byte[] bytes) throws Exception {
        if (this.isClosed) {
            throw new Exception("没有打开USB");
        }

        UsbDevice dev = this.getdevice();

        UsbDeviceConnection connection = USBManager.GetManager().openDevice(dev);
        try {
            connection.bulkTransfer(out_point, bytes, bytes.length, 5000);
        } finally {
            connection.close();
        }
    }

    public int ReceiveData(byte[] bytes, int timeout) throws Exception {
        if (this.isClosed) {
            throw new Exception("没有打开USB");
        }

        UsbDevice dev = this.getdevice();

        UsbDeviceConnection connection = USBManager.GetManager().openDevice(dev);
        try {
            return connection.bulkTransfer(this.in_point, bytes, bytes.length, timeout);
        } finally {
            connection.close();
        }
    }
}
