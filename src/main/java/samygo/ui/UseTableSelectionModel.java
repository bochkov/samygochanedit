package samygo.ui;

import java.util.Map;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import samygo.action.Command;
import samygo.action.UseTableContent;
import samygo.infra.Mode;
import samygo.infra.ModeKeep;

@Slf4j
public final class UseTableSelectionModel extends DefaultListSelectionModel {

    public UseTableSelectionModel(Map<String, Command> commands, ModeKeep modeKeep) {
        addListSelectionListener(e -> {
            LOG.debug("{}", e);
            if (e.getValueIsAdjusting())
                return;
            boolean selected = !isSelectionEmpty();
            for (Command cmd : commands.values()) {
                if (cmd.getClass().isAnnotationPresent(UseTableContent.class)) {
                    UseTableContent ann = cmd.getClass().getAnnotation(UseTableContent.class);
                    if (ann.modes().length > 0) {
                        cmd.setEnabled(selected && (modeKeep.currentMode().mask() & calcBits(ann.modes())) != 0);
                    } else {
                        cmd.setEnabled(selected);
                    }
                }
            }
        });
        fireValueChanged(0, 0);
    }

    private byte calcBits(Mode[] modes) {
        byte res = 0;
        for (Mode mode : modes) {
            res |= mode.mask();
        }
        return res;
    }
}
