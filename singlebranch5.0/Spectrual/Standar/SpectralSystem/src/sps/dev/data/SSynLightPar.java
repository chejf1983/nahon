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
public class SSynLightPar {

    public int plustype = 0;
    public int pluswidth = 0;
    public int plusinterval = 0;
    public int pluslag = 0;
    public int plusnum = 0;

    public SSynLightPar() {

    }

    public SSynLightPar(SSynLightPar par) {
        this.plusinterval = par.plusinterval;
        this.plusnum = par.plusnum;
        this.pluslag = par.pluslag;
        this.pluswidth = par.pluswidth;
        this.plustype = par.plustype;
    }

    public boolean equalto(SSynLightPar par) {
        return this.plusinterval == par.plusinterval
                && this.plusnum == par.plusnum
                && this.pluslag == par.pluslag
                && this.pluswidth == par.pluswidth
                && this.plustype == par.plustype;
    }
}
