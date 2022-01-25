package samygo.ui;

import java.awt.*;
import javax.swing.*;

public final class CmdPanel extends JPanel {

    public static final String DEFAULT_CAPABLE = "DEFAULT_CAPABLE";

    public CmdPanel(JRootPane rootPane, JButton... buttons) {
        this(rootPane, new FlowLayout(FlowLayout.CENTER), buttons);
    }

    public CmdPanel(JRootPane rootPane, LayoutManager layout, JButton... buttons) {
        this.setLayout(layout);
        for (JButton button : buttons) {
            add(button);
            if (button.getAction() != null && "true".equals(button.getAction().getValue(DEFAULT_CAPABLE)) ||
                    "true".equals(button.getClientProperty(DEFAULT_CAPABLE))) {
                button.setDefaultCapable(true);
                rootPane.setDefaultButton(button);
            }
        }
    }
}
