package samygo.frm;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;
import samygo.action.AcClose;
import samygo.infra.AppProps;
import samygo.ui.CmdPanel;
import samygo.ui.Images;
import samygo.ui.JLinkLabel;

@Slf4j
@Component
public final class About extends JDialog implements Frm {

    private static final String MESSAGE = """
            <html><b>SamyGO Channel Editor</b><br>
            a Java based Samsung Channel Editor<br>
            Version: %s<br>
            <br>
            Written and developed by polskafan &lt;polska@polskafan.de&gt; and<br>
            upgraded for Samsung %s TV models by rayzyt &lt;rayzyt@mail-buero.de&gt;<br>
            refactored by bochkov &lt;bochkov.sa@gmail.com&gt;<br/>
            """;
    private static final String LICENSE = """
            <html>This program is free software: you can redistribute it and/or modify<br>
            it under the terms of the GNU General Public License as published by<br>
            the Free Software Foundation, either version 3 of the License, or<br>
            (at your option) any later version.<br>
            <br>
            This program is distributed in the hope that it will be useful,<br>
            but WITHOUT ANY WARRANTY; without even the implied warranty of<br>
            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br>
            GNU General Public License for more details.<br>
            <br>
            You should have received a copy of the GNU General Public License<br>
            along with this program.  If not, see <a>http://www.gnu.org/licenses/</a>.<br>
            """;

    private final JFrame owner;

    public About(JFrame owner, AppProps props) {
        super(owner, "About SamyGO ChanEdit", ModalityType.APPLICATION_MODAL);
        this.owner = owner;
        setLayout(new MigLayout("fillx, flowy", "fill, grow", "[]25[]"));

        JPanel txtPanel = new JPanel(new MigLayout("flowy, fillx, insets 0", "fill, grow"));
        txtPanel.add(new JLabel(Images.FUNNY_LOGO));
        txtPanel.add(new JLabel(String.format(MESSAGE, props.version(), props.series())));
        txtPanel.add(new JLinkLabel("More information", open("http://www.ullrich.es/job/sendersortierung/samsung-samygo/")));
        txtPanel.add(new JLinkLabel("Source Code", open("https://sourceforge.net/projects/samygochanedit/")));
        txtPanel.add(new JLinkLabel("GitHub", open("https://github.com/bochkov/samygochanedit")));
        txtPanel.add(new JLabel(LICENSE));

        JButton okBtn = new JButton(new AcClose(this));
        okBtn.setText("OK");
        okBtn.setMnemonic('O');
        JPanel cmdPanel = new CmdPanel(this.getRootPane(), okBtn);
        cmdPanel.add(okBtn);

        add(txtPanel);
        add(cmdPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(this.owner);

        getRootPane()
                .getActionMap().put("exitAction", new AcClose(this));
        getRootPane()
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(owner);
        }
        super.setVisible(b);
    }

    private ActionListener open(String url) {
        return e -> {
            try {
                Desktop.getDesktop().browse(URI.create(url));
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        };
    }
}
