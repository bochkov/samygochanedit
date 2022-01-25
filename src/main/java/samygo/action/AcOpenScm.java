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
public final class AcOpenScm extends AcOpenSave {

    @Autowired
    private AppProps props;
    @Autowired
    private ScmExtractor scmExtractor;
    @Autowired
    private ChannelParser channelParser;
    @Autowired
    private ChannelServResolve servResolve;

    public AcOpenScm() {
        super("&Open scm...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            actionPerformed();
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    private void actionPerformed() throws IOException {
        File scmFile = selectFileToOpen("Open SCM File", FileFilters.SCM_FILTERS, props.getCurrDir());
        if (scmFile == null) {
            LOG.info("No File selected!");
            return;
        }

        int totalFiles = scmExtractor.extractScm(scmFile);
        if (totalFiles == 0)
            throw new IOException("Extraction of file: " + scmFile + " failed!");
        props.setScmFile(scmFile);
        LOG.info("Successful extraction of file: {}", scmFile);

        File chanFile = selectFileToOpen("Open File", FileFilters.MAP_FILTERS, props.getTempDir());
        if (chanFile == null) {
            LOG.info("No File selected!");
            return;
        }

        /* parse the file and refresh the gui */
        SortedMap<Integer, Channel> channels = channelParser.parse(chanFile);
        servResolve.service().replaceWith(channels.values());
        /* save chanFile for later */
        props.setChanFile(chanFile);
        LOG.info("Finished opening file: {}", props.getChanFile());
    }
}
