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
import samygo.service.ChannelWriter;

@Slf4j
public abstract class TreeChannels implements ChannelService {

    @Autowired
    private JTable table;
    @Autowired
    private ChannelWriter writer;

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
    public SortedMap<Integer, Channel> removeAfter(int idx) {
        SortedMap<Integer, Channel> tailMap = new TreeMap<>(channels.tailMap(idx));
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
}
