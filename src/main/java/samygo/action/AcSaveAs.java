package samygo.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.util.FileFilters;

@Slf4j
@Component
public final class AcSaveAs extends AcOpenSave {

    @Autowired
    private AppProps props;
    @Autowired
    private ChannelServResolve channels;

    public AcSaveAs() {
        super("Save As...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File spath = selectFileToSave("Save File as", FileFilters.ALL_FILTERS, props.getTempDir());
        if (spath == null) {
            return;
        }
        try {
            channels.service().writeTo(props.getChanFile());
            LOG.info("File saved as: {}", props.getChanFile());
        } catch (IOException ex) {
            LOG.warn(ex.getMessage());
        }
    }
}
