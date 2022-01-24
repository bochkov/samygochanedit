package samygo.ui;

import javax.swing.*;

import samygo.action.Command;

public final class HMenuItem extends JMenuItem {

    public HMenuItem(Action action) {
        super(action);
    }

    public HMenuItem(Action action, String tooltip) {
        super(action);
        setToolTipText(tooltip);
    }

    public HMenuItem(Action action, KeyStroke keyStroke) {
        super(action);
        setAccelerator(keyStroke);
    }

    public HMenuItem(String text) {
        super(text);
    }

    public HMenuItem(Command cmd) {
        super(cmd);
    }
}
