package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.infra.Mode;
import samygo.model.Channel;
import samygo.service.channels.ChannelService;

@Component
@UseTableContent(modes = {Mode.AIR, Mode.CABLE, Mode.SAT})
public final class ChanFav791 extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanFav791() {
        super("Toggle List 1");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelService serv = servResolve.service();
        for (Channel channel : serv.selectedChannels())
            channel.fav79 ^= Channel.FLAG_FAV_1;
        serv.refresh();
    }
}
