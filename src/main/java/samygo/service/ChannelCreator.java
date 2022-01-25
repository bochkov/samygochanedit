package samygo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.infra.ModeKeep;
import samygo.model.*;

@Component
public final class ChannelCreator {

    @Autowired
    private AppProps props;
    @Autowired
    private ModeKeep modeKeep;

    public Channel create() {
        Channel ch;
        switch (modeKeep.currentMode()) {
            case AIR -> {
                ch = new AirChannel();
            }
            case CABLE -> {
                if (props.getScmVersion() == 'C')
                    ch = new AirCableChannelC();
                else if (props.getScmVersion() == 'D')
                    ch = new AirCableChannelD();
                else
                    throw new IllegalStateException("Unexpected value: " + props.getScmVersion());
            }
            case SAT -> {
                if (props.getScmVersion() == 'C')
                    ch = new SatChannelC();
                else if (props.getScmVersion() == 'D')
                    ch = new SatChannelD();
                else
                    throw new IllegalStateException("Unexpected value: " + props.getScmVersion());
            }
            default -> throw new IllegalStateException("Unexpected value: " + modeKeep.currentMode());
        }
        return ch;
    }

}
