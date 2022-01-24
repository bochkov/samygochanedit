package samygo.service.parsers;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.Mode;
import samygo.infra.ModeKeep;
import samygo.infra.RawKeep;
import samygo.model.Channel;
import samygo.model.CloneChannel;

@Slf4j
@Component
public final class CloneParser extends AbstractChannelParser {

    @Autowired
    private ModeKeep modeKeep;
    @Autowired
    private RawKeep rawKeep;

    @Override
    public boolean canParse(File file) {
        return file.getName().endsWith("clon.bin");
    }

    @Override
    public Mode mode() {
        return Mode.CLONE;
    }

    @Override
    public SortedMap<Integer, Channel> parse(File file) throws IOException {
        byte[] rawData = getFileContentsAsBytes(file);

        modeKeep.currentMode(Mode.CLONE);
        rawKeep.set(rawData);

        // only read as many lines, as tv says are valid
        int size = convertEndianess(rawData[0x169f2], rawData[0x169f1]);

        SortedMap<Integer, Channel> channels = new TreeMap<>();
        for (int i = 0; i < size; i++) {
            int offset = 0x1342 + i * 81;
            /* empty line or inactive, skip to next */
            LOG.debug("{}", rawData[offset + 73]);
            if ((rawData[offset + 73] & CloneChannel.FLAG_INACTIVE) == CloneChannel.FLAG_INACTIVE)
                continue;

            CloneChannel chan = new CloneChannel();
            System.arraycopy(rawData, offset, chan.rawData, 0, 81);

            /* read channel name (max. 50 chars)
             * only reads a byte, has to be rewritten if
             * the channel name is actually unicode utf8
             */
            StringBuilder name = new StringBuilder();
            for (int j = 0; j < 50; j++) {
                int c = rawData[offset + j];
                if (c == 0x00) break;
                if (c < 0) c += 256;
                name.append((char) c);
            }
            chan.name = name.toString();
            chan.num = convertEndianess(rawData[offset + 51], rawData[offset + 50]);
            chan.vpid = convertEndianess(rawData[offset + 53], rawData[offset + 52]);
            chan.mpid = convertEndianess(rawData[offset + 55], rawData[offset + 54]);
            chan.freq = rawData[offset + 56];
            chan.fav = rawData[offset + 57];
            chan.nid = convertEndianess(rawData[offset + 60], rawData[offset + 59]);
            chan.tsid = convertEndianess(rawData[offset + 62], rawData[offset + 61]);
            chan.onid = convertEndianess(rawData[offset + 64], rawData[offset + 63]);
            chan.sid = convertEndianess(rawData[offset + 66], rawData[offset + 65]);
            chan.stype = rawData[offset + 71];
            chan.enc = rawData[offset + 73];

            /* store channel in TreeMap */
            channels.put(chan.num, chan);
        }
        return channels;
    }
}
