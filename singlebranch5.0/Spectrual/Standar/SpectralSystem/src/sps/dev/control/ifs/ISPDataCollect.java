/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.ifs;

import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public interface ISPDataCollect {

    // <editor-fold defaultstate="collapsed" desc="数据采集"> 
    //锁定开始采集
    public void PrepareCollect(SSDataCollectPar par) throws Exception;

    //是否取消了
    public boolean IsCanceled();
    
    //获取采集参数
    public SSDataCollectPar GetCollectPar() throws Exception;

    //采集
    public SSpectralDataPacket StartSingleTest(int window) throws Exception;

    //停止采集
    public void CancelCollectData() throws Exception;

    //停止采集
    public void FinishCollect();
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="暗电流控制"> 
    public boolean IsDKEnable() throws Exception;

    public void SetDkEnable(boolean value) throws Exception;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="滤波控制"> 
    public boolean IsFilterEnable();

    public void SetFilterEnable(boolean value);
    // </editor-fold>     

    // <editor-fold defaultstate="collapsed" desc="非线性控制"> 
    public boolean IsLinearEnable();

    public void SetLinearEnable(boolean value);
    // </editor-fold> 
}
