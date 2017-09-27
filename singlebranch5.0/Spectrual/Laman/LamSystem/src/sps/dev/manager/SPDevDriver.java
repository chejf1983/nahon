/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.manager;

import lam.drv.cmd.light.LaserLightCmd;
import lam.drv.cmd.light.PowTempCmd;
import nahon.drv.cmd.EquipmentInfoCmd;
import nahon.drv.data.EquipmentInfo;
import nahon.drv.data.SConnectInfo;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.cmd.collect.DataCollectCmd;
import spdev.drv.cmd.collect.DataCollectParCmd;
import spdev.drv.cmd.parameter.LinearParCmd;
import spdev.drv.cmd.parameter.SpectralParCmd;
import spdev.drv.cmd.parameter.WaveCaculateParCmd;
import spdev.drv.data.DataCollectPar;
import spdev.drv.data.SLinearParameter;
import spdev.drv.data.SpectralPar;
import spdev.drv.data.WaveCaculatePar;
import sps.dev.data.SSEquipmentInfo;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSDevAddr;
import sps.dev.data.SSIOInfo;
import sps.dev.data.SSLinearParameter;
import sps.dev.data.SSPDevInfo;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.data.SSpectralPar;

/**
 *
 * @author Administrator
 */
public class SPDevDriver implements ISPDevDriver {

    public MIGPNahonDriver spdevdrv;

    public SPDevDriver(EquipmentInfo eia, SConnectInfo coninfo) {
        this.seia = eia;
        this.sconn = coninfo;
        this.spdevdrv = new MIGPNahonDriver(coninfo);
    }

    private EquipmentInfo seia;
    private SConnectInfo sconn;

    @Override
    public SSPDevInfo GetDevInfo() {
        SSPDevInfo ret = new SSPDevInfo();
        ret.eia = new SSEquipmentInfo();
        ret.eia.DeviceName = seia.DeviceName;
        ret.eia.Hardversion = seia.Hardversion;
        ret.eia.SoftwareVersion = seia.SoftwareVersion;
        ret.eia.BuildSerialNum = seia.BuildSerialNum;
        ret.eia.BuildDate = seia.BuildDate;

        ret.address = new SSDevAddr();
        ret.address.devaddr = sconn.dstaddr;
        ret.address.host_laddr = sconn.srcaddr;

        ret.ioinfo = new SSIOInfo();
        ret.ioinfo.iopar = sconn.io.GetConnectInfo().par;
        ret.ioinfo.iotype = sconn.io.GetConnectInfo().iotype;

        return ret;
    }

    // <editor-fold defaultstate="collapsed" desc="驱动开关"> 
    private boolean isConnect = false;

    @Override
    public void Open() throws Exception {
        if (!this.isConnect) {
            this.spdevdrv.Open();

            this.isConnect = true;
        }
    }

    @Override
    public void Close() {
        if (this.isConnect) {
            this.isConnect = false;
            this.spdevdrv.Close();
        }
    }

    @Override
    public boolean IsOpened() {
        return this.isConnect;
    }

    @Override
    public void Cancel() {
        if (this.isConnect) {
            this.spdevdrv.Cancel();
        }
    }

