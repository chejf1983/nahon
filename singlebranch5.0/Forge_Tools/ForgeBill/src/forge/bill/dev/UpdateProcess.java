/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.dev;

import nahon.drv.update.IProcess;


/**
 *
 * @author Administrator
 */
public class UpdateProcess implements IProcess {

    private float pecent;
    private boolean isFinished = false;
    private boolean isSuccess = false;

    @Override
    public void SetPecent(float pecent) {
        this.pecent = pecent;
    }

    @Override
    public float GetPecent() {
        return pecent;
    }

    @Override
    public boolean IsFinished() {
        return this.isFinished;
    }

    public void SetSuccessFlag(boolean value) {
        this.isFinished = true;
        this.isSuccess = value;
    }

    @Override
    public boolean IsSucess() {
        return isSuccess;
    }

}
