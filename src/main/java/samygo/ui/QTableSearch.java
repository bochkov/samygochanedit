package samygo.ui;

import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public final class QTableSearch<T extends TableModel> extends JPanel implements DocumentListener {

    private final JTextField textField = new RegExpTextField();
    private final AtomicReference<TableRowSorter<?>> sorterRef = new AtomicReference<>();

    public QTableSearch() {
        this(null);
    }

    public QTableSearch(TableRowSorter<T> sorter) {
        setLayout(new MigLayout("fillx, insets 0", "[][fill, grow][]"));
        add(new JLabel("Search:"));
        add(textField);
        JButton clear = new JButton("Clear");
        clear.addActionListener(l -> textField.setText(""));
        add(clear);
        textField.getDocument().addDocumentListener(this);

        if (sorter != null)
            newSorter(sorter);
    }

    public void newSorter(TableRowSorter<?> sorter) {
        this.sorterRef.set(sorter);
        this.sorterRef.get().setStringConverter(new TableStringConverter() {
            @Override
            public String toString(TableModel model, int row, int column) {
                Object obj = model.getValueAt(row, column);
                return obj == null ?
                        "" :
                        obj.toString().toLowerCase();
            }
        });
        filter();
    }

    private void filter() {
        TableRowSorter<?> sorter = sorterRef.get();
        if (sorter != null) {
            try {
                sorter.setRowFilter(
                        RowFilter.regexFilter(textField.getText().toLowerCase())
                );
            } catch (Exception ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        filter();
    }
}
