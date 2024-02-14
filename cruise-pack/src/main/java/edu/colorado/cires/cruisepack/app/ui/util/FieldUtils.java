package edu.colorado.cires.cruisepack.app.ui.util;

import com.github.lgooddatepicker.components.DatePicker;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.AppendableTableWithSelections;

import java.util.List;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class FieldUtils {

  public static void updatePathField(JTextField textField, PropertyChangeEvent evt) {
    updateTextField(textField, evt, e -> {
      Path path = (Path) e.getNewValue();
      if (path == null) {
        return null;
      }
      return path.toAbsolutePath().normalize().toString();
    });
  }

  public static void updateTextField(JTextField textField, PropertyChangeEvent evt) {
    updateTextField(textField, evt, e -> (String) e.getNewValue());
  }

  public static void updateTextField(JTextArea textField, PropertyChangeEvent evt) {
    updateTextField(textField, evt, e -> (String) e.getNewValue());
  }

  public static void updateTextField(JTextField textField, PropertyChangeEvent evt, Function<PropertyChangeEvent, String> transform) {
    String oldValue = textField.getText();
    String newValue = transform.apply(evt);
    if (!Objects.equals(oldValue, newValue)) {
      textField.setText(newValue);
    }
  }

  public static void updateTextField(JTextArea textField, PropertyChangeEvent evt, Function<PropertyChangeEvent, String> transform) {
    String oldValue = textField.getText();
    String newValue = transform.apply(evt);
    if (!Objects.equals(oldValue, newValue)) {
      textField.setText(newValue);
    }
  }

  public static void updateTextArea(JTextArea textArea, PropertyChangeEvent evt) {
    String oldValue = (String) evt.getOldValue();
    String newValue = (String) evt.getNewValue();
    if (!oldValue.equals(newValue)) {
      textArea.setText(newValue);
    }
  }

  public static void updateStatefulRadioButton(StatefulRadioButton radioButton, PropertyChangeEvent evt) {
    Boolean oldValue = (Boolean) evt.getOldValue();
    Boolean newValue = (Boolean) evt.getNewValue();
    if (!oldValue.equals(newValue)) {
      radioButton.setSelectedValue(newValue);
    }
  }

  public static void updateCheckBox(JCheckBox checkBox, PropertyChangeEvent evt) {
    boolean oldValue = (boolean) evt.getOldValue();
    boolean newValue = (boolean) evt.getNewValue();
    if (oldValue != newValue) {
      checkBox.setSelected(newValue);
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

  public static void updateAppendableTable(AppendableTableWithSelections appendableTable, PropertyChangeEvent evt) {
    appendableTable.redrawComboboxes((List<DropDownItem>) evt.getNewValue());
  }

  private FieldUtils() {

  }
}
