package samygo.service.channels;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import samygo.model.Channel;
import samygo.service.ChannelCreator;
import samygo.service.ChannelWriter;

@Slf4j
public abstract class TreeChannels implements ChannelService {

    @Autowired
    private JTable table;
    @Autowired
    private ChannelWriter writer;
    @Autowired
    private ChannelCreator channelCreator;

    protected final SortedMap<Integer, Channel> channels = new TreeMap<>();

    @Override
    public Channel selectedChannel() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        return row < 0 ?
                null :
                model().get(row);
    }

    @Override
    public Channel[] selectedChannels() {
        int[] rows = table.getSelectedRows();
        Channel[] chans = new Channel[rows.length];
        for (int i = 0; i < rows.length; ++i) {
            int idx = table.convertRowIndexToModel(rows[i]);
            chans[i] = model().get(idx);
        }
        return chans;
    }

    @Override
    public int size() {
        return channels.values().size();
    }

    @Override
    public void writeTo(File file) throws IOException {
        writer.write(channels, file);
    }

    @Override
    public SortedMap<Integer, Channel> removeAfter(Channel chan) {
        SortedMap<Integer, Channel> tailMap = new TreeMap<>(channels.tailMap(chan.num));
        for (Channel channel : tailMap.values()) {
            channels.remove(channel.num);
        }
        model().setAll(channels.values());
        model().applyTo(table);
        return tailMap;
    }

    @Override
    public boolean exists(Channel ch) {
        return channels.containsKey(ch.num);
    }

    @Override
    public Channel get(int key) {
        return channels.get(key);
    }

    @Override
    public synchronized void removeSelected() {
        Channel[] selected = selectedChannels();
        int[] selectedRows = table.getSelectedRows();
        LOG.debug("removed = {}", Arrays.toString(selected));
        for (Channel ch : selected) {
            channels.remove(ch.num);
            int j = ch.num + 1;
            Channel c;
            while ((c = channels.get(j)) != null) {
                channels.remove(j);
                c.num -= 1;
                channels.put(c.num, c);
                ++j;
            }
        }
        model().setAll(channels.values());
        model().applyTo(table);
        if (!channels.isEmpty()) {
            for (int row : selectedRows) {
                table.getSelectionModel().addSelectionInterval(row, row);
            }
        }
    }

    @Override
    public void add(Channel ch) {
        channels.put(ch.num, ch);
        model().setAll(channels.values());
        model().applyTo(table);
    }

    @Override
    public void remove(Channel ch) {
        channels.remove(ch.num);
        model().setAll(channels.values());
        model().applyTo(table);
    }

    @Override
    public void refresh() {
        int[] selected = table.getSelectedRows();
        table.getSelectionModel().clearSelection();
        model().setAll(channels.values());
        model().applyTo(table);
        if (!channels.isEmpty()) {
            for (int row : selected) {
                table.getSelectionModel().addSelectionInterval(row, row);
            }
        }
    }

    @Override
    public void replaceWith(Collection<Channel> channels) {
        this.channels.clear();
        for (Channel ch : channels) {
            this.channels.put(ch.num, ch);
        }
        model().setAll(this.channels.values());
        model().applyTo(table);
    }

    @Override
    public void moveSelected(int targetNumber) {
        Channel[] selected = selectedChannels();
        move(selected, targetNumber);
    }

    @Override
    public void move(Channel[] channels, int targetNumber) {
        Channel targetChan = get(targetNumber);
        if (targetChan != null) {
            move(channels, targetChan);
        } else {
            Channel dummy = channelCreator.create();
            dummy.name = "DUMMY";
            dummy.num = targetNumber;
            add(dummy);
            move(channels, dummy);
            remove(dummy);
        }
    }

    private void move(Channel[] channels, Channel after) {
        int cIndex = after.num;
        // first delete all the channels to be moved from list
        for (Channel select : channels)
            remove(select);

        // then delete all channels after targetChan.num from list
        SortedMap<Integer, Channel> removed = removeAfter(after);

        // now read them at targetChannel.num, targetChannel might have moved up
        // read everything with new channel number
        for (Channel channel : channels) {
            channel.num = cIndex;
            add(channel);
            cIndex++;
        }

        // after that all other channels, only renumbering if we have to (mind the gap!)
        for (Channel ch : removed.values()) {
            if (exists(ch)) { // channel number already used, give it a new one
                ch.num = cIndex;
                add(ch);
                cIndex++;
            } else {
                add(ch);  // we hit a gap, just keep the numbers from now on
            }
        }
    }
}
