package samygo.service.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.Mode;
import samygo.model.AirCableChannel;
import samygo.model.AirCableChannelC;
import samygo.model.AirCableChannelD;
import samygo.model.Channel;

@Slf4j
@Component
@RequiredArgsConstructor
public final class AirCableWriter implements MapChannelWriter {

    private final AppProps props;

    @Override
    public boolean canWrite(Mode mode) {
        return mode == Mode.AIR || mode == Mode.CABLE;
    }

    @Override
    public void write(SortedMap<Integer, Channel> channels, File file) throws IOException {
        /* build byte array to write out */
        int recordLen = switch (props.getScmVersion()) {
            case 'C' -> AirCableChannelC.L_CHAN;
            case 'D' -> AirCableChannelD.L_CHAN;
            default -> throw new IOException("Function not implemented for " + props.getScmVersion() + "-Series TV");
        };

        int entries = 0;
        try (OutputStream out = new FileOutputStream(file)) {
            for (Channel channel : channels.values()) {
                AirCableChannel chan = (AirCableChannel) channel;
                out.write(chan.writeData(), 0, recordLen);
                ++entries;
            }
            // TODO test if fill up to 1000 records is still needed
            byte[] rawData = new byte[recordLen];
            while (entries < 999) {
                out.write(rawData, 0, recordLen); // write data into the file
                entries++;
            }
        }
        LOG.info("Channel list written to file: {}", file);
    }
}
