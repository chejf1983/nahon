/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.bill;

import java.math.BigDecimal;
import sps.dev.data.SSPData;
import sps.dev.data.SSWaveCaculatePar;
import sps.dev.data.SSpectralDataPacket;

/**
 *
 * @author Administrator
 */
public class SPDataBuilder {
    // <editor-fold defaultstate="collapsed" desc="数据生成">  

    //生成光谱数据包
    public SSpectralDataPacket BuildSPData(double[] oradata, SSWaveCaculatePar wcpar) throws Exception {
        double IP = this.GetMaxAdValue(oradata);
            
        //检查波长系数
        if ((wcpar == null) || (wcpar.C0 == 0 && wcpar.C1 == 0 && wcpar.C2 == 0 && wcpar.C3 == 0)) {
            throw new Exception("没有设置波长系数，无法转换成光谱数据");
        }
        /* 计算波长系数 */
        SSPData ADValue = this.CalculateWaveIndex(oradata, wcpar);
        
        return new SSpectralDataPacket(
                ADValue,
                IP);
    }

    //计算IP最大值
    private double GetMaxAdValue(double[] data) {
        /* 获取最大AD值 */
        double IP = data[0];
        for (int i = 0; i < data.length; i++) {
            if (IP < data[i]) {
                IP = data[i];
            }
        }
        return IP;
    }

    //计算波长系数
    private SSPData CalculateWaveIndex(final double[] advalue, SSWaveCaculatePar wcpar) {
        double[] wave = new double[advalue.length];

        for (int i = 0; i < wave.length; i++) {
            wave[i] = (wcpar.C0 == Double.NaN ? 0 : wcpar.C0)
                    + (wcpar.C1 == Double.NaN ? 1 : wcpar.C1) * i
                    + (wcpar.C2 == Double.NaN ? 0 : wcpar.C2) * i * i
                    + (wcpar.C3 == Double.NaN ? 0 : wcpar.C3) * i * i * i;

            wave[i] = new BigDecimal(wave[i]).setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        //创建光谱数据包
        return new SSPData(wave, advalue);
    }    
    // </editor-fold> 
}
