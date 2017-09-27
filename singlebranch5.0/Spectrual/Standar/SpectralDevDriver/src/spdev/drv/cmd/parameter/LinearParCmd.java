/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spdev.drv.cmd.parameter;

import java.util.concurrent.TimeUnit;
import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.entry.MIGPNahonDriver;
import spdev.drv.data.SLinearParameter;
import static spdev.drv.data.SLinearParameter.NodeLinearPar;
import static spdev.drv.data.SLinearParameter.NonLinearPar;
import static spdev.drv.data.SLinearParameter.WaveLinearPar;
import static spdev.drv.data.MIGPSPMEM.LinearPar;
import static spdev.drv.data.MIGPSPMEM.LinearType;

/**
 *
 * @author Administrator
 */
public class LinearParCmd {

    // <editor-fold defaultstate="collapsed" desc="非线性系数"> 
    public SLinearParameter GetLinearParameter(MIGPNahonDriver drv) throws Exception {

        SLinearParameter linearpar;
        /* 获取非线性参数 */
        int SynType = this.GetSynType(drv, 1000);
        TimeUnit.MILLISECONDS.sleep(10);
        if (SynType != NodeLinearPar && SynType != WaveLinearPar) {
            linearpar = new SLinearParameter(0);
        } else {
            linearpar = this.GetSynParameter(drv, 1000);
            TimeUnit.MILLISECONDS.sleep(10);
        }
        linearpar.SynType = SynType;
        return linearpar;

    }

    public void SetLinearParameter(MIGPNahonDriver drv, SLinearParameter par) throws Exception {

        if (par.SynType != NonLinearPar) {
            //给每个节点排序
            for (SLinearParameter.LinearParNode node : par.NodeArray) {
                node.SortNode();
            }
            //设置非线性类型
            this.SetSynType(drv, par.SynType, 1000);
            //下发非线性数据
            this.SetSynParameter(drv, par, 1000);
        } else {
            this.SetSynType(drv, par.SynType, 1000);
        }

    }

    public int GetSynType(MIGPNahonDriver drv, int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = drv.GetMEM(LinearType.getMEM, LinearType.addr, LinearType.length, timeout);
        return (int) tmpdata[0];
    }

    public void SetSynType(MIGPNahonDriver drv, int type, int timeout) throws Exception {
//        int timeout = 100;
        drv.SetMEM(LinearType.setMEM, LinearType.addr, LinearType.length, new byte[]{(byte) type}, timeout);
    }

    public SLinearParameter GetSynParameter(MIGPNahonDriver drv, int timeout) throws Exception {
//        int timeout = 100;
        byte[] tmpdata = drv.GetMEM(LinearPar.getMEM, LinearPar.addr, LinearPar.length, timeout);

        int NodeNumber = (int) NahonConvert.ByteArrayToFloat(tmpdata, 0);

        if (NodeNumber > 0) {
            SLinearParameter tmp = new SLinearParameter(NodeNumber);
            int tmpIndex = 4;

            for (int modeIndex = 0; modeIndex < NodeNumber; modeIndex++) {
                float wave = NahonConvert.ByteArrayToFloat(tmpdata, tmpIndex);
                tmpIndex += 4;
                int parNum = (int) NahonConvert.ByteArrayToFloat(tmpdata, tmpIndex);
                tmpIndex += 4;

                float[] tmp_adValue_array = new float[parNum];
                float[] tmp_kpar_array = new float[parNum];

                for (int adIndex = 0; adIndex < parNum; adIndex++) {
                    tmp_adValue_array[adIndex] = NahonConvert.ByteArrayToFloat(tmpdata, tmpIndex);
                    tmpIndex += 4;
                    tmp_kpar_array[adIndex] = NahonConvert.ByteArrayToFloat(tmpdata, tmpIndex);
                    tmpIndex += 4;
                }
                tmp.NodeArray[modeIndex] = tmp.new LinearParNode(wave, parNum, tmp_adValue_array, tmp_kpar_array);
            }
            return tmp;
        } else {
            return new SLinearParameter(0);
        }
    }

    public void SetSynParameter(MIGPNahonDriver drv, SLinearParameter par, int timeout) throws Exception {
//        int timeout = 100;

        byte[] tmpdata = new byte[LinearPar.length];

        int NodeNumber = par.NodeNumber;

        System.arraycopy(NahonConvert.FloatToByteArray(NodeNumber), 0, tmpdata, 0, 4);

        int tmpIndex = 4;

        for (int modeIndex = 0; modeIndex < NodeNumber; modeIndex++) {
            int tmpstart = tmpIndex;
            System.arraycopy(NahonConvert.FloatToByteArray(par.NodeArray[modeIndex].NodeWave),
                    0, tmpdata, tmpIndex, 4);
            tmpIndex += 4;
            System.arraycopy(NahonConvert.FloatToByteArray(par.NodeArray[modeIndex].NodeKNumber),
                    0, tmpdata, tmpIndex, 4);
            tmpIndex += 4;

            int parNum = par.NodeArray[modeIndex].NodeKNumber;
            for (int adIndex = 0; adIndex < parNum; adIndex++) {
                System.arraycopy(NahonConvert.FloatToByteArray(par.NodeArray[modeIndex].ADValueArray[adIndex]),
                        0, tmpdata, tmpIndex, 4);
                tmpIndex += 4;
                System.arraycopy(NahonConvert.FloatToByteArray(par.NodeArray[modeIndex].KParArray[adIndex]),
                        0, tmpdata, tmpIndex, 4);
                tmpIndex += 4;
            }
            if (tmpIndex > tmpdata.length) {
                tmpIndex = tmpstart;
                System.arraycopy(NahonConvert.FloatToByteArray(modeIndex), 0, tmpdata, 0, 4);
                break;
            }
        }

        drv.SetMEM(LinearPar.setMEM, LinearPar.addr, tmpIndex, tmpdata, timeout);
    }
    // </editor-fold>
}
