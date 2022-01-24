package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.channels.ChannelService;

@Component
public final class ChanFav794 extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanFav794() {
        super("Toggle List 4");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelService serv = servResolve.service();
        for (Channel channel : serv.selectedChannels())
            channel.fav79 ^= Channel.FLAG_FAV_4;
        serv.refresh();
    }
}
