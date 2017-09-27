/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.common.chartmath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author jiche
 */
public abstract class InitPaneHelper {

    public static void InitAppArea(JPanel Area, JComponent obj) {
        GroupLayout mainPanelLayout = new GroupLayout(Area);
        Area.removeAll();

        if (obj != null) {
            obj.setSize(Area.getSize());
            Area.add(obj);

            mainPanelLayout.setHorizontalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                    .addComponent(obj)));
            mainPanelLayout.setVerticalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                    .addComponent(obj)));
        }

        Area.setLayout(mainPanelLayout);
    }

    public static String FilePath = "";
    public static File GetFilePath(final String filend) {
        JFileChooser dialog = new JFileChooser(FilePath);
        dialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        dialog.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return filend;
            }

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                return f.getName().endsWith(filend);
            }
        });

        int result = dialog.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File tmp = dialog.getSelectedFile();
            FilePath = tmp.getAbsolutePath();
            if (tmp.getAbsolutePath().endsWith(filend)) {
                return tmp;
            } else {
                return new File(tmp.getAbsolutePath() + filend);
            }
        } else {
            return null;
        }
    }
    
    public static BufferedImage mergeImage(BufferedImage img1,
            BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

        // 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            DestImage = new BufferedImage(w1, h1 + h2,
                    BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }

        return DestImage;
    }
}
