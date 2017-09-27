/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.ifs;

import nahon.dev.data.EquipmentInfo;
import nahon.dev.data.SConnectInfo;
import spdev.dev.data.SpectralPar;
import spdev.dev.data.WaveCaculatePar;

/**
 *
 * @author Administrator
 */
public interface ISPDevConfig {
    //获取设备信息
    public EquipmentInfo GetEquipmentInfo();
    
    //获取设备连接信息
    public SConnectInfo GetDevConnectInfo();
    
    //获取设备光学参数
    public SpectralPar GetSpectralPar();
    
    //获取设备波长系数
    public WaveCaculatePar GetWaveParameter();
        
    //设置波长系数
    public boolean SetWaveParameter(WaveCaculatePar par);
}
