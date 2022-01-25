package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import samygo.frm.EditFrm;
import samygo.model.Channel;
import samygo.service.ChannelCreator;

@Slf4j
@Component
public final class ChanAdd extends AbstractAction implements Command {

    @Lazy
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
