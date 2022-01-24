package samygo.ui;

import javax.swing.table.TableRowSorter;

import samygo.service.channels.ChanTableModel;

public final class ChanRowSorter<M extends ChanTableModel> extends TableRowSorter<M> {
    public ChanRowSorter(M model) {
        super(model);
    }
}
