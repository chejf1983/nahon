package com.drv.nahon.usb;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LibUSB {

    public LibUSB(UsbDevice device,  UsbManager manager){
        this.dev = device;
        this.manager = manager;

        for(int i = 0; i < device.getInterfaceCount(); i++){
            UsbInterface inter = device.getInterface(i);
            if(inter.getEndpointCount() > 1){
                for(int j = 0; j < inter.getEndpointCount(); j++){
                    if(inter.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT){
                        out_point = inter.getEndpoint(j);
                    }else{
                        in_point = inter.getEndpoint(j);
                    }
                }
            }
        }
    }

    private UsbEndpoint out_point;
    private UsbEndpoint in_point;
    private UsbDevice dev;
    private UsbManager manager;

    public int USBBulkWriteDataImpl(byte[] sendbuffer, int len, int waittime){
        UsbDeviceConnection connection = manager.openDevice(this.dev);
        if(connection == null){
            return -1;
        }
        int ret = connection.bulkTransfer(out_point, sendbuffer, len, waittime);
        connection.close();
        return ret;
    }

    public int USBBulkReadDataImpl(byte[] readbuffer, int len, int waittime) {
        UsbDeviceConnection connection = manager.openDevice(this.dev);
        if (connection == null) {
            return -1;
        }
        int ret = connection.bulkTransfer(this.in_point, readbuffer, len, waittime);
        connection.close();
        return ret;
    }
}
