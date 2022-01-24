package samygo.service.writers;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import samygo.infra.Mode;
import samygo.model.Channel;

public interface MapChannelWriter {

    boolean canWrite(Mode mode);

    void write(SortedMap<Integer, Channel> channels, File file) throws IOException;

}
