/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.pro.migp;

/**
 *
 * @author jiche
 */
public class MIGPCOMMEM {

    
    public static MIGPCOMMEM instance = new MIGPCOMMEM();
    
    // <editor-fold defaultstate="collapsed" desc="MIGP Common CMD Key">
    public static final byte GET_DEV_STATE_CMD = 0x03;
    public static final byte SET_DEVNUM = 0x05;
    public static final byte REBOOT_CMD = 0x06;
    public static final byte HALT_BOOTMODE_CMD = 0x20;
    public static final byte IC_ADDR_QEURE_CMD = 0x21;
    public static final byte SYSTEM_JUMP_CMD = 0x22;
    public static final byte FLASH_CLEAN_CMD = 0x28;
    public static final byte FLASH_WRITE_CMD = 0x29;
    public static final byte FLASH_READ_CMD = 0x2A;

    public static final byte GETEIA = 0x30;
    public static final byte SETEIA = 0x31;
    public static final byte GETVPA = 0x40;//vpa sra
    public static final byte SETVPA = 0x41;
    public static final byte GETNVPA = 0x50;//nvpa spa
    public static final byte SETNVPA = 0x51;
    public static final byte GETMDA = 0x60;
    public static final byte SETMDA = 0x61;
    public static final byte GETSRA = 0x70;
    public static final byte SETSRA = 0x71;

    public static final byte TRUE = 0x66;
    public static final byte FALSE = (byte) 0x88;    
    
    public static int FLASH_SPAN_LENGTH = 2048;
    // </editor-fold> 
    
    /* EIA */
    public static EIA DeviceName = MIGPCOMMEM.instance.new EIA(0x00, 0x10);
    public static EIA Hardversion = MIGPCOMMEM.instance.new EIA(0x10, 0x08);
    public static EIA SoftwareVersion = MIGPCOMMEM.instance.new EIA(0x18, 0x08);
    public static EIA BuildSerialNum = MIGPCOMMEM.instance.new EIA(0x20, 0x10);
    public static EIA BuildDate = MIGPCOMMEM.instance.new EIA(0x30, 0x10);
    
    public static EIA DevNum = MIGPCOMMEM.instance.new EIA(0x44, 0x01);
    /* EIA */
    public  abstract class MEM {
        public byte getMEM;
        public byte setMEM;
        public int addr;
        public int length;
    }

    public class EIA extends MEM {

        public EIA(int addr, int memlength) {
            getMEM = GETEIA;
            setMEM = SETEIA;
            this.addr = addr;
            this.length  = memlength;
        }
    }

    public class SRA extends MEM {

        public SRA(int addr, int memlength) {
            this.getMEM = GETSRA;
            this.setMEM = SETSRA;
            this.addr = addr;
            this.length = memlength;
        }
    }

    public class VPA extends MEM {

        public VPA(int addr, int memlength) {
            this.getMEM = GETVPA;
            this.setMEM = SETVPA;
            this.addr = addr;
            this.length = memlength;
        }
    }

    public class NVPA extends MEM {

        public NVPA(int addr, int memlength) {
            this.getMEM = GETNVPA;
            this.setMEM = SETNVPA;
            this.addr = addr;
            this.length = memlength;
        }
    }

    public class MDA extends MEM {

        public MDA(int addr, int memlength) {
            this.getMEM = GETMDA;
            this.setMEM = SETMDA;
            this.addr = addr;
            this.length = memlength;
        }
    }
}
