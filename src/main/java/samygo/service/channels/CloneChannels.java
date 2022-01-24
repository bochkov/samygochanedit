package samygo.service.channels;

import org.springframework.stereotype.Service;

@Service
public final class CloneChannels extends TreeChannels {

    private final ChanTableModel model = new CloneModel();

    @Override
    public ChanTableModel model() {
        return model;
    }

}
