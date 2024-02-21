package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledDatePickerPanel extends JPanel {


  private final DatePicker datePicker = new DatePicker();
  private final JLabel errorLabel = createErrorLabel();

  public LabeledDatePickerPanel(String title) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(errorLabel, configureLayout(0, 0));
    add(datePicker, configureLayout(0, 1));
  }

  public JLabel getErrorLabel() {
    return errorLabel;
  }

  public DatePicker getDatePicker() {
    return datePicker;
  }
}
