package samygo.service.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.Mode;
import samygo.model.Channel;
import samygo.model.SatChannel;
import samygo.model.SatChannelC;
import samygo.model.SatChannelD;

@Slf4j
@Component
public final class SatWriter implements MapChannelWriter {

    @Autowired
    private AppProps props;

    @Override
    public boolean canWrite(Mode mode) {
        return mode == Mode.SAT;
    }

    @Override
    public void write(SortedMap<Integer, Channel> channels, File file) throws IOException {
        int recordLen = switch (props.getScmVersion()) {
            case 'C' -> SatChannelC.L_CHAN;
            case 'D' -> SatChannelD.L_CHAN;
            default -> throw new IOException("Function not implemented for " + props.getScmVersion() + "-Series TV");
        };
        try (OutputStream out = new FileOutputStream(file)) {
            for (Channel channel : channels.values()) {
                SatChannel chan = (SatChannel) channel;
                out.write(chan.writeData(), 0, recordLen);
            }
        }
        // todo status update
        LOG.info("Channel list written to file: {}", file);
    }
}
