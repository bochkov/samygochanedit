package samygo.service.parsers;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import samygo.infra.Mode;
import samygo.model.Channel;

public interface MapChannelParser {

    /**
     * detects if parser can parse a file
     */
    boolean canParse(File file);

    /**
     * tells what mode be setted before parse
     */
    Mode mode();

    SortedMap<Integer, Channel> parse(File file) throws IOException;

}
