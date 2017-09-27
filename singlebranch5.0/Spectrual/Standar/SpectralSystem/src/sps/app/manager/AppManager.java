/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.app.manager;

import sps.app.absorbe.AbsorbeApp;
import sps.app.ora.CommonApp;
import sps.app.reflect.ReflectApp;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class AppManager {
    public void InputData(SSpectralDataPacket spdata){
        switch(this.current_type){
            case COMM:
                this.comm_app.InputData(spdata);
                break;
            case ABSORBE:
                this.abs_app.InputData(spdata);
                break;
            case REFLECT:
                this.ref_app.InputData(spdata);
                break;
            default:
                return;          
        }
    }
    
    private CollectControl collectcontrol = new CollectControl(this);
    public CollectControl GetCollectControl(){
        return this.collectcontrol;
    }
    
    // <editor-fold defaultstate="collapsed" desc="app切换">     
    public enum AppType{
        COMM,
        ABSORBE,
        REFLECT
    }
    
    private AppType current_type = AppType.COMM;
    public AppType CurrentType(){
        return this.current_type;
    }
    
    public void SwitchApp(AppType type){
        this.current_type = type;
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="app列表">     
    private CommonApp comm_app = new CommonApp();
    public CommonApp GetCommonApp(){
        return this.comm_app;
    }
    
    private AbsorbeApp abs_app = new AbsorbeApp();
    public AbsorbeApp GetAbsorbeApp(){
        return this.abs_app;
    }
    
    private ReflectApp ref_app = new ReflectApp();
    public ReflectApp GetReflectApp(){
        return this.ref_app;
    }
    // </editor-fold> 
}
