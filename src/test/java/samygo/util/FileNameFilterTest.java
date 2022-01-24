package samygo.util;

import java.io.File;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FileNameFilterTest {

    @Test
    void getDescription() {
        FileNameFilter filter = new FileNameFilter("hello world", Pattern.compile("msk"));
        Assertions.assertThat(filter.getDescription()).isEqualTo("hello world");
    }

    @Test
    void accept() {
        FileFilter filter = FileFilters.CHAN_FILTER;
        Assertions.assertThat(filter.accept(new File("map-CableD"))).isTrue();
        Assertions.assertThat(filter.accept(new File("map-AirD"))).isTrue();
    }
}