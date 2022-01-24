package samygo.action;

import java.awt.event.ActionEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import samygo.infra.Mode;

@Slf4j
@Component
public final class NewAir extends NewAction {

    public NewAir() {
        super("map-AirD");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newModel(Mode.AIR);
    }
}
