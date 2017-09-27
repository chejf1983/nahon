/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.manager;

import sps.dev.control.ifs.ISPDataReceive;
import sps.dev.data.SSDataCollectPar;
import sps.dev.data.SSpectralDataPacket;
import sps.platform.SpectralPlatService;

/**
 *
 * @author Administrator
 */
public class CollectControl {

    private AppManager appmanager;

    public CollectControl(AppManager appmanger) {
        this.appmanager = appmanger;
    }

    //连续采集
    public boolean StartSustainCollect(SSDataCollectPar par, int window, int inteveral) {
        return SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().StartSustainCollect(
                new ISPDataReceive() {
            @Override
            public void ReceiveData(SSpectralDataPacket data) throws Exception {
                appmanager.InputData(data);
            }
        }, par, window, inteveral);
    }

    //单次采集
    public boolean StartSingleTest(SSDataCollectPar par, int window) {
        return SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().StartSingleTest(
                new ISPDataReceive() {
            @Override
            public void ReceiveData(SSpectralDataPacket data) throws Exception {
                appmanager.InputData(data);
            }
        }, par, window);
    }

    //停止采集
    public void StopCollectData() {
        SpectralPlatService.GetInstance().GetDevManager().GetDevControl().GetDataCollecor().StopCollectData();
    }
}
