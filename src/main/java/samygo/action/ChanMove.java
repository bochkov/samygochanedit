package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.frm.Frm;

@Component
public final class ChanMove extends AbstractAction implements Command {

    @Autowired
    private Frm move;

    public ChanMove() {
        super("Move Channel(s)");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move.setVisible(true);
    }
}
