package samygo.service.parsers;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.Mode;
import samygo.model.AirCableChannel;
import samygo.model.AirCableChannelC;
import samygo.model.AirCableChannelD;
import samygo.model.Channel;

/**
 * reads the file and loads the data into the memory
 * detects Cable Channel-list based on Frequency different zero
 */
@Component
public final class AirCableParser extends AbstractChannelParser {

    @Autowired
    private AppProps props;

    @Override
    public boolean canParse(File file) {
        return file.getName().endsWith("map-AirD") || file.getName().endsWith("map-CableD");
    }

    @Override
    public Mode mode() {
        return Mode.CABLE;
    }

    @Override
    public SortedMap<Integer, Channel> parse(File file) throws IOException {
        SortedMap<Integer, Channel> channels = new TreeMap<>();
        // read rawData
        // attention, byte data type is not unsigned, conversion must
        // be applied to negative values
        byte[] rawData = getFileContentsAsBytes(file);
        int row = 0;
        while (true) {
            AirCableChannel chan = switch (props.getScmVersion()) {
                case 'C' -> new AirCableChannelC();
                case 'D' -> new AirCableChannelD();
                default -> throw new IOException("Function not implemented for " + props.getScmVersion() + "-Series TV");
            };
            int num = chan.parse(row, rawData);
            if (num == 0)
                break;
            channels.put(num, chan);
            ++row;
        }
        return channels;
    }
}
