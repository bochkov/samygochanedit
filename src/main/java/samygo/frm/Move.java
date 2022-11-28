package samygo.frm;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.action.AcClose;
import samygo.infra.ChannelServResolve;
import samygo.ui.IntTextField;
import sb.bdev.ui.HotKey;
import sb.bdev.ui.common.CmdPanel;

@Slf4j
@Component
public final class Move extends JDialog implements Frm {

    @Autowired
    private ChannelServResolve servResolve;

    private final JFrame owner;
    private final JTextField startField = new IntTextField();
    private final JButton moveButton = new JButton(new DoMove());

    public Move(JFrame owner) {
        super(owner, "Move Channel(s)", ModalityType.APPLICATION_MODAL);
        this.owner = owner;
        createGUI();
        pack();
        setLocationRelativeTo(this.owner);
        setResizable(false);
    }

    private void createGUI() {
        setLayout(new MigLayout("flowy", "fill, grow"));
        JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill, grow]"));

        panel.add(new JLabel("Starting number:"));
        panel.add(startField);
        add(panel);

        JButton cancelButton = new JButton(new AcClose(this));
        cancelButton.setText("Cancel");
        CmdPanel cmdPanel = new CmdPanel(this.getRootPane(), moveButton, cancelButton);
        add(cmdPanel);
        HotKey.escBy(getRootPane(), new AcClose(this));
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            startField.setText("");
            setLocationRelativeTo(owner);
        }
        super.setVisible(visible);
    }

    private final class DoMove extends AbstractAction {

        public DoMove() {
            super("Move");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                actionPerformed();
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            } finally {
                dispatchEvent(new WindowEvent(Move.this, WindowEvent.WINDOW_CLOSING));
            }
        }

        private void actionPerformed() throws IllegalArgumentException {
            if (servResolve.service().selectedChannel() == null)
                throw new IllegalArgumentException("channel not selected");
            if (startField.getText().isEmpty())
                return;

            int targetNumber = Integer.parseInt(startField.getText());
            servResolve.service().moveSelected(targetNumber);
        }
    }
}
