package samygo.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.infra.FilesListener;
import samygo.util.FileFilters;

@Slf4j
@Component
public final class AcSaveAs extends AcOpenSave implements FilesListener {

    private final AppProps props;

    @Autowired
    private ChannelServResolve channels;
    @Autowired
    private JLabel statusLabel;

    public AcSaveAs(AppProps props) {
        super("Save As...");
        this.props = props;
        this.props.addFilesListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (props.getChanFile() != null) {
            File file = selectFileToSave("Save File as", FileFilters.ALL_FILTERS, props.getTempDir());
            if (file != null) {
                try {
                    channels.service().writeTo(file);
                    LOG.info("File saved as: {}", file);
                    statusLabel.setText("File saved as: " + file);
                } catch (IOException ex) {
                    LOG.warn(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void fileChanged() {
        setEnabled(props.getChanFile() != null);
    }
}
