/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.builder;

import static forge.bill.eia.builder.NahonDevMap.DevType.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class NahonDevMap {

    public static String CompanyName = "NAH";
    public static enum DevType {
        OSA_Turb,
        OSA_TS,
        OSA__ChlA,
        OSA_Cyano_I,
        OSA_Oil_I,
        OSA_FDO,
        ISA_Ammo_I,
        ISA_Ammo_II,
        ESA_pH,
        ESA_DO,
        ESA_EC_I,
        ESA_EC_II,
        ESA_Chlori,
        DSR100,
        DSR200,
        GCS300S,
        GCS300H,
        GCS100_OEM
    }

    public static String GetName(DevType type) {
        for (devinfo info : devinfolist) {
            if (info.devtype == type) {
                return info.devname;
            }
        }
        return null;
    }

    public static String GetDevNum(DevType type) {
        for (devinfo info : devinfolist) {
            if (info.devtype == type) {
                return info.devnum;
            }
        }
        return null;
    }

    public static DevType GetDevTypeByName(String devname) {
        for (devinfo info : devinfolist) {
            if (info.devname.contentEquals(devname)) {
                return info.devtype;
            }
        }
        return null;
    }

    public static DevType GetDevTypeByDevNum(String devnum) {
        for (devinfo info : devinfolist) {
            if (info.devnum.contentEquals(devnum)) {
                return info.devtype;
            }
        }
        return null;
    }
        
    //根据序列号获取设备类型
    public static NahonDevMap.DevType GetDevTypeBySerialNum(String serialNum) {
        if (serialNum.startsWith(CompanyName)) {
            String devnum = serialNum.substring(3, 8);
            return NahonDevMap.GetDevTypeByDevNum(devnum);
        } else {
            return null;
        }
    }

    private static ArrayList<devinfo> devinfolist
            = new ArrayList<devinfo>(Arrays.asList(
            new devinfo(OSA_Turb, "OSA-Turb", "03001"),
            new devinfo(OSA_TS, "OSA-TS", "03002"),
            new devinfo(OSA__ChlA, "OSA-ChlA", "03003"),
            new devinfo(OSA_Cyano_I, "OSA-Cyano I", "03004"),
            new devinfo(OSA_Oil_I, "OSA-Oil I", "03005"),
            new devinfo(OSA_FDO, "OSA-FDO", "03006"),
            new devinfo(ISA_Ammo_I, "ISA-Ammo I", "03010"),
            new devinfo(ISA_Ammo_II, "ISA-Ammo II", "03011"),
            new devinfo(ESA_pH, "ESA-pH", "03020"),
            new devinfo(ESA_DO, "ESA-DO", "03021"),
            new devinfo(ESA_EC_I, "ESA-EC I", "03022"),
            new devinfo(ESA_EC_II, "ESA-EC II", "03023"),
            new devinfo(ESA_Chlori, "ESA-Chlori", "03024"),
            new devinfo(DSR100, "DSR100", "03081"),
            new devinfo(DSR200, "DSR200", "03091"),
            new devinfo(GCS300S, "GCS300S", "05030"),
            new devinfo(GCS300H, "GCS300H", "05031"),
            new devinfo(GCS100_OEM, "GCS100_OEM", "05010")));
}

class devinfo {

    public NahonDevMap.DevType devtype;
    public String devname;
    public String devnum;

    public devinfo(NahonDevMap.DevType devtype, String devname, String devnum) {
        this.devtype = devtype;
        this.devname = devname;
        this.devnum = devnum;
    }
}
