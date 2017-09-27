/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.filter;

/**
 *
 * @author Administrator
 */
public class SingleCompare {
    public static boolean Compara(double[] std, double[] other){
        if(std.length != other.length){
            return false;
        }
        
        double A = 0;
        double B = 0;
        double C = 0;
        for(int i = 0; i < std.length; i++){
            A += std[i] * std[i];
            B += other[i] * other[i];
            C += std[i] * other[i];
        }
        
        double csimilar = (C) /Math.sqrt(A * B);
        double cpecent = Math.abs(1 - A / B);
        
        //一般相似度，csimilar > 0.9999, 表示相似， cpecent < 0.05(5%),表示面积基本相同
        
        return csimilar > 0.9999 && cpecent < 0.05;
    }
}
