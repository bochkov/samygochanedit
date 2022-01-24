package samygo.frm;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.Mode;
import samygo.infra.ModeKeep;
import samygo.model.AirCableChannel;
import samygo.model.Channel;
import samygo.model.CloneChannel;
import samygo.model.SatChannel;
import samygo.service.channels.ChannelService;
import samygo.ui.CmdPanel;

@Slf4j
@Component
public final class Edit extends JDialog implements EditFrm {

    private final JTextField nameField = new JTextField();
    private final JTextField onidField = new JTextField();
    private final JTextField tsidField = new JTextField();
    private final JTextField sidField = new JTextField();
    private final JTextField pidField = new JTextField();
    private final JTextField vpidField = new JTextField();

    private final JRadioButton btn0 = new JRadioButton("TV");
    private final JRadioButton btn1 = new JRadioButton("Radio");
    private final JRadioButton btn2 = new JRadioButton("Data");
    private final JRadioButton btn3 = new JRadioButton("HD");

    private final JCheckBox encCheck = new JCheckBox("Encrypted");
    private final JCheckBox lockCheck = new JCheckBox("Locked");
    private final JCheckBox favCheck = new JCheckBox("Favourite");
    private final JCheckBox fav1Check = new JCheckBox("Fav1");
    private final JCheckBox fav2Check = new JCheckBox("Fav2");
    private final JCheckBox fav3Check = new JCheckBox("Fav3");
    private final JCheckBox fav4Check = new JCheckBox("Fav4");

    private final JTextField nidField = new JTextField();
    private final JTextField freqField = new JTextField();
    private final JTextField symbField = new JTextField();

    private final JRadioButton qam64 = new JRadioButton("QAM64");
    private final JRadioButton qam246 = new JRadioButton("QAM256");

    private final JTextField satField = new JTextField();
    private final JTextField tpField = new JTextField();

    private final JButton okButton = new JButton(new DoEdit());

    @Autowired
    private Map<String, ChannelService> services;
    private final ModeKeep modeKeep;

    private Channel saveChannel;

