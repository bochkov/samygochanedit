package samygo.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

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
        }
    }

    private void actionPerformed() throws IOException {
        if (props.getScmFile() == null && props.getTempDir() == null) {
            LOG.info("Nothing to save ... please open a file first!");
            return;
        }
        if (props.getChanFile() != null) {
            channels.service().writeTo(props.getChanFile());
            LOG.info("File {} saved.", props.getChanFile());
        }
        if (props.getScmFile() != null) {
            File scm = selectFileToSave("Save SCM as", FileFilters.SCM_FILTERS, props.getScmFile());
            if (scm == null)
                return;
            props.setScmFile(scm);
            int totalFiles = Zip.compress(scm, props.getTempDir());
            if (totalFiles < 0)
                throw new IOException("scmFile " + props.getScmFile() + " not saved!");
            LOG.info("SCM-File saved as: {}", props.getScmFile());
        }
    }

    @Override
    public void fileChanged() {
        setEnabled(props.getScmFile() != null);
    }
}
