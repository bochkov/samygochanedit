package samygo.action;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;
import samygo.infra.Mode;

@Component
public final class NewCable extends NewAction {

    public NewCable() {
        super("map-CableD");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newModel(Mode.CABLE);
    }
}
