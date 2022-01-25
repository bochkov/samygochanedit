package samygo.service.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import samygo.model.Channel;
import samygo.ui.ChanRowSorter;

public abstract class ChanTableModel0 extends AbstractTableModel implements ChanTableModel {

    protected final List<Channel> model = new ArrayList<>();

    @Override
    public void applyTo(JTable table) {
        table.setModel(this);
        table.getColumnModel().getColumn(1).setMinWidth(200); // name column
        ChanRowSorter<ChanTableModel> rowSorter = new ChanRowSorter<>(this);
        table.setRowSorter(rowSorter);
        fireTableDataChanged();
    }

    @Override
    public void add(Channel t) {
        model.add(t);
        fireTableDataChanged();
    }

    @Override
    public void setAll(Collection<Channel> channels) {
        model.clear();
        model.addAll(channels);
        fireTableDataChanged();
    }

    @Override
    public Channel get(int idx) {
        return model.get(idx);
    }

    @Override
    public Collection<Channel> values() {
        return model;
    }
}
