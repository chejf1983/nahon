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
public class SynLightPar {

    public int plustype = 0;
    public int pluswidth = 0;
    public int plusinterval = 0;
    public int pluslag = 0;
    public int plusnum = 0;

    public SynLightPar() {

    }

    public SynLightPar(SynLightPar par) {
        this.plusinterval = par.plusinterval;
        this.plusnum = par.plusnum;
        this.pluslag = par.pluslag;
        this.pluswidth = par.pluswidth;
        this.plustype = par.plustype;
    }

    public boolean equalto(SynLightPar par) {
        return this.plusinterval == par.plusinterval
                && this.plusnum == par.plusnum
                && this.pluslag == par.pluslag
                && this.pluswidth == par.pluswidth
                && this.plustype == par.plustype;
    }
}
