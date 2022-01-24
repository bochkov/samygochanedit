package samygo.frm;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.action.Command;
import samygo.infra.AppProps;
import samygo.infra.ChannelServResolve;
import samygo.ui.HMenu;
import samygo.ui.HMenuBar;
import samygo.ui.HMenuItem;
import samygo.ui.QTableSearch;

/**
 * The Main class does create the application and controls all interaction
 */
@Slf4j
@Component
public final class Main extends JFrame {

    private static final int WIDTH = 820;
    private static final int HEIGHT = 500;
    private static final int META_KEY = System.getProperty("os.name").toLowerCase().contains("mac") ?
            InputEvent.META_DOWN_MASK :
            InputEvent.CTRL_DOWN_MASK;

    private final AppProps props;

    @Autowired
    private ChannelServResolve servResolve;
    @Autowired
    private JTable mainTable;
    @Autowired
    private Map<String, Command> commands;
    @Autowired
    private JLabel modeLabel;
    @Autowired
    private JLabel statusLabel;

    public Main(@Autowired AppProps props) {
        this.props = props;
        LOG.info("{}", props);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitle("SamyGO Channel Editor " + props.version());
        setLayout(new MigLayout("wrap, fill", "fill, grow", "[][fill, grow][]"));
    }

    @PostConstruct
    private void init() {
        // region menu
        setJMenuBar(
                new HMenuBar(
                        new HMenu("&File",
                                new HMenu("New",
                                        new HMenuItem(commands.get("newAir")),
                                        new HMenuItem(commands.get("newCable")),
                                        new HMenuItem(commands.get("newSate"))
                                ),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acOpen"), KeyStroke.getKeyStroke(KeyEvent.VK_O, META_KEY)),
                                new HMenuItem(commands.get("acSave"), KeyStroke.getKeyStroke(KeyEvent.VK_S, META_KEY)),
                                new HMenuItem(commands.get("acSaveAs")),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acOpenScm"), KeyStroke.getKeyStroke(KeyEvent.VK_P, META_KEY)),
                                new HMenuItem(commands.get("acSaveScm"), KeyStroke.getKeyStroke(KeyEvent.VK_C, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acExit"))
                        ),
                        new HMenu("&Edit",
                                new HMenuItem(commands.get("chanAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_N, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanEdit"), KeyStroke.getKeyStroke(KeyEvent.VK_E, META_KEY)),
                                new HMenuItem(commands.get("chanMove"), KeyStroke.getKeyStroke(KeyEvent.VK_M, META_KEY)),
                                new HMenuItem(commands.get("chanDel"), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanFavAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_UP, META_KEY)),
                                new HMenuItem(commands.get("chanFavDel"), KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanLockAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_UP, META_KEY | InputEvent.ALT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanLockDel"), KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, META_KEY | InputEvent.ALT_DOWN_MASK)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("addSkyFeed"))
                        ),
                        new HMenu("&Fav79",
                                new HMenuItem(commands.get("chanFav791"), KeyStroke.getKeyStroke(KeyEvent.VK_1, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav792"), KeyStroke.getKeyStroke(KeyEvent.VK_2, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav793"), KeyStroke.getKeyStroke(KeyEvent.VK_3, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav794"), KeyStroke.getKeyStroke(KeyEvent.VK_4, META_KEY | InputEvent.SHIFT_DOWN_MASK))
                        ),
                        new HMenu("&Help",
                                new HMenuItem(commands.get("helpOnline")),
                                new HMenuItem(commands.get("helpForum")),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acAbout"))
                        )
                )
        );
        // endregion

        mainTable.setFillsViewportHeight(true);
        mainTable.setShowGrid(true);
        mainTable.setRowSelectionAllowed(true);
        mainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // TODO drag'n'drop
        // TODO selections in table

        JPanel statusPanel = new JPanel(new MigLayout("fillx, wrap 2", "fill, grow"));
        statusLabel.setText("Ready.");
        modeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusPanel.add(statusLabel);
        statusPanel.add(modeLabel);

        QTableSearch<TableModel> tableSearch = new QTableSearch<>();
        mainTable.addPropertyChangeListener("rowSorter", evt ->
                tableSearch.newSorter((TableRowSorter<?>) evt.getNewValue())
        );
        add(tableSearch);
        add(new JScrollPane(mainTable));
        add(statusPanel);

        pack();
        setLocationRelativeTo(null);

        mainTable.setModel(servResolve.service().model());
        statusLabel.setText("Version: " + props.version() + " started with scmVersion: " + props.getScmVersion());
    }
}
