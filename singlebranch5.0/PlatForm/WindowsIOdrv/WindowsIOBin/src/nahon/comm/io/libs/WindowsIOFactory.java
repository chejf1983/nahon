/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.io.libs;

import USBDriver.USBLib;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jiche
 */
public class WindowsIOFactory {

    public enum IOTYPE {
        COM,
        TCP,
        USB
    }

    public static WindowsIO CreateIO(WIOInfo con) {
        if (!isInited) {
            try {
                InitWindowsIODriver();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
        if (con == null) {
            return null;
        }

        WindowsIO newio;
        switch (IOTYPE.valueOf(con.iotype)) {
            case COM:
                newio = new IO_COM(con.par[0], Integer.valueOf(con.par[1]));
                break;
            case TCP:
                newio = new IO_TCP(con.par[0], Integer.valueOf(con.par[1]));
                break;
            case USB:
                newio = new IO_USB(Integer.valueOf(con.par[0]));
                break;
            default:
                return null;
        }
//        iolist.add(newio);
        return newio;
    }

    public static IOTYPE GetIOtype(WIOInfo con) {
        return IOTYPE.valueOf(con.iotype);
    }

    private static boolean isInited = false;

    public static void InitWindowsIODriver() throws Exception {
        if (!isInited) {
            IO_COM.InitLib();
            USBLib.InitLib();
            isInited = true;
        }
    }
}
