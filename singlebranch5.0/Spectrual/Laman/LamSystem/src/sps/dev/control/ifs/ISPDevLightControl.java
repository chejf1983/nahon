/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

/**
 *
 * @author Administrator
 */
public interface ISPDevLightControl {

    public int GetLLgihtIr() throws Exception;

    public void SetLLgihtIr(int ir) throws Exception;

    public String GetLLgihtType() throws Exception;

    public boolean IsLightEnable()throws Exception;

    public void EnableLight(boolean value) throws Exception;
}
