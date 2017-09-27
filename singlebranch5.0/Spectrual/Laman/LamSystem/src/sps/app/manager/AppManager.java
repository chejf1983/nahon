/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.manager;

import sps.app.raman.RamanApp;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class AppManager {

    public void InputData(SSpectralDataPacket spdata) {
        this.ramapp.InputData(spdata);
    }

    private CollectControl collectcontrol = new CollectControl(this);
    public CollectControl GetCollectControl() {
        return this.collectcontrol;
    }

    private RamanApp ramapp = new RamanApp();

    public RamanApp GetCommonApp() {
        return this.ramapp;
    }
}
