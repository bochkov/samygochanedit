package samygo.action;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public abstract class AcOpenSave extends AbstractAction implements Command {

    protected AcOpenSave(String text) {
        super(text);
    }

    /**
     * File Dialog to select a file for reading
     * @param message displayed for selection
     * @param filters file selection filters
     * @param dir     directory to look into
     * @return String  - the filename selected or
     * <dd>null - if unsuccessful</dd>
     */
    protected File selectFileToOpen(String message, FileFilter[] filters, File dir) {
        JFileChooser chooser = new JFileChooser(dir);
        for (FileFilter filter : filters)
            chooser.addChoosableFileFilter(filter);
        chooser.setDialogTitle(message);
        int res = chooser.showOpenDialog(null);
        return res == JFileChooser.APPROVE_OPTION ?
                chooser.getSelectedFile() :
                null;
    }

    protected String extensionOf(File file) {
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            return fileName.substring(i + 1).toLowerCase();
        }
        return "";
    }

    /**
     * File Dialog to select a file for writing
     * @param message displayed for selection
     * @param filters  file selection filters
     * @param dir     directory to look into
     * @return String  - the filename selected or
     * <dd>null - if unsuccessful</dd>
     */
    protected File selectFileToSave(String message, FileFilter[] filters, File dir) {
        JFileChooser chooser = new JFileChooser(dir);
        for (FileFilter filter : filters)
            chooser.addChoosableFileFilter(filter);
        chooser.setDialogTitle(message);
        int res = chooser.showSaveDialog(null);
        return res == JFileChooser.APPROVE_OPTION ?
                chooser.getSelectedFile() :
                null;
    }

}
