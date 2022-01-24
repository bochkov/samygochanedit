package samygo.service.channels;

import samygo.model.Channel;
import samygo.model.SatChannel;

public final class SatModel extends ChanTableModel0 {

    private static final String[] COL_NAMES = {
            "No.", "Name", "Sat", "TPID",
            "Onid", "Tsid", "Sid", "Pid", "Vpid",
            "Typ", "Fav79", "Lock"
    };

    @Override
    public int getRowCount() {
        return model.size();
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SatChannel ch = (SatChannel) model.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> ch.num;
            case 1 -> ch.name;
            case 2 -> ch.sat;
            case 3 -> ch.tpid;
            case 4 -> ch.onid;
            case 5 -> ch.tsid;
            case 6 -> ch.sid;
            case 7 -> ch.mpid;
            case 8 -> ch.vpid;
            case 9 -> ch.typ();
            case 10 -> ch.fav79();
            case 11 -> ch.lock == Channel.FLAG_LOCK;
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2, 3, 4, 5, 6, 7, 8 -> Integer.class;
            case 11 -> Boolean.class;
            default -> String.class;
        };
    }
}
