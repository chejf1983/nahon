/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.data;

/**
 *
 * @author jiche
 */
public class AsyLightPar {

    public int hightime = 0;
    public int lowtime = 0;

    public AsyLightPar() {

    }

    public AsyLightPar(AsyLightPar par) {
        this.hightime = par.hightime;
        this.lowtime = par.lowtime;
    }
    
    public boolean equalto(AsyLightPar par){
        return this.hightime == par.hightime && this.lowtime == par.lowtime;
    }
}
