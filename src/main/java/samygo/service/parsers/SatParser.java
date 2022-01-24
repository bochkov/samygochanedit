package samygo.service.parsers;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

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
public final class SatParser extends AbstractChannelParser {

    @Autowired
    private AppProps props;

    @Override
    public boolean canParse(File file) {
        return file.getName().endsWith("map-SateD");
    }

    @Override
    public Mode mode() {
        return Mode.SAT;
    }

    @Override
    public SortedMap<Integer, Channel> parse(File file) throws IOException {
        if (props.getScmVersion() != 'C' && props.getScmVersion() != 'D') {
            throw new IOException("Function not implemented for " + props.getScmVersion() + "-Series file: " + file);
        }
        SortedMap<Integer, Channel> channels = new TreeMap<>();
        byte[] rawData = getFileContentsAsBytes(file);
        int row = 0;
        while (true) {
            SatChannel chan = switch (props.getScmVersion()) {
                case 'C' -> new SatChannelC();
                case 'D' -> new SatChannelD();
                default -> throw new IOException("Function not implemented for " + props.getScmVersion() + "-Series TV");
            };
            int num = chan.parse(row, rawData);
            if (num == 0)
                break;
            channels.put(chan.num, chan);
            ++row;
        }
        return channels;
    }
}
