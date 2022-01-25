package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import samygo.frm.EditFrm;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;

@Component
@UseTableContent
public final class ChanEdit extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;
    @Lazy
    @Autowired
    private EditFrm editFrm;

    public ChanEdit() {
        super("Edit Channel...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Channel channel = servResolve.service().selectedChannel();
        editFrm.workWith(channel);
        editFrm.setVisible(true);
    }
}
