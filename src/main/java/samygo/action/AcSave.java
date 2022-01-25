package samygo.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.infra.FilesListener;

@Slf4j
@Component
public final class AcSave extends AbstractAction implements Command, FilesListener {

    private final AppProps props;

    @Autowired
    private ChannelServResolve channels;

    public AcSave(AppProps props) {
        super("Save");
        this.props = props;
        this.props.addFilesListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (props.getChanFile() != null) {
            try {
                channels.service().writeTo(props.getChanFile());
                LOG.info("File {} saved.", props.getChanFile());
            } catch (IOException ex) {
                LOG.warn(ex.getMessage());
            }
        }
    }

    @Override
    public void fileChanged() {
        this.setEnabled(props.getChanFile() != null);
    }
}
