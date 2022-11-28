package samygo.frm;

import java.awt.*;
import java.awt.desktop.QuitStrategy;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samygo.action.Command;
import samygo.infra.AppProps;
import samygo.infra.ModeKeep;
import samygo.ui.*;
import sb.bdev.ui.swing.HMenu;
import sb.bdev.ui.swing.HMenuBar;
import sb.bdev.ui.swing.HMenuItem;
import sb.bdev.ui.swing.HPopupMenu;

/**
 * The Main class does create the application and controls all interaction
 */
@Slf4j
@Component
public final class Main extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int META_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    private static final KeyStroke DEL_KEY = System.getProperty("os.name").toLowerCase().contains("mac") ?
            KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0) :
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

    private final AppProps props;

    @Autowired
    private JTable mainTable;
    @Autowired
    private Map<String, Command> commands;
    @Autowired
    private JLabel modeLabel;
    @Autowired
    private JLabel statusLabel;
    @Autowired
    private ModeKeep modeKeep;
    @Autowired
    private TableTransfer tableTransfer;

    public Main(@Autowired AppProps props) {
        this.props = props;
        LOG.info("{}", props);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Images.LOGO.getImage());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitle("SamyGO Channel Editor " + props.version());
        setLayout(new MigLayout("wrap, fill, insets 5", "fill, grow", "[][fill, grow][]"));
        if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE))
            Taskbar.getTaskbar().setIconImage(Images.FUNNY_LOGO.getImage());
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
                                new HMenuItem(commands.get("acOpenScm"), KeyStroke.getKeyStroke(KeyEvent.VK_O, META_KEY)),
                                new HMenuItem(commands.get("acSaveScm"), KeyStroke.getKeyStroke(KeyEvent.VK_S, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acOpen")),
                                new HMenuItem(commands.get("acSave")),
                                new HMenuItem(commands.get("acSaveAs")),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("acExit"))
                        ),
                        new HMenu("&Edit",
                                new HMenuItem(commands.get("chanAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_N, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanEdit"), KeyStroke.getKeyStroke(KeyEvent.VK_E, META_KEY)),
                                new HMenuItem(commands.get("chanMove"), KeyStroke.getKeyStroke(KeyEvent.VK_M, META_KEY)),
                                new HMenuItem(commands.get("chanDel"), DEL_KEY),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanFavAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_UP, META_KEY)),
                                new HMenuItem(commands.get("chanFavDel"), KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, META_KEY)),
                                new HMenuItem("---"),
                                new HMenuItem(commands.get("chanLockAdd"), KeyStroke.getKeyStroke(KeyEvent.VK_UP, META_KEY | InputEvent.ALT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanLockDel"), KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, META_KEY | InputEvent.ALT_DOWN_MASK))
//                                new HMenuItem("---"),
//                                new HMenuItem(commands.get("addSkyFeed"))
                        ),
                        new HMenu("&Fav79",
                                new HMenuItem(commands.get("chanFav791"), KeyStroke.getKeyStroke(KeyEvent.VK_1, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav792"), KeyStroke.getKeyStroke(KeyEvent.VK_2, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav793"), KeyStroke.getKeyStroke(KeyEvent.VK_3, META_KEY | InputEvent.SHIFT_DOWN_MASK)),
                                new HMenuItem(commands.get("chanFav794"), KeyStroke.getKeyStroke(KeyEvent.VK_4, META_KEY | InputEvent.SHIFT_DOWN_MASK))
                        ),
                        new HMenu("&Help",
                                new HMenuItem(commands.get("acAbout"))
                        )
                )
        );
        // endregion
        HPopupMenu popupMenu = new HPopupMenu(
                new HMenuItem(commands.get("chanAdd")),
                new HMenuItem("---"),
                new HMenuItem(commands.get("chanEdit")),
                new HMenuItem(commands.get("chanMove")),
                new HMenuItem(commands.get("chanDel")),
                new HMenuItem("---"),
                new HMenuItem(commands.get("chanFavAdd")),
                new HMenuItem(commands.get("chanFavDel")),
                new HMenuItem("---"),
                new HMenuItem(commands.get("chanLockAdd")),
                new HMenuItem(commands.get("chanLockDel"))
        );

        mainTable.setComponentPopupMenu(popupMenu);
        mainTable.setFillsViewportHeight(true);
        mainTable.setShowGrid(true);
        mainTable.setRowSelectionAllowed(true);
        mainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // TODO drag'n'drop text & files
        mainTable.setDragEnabled(true);
        mainTable.setDropMode(DropMode.INSERT_ROWS);
        mainTable.setTransferHandler(tableTransfer);
        mainTable.setSelectionModel(new UseTableSelectionModel(commands, modeKeep));
        mainTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    commands.get("chanEdit").actionPerformed(null);
            }
        });

        JPanel statusPanel = new JPanel(new MigLayout("fillx, wrap 2, insets 0", "fill, grow"));
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

        commands.get("newCable").actionPerformed(null); // set model into table
        statusLabel.setText("Version: " + props.version() + " started with scmVersion: " + props.getScmVersion());

        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_ABOUT))
            Desktop.getDesktop().setAboutHandler(e -> commands.get("acAbout").actionPerformed(null));
        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_QUIT_STRATEGY))
            Desktop.getDesktop().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            mainTable.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
