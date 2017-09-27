/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.form.other;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author jiche
 */
public abstract class InitPaneHelper {
//    public static void InitAppArea(JPanel Area, JComponent obj) {
//        GroupLayout mainPanelLayout = new GroupLayout(Area);
//        Area.removeAll();
//
//        if (obj != null) {
//            obj.setSize(Area.getSize());
//            Area.add(obj);
//
//            mainPanelLayout.setHorizontalGroup(
//                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addGroup(mainPanelLayout.createSequentialGroup()
//                    .addComponent(obj)));
//            mainPanelLayout.setVerticalGroup(
//                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addGroup(mainPanelLayout.createSequentialGroup()
//                    .addComponent(obj)));
//        }
//
//        Area.setLayout(mainPanelLayout);
//    }    
    
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
            FilePath = tmp.getPath();
            if (tmp.getAbsolutePath().contains(".")) {
                return tmp;
            } else {
                return new File(tmp.getAbsolutePath() + filend);
            }
        } else {
            return null;
        }
    }
}
