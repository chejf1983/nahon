/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppCommon.SPDataChart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author jiche
 */
public class FreeTextPaint {

    private SpectralChart spectralChartPane;

    public FreeTextPaint(SpectralChart dataChartPane) {
        this.spectralChartPane = dataChartPane;
    }

    private ArrayList<JLabel> lables = new ArrayList();

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

    public void RemoveLable(JLabel lable) {
        if (this.lables.remove(lable)) {
            this.spectralChartPane.dataChartPane.remove(lable);
            this.spectralChartPane.dataChartPane.updateUI();
        }
    }
}
