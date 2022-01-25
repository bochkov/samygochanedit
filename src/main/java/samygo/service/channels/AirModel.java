package samygo.service.channels;

import samygo.model.AirCableChannel;
import samygo.model.Channel;

public final class AirModel extends ChanTableModel0 {

    private static final String[] COL_NAMES = {
            "No.", "Name", "Freq", "LCN",
            "Onid", "Tsid", "Sid", "Pid", "Vpid",
            "Typ", "Fav", "Fav79", "Enc", "Lock"
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
        AirCableChannel ch = (AirCableChannel) model.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> ch.num;
            case 1 -> ch.name;
            case 2 -> ch.freq;
            case 3 -> ch.lcn;
            case 4 -> ch.onid;
            case 5 -> ch.tsid;
            case 6 -> ch.sid;
            case 7 -> ch.mpid;
            case 8 -> ch.vpid;
            case 9 -> ch.typ();
            case 10 -> (ch.fav & Channel.FLAG_FAV_1) != 0;
            case 11 -> ch.fav79();
            case 12 -> (ch.enc & Channel.FLAG_SCRAMBLED) != 0;
            case 13 -> (ch.lock & Channel.FLAG_LOCK) != 0;
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
            case 10, 12, 13 -> Boolean.class;
            default -> String.class;
        };
    }
}