/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package USBDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public class USBLib {

    private static boolean IsInit = false;

    public static boolean IsInitLib() {
        return IsInit;
    }

    public static void InitLib() throws Exception {
        if (!IsInit) {
            CreateDLLTempFile("USB_Driver.dll");
            CreateDLLTempFile("libusb0_x86.dll");
            CreateDLLTempFile("libusb0.dll");
            CreateDLLTempFile("NahonUSBLib.dll");
            //String dllpath = CreateDLLTempFile("NahonUSBLib.dll");
            System.load(CreateDLLTempFile("USB_Driver.dll"));
            System.load(CreateDLLTempFile("libusb0_x86.dll"));
            System.load(CreateDLLTempFile("libusb0.dll"));
            System.load(CreateDLLTempFile("NahonUSBLib.dll"));
            IsInit = true;
        }
    }

    private static String CreateDLLTempFile(String Filename) throws Exception {
        File tmp = new File("./jre/bin");
        if (tmp.exists()) {
            tmp = new File("./jre/bin/" + Filename);
        } else {
            tmp = new File("./" + Filename);
        }

        if (!tmp.exists()) {
            InputStream in = USBLib.class.getResourceAsStream("/Resource/" + Filename);
            FileOutputStream out = new FileOutputStream(tmp);

            int i;
            byte[] buf = new byte[1024];
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }

            in.close();
            out.close();
            System.out.println("create file:" + Filename);
        }
        // return tmp.getAbsoluteFile().getAbsolutePath();
        return tmp.getCanonicalPath();
    }

    public static int[] SearchUSBDev() {
        int devnum = USBLib.USBScanDevImpl(1);

        int[] tmp = new int[devnum];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = i;
        }

        return tmp;
    }

    static native int USBScanDevImpl(int NeedInit);

    static native int OpenUSBImpl(int DevIndex);

    static native int USBBulkWriteDataImpl(int nBoardID, byte[] sendbuffer, int len, int waittime);

    static native int USBBulkReadDataImpl(int nBoardID, byte[] readbuffer, int len, int waittime);

    static native int USBCtrlDataImpl(int nBoardID, int requesttype, int request, int value, int index, byte[] bytes, int size, int waittime);

    static native int CloseUSBImpl(int DevIndex);

}
