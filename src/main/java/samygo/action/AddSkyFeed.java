package samygo.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import samygo.infra.ChannelServResolve;
import samygo.infra.Mode;
import samygo.infra.ModeKeep;
import samygo.model.Channel;
import samygo.model.CloneChannel;

@Deprecated
public final class AddSkyFeed extends AbstractAction implements Command {

    @Autowired
    private ChannelServResolve servResolve;
    @Autowired
    private ModeKeep modeKeep;

    public AddSkyFeed() {
        super("Add Sky.de Feed Channels");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            actionPreformed();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actionPreformed() throws IllegalArgumentException {
        /* get reference channels, so we now their parameters to copy them */
        Channel[] ref = new Channel[5];
        for (Channel curr : servResolve.service().model().values()) {
            if (curr.onid != 133)
                continue;
            switch (curr.tsid) {
                case 0x01:
                    ref[0] = curr;
                    break;
                case 0x02:
                    ref[1] = curr;
                    break;
                case 0x03:
                    ref[2] = curr;
                    break;
                case 0x11:
                    ref[3] = curr;
                    break;
                case 0x04:
                    ref[4] = curr;
                    break;
            }
        }

        if (ref[0] == null || ref[1] == null || ref[2] == null || ref[3] == null || ref[4] == null) {
            throw new IllegalArgumentException("Could not find enough sky reference channels for frequency extraction.\nPlease load a channel list containing sky channels, before using this function.");
        }

        addChannel(ref[4], 9700, "Sky Select", 18);
        addChannel(ref[1], 9701, "Sky Select 1", 251);
        addChannel(ref[1], 9702, "Sky Select 2", 261);
        addChannel(ref[2], 9703, "Sky Select 3", 271);
        addChannel(ref[2], 9704, "Sky Select 4", 281);
        addChannel(ref[2], 9705, "Sky Select 5", 291);
        addChannel(ref[2], 9706, "Sky Select 6", 301);
        addChannel(ref[3], 9707, "Sky Select 7", 311);
        addChannel(ref[3], 9708, "Sky Select 8", 321);
        addChannel(ref[3], 9709, "Sky Select 9", 331);
        addChannel(ref[1], 9710, "Sky Event A", 254);
        addChannel(ref[1], 9711, "Sky Event B", 264);

        addChannel(ref[2], 9800, "Sky Sport Info", 17);
        addChannel(ref[4], 9801, "Sky Sport 1", 221);
        addChannel(ref[4], 9802, "Sky Sport 2", 222);
        addChannel(ref[1], 9803, "Sky Sport 3", 253);
        addChannel(ref[3], 9804, "Sky Sport 4", 333);
        addChannel(ref[3], 9805, "Sky Sport 5", 323);
        addChannel(ref[3], 9806, "Sky Sport 6", 313);
        addChannel(ref[2], 9807, "Sky Sport 7", 303);
        addChannel(ref[2], 9808, "Sky Sport 8", 293);
        addChannel(ref[2], 9809, "Sky Sport 9", 283);
        addChannel(ref[1], 9810, "Sky Sport 10", 263);
        addChannel(ref[2], 9811, "Sky Sport 11", 273);
        addChannel(ref[0], 9812, "Sky Sport 12", 363);
        addChannel(ref[0], 9813, "Sky Sport 13", 373);

        addChannel(ref[4], 9900, "Sky Bundesliga", 223);
        addChannel(ref[1], 9901, "Sky Bundesl. 1", 262);
        addChannel(ref[2], 9902, "Sky Bundesl. 2", 272);
        addChannel(ref[2], 9903, "Sky Bundesl. 3", 282);
        addChannel(ref[2], 9904, "Sky Bundesl. 4", 292);
        addChannel(ref[2], 9905, "Sky Bundesl. 5", 302);
        addChannel(ref[3], 9906, "Sky Bundesl. 6", 312);
        addChannel(ref[3], 9907, "Sky Bundesl. 7", 322);
        addChannel(ref[3], 9908, "Sky Bundesl. 8", 332);
        addChannel(ref[0], 9909, "Sky Bundesl. 9", 342);
        addChannel(ref[0], 9910, "Sky Bundesl. 10", 352);
        addChannel(ref[1], 9911, "Sky Bundesl. 11", 252);

        addChannel(ref[4], 9600, "Blue Movie", 513);
        addChannel(ref[0], 9601, "Blue Movie 1", 345);
        addChannel(ref[0], 9602, "Blue Movie 2", 355);
        addChannel(ref[0], 9603, "Blue Movie 3", 365);
    }

    private void addChannel(Channel ref, int num, String name, int sid) {
        Channel c = ref.clone();
        c.num = num;
        c.name = name;
        c.sid = sid;
        c.mpid = 0xFFFF;
        c.vpid = 0xFFFF;
        c.stype = Channel.STYPE_TV;
        c.fav &= ~Channel.FLAG_FAV_1;
        c.fav79 &= ~(Channel.FLAG_FAV_1 | Channel.FLAG_FAV_2 | Channel.FLAG_FAV_3 | Channel.FLAG_FAV_4);

        if (modeKeep.currentMode() == Mode.CLONE) {
            CloneChannel clone = (CloneChannel) c;
            clone.enc |= CloneChannel.FLAG_SCRAMBLED;
        } else {
            c.enc |= Channel.FLAG_SCRAMBLED;
        }
        servResolve.service().add(c);
    }
}
