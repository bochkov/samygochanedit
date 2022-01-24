package samygo.infra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString
public final class AppProps {

    private static final String VERSION = "v0.54cd";
    private static final String SERIES = "C and D-Series";

    private File tempDir = new File(System.getProperty("user.dir"), "SamyGoTemp");
    private File chanFile;
    private File scmFile;
    private char scmVersion = 'C';

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

    public String version() {
        return VERSION;
    }

    public String series() {
        return SERIES;
    }
}

