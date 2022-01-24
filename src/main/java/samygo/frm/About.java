package samygo.frm;

import java.awt.event.WindowEvent;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.infra.AppProps;
import samygo.ui.CmdPanel;

@Component
public final class About extends JDialog implements Frm {

    private static final String MESSAGE = """
            <html>SamyGO Channel Editor<br>
            a Java based Samsung Channel Editor<br>
            Version: %s<br>
            <br>
            Written and developed by polskafan <polska@polskafan.de> and<br>
            upgraded for Samsung %s TV models by rayzyt <rayzyt@mail-buero.de><br>
            For more information see: <a>http://www.ullrich.es/job/sendersortierung/samsung-samygo/</a><br>
            Source Code and the latest version: <a>https://sourceforge.net/projects/samygochanedit/</a><br>
            <br>
            This program is free software: you can redistribute it and/or modify<br>
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
            along with this program.  If not, see <a>http://www.gnu.org/licenses/</a>.
            """;

    public About(@Autowired AppProps props) {
        super();
        setTitle("About SamyGO ChanEdit");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setLayout(new MigLayout("fill, wrap 1", "fill, grow"));

        JLabel label = new JLabel(String.format(MESSAGE, props.version(), props.series()));
        // TODO add click on links
        // java.awt.Desktop.getDesktop().browse(new URI(event.text));
        add(label);

        JButton okBtn = new JButton("&OK");
        okBtn.addActionListener(e -> dispatchEvent(new WindowEvent(About.this, WindowEvent.WINDOW_CLOSING)));
        JPanel cmdPanel = new CmdPanel(this.getRootPane(), okBtn);
        cmdPanel.add(okBtn);

        add(cmdPanel);

        pack();
        setLocationRelativeTo(null);
    }
}
