package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DatasetContentButtonRowPanel extends JPanel {

  private static final String INST_FILE_PATH_LABEL = "Instrument Files Path";
  private static final String SELECT_DIR_LABEL = "Select Directory";
  private static final String RAW_LABEL = "Raw";
  private static final String PROCESSED_LABEL = "Processed";
  private static final String PRODUCTS_LABEL = "Product";

  private final JTextField instrumentFilePathField = new JTextField();
  private final JButton selectDirectoryButton = new JButton(SELECT_DIR_LABEL);
  private final JRadioButton rawButton = new JRadioButton(RAW_LABEL);
  private final JRadioButton processedButton = new JRadioButton(PROCESSED_LABEL);
  private final JRadioButton productsButton = new JRadioButton(PRODUCTS_LABEL);


  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(new JLabel(INST_FILE_PATH_LABEL));
    add(instrumentFilePathField);
    add(selectDirectoryButton);
    add(rawButton);
    add(processedButton);
    add(productsButton);

    ButtonGroup dataLevelGroup = new ButtonGroup();
    dataLevelGroup.add(rawButton);
    dataLevelGroup.add(processedButton);
    dataLevelGroup.add(productsButton);
  }
}
