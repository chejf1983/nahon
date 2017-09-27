/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.inst;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import lam.devcontrol.ifs.ISPDevConfig;
import lam.devcontrol.ifs.ISPDevControl.CSTATE;
import lam.faultcenter.FaultCenter;
import nahon.dev.data.EquipmentInfo;
import nahon.dev.data.SConnectInfo;
import spdev.dev.data.SpectralPar;
import spdev.dev.data.WaveCaculatePar;

/**
 *
 * @author Administrator
 */
public class SPDevConfig implements ISPDevConfig {

    private SPDevControl control;

    public SPDevConfig(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        //获取eia缓存
        this.eia = this.control.spdev.GetNahonDevConfig().GetEquipmentInfo();
        TimeUnit.MILLISECONDS.sleep(10);
        //获取光谱仪参数缓存
        this.sppar = this.control.spdev.GetSpDevConfig().GetSpectralParameter();
        TimeUnit.MILLISECONDS.sleep(10);
        //获取波长系数缓存
        this.wpar = this.control.spdev.GetSpDevConfig().GetWaveCaculateParameter();
        TimeUnit.MILLISECONDS.sleep(10);
    }

    //eia信息
    private EquipmentInfo eia;

    @Override
    public EquipmentInfo GetEquipmentInfo() {
        return new EquipmentInfo(this.eia);
    }

    //连接信息
    @Override
    public SConnectInfo GetDevConnectInfo() {
        return this.control.spdev.GetConnectInfo();
    }

    //光谱仪参数
    private SpectralPar sppar;

    @Override
    public SpectralPar GetSpectralPar() {
        return new SpectralPar(sppar);
    }

    //波长系数参数
    private WaveCaculatePar wpar;

    @Override
    public WaveCaculatePar GetWaveParameter() {
        return new WaveCaculatePar(wpar);
    }

    @Override
    public boolean SetWaveParameter(WaveCaculatePar par) {
        if (this.control.GetControlState() == CSTATE.CONNECT) {
            if (!this.wpar.EuqalTo(par)) {
                try {
                    this.control.controllock.lock();
                    this.control.spdev.Connect();
                    this.control.spdev.GetSpDevConfig().SetWaveCaculateParameter(par);
                } catch (Exception ex) {
                    FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "设置光谱参数失败！" + ex.toString());
                    return false;
                }finally{
                    this.control.spdev.DisConnect();
                    this.control.controllock.unlock();
                }
            }
            return true;

        } else {
            FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "设备正忙，无法设置光谱数据！" + this.control.GetControlState());
            return false;
        }
    }

}
