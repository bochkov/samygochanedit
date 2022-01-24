package samygo.util;

import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class FileFilters {

    public static final FileFilter CHAN_FILTER = new FileNameFilter("map files (*map-*D)", Pattern.compile("^.*?map-.+?D$"));
    public static final FileFilter SCM_FILTER = new FileNameExtensionFilter("SCM files (*.scm)", "scm");

    public static final FileFilter[] ALL_FILTERS = {
            FileFilters.CHAN_FILTER, FileFilters.SCM_FILTER
    };
    public static final FileFilter[] SCM_FILTERS = {
            FileFilters.SCM_FILTER
    };
    public static final FileFilter[] MAP_FILTERS = {
            FileFilters.CHAN_FILTER
    };

    private FileFilters() {
    }
}
