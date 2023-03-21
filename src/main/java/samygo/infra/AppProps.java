package samygo.infra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString
public final class AppProps {

    private static final String VERSION = "v0.6";
    private static final String SERIES = "C and D-Series";

    @ToString.Exclude
    private final List<FilesListener> filesListeners = new ArrayList<>();

    private File tempDir;
    private File chanFile;
    private File scmFile;
    private char scmVersion = 'D';

    private File currDir = new File(System.getProperty("user.dir"));

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(tempDir.toPath());
            LOG.info("created dir {}", tempDir);
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    public void addFilesListener(FilesListener listener) {
        this.filesListeners.add(listener);
        listener.fileChanged();
    }

    public String version() {
        return VERSION;
    }

    public String series() {
        return SERIES;
    }

    public void setChanFile(File file) {
        this.chanFile = file;
        notifyListeners();
    }

    public void setScmFile(File file) {
        this.scmFile = file;
        notifyListeners();
    }

    private void notifyListeners() {
        for (FilesListener listen : filesListeners) {
            listen.fileChanged();
        }
    }
}

