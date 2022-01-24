package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.channels.ChannelService;

@Component
public final class ChanFavAdd extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanFavAdd() {
        super("Add to favourites");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelService serv = servResolve.service();
        for (Channel channel : serv.selectedChannels())
            channel.fav |= Channel.FLAG_FAV_1;
        serv.refresh();
    }
}
