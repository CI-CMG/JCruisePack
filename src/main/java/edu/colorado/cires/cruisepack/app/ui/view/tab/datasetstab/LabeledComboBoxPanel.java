package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledComboBoxPanel extends JPanel {

  private static final String INSTRUMENT_LABEL = "Instrument";

  private final JComboBox<DropDownItem> instrumentField = new JComboBox<>();
  private final JLabel errorLabel = createErrorLabel();

  public LabeledComboBoxPanel() {
    this(INSTRUMENT_LABEL);
  }

  public LabeledComboBoxPanel(String title) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(errorLabel, configureLayout(0, 0));
    add(instrumentField, configureLayout(0, 1));
  }

  public JLabel getErrorLabel() {
    return errorLabel;
  }

  public JComboBox<DropDownItem> getInstrumentField() {
    return instrumentField;
  }
}
