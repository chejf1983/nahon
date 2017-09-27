/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.inst;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import sps.dev.control.ifs.ISPDevControl;
import sps.dev.control.ifs.ISPDevLightControl;
import nahon.comm.faultsystem.FaultCenter;

/**
 *
 * @author Administrator
 */
public class SPLightControl implements ISPDevLightControl {

    private final SPDevControl control;

    public SPLightControl(SPDevControl control) {
        this.control = control;
    }
    
    public void Init() throws Exception{
        this.ir = this.control.GetDevice().GetLLgihtIr();
        TimeUnit.MILLISECONDS.sleep(10);
        this.isEnable = this.control.GetDevice().IsLightEnable();
        TimeUnit.MILLISECONDS.sleep(10);
    }
    
    public void Close(){
        
    }

    //激光器参数
    int ir;
    @Override
    public int GetLLgihtIr() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.ir;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    @Override
    public void SetLLgihtIr(int ir) throws Exception {
        this.control.LockDevice();
        try {
            if (ir < 1500 && ir > 0) {
                this.control.GetDevice().SetLLgihtIr(ir);
                this.ir = ir;
            } else {
                FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                        "输入参数超过限制(1-1500)");
            }
        } catch (Exception ex) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE,
                    "设置异常");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        } finally {
            this.control.UnlockDevice();
        }

    }

    //激光器类型
    @Override
    public String GetLLgihtType() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return "785";
        } else {
            throw new Exception("设备未连接!");
        }
    }

    //激光器开关
    private boolean isEnable = false;
    @Override
    public boolean IsLightEnable() throws Exception {
        if (this.control.GetControlState() != ISPDevControl.CSTATE.CLOSE) {
            return this.isEnable;
        } else {
            throw new Exception("设备未连接!");
        }
    }

    @Override
    public void EnableLight(boolean value) throws Exception {
        this.control.LockDevice();
        try {
            this.control.GetDevice().EnableLight(value);
            this.isEnable = value;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            throw new Exception("设置异常!");
        } finally {
            this.control.UnlockDevice();
        }
    }

}
