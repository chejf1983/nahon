/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author Administrator
 */
public class SSEquipmentInfo {
    public String DeviceName = "Unknow";
    public String Hardversion = "Unknow";
    public String SoftwareVersion = "Unknow";
    public String BuildSerialNum = "Unknow";
    public String BuildDate = "Unknow";
    
    public boolean SameAs(SSEquipmentInfo other){
        return this.DeviceName.contentEquals(other.DeviceName) &&
                this.Hardversion.contentEquals(other.Hardversion) &&
                this.SoftwareVersion.contentEquals(other.SoftwareVersion) &&
                this.BuildSerialNum.contentEquals(other.BuildSerialNum) &&
                this.BuildDate.contentEquals(other.BuildDate);
    }
}
