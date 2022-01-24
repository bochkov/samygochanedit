package samygo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ModeKeep;
import samygo.model.Channel;
import samygo.service.writers.MapChannelWriter;

@Component
public final class ChannelWriter {

    @Autowired
    private ModeKeep modeKeep;
    @Autowired
    private List<MapChannelWriter> writers;

    public void write(SortedMap<Integer, Channel> channels, File file) throws IOException {
        for (MapChannelWriter writer : writers) {
            if (writer.canWrite(modeKeep.currentMode())) {
                writer.write(channels, file);
                break;
            }
        }
    }
}
