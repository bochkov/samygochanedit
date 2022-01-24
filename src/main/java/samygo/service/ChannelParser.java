package samygo.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ModeKeep;
import samygo.model.Channel;
import samygo.service.parsers.MapChannelParser;

@Slf4j
@Component
public final class ChannelParser {

    @Autowired
    private ModeKeep modeKeep;
    @Autowired
    private Map<String, MapChannelParser> parsers;

    public SortedMap<Integer, Channel> parse(File file) throws IOException {
        LOG.info("start parsing file {}", file);
        for (MapChannelParser parser : parsers.values()) {
            if (parser.canParse(file)) {
                modeKeep.currentMode(parser.mode());
                return parser.parse(file);
            }
        }
        throw new IOException("Function not implemented for files with name: " + file);
    }
}
