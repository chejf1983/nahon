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
public class SSPDevInfo {
    public SSEquipmentInfo eia;
    public SSDevAddr address;
    public SSIOInfo ioinfo;
    
    public boolean SameAs(SSPDevInfo other){
        if(other == null){
            return false;
        }
        
        return this.eia.SameAs(other.eia) && 
                 this.address.SameAs(other.address) &&
                this.ioinfo.SameAs(other.ioinfo);
    }
}
