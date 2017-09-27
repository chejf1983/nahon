/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import sps.dev.control.ifs.ISPDevConfig;
import sps.dev.control.ifs.ISPDevControl.CSTATE;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSpectralPar;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.manager.ISPDevDriver;

/**
 *
 * @author Administrator
 */
public class SPDevConfig implements ISPDevConfig {

    private final SPDevControl control;

    public SPDevConfig(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        ISPDevDriver dev = this.control.GetDevice();
        this.sppar = dev.GetSpectralPar();
        TimeUnit.MILLISECONDS.sleep(10);
        this.swpar = dev.GetWaveParameter();
        TimeUnit.MILLISECONDS.sleep(10);
        this.linearpar = dev.GetLinearPar();
        TimeUnit.MILLISECONDS.sleep(10);
    }

    public void Close() {
    }

    // <editor-fold defaultstate="collapsed" desc="非线性系数"> 
    public SSLinearParameter linearpar;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光谱仪参数"> 
    public SSpectralPar sppar;

    //获取光谱仪参数
    @Override
    public SSpectralPar GetSpectralPar() throws Exception {
        if (this.control.GetControlState() != CSTATE.CLOSE) {
            return this.sppar;
        } else {
            throw new Exception("设备未连接!");
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="波长系数参数"> 
    public SSWaveCaculatePar swpar;

    //获取波长系数
    @Override
    public SSWaveCaculatePar GetWaveParameter() throws Exception {
        if (this.control.GetControlState() != CSTATE.CLOSE) {
            return this.swpar;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    //设置波长系数
    @Override
    public void SetWaveParameter(SSWaveCaculatePar par) throws Exception {
        this.control.LockDevice();
        try {
            if (!this.swpar.EqualTo(par)) {
                this.control.GetDevice().SetWaveParameter(par);
                this.swpar = new SSWaveCaculatePar(par);
            }
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            throw new Exception("设置光谱参数失败!");
        } finally {
            this.control.UnlockDevice();
        }
    }
    // </editor-fold> 
}
