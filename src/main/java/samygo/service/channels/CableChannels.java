package samygo.service.channels;

import org.springframework.stereotype.Service;

@Service
public final class CableChannels extends TreeChannels {

    private final ChanTableModel model = new CableModel();

    @Override
    public ChanTableModel model() {
        return model;
    }
}
