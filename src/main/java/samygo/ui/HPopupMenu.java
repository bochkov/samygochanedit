package samygo.ui;

import java.util.Arrays;
import java.util.Collection;
import javax.swing.*;

public final class HPopupMenu extends JPopupMenu {

    public HPopupMenu(JMenuItem... items) {
        this(Arrays.asList(items));
    }

    public HPopupMenu(Collection<JMenuItem> items) {
        for (JMenuItem item : items) {
            if (item.getText().equals("---"))
                this.addSeparator();
            else
                this.add(item);
        }
    }
}
