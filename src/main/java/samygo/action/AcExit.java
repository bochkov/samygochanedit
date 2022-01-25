package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.stereotype.Component;

@Component
public final class AcExit extends AbstractAction implements Command {

    public AcExit() {
        super("E&xit");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
