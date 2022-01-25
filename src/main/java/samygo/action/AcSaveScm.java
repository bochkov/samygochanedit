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
import samygo.util.Zip;

@Slf4j
@Component
public final class AcSaveScm extends AcOpenSave implements FilesListener {

    private final AppProps props;

    @Autowired
    private ChannelServResolve channels;
    @Autowired
    private JLabel statusLabel;

    public AcSaveScm(AppProps props) {
        super("&Save scm...");
        this.props = props;
        this.props.addFilesListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            actionPerformed();
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionPerformed() throws IOException {
        if (props.getScmFile() == null && props.getTempDir() == null) {
            throw new IOException("Nothing to save ... please open a file first!");
        }
        if (props.getChanFile() != null) {
            channels.service().writeTo(props.getChanFile());
            LOG.info("File {} saved.", props.getChanFile());
            statusLabel.setText("File " + props.getChanFile() + " saved.");
        }
        if (props.getScmFile() != null) {
            File scm = selectFileToSave("Save SCM as", FileFilters.SCM_FILTERS, props.getScmFile());
            if (scm != null) {
                props.setScmFile(scm);
                int totalFiles = Zip.compress(scm, props.getTempDir());
                if (totalFiles < 0)
                    throw new IOException("ScmFile " + props.getScmFile() + " not saved!");
                LOG.info("Scm file saved as: {}", props.getScmFile());
                statusLabel.setText("Scm file saved as: " + props.getScmFile());
            }
        }
    }

    @Override
    public void fileChanged() {
        setEnabled(props.getScmFile() != null);
    }
}
