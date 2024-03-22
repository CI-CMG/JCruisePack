package edu.colorado.cires.cruisepack.app.ui.util;

import com.github.lgooddatepicker.components.DatePicker;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.AbstractButton;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

  public static String getSelectedButtonText(ButtonGroup buttonGroup) {
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
      AbstractButton button = buttons.nextElement();
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return null;
  }

  public static void setSelectedButton(ButtonGroup buttonGroup, String text) {
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
      AbstractButton button = buttons.nextElement();
      button.setSelected(Objects.equals(text, button.getText()));
    }
  }

  public static void updateRadioButtonGroup(ButtonGroup buttonGroup, PropertyChangeEvent evt) {
    String oldValue = getSelectedButtonText(buttonGroup);
    String newValue = (String) evt.getNewValue();
    if (!Objects.equals(oldValue, newValue)) {
      setSelectedButton(buttonGroup, newValue);
    }
  }
  
  public static void updateButtonEnabled(ButtonModel button, PropertyChangeEvent evt) {
    boolean enable = (boolean) evt.getNewValue();
    boolean currentEnabled = button.isEnabled();
    if (enable != currentEnabled) {
      button.setEnabled(enable);
    }
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
    if (!Objects.equals(oldValue, newValue)) {
      textArea.setText(newValue);
    }
  }

  public static void updateStatefulRadioButton(StatefulRadioButton radioButton, PropertyChangeEvent evt) {
    Boolean oldValue = (Boolean) evt.getOldValue();
    Boolean newValue = (Boolean) evt.getNewValue();
    if (!Objects.equals(newValue, oldValue)) {
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

  public static void updateLabelText(JLabel label, PropertyChangeEvent evt) {
    String newValue = (String) evt.getNewValue();
    String oldValue = (String) evt.getOldValue();

    if (!Objects.equals(oldValue, newValue)) {
      label.setText(newValue);
    }
  }

  public static JLabel createErrorLabel() {
    JLabel label = new JLabel();
    label.setText("");
    label.setForeground(new Color(Color.RED.getRGB()));
    return label;
  }

  public static JPanel createLabelWithErrorPanel(String labelText, JLabel errorLabel) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(10, 0));
    panel.add(new JLabel(labelText), BorderLayout.LINE_START);
    panel.add(errorLabel, BorderLayout.CENTER);
    return panel;
  }

  public static void updateComboBoxModel(JComboBox<DropDownItem> comboBox, List<DropDownItem> items) {
    DropDownItem selectedItem = (DropDownItem) comboBox.getSelectedItem();
    comboBox.setModel(new DefaultComboBoxModel<>(items.toArray(new DropDownItem[0])));
    boolean selectedItemInItems = selectedItem != null && items.stream()
        .anyMatch(dd -> dd.getId().equals(selectedItem.getId()) && dd.getValue().equals(selectedItem.getValue()));
    
    if (!selectedItemInItems) {
      comboBox.setSelectedItem(items.get(0));
    } else {
      comboBox.setSelectedItem(selectedItem);
    }
  }
  
  public static void updateProgressBarModel(BoundedRangeModel model, PropertyChangeEvent event) {
    if (event.getNewValue() instanceof Integer) {
      int newValue = (int) event.getNewValue();
      int oldValue = (int) event.getOldValue();
      if (newValue != oldValue) {
        model.setValue(newValue);
      }
    }
  }

  private FieldUtils() {

  }
}
