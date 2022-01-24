package samygo.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.ChannelParser;
import samygo.service.ScmExtractor;
import samygo.util.FileFilters;

@Slf4j
@Component
public final class AcOpen extends AcOpenSave {

    @Autowired
    private AppProps props;
    @Autowired
    private ChannelParser channelParser;
    @Autowired
    private ChannelServResolve servResolve;

    public AcOpen() {
        super("&Open...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            actionPerformed();
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
            // statusUpdate(Main.LOG_INFO, "Open: )
        }
    }

    private void actionPerformed() throws IOException {
        File dir = props.getScmFile() == null ?
                props.getCurrDir() :
                props.getTempDir();
        File chanFile = selectFileToOpen("Open File", FileFilters.MAP_FILTERS, dir);
        if (chanFile == null) {
            LOG.info("No File selected!");
            return;
        }

        SortedMap<Integer, Channel> channels = channelParser.parse(chanFile);
        servResolve.service().replaceWith(channels.values());
        props.setChanFile(chanFile);
        LOG.info("Finished opening file: " + props.getChanFile());
    }
}
