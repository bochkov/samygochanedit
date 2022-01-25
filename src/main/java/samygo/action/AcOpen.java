package samygo.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.ChannelParser;
import samygo.util.FileFilters;

@Slf4j
@Component
public final class AcOpen extends AcOpenSave {

    @Autowired
    private AppProps props;
    @Autowired
    private JLabel statusLabel;
    @Autowired
    private ChannelParser channelParser;
    @Autowired
    private ChannelServResolve servResolve;

    public AcOpen() {
        super("Open...");
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
        File dir = props.getScmFile() == null ?
                props.getCurrDir() :
                props.getTempDir();
        File chanFile = selectFileToOpen("Open File", FileFilters.MAP_FILTERS, dir);
        if (chanFile == null) {
            LOG.info("No Channel-File selected!");
            statusLabel.setText("Open: No Channel-File selected!");
            return;
        }

        SortedMap<Integer, Channel> channels = channelParser.parse(chanFile);
        servResolve.service().replaceWith(channels.values());
        props.setChanFile(chanFile);
        LOG.info("Finished opening file: {}", props.getChanFile());
        statusLabel.setText("Finished opening file: " + props.getChanFile());
    }
}
