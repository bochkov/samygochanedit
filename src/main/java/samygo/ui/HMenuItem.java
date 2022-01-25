package samygo.ui;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import samygo.action.Command;

@Slf4j
public final class HMenuItem extends JMenuItem {

    public HMenuItem(String text) {
        super(text);
    }

    public HMenuItem(Command cmd) {
        super(cmd);
        new Mnemo(cmd).setup(this);
    }

    public HMenuItem(Command cmd, KeyStroke keyStroke) {
        this(cmd);
        setAccelerator(keyStroke);
    }
}
