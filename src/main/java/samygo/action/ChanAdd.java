package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.frm.ChannelCreator;
import samygo.frm.EditFrm;
import samygo.model.Channel;

@Component
public final class ChanAdd extends AbstractAction implements Command {

    @Autowired
    private EditFrm editFrm;
    @Autowired
    private ChannelCreator channelCreator;

    public ChanAdd() {
        super("Add new Channel...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Channel channel = channelCreator.create();
        editFrm.workWith(channel);
        editFrm.setVisible(true);
    }
}
