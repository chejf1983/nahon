/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSEquipmentInfo;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSPDevInfo;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.data.SSpectralPar;

/**
 *
 * @author Administrator
 */
public interface ISPDevDriver {

    // <editor-fold defaultstate="collapsed" desc="IO开关">  
    public void Open() throws Exception;

    public void Close();

    public boolean IsOpened();
    // </editor-fold> 

    public boolean IsCmdCancled();

    public void Cancel();

    public SSPDevInfo GetDevInfo();
    
    // <editor-fold defaultstate="collapsed" desc="采集流控制">  
    public double[] CollectData(SSDataCollectPar par, int nodenum) throws Exception;

    public void StopCollectData() throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="EIA">  
    public SSEquipmentInfo GetEquipmentInfo() throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="非线性系数">  
    public SSLinearParameter GetLinearPar() throws Exception;

    public void SetLinearPar(SSLinearParameter par) throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光谱仪参数系数">  
    public SSpectralPar GetSpectralPar() throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="波长系数性系数">  
    public SSWaveCaculatePar GetWaveParameter() throws Exception;

    public void SetWaveParameter(SSWaveCaculatePar par) throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="电池电量和温度">  
    public float GetBatteryPower() throws Exception;    

    public float GetTemperature() throws Exception;
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="光源控制">  
    public int GetLLgihtIr() throws Exception;

    public void SetLLgihtIr(int ir) throws Exception;

    public boolean IsLightEnable() throws Exception;

    public void EnableLight(boolean value) throws Exception;
    // </editor-fold> 

}
