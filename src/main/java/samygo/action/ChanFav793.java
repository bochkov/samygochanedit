package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.channels.ChannelService;

@Component
public final class ChanFav793 extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanFav793() {
        super("Toggle List 3");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelService serv = servResolve.service();
        for (Channel channel : serv.selectedChannels())
            channel.fav79 ^= Channel.FLAG_FAV_3;
        serv.refresh();
    }
}
