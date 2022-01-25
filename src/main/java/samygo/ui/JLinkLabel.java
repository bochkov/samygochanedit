package samygo.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

public final class JLinkLabel extends JLabel {

    private static final Color ENABLED_COLOR = new Color(0, 0, 207);
    private static final Color DISABLED_COLOR = UIManager.getColor("Label.disabledForeground");

    private String innerText;

    public JLinkLabel(String text, ActionListener listener) {
        setText(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        if (listener != null)
            addActionListener(listener);
    }

    @Override
    public void setText(String text) {
        this.innerText = text;
        super.setText(
                String.format(
                        "<html><font color=\"%s\"><u>%s</u></font></html>",
                        isEnabled() ?
                                HexColor.asString(ENABLED_COLOR) :
                                HexColor.asString(DISABLED_COLOR),
                        innerText
                )
        );
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setText(innerText);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        switch (e.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                if (isEnabled())
                    fireActionPerformed(
                            new ActionEvent(
                                    this,
                                    ActionEvent.ACTION_PERFORMED,
                                    getText()
                            )
                    );
                break;
            case MouseEvent.MOUSE_ENTERED:
                if (isEnabled())
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                break;
            case MouseEvent.MOUSE_EXITED:
                if (isEnabled())
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
            default:
                break;
        }
    }

    public void addActionListener(ActionListener actionListener) {
        listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        listenerList.remove(ActionListener.class, actionListener);
    }

    private void fireActionPerformed(ActionEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }
}
