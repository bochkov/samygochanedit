package samygo.service.channels;

import java.util.Collection;
import javax.swing.*;
import javax.swing.table.TableModel;

import samygo.model.Channel;

public interface ChanTableModel extends TableModel {

    void applyTo(JTable table);

    void add(Channel ch);

    Channel get(int idx);

    void setAll(Collection<Channel> channels);

    Collection<Channel> values();

}
