package samygo.service.channels;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.SortedMap;

import samygo.model.Channel;

public interface ChannelService {

    int size();

    Channel selectedChannel();

    Channel[] selectedChannels();

    void refresh();

    void writeTo(File file) throws IOException;

    boolean exists(Channel ch);

    void add(Channel ch);

    void remove(Channel ch);

    SortedMap<Integer, Channel> removeAfter(Channel chan);

    Channel get(int key);

    void removeSelected();

    void replaceWith(Collection<Channel> channels);

    ChanTableModel model();

    void move(Channel[] channels, int target);

    void moveSelected(int target);

}
