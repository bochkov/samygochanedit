package samygo.infra;

import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class DefaultModeKeep implements ModeKeep {

    private final AtomicReference<Mode> mode = new AtomicReference<>(Mode.CABLE);

    @Autowired
    private JLabel modeLabel;

    @PostConstruct
    public void updateLabel() {
        this.modeLabel.setText("Mode: " + mode.get().description());
    }

    @Override
    public Mode currentMode() {
        return this.mode.get();
    }

    @Override
    public void currentMode(Mode mode) {
        this.mode.set(mode);
        updateLabel();
    }
}
