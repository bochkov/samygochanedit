package samygo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.util.Zip;

@Slf4j
@Component
public final class ScmExtractor {

    @Autowired
    private AppProps props;
    @Autowired
    private JLabel statusLabel;

    // SCM filename, e.g.: channel_list_UE40D8000_1101.scm
    private static final Pattern SCM_FILENAME = Pattern.compile(".*[A-Za-z][A-Za-z][0-9][0-9]+([BCD])[0-9][0-9][0-9][0-9].*.scm");
    private static final Pattern CLO_FILENAME = Pattern.compile(".*[A-Za-z]+ +[A-Za-z]+[0-9][0-9]+([BCD])[0-9]+.*");

    /**
     * extracts an SCM file into a tempDir that it selects and creates itself
     * @param file String variable that contains the path to the SCM file
     * @return number of files extracted or
     * <dd>-1 if SCM file format error</dd>
     */
    public int extractScm(File file) throws IOException {
        int totalFiles = Zip.decompress(file, props.getTempDir());
        if (totalFiles <= 0)
            throw new IOException("File empty or not in SCM Format!");

        Matcher matcher = SCM_FILENAME.matcher(file.getName());
        if (matcher.find()) {
            props.setScmVersion(matcher.group(1).charAt(0));
        }
        statusLabel.setText("Guess TV Version is: " + props.getScmVersion() + " based on filename " + file.getName());
        props.setScmFile(file);

        File d = props.getTempDir();
        File[] aList = d.listFiles();
        int i = 0;
        if (aList != null) {
            i = aList.length;
            // assure that we do not leave the files laying around
            for (File tmp : aList)
                tmp.deleteOnExit();
        }

        // extract TV Model from file system file Clone.Info content: UED UE65C6700
        File cloneFile = new File(props.getTempDir(), "CloneInfo");
        byte[] data = Files.readAllBytes(cloneFile.toPath());
        StringBuilder line = new StringBuilder();
        for (byte datum : data) {
            int c = datum;
            if (c == 0x00)
                c = 0x20; // build a long string over borders / 0x00 is the end delimiter
            if (c < 0)
                c += 256;
            line.append((char) c);
        }
        Matcher cloneMatcher = CLO_FILENAME.matcher(line);
        char version = cloneMatcher.find() ?
                cloneMatcher.group(1).charAt(0) :
                ' ';
        if (version == ' ') {
            LOG.info("No TV Version info found in file {}", cloneFile);
            statusLabel.setText("No TV Version info found in file " + cloneFile);
        } else {
            LOG.info("Read TV Version: {} from file {}", version, cloneFile);
            statusLabel.setText("Read TV Version: " + version + " from file " + cloneFile);
            props.setScmVersion(version);
        }
        return i;
    }
}
