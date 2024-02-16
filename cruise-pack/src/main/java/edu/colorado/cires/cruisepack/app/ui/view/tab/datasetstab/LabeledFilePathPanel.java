package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledFilePathPanel extends JPanel {

  private static final String SELECT_DIR_LABEL = "...";

  private final JTextField textField = new JTextField();
  private final JButton fileSelectButton = new JButton(SELECT_DIR_LABEL);
//  private final JFileChooser fileChooser = new JFileChooser(SELECT_DIR_LABEL);

  public LabeledFilePathPanel(String title) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(textField, configureLayout(0, 0, c -> c.weightx = 1));
    add(fileSelectButton, configureLayout(1, 0, c -> c.weightx = 0));
  }

  public JTextField getTextField() {
    return textField;
  }
}
