package samygo.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;

@Slf4j
@Component
public final class AcSave extends AbstractAction implements Command {

    @Autowired
    private AppProps props;
    @Autowired
    private ChannelServResolve channels;

    public AcSave() {
        super("&Save");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (props.getChanFile() == null) {
            LOG.warn("Nothing to save ... please open a file first!");
            return;
        }
        try {
            channels.service().writeTo(props.getChanFile());
            LOG.info("File {} saved.", props.getChanFile());
        } catch (IOException ex) {
            LOG.warn(ex.getMessage());
        }
    }
}
