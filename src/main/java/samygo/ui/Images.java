package samygo.ui;

import javax.swing.*;

import samygo.util.ResourceContent;

public final class Images {

    public static final ImageIcon LOGO = new ResourceContent("/img/logo.png").asIcon(128);
    public static final ImageIcon FUNNY_LOGO = new ResourceContent("/img/tv-show.png").asIcon(64);

    public static final Icon CLEAR_FIELDS = new ResourceContent("/img/broom.png").asIcon(16);
    public static final Icon SEARCH = new ResourceContent("/img/search.png").asIcon(16);

    private Images() {
    }
}
