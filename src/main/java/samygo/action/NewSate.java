package samygo.action;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;
import samygo.infra.Mode;

@Component
public final class NewSate extends NewAction {

    public NewSate() {
        super("map-SateD");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newModel(Mode.SAT);
    }
}
