package samygo.service.channels;

import org.springframework.stereotype.Service;

@Service
public final class SatChannels extends TreeChannels {

    private final ChanTableModel model = new SatModel();

    @Override
    public ChanTableModel model() {
        return model;
    }

}
