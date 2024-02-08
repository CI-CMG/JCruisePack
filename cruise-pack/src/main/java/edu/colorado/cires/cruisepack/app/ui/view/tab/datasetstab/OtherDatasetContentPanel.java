package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class OtherDatasetContentPanel extends JPanel {

  private static final String PROCESSING_LEVEL_LABEL = "Processing Level";
  private static final String COMMENTS_LABEL = "Comments";
  private static final String INSTRUMENT_LABEL = "Instrument";

  private static final String RAW_LABEL = "Raw";
  private static final String PROCESSED_LABEL = "Processed";
  private static final String PRODUCTS_LABEL = "Products";

  private final JTextArea commentsField = new JTextArea();
  private final JRadioButton rawButton = new JRadioButton(RAW_LABEL);
  private final JRadioButton processedButton = new JRadioButton(PROCESSED_LABEL);
  private final JRadioButton productsButton = new JRadioButton(PRODUCTS_LABEL);
  private final ButtonGroup processingLevelGroup = new ButtonGroup();

  private final JComboBox<DropDownItem> instrumentField = new JComboBox<>();

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    processingLevelGroup.add(rawButton);
    processingLevelGroup.add(processedButton);
    processingLevelGroup.add(productsButton);
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);



    JPanel instrumentPanel = new JPanel();
    instrumentPanel.setLayout(new GridBagLayout());
    instrumentPanel.setBackground(Color.WHITE);
    instrumentPanel.setBorder(BorderFactory.createTitledBorder(INSTRUMENT_LABEL));
    instrumentPanel.add(instrumentField, configureLayout(0,0));


    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(BorderFactory.createTitledBorder(PROCESSING_LEVEL_LABEL));
    buttonPanel.add(rawButton);
    buttonPanel.add(processedButton);
    buttonPanel.add(productsButton);

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.setBackground(Color.WHITE);
    row1.add(instrumentPanel, configureLayout(0, 0));
    row1.add(buttonPanel, configureLayout(1, 0, c -> c.weightx = 0));

    add(row1, configureLayout(0, 0));

    JPanel commentsPanel = new JPanel();
    commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
    commentsPanel.setBackground(Color.WHITE);
    commentsPanel.setBorder(BorderFactory.createTitledBorder(COMMENTS_LABEL));
    commentsPanel.add(commentsField);
    add(commentsPanel, configureLayout(0, 1));

  }

  private void setupMvc() {

  }
}
