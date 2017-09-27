/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.dev.control.bill;

import sps.dev.data.SSPData;
import sps.dev.data.SSLinearParameter;
import nahon.comm.filter.Newton;

/**
 *
 * @author Administrator
 */
public class LinearCal {
    // <editor-fold defaultstate="collapsed" desc="非线性校准">  
    //非线性校准

    public void LinearCalibrate(SSPData ADValue, SSLinearParameter linearpar) {
        if (linearpar == null) {
            return;
        }

        double[] indexarray;
        if (linearpar.SynType == SSLinearParameter.WaveLinearPar) {
            indexarray = ADValue.waveIndex;
        } else if (linearpar.SynType == SSLinearParameter.NodeLinearPar) {
            indexarray = ADValue.pixelIndex;
        } else {
            return;
        }

        //没有定标系数或者个数不正常，直接返回
        if (linearpar.NodeNumber <= 0) {
            return;
        }

        for (int i = 0; i < indexarray.length; i++) {
            int leftNodeIndex = 0;
            int rightNodeIndex = 0;

            if (linearpar.NodeNumber == 1) {
                /* if only have one node  */
                leftNodeIndex = 0;
                rightNodeIndex = 0;
            } else if (linearpar.NodeArray[0].NodeWave >= indexarray[i]) {
                leftNodeIndex = 0;
                rightNodeIndex = leftNodeIndex + 1;
            } else if (linearpar.NodeArray[linearpar.NodeNumber - 1].NodeWave <= indexarray[i]) {
                rightNodeIndex = linearpar.NodeNumber - 1;
                leftNodeIndex = rightNodeIndex - 1;
            } else {
                for (int j = 0; j < linearpar.NodeNumber; j++) {
                    if (linearpar.NodeArray[j].NodeWave <= indexarray[i]
                            && linearpar.NodeArray[j + 1].NodeWave > indexarray[i]) {
                        leftNodeIndex = j;
                        rightNodeIndex = j + 1;
                        break;
                    }
                }
            }

            double Ksmall = Newton.predict(
                    linearpar.NodeArray[leftNodeIndex].ADValueArray,
                    linearpar.NodeArray[leftNodeIndex].KParArray,
                    linearpar.NodeArray[leftNodeIndex].ADValueArray.length,
                    (float) ADValue.datavalue[i]);

            double Kbig = Newton.predict(
                    linearpar.NodeArray[rightNodeIndex].ADValueArray,
                    linearpar.NodeArray[rightNodeIndex].KParArray,
                    linearpar.NodeArray[rightNodeIndex].ADValueArray.length,
                    (float) ADValue.datavalue[i]);

            double Kpar = Newton.predict(
                    new float[]{linearpar.NodeArray[leftNodeIndex].NodeWave, linearpar.NodeArray[rightNodeIndex].NodeWave},
                    new float[]{(float) Ksmall, (float) Kbig},
                    2,
                    (float) indexarray[i]);

            ADValue.datavalue[i] *= Kpar;
            if (Double.isInfinite(ADValue.datavalue[i]) || Double.isNaN(ADValue.datavalue[i])) {
                ADValue.datavalue[i] = 0;
            }
        }
    }
    // </editor-fold> 
}
