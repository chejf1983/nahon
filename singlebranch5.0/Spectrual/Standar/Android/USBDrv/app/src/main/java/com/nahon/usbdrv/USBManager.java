package com.nahon.usbdrv;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/31.
 */

public class USBManager {
    //private static UsbManager manager;
    private static ArrayList<IO_USB> devlist = new ArrayList<>();
    private static Context context;
    private static UsbManager manager;

    public static void InitUSBManager(Context basecont) {
        context = basecont;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);

        BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        ApplyDev(device);
                    }
                }
            }
        };

        context.registerReceiver(mUsbReceiver, usbDeviceStateFilter);
    }

    public static IO_USB[] ScanUSBDevice() {
        devlist.clear();

        if (context != null) {
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            if (!deviceList.isEmpty()) {
                for (String key : deviceList.keySet()) {
                    System.out.println("key= " + key);
                    UsbDevice dev = deviceList.get(key);
                    if (manager.hasPermission(dev)) {
                        devlist.add(new IO_USB(dev.getProductId(), dev.getVendorId()));
                    } else {
                        ApplyDev(dev);
                    }
                }
            }
        }

        return devlist.toArray(new IO_USB[0]);
    }

    public static UsbManager GetManager() {
        return manager;
    }

    public static UsbDevice FindDevice(int pid, int vid) {
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        if (!deviceList.isEmpty()) {
            for (String key : deviceList.keySet()) {
                System.out.println("key= " + key);
                UsbDevice dev = deviceList.get(key);
                if (pid == dev.getProductId() && dev.getVendorId() == vid) {
                    return dev;
                }

            }
        }
        return null;
    }

    public static void ApplyDev(UsbDevice dev) {
        String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        manager.requestPermission(dev, mPermissionIntent);
    }
}