    @Override
    public boolean IsCmdCancled() {
        return this.spdevdrv.IsCanceled();
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="EIA"> 
    //获取EIA信息
    @Override
    public SSEquipmentInfo GetEquipmentInfo() throws Exception {
        EquipmentInfo eia = new EquipmentInfoCmd().GetFromDevice(this.spdevdrv, 1000);

        SSEquipmentInfo seia = new SSEquipmentInfo();
        seia.DeviceName = eia.DeviceName;
        seia.Hardversion = eia.Hardversion;
        seia.SoftwareVersion = eia.SoftwareVersion;
        seia.BuildSerialNum = eia.BuildSerialNum;
        seia.BuildDate = eia.BuildDate;
        return seia;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光谱仪参数"> 
    //获取光谱仪参数   
    @Override
    public SSpectralPar GetSpectralPar() throws Exception {
        SpectralPar sppar = new SpectralParCmd().GetSpectralParameter(this.spdevdrv, 1000);

        SSpectralPar ret = new SSpectralPar();
        ret.maxIntegralTime = sppar.maxIntegralTime;
        ret.minIntegralTime = sppar.minIntegralTime;
        ret.maxWaveLength = sppar.maxWaveLength;
        ret.minWaveLength = sppar.minWaveLength;
        ret.nodeNumber = sppar.nodeNumber;
        return ret;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="波长系数参数"> 
    //波长系数参数
    private WaveCaculatePar wcpar;

    @Override
    public SSWaveCaculatePar GetWaveParameter() throws Exception {
        this.wcpar = new WaveCaculateParCmd().GetWaveCaculateParameter(this.spdevdrv, 1000);

        SSWaveCaculatePar ret = new SSWaveCaculatePar();
        ret.C0 = this.wcpar.C0;
        ret.C1 = this.wcpar.C1;
        ret.C2 = this.wcpar.C2;
        ret.C3 = this.wcpar.C3;
        return ret;
    }

    //设置波长系数
    @Override
    public void SetWaveParameter(SSWaveCaculatePar par) throws Exception {
        WaveCaculatePar tmppar = new WaveCaculatePar();
        tmppar.C0 = par.C0;
        tmppar.C1 = par.C1;
        tmppar.C2 = par.C2;
        tmppar.C3 = par.C3;

        if (this.wcpar != null && this.wcpar.EuqalTo(tmppar)) {
            return;
        }

        new WaveCaculateParCmd().SetWaveCaculateParameter(this.spdevdrv, tmppar, 1000);
        this.wcpar = tmppar;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="非线性系数"> 
    @Override
    public SSLinearParameter GetLinearPar() throws Exception {
        SLinearParameter linearpar = new LinearParCmd().GetLinearParameter(this.spdevdrv);
        SSLinearParameter ret = new SSLinearParameter(linearpar.NodeNumber);

        ret.SynType = linearpar.SynType;
        ret.NodeArray = new SSLinearParameter.LinearParNode[ret.NodeNumber];

        for (int i = 0; i < ret.NodeArray.length; i++) {
            SLinearParameter.LinearParNode node = linearpar.NodeArray[i];
            float[] adarray = new float[node.ADValueArray.length];
            float[] karray = new float[node.KParArray.length];
            System.arraycopy(node.ADValueArray, 0, adarray, 0, adarray.length);
            System.arraycopy(node.KParArray, 0, karray, 0, karray.length);
            ret.NodeArray[i] = ret.new LinearParNode(node.NodeWave, node.NodeKNumber,
                    adarray, karray);
        }

        return ret;
    }

    @Override
    public void SetLinearPar(SSLinearParameter linearpar) throws Exception {
        SLinearParameter partmp = new SLinearParameter(linearpar.NodeNumber);

        partmp.SynType = linearpar.SynType;
        partmp.NodeArray = new SLinearParameter.LinearParNode[partmp.NodeNumber];

        for (int i = 0; i < partmp.NodeArray.length; i++) {
            SSLinearParameter.LinearParNode node = linearpar.NodeArray[i];
            float[] adarray = new float[node.ADValueArray.length];
            float[] karray = new float[node.KParArray.length];
            System.arraycopy(node.ADValueArray, 0, adarray, 0, adarray.length);
            System.arraycopy(node.KParArray, 0, karray, 0, karray.length);
            partmp.NodeArray[i] = partmp.new LinearParNode(node.NodeWave, node.NodeKNumber,
                    adarray, karray);
        }

        new LinearParCmd().SetLinearParameter(this.spdevdrv, partmp);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="采集控制"> 
    private SSDataCollectPar collectpar = null;

    @Override
    public double[] CollectData(SSDataCollectPar par, int nodenum) throws Exception {
        if (this.collectpar == null || !this.collectpar.EqualTo(par)) {
            this.collectpar = par;
            DataCollectPar tmppar = new DataCollectPar(par.integralTime, par.averageTime, par.WorkMode);
            new DataCollectParCmd().SetCollectPar(this.spdevdrv, tmppar, 1000);
        }

        //获取原始数据
        return new DataCollectCmd().GetSPData(this.spdevdrv, nodenum, GetCollectTimeout(par));
    }

    //计算超时时间
    private int GetCollectTimeout(SSDataCollectPar par) {
        if (par.WorkMode == DataCollectPar.SoftMode) {
            //软件模式，根据平均次数，积分时间计算超时时间
            int CMDLagtime = 2000; //安全余量
            int HWTimePeiod = 5;//硬件平均周期
            return (int) (par.integralTime + HWTimePeiod) * par.averageTime + CMDLagtime;
        } else {
            //硬件模式，超时时间无穷大
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void StopCollectData() throws Exception {
        this.spdevdrv.Cancel();

        //如果是取消了，重新下发软件模式到设备上
        if (this.collectpar.WorkMode != DataCollectPar.SoftMode) {
            this.collectpar.WorkMode = DataCollectPar.SoftMode;
            DataCollectPar npar = new DataCollectPar(collectpar.integralTime, collectpar.averageTime, DataCollectPar.SoftMode);
            new DataCollectParCmd().SetCollectPar(this.spdevdrv, npar, 1000);
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="光源控制"> 
    @Override
    public int GetLLgihtIr() throws Exception {
        return new LaserLightCmd().GetLLIr(this.spdevdrv, 1000);
    }

    @Override
    public void SetLLgihtIr(int ir) throws Exception {
        new LaserLightCmd().SetLLIr(this.spdevdrv, ir, 1000);
    }

    @Override
    public boolean IsLightEnable() throws Exception {
        return new LaserLightCmd().IsLightEnable(this.spdevdrv, 1000);
    }

    @Override
    public void EnableLight(boolean value) throws Exception {
        new LaserLightCmd().SetLightEnable(this.spdevdrv, value, 1000);
    }
    // </editor-fold> 

    @Override
    public float GetBatteryPower() throws Exception {
        return new PowTempCmd().GetBatteryPower(this.spdevdrv, 1000);
    }

    @Override
    public float GetTemperature() throws Exception {
        return new PowTempCmd().GetTemperature(this.spdevdrv, 1000);
    }

}
