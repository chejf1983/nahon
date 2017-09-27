/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.io.libs;

import USBDriver.USBDevice;
import nahon.comm.io.libs.WindowsIOFactory.IOTYPE;

/**
 *
 * @author Administrator
 */
public class IO_USB implements WindowsIO {

    private final USBDevice usbdev;

    public IO_USB(int usbnum) {
        usbdev = new USBDevice(usbnum);
    }

    public IO_USB(USBDevice usbdev) throws Exception {
        this.usbdev = usbdev;
    }

    @Override
    public boolean IsClosed() {
        return !this.usbdev.IsOpen();
    }

    @Override
    public void SendData(byte[] data) throws Exception {
        usbdev.SendData(data);
    }

    @Override
    public int ReceiveData(byte[] data, int timeout) throws Exception {
        return usbdev.ReceiveData(data, timeout);
    }

    @Override
    public void Close() {
        if (!this.IsClosed()) {
            this.usbdev.CloseUSB();
        }
    }

    @Override
    public void Open() throws Exception {        
        if (this.IsClosed()) {
            this.usbdev.OpenUSB();
        }
    }

    @Override
    public WIOInfo GetConnectInfo() {
        return new WIOInfo(IOTYPE.USB.toString(), String.valueOf(this.usbdev.GetDevNum()));
    }

    @Override
    public int MaxBuffersize() {
        return 40;
    }
}
