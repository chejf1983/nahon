/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.TimeUnit;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.ifs.ISPDevLightControl;
import sps.dev.data.SSAsyLightPar;
import sps.dev.data.SSynLightPar;

/**
 *
 * @author Administrator
 */
public class SPLightControl implements ISPDevLightControl {

    private final SPDevControl control;

    public SPLightControl(SPDevControl control) {
        this.control = control;
    }

    public void Init() throws Exception {
        ltype = LightType.Syn;
        this.isSusLightEnable = this.control.GetDevice().IsSusEnable();
        TimeUnit.MILLISECONDS.sleep(10);
        this.suslightpar = this.control.GetDevice().GetLightPar();
        TimeUnit.MILLISECONDS.sleep(10);
        this.isAsynLightEnable = this.control.GetDevice().IsAsynEnable();
        TimeUnit.MILLISECONDS.sleep(10);
        this.asypar = this.control.GetDevice().GetASynLightPar();
        TimeUnit.MILLISECONDS.sleep(10);
        this.isSynLightEnable = this.control.GetDevice().IsSynEnable();
        TimeUnit.MILLISECONDS.sleep(10);
        this.synpar = this.control.GetDevice().GetSynLightPar();
        TimeUnit.MILLISECONDS.sleep(10);
    }

    // <editor-fold defaultstate="collapsed" desc="光源控制"> 
    private LightType ltype;

    @Override
    public LightType GetLLgihtType(){
        return this.ltype;
    }

    @Override
    public void SetLLgihtType(LightType type) throws Exception {
        if (this.ltype != type) {
            this.EnableAsynLight(false);
            this.EnableSusLight(false);
            this.EnableSynLight(false);
            this.ltype = type;
        }
    }

    @Override
    public boolean IsLightEnable() throws Exception {
        switch (this.ltype) {
            case Sus:
                return this.isSusLightEnable;
            case Asyn:
                return this.isAsynLightEnable;
            case Syn:
                return this.isSynLightEnable;
            default:
                return false;
        }
    }

    @Override
    public void EnableLight(boolean value) throws Exception {
        switch (this.ltype) {
            case Sus:
                this.EnableSusLight(value);
            case Asyn:
                this.EnableAsynLight(value);
            case Syn:
                this.EnableSynLight(value);
            default:
                return;
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="持续光源"> 
    private boolean isSusLightEnable = false;

    private void EnableSusLight(boolean value) throws Exception {
        if (this.isSusLightEnable != value) {
            this.control.LockDevice();
            try {
                this.control.GetDevice().EnableSus(value);
                this.isSusLightEnable = value;
            } finally {
                this.control.UnlockDevice();
            }
        }
    }

    private boolean[] suslightpar;

    @Override
    public void SetLightPar(boolean[] value) throws Exception {
        this.control.LockDevice();
        try {
            this.control.GetDevice().SetLightPar(suslightpar);
        } finally {
            this.control.UnlockDevice();
        }
    }

    @Override
    public boolean[] GetLightPar() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.suslightpar;
        } else {
            throw new Exception("设备未连接!");
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="异步光源"> 
    private boolean isAsynLightEnable = false;

    private void EnableAsynLight(boolean value) throws Exception {
        if (this.isAsynLightEnable != value) {
            this.control.LockDevice();
            try {
                this.control.GetDevice().EnableAsyn(value);
                this.isAsynLightEnable = value;
            } finally {
                this.control.UnlockDevice();
            }
        }
    }

    private SSAsyLightPar asypar;

    @Override
    public SSAsyLightPar GetASynLightPar() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.asypar;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    @Override
    public void SetASynLightPar(SSAsyLightPar par) throws Exception {
        if (!this.asypar.equalto(par)) {
            try {
                this.control.GetDevice().SetASynLightPar(par);
                this.asypar = new SSAsyLightPar(par);
            } finally {
                this.control.UnlockDevice();
            }
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="同步光源"> 
    private boolean isSynLightEnable = false;
    private SSynLightPar synpar;

    private void EnableSynLight(boolean value) throws Exception {
        if (this.isSynLightEnable != value) {
            this.control.LockDevice();
            try {
                this.control.GetDevice().EnableSyn(value);
                this.isSynLightEnable = value;
            } finally {
                this.control.UnlockDevice();
            }
        }
    }

    @Override
    public SSynLightPar GetSynLightPar() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.synpar;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    @Override
    public void SetSynLightPar(SSynLightPar par) throws Exception {
        if (!this.synpar.equalto(par)) {
            try {
                this.control.GetDevice().SetSynLightPar(par);
                this.synpar = new SSynLightPar(par);
            } finally {
                this.control.UnlockDevice();
            }
        }
    }
    // </editor-fold> 

}
