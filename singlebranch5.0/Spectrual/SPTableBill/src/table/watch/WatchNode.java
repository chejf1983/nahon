/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table.watch;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class WatchNode {  
    public WatchNode(double nm) {
        this.watchkey = nm;
    }

    // <editor-fold defaultstate="collapsed" desc="数据刷新">
    //当前节点，关键字
    private double watchkey;
    private double watchvalue;
    
    public double GetNmValue() {
        return this.watchkey;
    }

    public void UpdateCurrentValue(double value) {
        this.watchvalue = value;
    }

    public double GetCurrentValue() {
        return this.watchvalue;
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="历史数据刷新">
    private ArrayList<Double> history = new ArrayList();
    //组大历史记录条数
    public final int MaxLen = 1000;
    
    public void UpdateHistory(double value) {
        if (this.history.size() > MaxLen) {
            this.history.remove(0);
            this.history.add(value);
        }
    }

    public Double[] GetHistory() {
        return this.history.toArray(new Double[0]);
    }
    
    public void ClearHistory(){
        this.history.clear();
    }
    // </editor-fold>  

}
