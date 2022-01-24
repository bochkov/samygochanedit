package samygo.service.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.SortedMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.Mode;
import samygo.infra.RawKeep;
import samygo.model.Channel;
import samygo.model.CloneChannel;

@Slf4j
@Component
public final class CloneWriter implements MapChannelWriter {

    @Autowired
    private RawKeep rawKeep;

    @Override
    public boolean canWrite(Mode mode) {
        return mode == Mode.CLONE;
    }

    @Override
    public void write(SortedMap<Integer, Channel> channels, File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(rawKeep.get(), 0, 0x1342); // write bytes 0 - 0x1341 out, nothing has changed there

            /* build byte array to write out, stop at 999 channels */
            int entries = 0;

            Iterator<Channel> it = channels.values().iterator();
            while (it.hasNext() && entries < 999) {
                CloneChannel chan = (CloneChannel) it.next();
                byte[] rawData = chan.rawData;

                char[] name = chan.name.toCharArray();
                int n = 0;
                for (; n < name.length && n < 50; n++) {
                    rawData[n] = (byte) name[n];
                }
                rawData[75] = (byte) n;
                for (; n < 50; n++) {
                    rawData[n] = (byte) 0x00;
                }

                revertClone(rawData, 50, chan.num);
                revertClone(rawData, 52, chan.vpid);
                revertClone(rawData, 54, chan.mpid);
                rawData[56] = (byte) chan.freq;
                rawData[57] = chan.fav;
                revertClone(rawData, 59, chan.nid);
                revertClone(rawData, 61, chan.tsid);
                revertClone(rawData, 63, chan.onid);
                revertClone(rawData, 65, chan.sid);
                rawData[71] = chan.stype;
                rawData[73] = chan.enc;

                out.write(rawData);
                entries++;
            }
            LOG.info("total entries = {}", entries);
            revertClone(rawKeep.get(), 0x169ee, entries);
            revertClone(rawKeep.get(), 0x169f1, entries);

            // fill with 0xFF until we reach 999 channels
            byte[] rawData = new byte[81];
            for (int i = 0; i < 81; i++)
                rawData[i] = (byte) 0xFF;
            while (entries < 999) {
                out.write(rawData);
                entries++;
            }

            // write bytes 0x14F59 - 0x1C390 out, nothing has changed there
            out.write(rawKeep.get(), 0x14F59, 0x74A7);
        }
        // todo status update
        LOG.info("Channel list written to file: {}", file);
    }

    private void revertClone(byte[] b, int offset, int data) {
        b[offset + 1] = (byte) data;
        b[offset] = (byte) (data >> 8);
    }
}
