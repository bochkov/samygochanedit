package samygo.service.channels;

import org.springframework.stereotype.Service;

@Service
public final class AirChannels extends TreeChannels {

    private final ChanTableModel model = new AirModel();

    @Override
    public ChanTableModel model() {
        return model;
    }
}
