package edu.colorado.cires.cruisepack.app.ui.util;

import java.beans.PropertyChangeEvent;
import java.util.Objects;
import javax.swing.JTextField;

public final class FieldUtils {

  public static void updateTextField(JTextField textField, PropertyChangeEvent evt) {
    String oldValue = textField.getText();
    String newValue = evt.getNewValue().toString();
    if (!Objects.equals(oldValue, newValue)) {
      textField.setText(newValue);
    }
  }

  private FieldUtils() {

  }
}
