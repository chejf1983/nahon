/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.SSDataCollectPar;

/**
 *
 * @author Administrator
 */
public interface ISPDataCollect {

    // <editor-fold defaultstate="collapsed" desc="数据采集"> 
    //是否开始采集数据
    public boolean IsStartCollect();

    //连续采集
    public boolean StartSustainCollect(ISPDataReceive receive, SSDataCollectPar par, int window, int inteveral);

    //单次采集
    public boolean StartSingleTest(ISPDataReceive receive, SSDataCollectPar par, int window);

    //停止采集
    public void StopCollectData();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="暗电流控制"> 
    public boolean IsDKEnable();

    public void SetDkEnable(boolean value);
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="温度电量控制"> 
    public float GetBatteryPower() throws Exception;

    public float GetTemperature() throws Exception;
    // </editor-fold> 

}
