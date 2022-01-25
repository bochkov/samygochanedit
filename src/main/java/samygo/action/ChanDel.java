package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;

@Slf4j
@Component
@UseTableContent
public final class ChanDel extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanDel() {
        super("Delete Channel(s)");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        servResolve.service().removeSelected();
    }
}
