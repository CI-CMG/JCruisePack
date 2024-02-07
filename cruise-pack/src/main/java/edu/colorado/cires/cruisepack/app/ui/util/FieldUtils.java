package edu.colorado.cires.cruisepack.app.ui.util;

import com.github.lgooddatepicker.components.DatePicker;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public final class FieldUtils {

  public static void updateTextField(JTextField textField, PropertyChangeEvent evt) {
    updateTextField(textField, evt, e -> (String) e.getNewValue());
  }

  public static void updateTextField(JTextField textField, PropertyChangeEvent evt, Function<PropertyChangeEvent, String> transform) {
    String oldValue = textField.getText();
    String newValue = transform.apply(evt);
    if (!Objects.equals(oldValue, newValue)) {
      textField.setText(newValue);
    }
  }

  public static void updateDatePicker(DatePicker datePicker, PropertyChangeEvent evt) {
    LocalDate oldValue = datePicker.getDate();
    LocalDate newValue = (LocalDate) evt.getNewValue();
    if (!Objects.equals(oldValue, newValue)) {
      datePicker.setDate(newValue);
    }
  }

  public static void updateComboBox(JComboBox<DropDownItem> comboBox, PropertyChangeEvent evt) {
    DropDownItem oldValue = (DropDownItem) comboBox.getSelectedItem();
    DropDownItem newValue = (DropDownItem) evt.getNewValue();
    if (!Objects.equals(oldValue, newValue)) {
      comboBox.setSelectedItem(newValue);
    }
  }

  private FieldUtils() {

  }
}
