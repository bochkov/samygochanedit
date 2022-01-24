package samygo.action;

import java.util.Collections;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import samygo.infra.ChannelServResolve;
import samygo.infra.Mode;

public abstract class NewAction extends AbstractAction implements Command {

    @Autowired
    protected JTable mainTable;
    @Autowired
    protected ChannelServResolve servResolve;

    protected NewAction(String text) {
        super(text);
    }

    protected void newModel(Mode mode) {
        servResolve.updateMode(mode);
        servResolve.service().replaceWith(Collections.emptyList());
    }
}
