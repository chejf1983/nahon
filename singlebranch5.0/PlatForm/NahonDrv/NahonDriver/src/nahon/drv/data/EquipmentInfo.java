/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.data;

/**
 *
 * @author jiche
 */
public class EquipmentInfo {

    public String DeviceName = "Unknow";
    public String Hardversion = "Unknow";
    public String SoftwareVersion = "Unknow";
    public String BuildSerialNum = "Unknow";
    public String BuildDate = "Unknow";

    public EquipmentInfo() {

    }

    public EquipmentInfo(EquipmentInfo eia) {
        this.DeviceName = eia.DeviceName;
        this.Hardversion = eia.Hardversion;
        this.SoftwareVersion = eia.SoftwareVersion;
        this.BuildSerialNum = eia.BuildSerialNum;
        this.BuildDate = eia.BuildDate;
    }

    public boolean EqualTo(EquipmentInfo eia) {
        return this.DeviceName.matches(eia.DeviceName)
                && this.Hardversion.matches(eia.Hardversion)
                && this.SoftwareVersion.matches(eia.SoftwareVersion)
                && this.BuildSerialNum.matches(eia.BuildSerialNum)
                && this.BuildDate.matches(eia.BuildDate);
    }
}
