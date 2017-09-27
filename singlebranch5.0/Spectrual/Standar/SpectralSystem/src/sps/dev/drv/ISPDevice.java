/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.drv;

import sps.dev.data.SSAsyLightPar;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSEquipmentInfo;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSPDevInfo;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.data.SSpectralPar;
import sps.dev.data.SSynLightPar;

/**
 *
 * @author Administrator
 */
public interface ISPDevice {

    // <editor-fold defaultstate="collapsed" desc="基本控制接口">  
    public void Open() throws Exception;

    public void Close();

    public boolean IsOpened();

    public boolean IsCmdCancled();

    public void Cancel();

    public SSPDevInfo GetDevInfo();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集控制">  
    public double[] CollectData(int nodenum, int timeout) throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="参数设置">  
    // <editor-fold defaultstate="collapsed" desc="EIA">  
    public SSEquipmentInfo GetEquipmentInfo() throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集参数">  
    public SSDataCollectPar GetCollectPar() throws Exception;

    public void SetCollectPar(SSDataCollectPar par) throws Exception;
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
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光源控制">  
    public boolean IsSusEnable() throws Exception;

    public void EnableSus(boolean value) throws Exception;

    public void SetLightPar(boolean[] value) throws Exception;

    public boolean[] GetLightPar() throws Exception;

    public boolean IsAsynEnable() throws Exception;

    public void EnableAsyn(boolean value) throws Exception;

    public SSAsyLightPar GetASynLightPar() throws Exception;

    public void SetASynLightPar(SSAsyLightPar par) throws Exception;

    public boolean IsSynEnable() throws Exception;

    public void EnableSyn(boolean value) throws Exception;

    public SSynLightPar GetSynLightPar() throws Exception;

    public void SetSynLightPar(SSynLightPar par) throws Exception;
    // </editor-fold> 
}
