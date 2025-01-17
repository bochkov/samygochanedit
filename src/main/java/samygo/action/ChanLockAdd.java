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
@UseTableContent(modes = {Mode.AIR, Mode.CABLE})
public final class ChanLockAdd extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;

    public ChanLockAdd() {
        super("Add parental lock");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelService serv = servResolve.service();
        for (Channel channel : serv.selectedChannels())
            channel.lock |= Channel.FLAG_LOCK;
        serv.refresh();
    }
}
