package samygo.ui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public final class IntTextField extends JTextField {

    private final int length;

    public IntTextField() {
        this("", Integer.MAX_VALUE);
    }

    public IntTextField(int length) {
        this("", length);
    }

    public IntTextField(String text, int length) {
        super(text);
        this.length = length;
    }

    @Override
    protected Document createDefaultModel() {
        return new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = getText(0, getLength());
                if (str != null && text.length() <= length && text.length() + str.length() <= length) {
                    for (char ch : str.toCharArray()) {
                        if (!Character.isDigit(ch))
                            return;
                    }
                    super.insertString(offs, str, a);
                }
            }
        };
    }
}
