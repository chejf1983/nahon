/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.*;


/**
 *
 * @author Administrator
 */
public interface ISPDevConfig {
    //获取设备光学参数
    public SSpectralPar GetSpectralPar() throws Exception;
    
    //获取设备波长系数
    public SSWaveCaculatePar GetWaveParameter() throws Exception;
        
    //设置波长系数
    public void SetWaveParameter(SSWaveCaculatePar par) throws Exception;
}
