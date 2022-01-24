package samygo.ui;

import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Строка ввода символов, нечувствительных к спецсимволам RexEx
 * Запрет ввода символов '{}[]'
 */
public final class RegExpTextField extends JTextField {

    private static final List<Character> EXCLUDE = Arrays.asList(
            '{', '}', '[', ']'
    );

    public RegExpTextField() {
        this("");
    }

    public RegExpTextField(String text) {
        super(text);
    }

    @Override
    protected Document createDefaultModel() {
        return new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str != null) {
                    for (char ch : str.toCharArray()) {
                        if (EXCLUDE.contains(ch))
                            return;
                    }
                    super.insertString(offs, str, a);
                }
            }
        };
    }
}
