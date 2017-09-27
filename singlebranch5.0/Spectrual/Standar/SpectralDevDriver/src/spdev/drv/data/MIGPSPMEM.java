/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

import nahon.pro.migp.MIGPCOMMEM;



/**
 *
 * @author jiche
 */
public class MIGPSPMEM extends MIGPCOMMEM {

    /*MDA*/
    public static MDA TestData = MIGPCOMMEM.instance.new MDA(0x00, 0x00);
    /*MDA*/

    /*SRA*/
    /*SRA*/
    /*NVPA(SPA)*/
    public static NVPA MinwaveLength = MIGPCOMMEM.instance.new NVPA(0x00, 0x02);
    public static NVPA MaxwaveLength = MIGPCOMMEM.instance.new NVPA(0x02, 0x02);
    public static NVPA MinIntegerTime = MIGPCOMMEM.instance.new NVPA(0x04, 0x04);
    public static NVPA MaxIntegerTime = MIGPCOMMEM.instance.new NVPA(0x08, 0x04);
    public static NVPA NodeNum = MIGPCOMMEM.instance.new NVPA(0x0C, 0x02);

    public static NVPA C0 = MIGPCOMMEM.instance.new NVPA(0x20, 0x08);
    public static NVPA C1 = MIGPCOMMEM.instance.new NVPA(0x28, 0x08);
    public static NVPA C2 = MIGPCOMMEM.instance.new NVPA(0x30, 0x08);
    public static NVPA C3 = MIGPCOMMEM.instance.new NVPA(0x38, 0x08);

    public static NVPA LinearType = MIGPCOMMEM.instance.new NVPA(0x40, 0x01);
    public static NVPA LinearPar = MIGPCOMMEM.instance.new NVPA(0x41, 0x2C8);
    /*NVPA(SPA)*/

    /*VPA(NSPA)*/
    public static VPA CollectMode = MIGPCOMMEM.instance.new VPA(0x00, 0x01);
    public static VPA IntegralTime = MIGPCOMMEM.instance.new VPA(0x01, 0x04);
    public static VPA AverageTime = MIGPCOMMEM.instance.new VPA(0x05, 0x02);
    public static VPA SustainLight = MIGPCOMMEM.instance.new VPA(0x10, 0x01);
    public static VPA LightPar = MIGPCOMMEM.instance.new VPA(0x26, 0x01);
    public static VPA AsynLight = MIGPCOMMEM.instance.new VPA(0x11, 0x05);
    public static VPA SynLight = MIGPCOMMEM.instance.new VPA(0x16, 0x0E);
    
    /*VPA(NSPA)*/
}
