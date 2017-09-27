/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lam.devcontrol.inst;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lam.devcontrol.ifs.ISPDevControl.CSTATE;
import sps.system.SPDevSystem;

/**
 *
 * @author Administrator
 */
public class SPKeepAlive {

    private SPDevControl control;
    private boolean isStart = false;
    private int keepalivetime = 1000;
    private int failedtime = 0;
    private int maxfailedtime = 3;
    private Future<?> monitorprocess;

    public SPKeepAlive(SPDevControl control) {
        this.control = control;
    }

    //开始心跳检测
    public void StartKeepAlive() {
        if (!this.isStart) {
            this.isStart = true;
            this.monitorprocess = SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    while (isStart) {
                        if (CheckDevice()) {
                            if (control.GetControlState() == CSTATE.DISCONNECT) {
                                control.ChangeState(CSTATE.CONNECT);
                            }
                        } else {
                            if (control.GetControlState() == CSTATE.CONNECT) {
                                control.ChangeState(CSTATE.DISCONNECT);
                            }
                        }

                        try {
                            TimeUnit.MILLISECONDS.sleep(keepalivetime);
                        } catch (Exception ex) {
                        }
                    }
                }
            });
        }
    }

    //停止心跳检测
    public void StopKeepAlive() {
        if (this.isStart) {
            while (!monitorprocess.isDone()) {
                this.isStart = false;
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private boolean CheckDevice() {
        if (!this.control.spdev.IsConnect()) {
            try {
                this.control.controllock.lock();
                this.control.spdev.Connect();

                this.control.spdev.GetNahonDevConfig().GetEquipmentInfo();

                failedtime = 0;
            } catch (Exception ex) {
                failedtime++;
            } finally {
                this.control.spdev.DisConnect();
                this.control.controllock.unlock();
            }
        }

        return failedtime <= maxfailedtime;
    }

    public boolean IsStart() {
        return this.isStart;
    }
}
