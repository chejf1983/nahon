package com.nahon.io;

import nahon.drv.absractio.AbstractIO;

/**
 * Created by Administrator on 2017/9/7.
 */

public class IOManager {
    public static AbstractIO[] CreateIOList(){
        return USBManager.ScanUSBDevice();
    }
}
