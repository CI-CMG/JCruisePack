package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import java.util.function.Consumer;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextFormField extends JTextField {

    private TextFormField(String initialValue, Consumer<String> valueSetter) {
        super(initialValue);
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleChange(e, valueSetter);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleChange(e, valueSetter);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleChange(e, valueSetter);
            }
            
        });
    }

    public static TextFormField create(Consumer<String> valueSetter) {
        return new TextFormField(null, valueSetter);
    }

    public static TextFormField createWithInitialValue(String initialValue, Consumer<String> valueSetter) {
        return new TextFormField(initialValue, valueSetter);
    }

    private void handleChange(DocumentEvent event, Consumer<String> valueSetter) {
        Document document = event.getDocument();

        try {
            valueSetter.accept(
                document.getText(0, document.getLength())
            );
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
