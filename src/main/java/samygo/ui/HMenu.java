package samygo.ui;

import java.util.Arrays;
import java.util.Collection;
import javax.swing.*;

public final class HMenu extends JMenu {

    public HMenu(String title, MenuElement... items) {
        this(title, Arrays.asList(items));
    }

    public HMenu(String title, Collection<MenuElement> items) {
        setText(title);
        for (MenuElement item : items) {
            if (item instanceof JMenu menu) {
                add(menu);
            } else if (item instanceof JMenuItem menuItem) {
                if (menuItem.getText().equals("---"))
                    this.addSeparator();
                else
                    this.add(menuItem);
            }
        }
    }
}