package samygo.frm;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.SortedMap;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.ChannelServResolve;
import samygo.model.Channel;
import samygo.service.channels.ChannelService;
import samygo.ui.CmdPanel;
import samygo.ui.IntTextField;

@Slf4j
@Component
public final class Move extends JDialog implements Frm {

    @Autowired
    private ChannelServResolve servResolve;
    @Autowired
    private ChannelCreator channelCreator;

    private final JTextField startField = new IntTextField();
    private final JButton moveButton = new JButton(new DoMove());

    public Move() {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Move Channel(s)");
        createGUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void createGUI() {
        setLayout(new MigLayout("flowy"));
        JPanel panel = new JPanel(new MigLayout());

        panel.add(new JLabel("Starting number:"));
        panel.add(startField);
        add(panel);

        JButton cancelButton = new JButton("Abort");
        cancelButton.addActionListener(e -> dispatchEvent(new WindowEvent(Move.this, WindowEvent.WINDOW_CLOSING)));
        CmdPanel cmdPanel = new CmdPanel(this.getRootPane(), moveButton, cancelButton);
        add(cmdPanel);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            startField.setText("");
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

            int targetNumber = Integer.parseInt(startField.getText());
            ChannelService serv = servResolve.service();
            Channel[] selected = serv.selectedChannels();

            Channel targetChan = serv.get(targetNumber);
            if (targetChan != null) {
                moveChannels(selected, targetChan);
            } else {
                Channel dummy = channelCreator.create();
                dummy.name = "DUMMY";
                dummy.num = targetNumber;
                serv.add(dummy);
                moveChannels(selected, dummy);
                serv.remove(dummy);
            }
        }

        private void moveChannels(Channel[] selected, Channel target) {
            ChannelService serv = servResolve.service();

            int cIndex = target.num;
            // first delete all the channels to be moved from list
            for (Channel select : selected)
                serv.remove(select);

            // then delete all channels after targetChan.num from list
            SortedMap<Integer, Channel> removed = serv.removeAfter(cIndex);

            // now read them at targetChannel.num, targetChannel might have moved up
            // read everything with new channel number
            for (Channel channel : selected) {
                channel.num = cIndex;
                serv.add(channel);
                cIndex++;
            }

            // after that all other channels, only renumbering if we have to (mind the gap!)
            for (Channel ch : removed.values()) {
                if (serv.exists(ch)) { // channel number already used, give it a new one
                    ch.num = cIndex;
                    serv.add(ch);
                    cIndex++;
                } else {
                    serv.add(ch);  // we hit a gap, just keep the numbers from now on
                }
            }
        }
    }
}
