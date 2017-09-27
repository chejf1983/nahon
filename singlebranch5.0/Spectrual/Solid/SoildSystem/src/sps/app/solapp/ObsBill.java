/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.solapp;

import sps.app.solapp.data.SolTestData;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import nahon.comm.event.EventCenter;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.exl.ExcelWriter;
import nahon.comm.faultsystem.FaultCenter;
import sps.app.solapp.data.NPKData;
import sps.dev.data.SSPData;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class ObsBill {

    public EventCenter ConfigChange = new EventCenter();
    
    // <editor-fold defaultstate="collapsed" desc="标准灯设置"> 
    private SSpectralDataPacket basedata;

    public void UpdateBaseLight(SSpectralDataPacket data) {
        if (data == null) {
            this.basedata = null;
            return;
        }

        this.basedata = new SSpectralDataPacket(data);
        for (int i = 0; i < this.basedata.data.datavalue.length; i++) {
            if (this.basedata.data.datavalue[i] < 0) {
                this.basedata.data.datavalue[i] = 0;
            }
        }
    }

    private boolean is_baseenable = false;

    public boolean IsBaseEnable() {
        return this.is_baseenable;
    }

    public void EnableBase(boolean value) {
        this.is_baseenable = value;
        this.ConfigChange.CreateEvent(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="标准N0设置">
    private double N0 = 0;
    private boolean is_baseN0Enable = false;

    public void SetBaseN0(double N0) {
        this.N0 = N0;
        this.is_baseN0Enable = true;
    }

    public double GetBaseN0() {
        return this.N0;
    }

    public boolean IsBaseN0Enable() {
        return this.is_baseN0Enable;
    }

    public void EnableBaseN0(boolean value) {
        this.is_baseN0Enable = value;
        this.ConfigChange.CreateEvent(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="标准P0设置">
    private double P0 = 0;
    private boolean is_baseP0Enable = false;

    public void SetBaseP0(double P0) {
        this.P0 = P0;
        this.is_baseP0Enable = true;
    }

    public double GetBaseP0() {
        return this.P0;
    }

    public boolean IsBaseP0Enable() {
        return this.is_baseP0Enable;
    }

    public void EnableBaseP0(boolean value) {
        this.is_baseP0Enable = value;
        this.ConfigChange.CreateEvent(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="标准K0设置">
    private double K0 = 0;
    private boolean is_baseK0Enable = false;

    public void SetBaseK0(double K0) {
        this.K0 = K0;
        this.is_baseK0Enable = true;
    }

    public double GetBaseK0() {
        return this.K0;
    }

    public boolean IsBaseK0Enable() {
        return this.is_baseK0Enable;
    }

    public void EnableBaseK0(boolean value) {
        this.is_baseK0Enable = value;
        this.ConfigChange.CreateEvent(null);
    }
    // </editor-fold>

    public boolean IsCalReady() {
        return this.is_baseenable && this.is_baseN0Enable
                && this.is_baseP0Enable && this.is_baseK0Enable;
    }

    public SolTestData BuildTestData(SSpectralDataPacket spdata) {
        if (this.basedata == null || this.basedata.data.datavalue.length != spdata.data.datavalue.length) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, "无法计算吸光度");
            return null;
        }

        //计算吸收光
        double[] tmp = new double[spdata.data.datavalue.length];
        for (int i = 0; i < tmp.length; i++) {
            if (basedata.data.datavalue[i] <= 0) {
                tmp[i] = 0;
            } else if (spdata.data.datavalue[i] <= 0
                    || (basedata.data.datavalue[i] / spdata.data.datavalue[i]) > 10000) {
                tmp[i] = 4;
            } else {
                try {
                    tmp[i] = Math.log10(basedata.data.datavalue[i] / spdata.data.datavalue[i]);
                } catch (Exception ex) {
                    tmp[i] = 0;
                }
                if (tmp[i] < 0) {
                    tmp[i] = 0;
                } else {
                    tmp[i] = new BigDecimal(tmp[i]).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        }

        return new SolTestData(spdata, new SSPData(spdata.data.waveIndex, tmp));
    }
}
