package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.frm.Frm;

@Component
public final class AcAbout extends AbstractAction implements Command {

    @Autowired
    private Frm about;

    public AcAbout() {
        super("About");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        about.setVisible(true);
    }
}
