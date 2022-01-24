package samygo.util;

import java.io.File;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FileNameFilter extends FileFilter {

    private final String description;
    private final Pattern mask;

    public FileNameFilter(String description, Pattern mask) {
        this.description = description;
        this.mask = mask;
    }

    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            return mask.matcher(f.getName()).matches();
        }
        return false;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
