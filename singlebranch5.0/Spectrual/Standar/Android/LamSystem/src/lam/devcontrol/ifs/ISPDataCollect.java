/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.ifs;

import nahon.comm.event.EventListener;
import spdev.alg.spdatacal.SpectralDataFactory;
import spdev.dev.data.DataCollectPar;
import spdev.dev.data.SpectralDataPacket;

/**
 *
 * @author Administrator
 */
public interface ISPDataCollect {
    //获取数据采集参数
    public DataCollectPar GetDataCollectPar();

    //获取光谱生成器
    public SpectralDataFactory GetSpBuildModel();
    
    
    // <editor-fold defaultstate="collapsed" desc="数据采集"> 
    //连续采集
    public boolean StartSustainCollect(DataCollectPar par,int inteveral);
    
    //单次采集
    public boolean StartSingleTest(DataCollectPar par);

    //停止采集
    public void StopCollectData();
    
    //获取上一次采集原始数据
    public double[] GetLastOriginalData();
    // </editor-fold> 

    //注册数据采集响应
    public void RegisterDataCollectListener(EventListener<SpectralDataPacket> listener);
    
    //取消数据采集响应接口
    public void UnRegisterDataCollectListener(EventListener<SpectralDataPacket> listener);
}
