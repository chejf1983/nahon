/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart.spchart.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * 浮动文字
 * @author jiche
 */
public class FloatTextBuilder {

    private final SpectralChart spectralChartPane;

    public FloatTextBuilder(SpectralChart dataChartPane) {
        this.spectralChartPane = dataChartPane;
    }

    private ArrayList<JLabel> lables = new ArrayList();

    //创建新的浮动字
    public JLabel CreateNewLable(String input) {
        JLabel FreeText = new JLabel();

        FreeText.setText(input);
        FreeText.setForeground(Color.red);
        FreeText.setFont(new Font(null, Font.BOLD, 20));
        this.lables.add(FreeText);
        this.spectralChartPane.dataChartPane.add(FreeText);
        this.spectralChartPane.dataChartPane.updateUI();
        return FreeText;
    }

    //移除浮动字
    public void RemoveLable(JLabel lable) {
        if (this.lables.remove(lable)) {
            this.spectralChartPane.dataChartPane.remove(lable);
            this.spectralChartPane.dataChartPane.updateUI();
        }
    }
}
