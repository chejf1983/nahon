package sps.platform;

import sps.app.solapp.CollectControl;
import sps.app.solapp.SoildApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class SoildSystem {

    private static SoildSystem instance = new SoildSystem();
    
    public static SoildSystem GetInstance(){
        return instance;
    }
    
    private CollectControl soilapp = new CollectControl();

    public CollectControl GetSoilControl() {
        return this.soilapp;
    }
}
