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
public class SSIOInfo {    
    public enum IOType{
        USB,
        COMM,
        TCP
    }
    
    public String iotype = "";
    public String[] iopar;
    
    public SSIOInfo(){
        
    }
    
    public SSIOInfo(String type, String ... args){
        this.iopar = args;
        this.iotype = type;
    }
    
    public boolean SameAs(SSIOInfo ioinfo) {
        if(this.iotype.contentEquals(ioinfo.iotype) && iopar.length == ioinfo.iopar.length ){
            
            for(int i = 0; i < iopar.length; i++){
                if(!iopar[i].contentEquals(ioinfo.iopar[i])){
                    return false;
                }
            }
            
            return true;
        }
        
        return false;
    }
}
