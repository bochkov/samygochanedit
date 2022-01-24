package samygo.infra;

import samygo.service.channels.ChannelService;

public interface ChannelServResolve {

    ChannelService service();

    void updateMode(Mode mode);

}