    public Edit(ModeKeep modeKeep) {
        super();
        this.modeKeep = modeKeep;
        setModalityType(ModalityType.APPLICATION_MODAL);
        createGUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void createGUI() {
        setLayout(new MigLayout("flowy, fillx"));
        // region mainPanel
        JPanel mainPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill, grow]"));
        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("ONID:"));
        mainPanel.add(onidField);
        mainPanel.add(new JLabel("TSID:"));
        mainPanel.add(tsidField);
        mainPanel.add(new JLabel("SID:"));
        mainPanel.add(sidField);
        mainPanel.add(new JLabel("PID:"));
        mainPanel.add(pidField);
        mainPanel.add(new JLabel("VPID:"));
        mainPanel.add(vpidField);
        add(mainPanel);
        // endregion
        // region servicePanel
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Service Type"));
        ButtonGroup grp = new ButtonGroup();
        grp.add(btn0);
        panel.add(btn0);
        grp.add(btn1);
        panel.add(btn1);
        grp.add(btn2);
        panel.add(btn2);
        grp.add(btn3);
        panel.add(btn3);
        add(panel);
        // endregion
        // region miscPanel
        JPanel miscPanel = new JPanel();
        miscPanel.setBorder(BorderFactory.createTitledBorder("Misc"));
        miscPanel.add(encCheck);
        if ((modeKeep.currentMode().mask() & (Channel.TYPE_AIR | Channel.TYPE_CABLE | Channel.TYPE_SAT)) != 0) {
            miscPanel.add(lockCheck);
        }
        miscPanel.add(favCheck);
        add(miscPanel);
        // endregion
        // region favourites79
        if ((modeKeep.currentMode().mask() & (Channel.TYPE_AIR | Channel.TYPE_CABLE | Channel.TYPE_SAT)) != 0) {
            JPanel x79Panel = new JPanel();
            x79Panel.setBorder(BorderFactory.createTitledBorder("Favourites (x79)"));
            x79Panel.add(fav1Check);
            x79Panel.add(fav2Check);
            x79Panel.add(fav3Check);
            x79Panel.add(fav4Check);
            add(x79Panel);
        }
        // endregion
        // region specPanel
        JPanel specPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill, grow]"));
        if (modeKeep.currentMode() == Mode.AIR) {
            editAir(specPanel);
        } else if (modeKeep.currentMode() == Mode.CABLE) {
            editCable(specPanel);
        } else if (modeKeep.currentMode() == Mode.SAT) {
            editSat(specPanel);
        }
        add(specPanel);
        // endregion
        // region cmd
        JButton cancelButton = new JButton("Abort");
        cancelButton.addActionListener(e -> dispatchEvent(new WindowEvent(Edit.this, WindowEvent.WINDOW_CLOSING)));
        CmdPanel cmdPanel = new CmdPanel(this.getRootPane(), okButton, cancelButton);
        add(cmdPanel);
        // endregion
    }

    private void editCable(JPanel panel) {
        panel.add(new JLabel("NID:"));
        panel.add(nidField);

        panel.add(new JLabel("Frequency:"));
        panel.add(freqField);

        panel.add(new JLabel("SymbR (ksymb/s):"));
        panel.add(symbField);

        JPanel grpPanel = new JPanel();
        grpPanel.setBorder(BorderFactory.createTitledBorder("QAM"));
        ButtonGroup grp = new ButtonGroup();
        grp.add(qam64);
        grpPanel.add(qam64);
        grp.add(qam246);
        grpPanel.add(qam246);

        panel.add(grpPanel);
    }

    private void editAir(JPanel panel) {
        panel.add(new JLabel("NID:"));
        panel.add(nidField);

        panel.add(new JLabel("Frequency:"));
        panel.add(freqField);
    }

    private void editSat(JPanel panel) {
        panel.add(new JLabel("Sat:"));
        panel.add(satField);

        panel.add(new JLabel("TP:"));
        panel.add(tpField);
    }

    @Override
    public void workWith(Channel ch) {
        this.saveChannel = ch;
        setTitle(ch.num == -1 ? "Add Channel" : "Edit Channel");
        nameField.setText(ch.name);
        onidField.setText(String.valueOf(ch.onid));
        tsidField.setText(String.valueOf(ch.tsid));
        sidField.setText(String.valueOf(ch.sid));
        pidField.setText(String.valueOf(ch.mpid));
        vpidField.setText(String.valueOf(ch.vpid));
        btn0.setSelected(ch.stype == Channel.STYPE_TV);
        btn1.setSelected(ch.stype == Channel.STYPE_RADIO);
        btn2.setSelected(ch.stype == Channel.STYPE_DATA);
        btn3.setSelected(ch.stype == Channel.STYPE_HD);
        if ((modeKeep.currentMode().mask() & (Channel.TYPE_CLONE)) != 0) {
            encCheck.setSelected((ch.enc & CloneChannel.FLAG_SCRAMBLED) != 0);
        } else {
            encCheck.setSelected((ch.enc & Channel.FLAG_SCRAMBLED) != 0);
        }
        lockCheck.setSelected((ch.lock & Channel.FLAG_LOCK) != 0);
        favCheck.setSelected((ch.fav & Channel.FLAG_FAV_1) != 0);
        fav1Check.setSelected((ch.fav79 & Channel.FLAG_FAV_1) != 0);
        fav2Check.setSelected((ch.fav79 & Channel.FLAG_FAV_2) != 0);
        fav3Check.setSelected((ch.fav79 & Channel.FLAG_FAV_3) != 0);
        fav4Check.setSelected((ch.fav79 & Channel.FLAG_FAV_4) != 0);
        okButton.setText(ch.num == -1 ? "Add" : "Change");
        if (modeKeep.currentMode().equals(Mode.CABLE)) {
            AirCableChannel c = (AirCableChannel) ch;
            nidField.setText(String.valueOf(c.nid));
            freqField.setText(c.getFreq());
            symbField.setText(String.valueOf(c.symbr));
            qam64.setSelected(c.qam == AirCableChannel.QAM64);
            qam246.setSelected(c.qam == AirCableChannel.QAM256);
        } else if (modeKeep.currentMode().equals(Mode.AIR)) {
            AirCableChannel c = (AirCableChannel) ch;
            nidField.setText(String.valueOf(c.nid));
            freqField.setText(String.valueOf(c.freq));
        } else if (modeKeep.currentMode().equals(Mode.SAT)) {
            SatChannel c = (SatChannel) ch;
            satField.setText(String.valueOf(c.sat));
            tpField.setText(String.valueOf(c.tpid));
        }
    }

    private final class DoEdit extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveChannel.name = nameField.getText();
            try {
                saveChannel.onid = Integer.parseInt(onidField.getText());
                saveChannel.tsid = Integer.parseInt(tsidField.getText());
                saveChannel.sid = Integer.parseInt(sidField.getText());
                saveChannel.mpid = Integer.parseInt(pidField.getText());
                saveChannel.vpid = Integer.parseInt(vpidField.getText());
            } catch (NumberFormatException ex) {
                LOG.warn("Cannot get number representation " + ex.getMessage());
                return;
            }

            if (btn0.isSelected()) saveChannel.stype = Channel.STYPE_TV;
            if (btn1.isSelected()) saveChannel.stype = Channel.STYPE_RADIO;
            if (btn2.isSelected()) saveChannel.stype = Channel.STYPE_DATA;
            if (btn3.isSelected()) saveChannel.stype = Channel.STYPE_HD;

            if ((modeKeep.currentMode().mask() & Channel.TYPE_CLONE) != 0) {
                if (encCheck.isSelected())
                    saveChannel.enc |= CloneChannel.FLAG_SCRAMBLED;
                else
                    saveChannel.enc &= ~CloneChannel.FLAG_SCRAMBLED;
            } else {
                if (encCheck.isSelected())
                    saveChannel.enc |= Channel.FLAG_SCRAMBLED;
                else
                    saveChannel.enc &= ~Channel.FLAG_SCRAMBLED;
            }

            if (favCheck.isSelected())
                saveChannel.fav |= Channel.FLAG_FAV_1;
            else
                saveChannel.fav &= ~Channel.FLAG_FAV_1;

            if ((modeKeep.currentMode().mask() & (Channel.TYPE_AIR | Channel.TYPE_CABLE | Channel.TYPE_SAT)) != 0) {
                if (lockCheck.isSelected())
                    saveChannel.lock |= Channel.FLAG_LOCK;
                else
                    saveChannel.lock &= ~Channel.FLAG_LOCK;

                if (fav1Check.isSelected())
                    saveChannel.fav79 |= Channel.FLAG_FAV_1;
                else
                    saveChannel.fav79 &= ~Channel.FLAG_FAV_1;

                if (fav2Check.isSelected())
                    saveChannel.fav79 |= Channel.FLAG_FAV_2;
                else
                    saveChannel.fav79 &= ~Channel.FLAG_FAV_2;

                if (fav3Check.isSelected())
                    saveChannel.fav79 |= Channel.FLAG_FAV_3;
                else
                    saveChannel.fav79 &= ~Channel.FLAG_FAV_3;

                if (fav4Check.isSelected())
                    saveChannel.fav79 |= Channel.FLAG_FAV_4;
                else
                    saveChannel.fav79 &= ~Channel.FLAG_FAV_4;
            }

            switch (modeKeep.currentMode()) {
                case CABLE -> {
                    AirCableChannel cable = (AirCableChannel) saveChannel;
                    try {
                        cable.nid = Integer.parseInt(nidField.getText());
                        cable.setFreq(freqField.getText());
                        cable.symbr = Integer.parseInt(symbField.getText());
                    } catch (NumberFormatException ex) {
                        LOG.warn("Cannot get number representation " + ex.getMessage());
                        return;
                    }
                    if (qam64.isSelected())
                        cable.qam = AirCableChannel.QAM64;
                    if (qam246.isSelected())
                        cable.qam = AirCableChannel.QAM256;
                }
                case AIR -> {
                    AirCableChannel air = (AirCableChannel) saveChannel;
                    try {
                        air.nid = Integer.parseInt(nidField.getText());
                        air.freq = Integer.parseInt(freqField.getText());
                    } catch (NumberFormatException ex) {
                        LOG.warn("Cannot get number representation " + ex.getMessage());
                        return;
                    }
                }
                case SAT -> {
                    SatChannel sat = (SatChannel) saveChannel;
                    try {
                        sat.sat = Integer.parseInt(satField.getText());
                        sat.tpid = Integer.parseInt(tpField.getText());
                    } catch (NumberFormatException ex) {
                        LOG.warn("Cannot get number representation " + ex.getMessage());
                        return;
                    }
                }
            }

            ChannelService serv = services.get(modeKeep.currentMode().componentId());
            if (saveChannel.num == -1) {
                saveChannel.num = serv.size() + 1;
                serv.add(saveChannel);
            }
            dispatchEvent(new WindowEvent(Edit.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}

