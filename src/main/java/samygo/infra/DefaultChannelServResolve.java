package samygo.infra;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.service.channels.ChannelService;

@Slf4j
@Component
public final class DefaultChannelServResolve implements ChannelServResolve {

    @Autowired
    private Map<String, ChannelService> services;
    @Autowired
    private ModeKeep modeKeep;

    @Override
    public ChannelService service() {
        return services.get(
                modeKeep.currentMode().componentId()
        );
    }

    @Override
    public void updateMode(Mode mode) {
        modeKeep.currentMode(mode);
    }
}
