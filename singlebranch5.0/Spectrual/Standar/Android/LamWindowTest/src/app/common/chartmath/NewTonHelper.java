/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.common.chartmath;

/**
 *
 * @author Administrator
 */
public class NewTonHelper {

    public static void Order(double[] array, double[] company) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    double tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                    
                    tmp = company[i];
                    company[i] = company[j];
                    company[j] = tmp;
                }
            }
        }
    }

    public static double NewInt(double[] index, double[] value, int valueNum, double newIndex, boolean needOrder) {
        if (needOrder) {
            double[] indextmp = new double[valueNum];
            System.arraycopy(index, 0, indextmp, 0, valueNum);
            double[] valuetmp = new double[valueNum];
            System.arraycopy(value, 0, valuetmp, 0, valueNum);

            Order(indextmp, valuetmp);
            return NewInt(indextmp, valuetmp, valueNum, newIndex);
        } else {
            return NewInt(index, value, valueNum, newIndex);
        }
    }

    public static double NewInt(double[] index, double[] value, int valueNum, double newIndex) {
        int smallindex = 0;
        int bigerindex = index.length - 1;

        if(index.length == 1){
            smallindex = bigerindex = 0;
        }else if (newIndex <= index[smallindex]) {
            /* small than first point */
            bigerindex = smallindex + 1;
        } else if (newIndex >= index[bigerindex]) {
            /* bigger than last point */
            smallindex = bigerindex - 1;
        } else {
            for (int i = 0; i < valueNum; i++) {
                int halfindex = (bigerindex + smallindex) / 2;
                if (newIndex > index[halfindex]) {
                    smallindex = halfindex;
                } else if (newIndex < index[halfindex]) {
                    bigerindex = halfindex;
                } else {
                    bigerindex = halfindex;
                    smallindex = halfindex - 1;
                }

                if (smallindex == bigerindex + 1) {
                    break;
                }
            }
        }

        double slope = 0;
        if((index[bigerindex] - index[smallindex]) != 0){
            slope = (newIndex - index[smallindex]) / (index[bigerindex] - index[smallindex]);
        }
        
        double result = value[smallindex] + slope * (value[bigerindex] - value[smallindex]);
        return result;
    }

    public static float NewInt(float[] index, float[] value, int valueNum, float newIndex) {
        double[] tmpindx = new double[valueNum];
        double[] tmpvalue = new double[valueNum];

        for (int i = 0; i < valueNum; i++) {
            tmpindx[i] = (double) index[i];
            tmpvalue[i] = (double) value[i];
        }

        return (float) NewInt(tmpindx, tmpvalue, valueNum, newIndex);
    }
}
