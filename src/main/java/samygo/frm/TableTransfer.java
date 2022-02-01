package samygo.frm;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.channels.ChanTableModel;

@Slf4j
@Component
public final class TableTransfer extends TransferHandler {

    private static final DataFlavor CHANNEL_FLAVOR = new DataFlavor(Channel.class, "application/x-java-samychan");

    @Autowired
    private ChannelServResolve servResolve;

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{CHANNEL_FLAVOR};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return CHANNEL_FLAVOR.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) {
                return servResolve.service().selectedChannels();
            }
        };
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop()
                && support.isDataFlavorSupported(CHANNEL_FLAVOR);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support))
            return false;

        Channel[] data;
        try {
            data = (Channel[]) support.getTransferable().getTransferData(CHANNEL_FLAVOR);
        } catch (Exception ex) {
            return false;
        }

        JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
        int targetRow = dl.getRow();

        JTable tbl = (JTable) support.getComponent();
        int rowIndexToModel = tbl.convertRowIndexToModel(targetRow);
        Channel targetAfter = ((ChanTableModel) tbl.getModel()).get(rowIndexToModel);
        servResolve.service().move(data, targetAfter.num);
        return true;
    }
}
