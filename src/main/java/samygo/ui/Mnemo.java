package samygo.ui;

import javax.swing.*;

public final class Mnemo {

    private final String title;

    public Mnemo(String title) {
        this.title = title;
    }

    public Mnemo(Action action) {
        this((String) action.getValue(Action.NAME));
    }

    public void setup(AbstractButton btn) {
        int idx = title.indexOf("&");
        if (idx < 0) {
            btn.setText(title);
        } else {
            btn.setText(title.substring(0, idx) + title.substring(idx + 1));
            btn.setMnemonic(title.charAt(idx + 1));
        }
    }
}
