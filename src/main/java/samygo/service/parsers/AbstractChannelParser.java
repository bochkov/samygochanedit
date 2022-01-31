package samygo.service.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractChannelParser implements MapChannelParser {

    /**
     * Endianess must be converted as Samsung and Java VM don't share the same endianess
     */
    protected int convertEndianess(byte b, byte c) {
        int lower = b;
        int upper = c;
        if (b < 0)
            lower += 256;
        if (c < 0)
            upper += 256;
        return lower + (upper << 8);
    }

    /** read bytes, so we are binary safe */
    protected byte[] getFileContentsAsBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
