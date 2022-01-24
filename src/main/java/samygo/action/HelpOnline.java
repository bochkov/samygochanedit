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
public final class HelpOnline extends AbstractAction implements Command {

    private static final String URL = "http://www.ullrich.es/job/sendersortierung/samsung-samygo/";

    public HelpOnline() {
        super("Online Help");
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
