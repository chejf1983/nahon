package com.drv.nahon.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.drv.nahon.usbtest.FaultToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/8/31.
 */

public class USBManager {
    private static UsbManager manager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static ArrayList<UsbDevice> devlist = new ArrayList<>();
    private static Context context;
    private UsbEndpoint in_point;

    public static UsbDevice[] GetDevList(){
        return devlist.toArray(new UsbDevice[0]);
    }

    public static boolean IsPermissioon(int index){
        return manager.hasPermission(devlist.get(index));
    }

    public static int USBScanDevImpl(Context basecont){
        context = basecont;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        devlist.clear();
        if(deviceList.isEmpty()){
            return 0;
        }

        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            devlist.add(deviceIterator.next());
        }

        return devlist.size();
    }

    public static LibUSB OpenUSBImpl(int index){
        UsbEndpoint out_point= null; 
        UsbEndpoint in_point=  null;        
        UsbDeviceConnection connection= null;

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_USB_PERMISSION), 0);

        UsbDevice device = devlist.get(index);

        manager.requestPermission(device, mPermissionIntent);
        //connection = manager.openDevice(device);

//        for(int i = 0; i < device.getInterfaceCount(); i++){
//            UsbInterface inter = device.getInterface(i);
//            if(inter.getEndpointCount() > 1){
//                for(int j = 0; j < inter.getEndpointCount(); j++){
//                    if(inter.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT){
//                        out_point = inter.getEndpoint(j);
//                    }else{
//                        in_point = inter.getEndpoint(j);
//                    }
//                }
//            }
//        }

        return new LibUSB(device, manager);
    }
}
