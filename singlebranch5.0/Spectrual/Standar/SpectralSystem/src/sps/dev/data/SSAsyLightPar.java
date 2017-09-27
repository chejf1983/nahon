/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.data;

/**
 *
 * @author jiche
 */
public class SSAsyLightPar {

    public int hightime = 0;
    public int lowtime = 0;

    public SSAsyLightPar() {

    }

    public SSAsyLightPar(SSAsyLightPar par) {
        this.hightime = par.hightime;
        this.lowtime = par.lowtime;
    }
    
    public boolean equalto(SSAsyLightPar par){
        return this.hightime == par.hightime && this.lowtime == par.lowtime;
    }
}
