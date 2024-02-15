package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledTextFieldPanel extends JPanel {

  private final JTextField field = new JTextField();

  public LabeledTextFieldPanel(String title) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(field);
  }

  public JTextField getField() {
    return field;
  }
}
