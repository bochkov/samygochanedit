package samygo.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class AcClose extends AbstractAction {

    private final Window wnd;

    @Override
    public void actionPerformed(ActionEvent e) {
        wnd.dispatchEvent(new WindowEvent(wnd, WindowEvent.WINDOW_CLOSING));
    }
}
