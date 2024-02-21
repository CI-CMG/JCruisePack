package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledTextFieldPanel extends JPanel {

  private final JTextField field = new JTextField();
  private final JLabel errorLabel = createErrorLabel();

  public LabeledTextFieldPanel(String title) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(errorLabel, configureLayout(0, 0));
    add(field, configureLayout(0, 1));
  }

  public JLabel getErrorLabel() {
    return errorLabel;
  }

  public JTextField getField() {
    return field;
  }
}
