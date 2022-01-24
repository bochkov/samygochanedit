package samygo.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class HelpForum extends AbstractAction implements Command {

    private static final String URL = "http://www.hifi-forum.de/index.php?action=browseT&forum_id=151&thread=11695&postID=first#first";

    public HelpForum() {
        super("Help Forum");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(URL));
        } catch (IOException | URISyntaxException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
