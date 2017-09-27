/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.SSAsyLightPar;
import sps.dev.data.SSynLightPar;

/**
 *
 * @author Administrator
 */
public interface ISPDevLightControl {
    
    public enum LightType {
        Sus,
        Asyn,
        Syn
    }

    public LightType GetLLgihtType();

    public void SetLLgihtType(LightType type) throws Exception;

    public boolean IsLightEnable() throws Exception ;

    public void EnableLight(boolean value) throws Exception ;
    
    public void SetLightPar(boolean[] value) throws Exception ;

    public boolean[] GetLightPar() throws Exception;

    public SSAsyLightPar GetASynLightPar() throws Exception ;

    public void SetASynLightPar(SSAsyLightPar par) throws Exception ;

    public SSynLightPar GetSynLightPar() throws Exception ;

    public void SetSynLightPar(SSynLightPar par) throws Exception ;
}
