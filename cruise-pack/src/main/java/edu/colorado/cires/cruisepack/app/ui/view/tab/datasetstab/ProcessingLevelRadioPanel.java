package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ProcessingLevelRadioPanel extends JPanel {

  private static final String RAW_LABEL = "Raw";
  private static final String PROCESSED_LABEL = "Processed";
  private static final String PRODUCTS_LABEL = "Products";
  private static final String PROCESSING_LEVEL_LABEL = "Processing Level";

  private final JRadioButton rawButton = new JRadioButton(RAW_LABEL);
  private final JRadioButton processedButton = new JRadioButton(PROCESSED_LABEL);
  private final JRadioButton productsButton = new JRadioButton(PRODUCTS_LABEL);
  private final ButtonGroup processingLevelGroup = new ButtonGroup();

  public ProcessingLevelRadioPanel() {
    processingLevelGroup.add(rawButton);
    processingLevelGroup.add(processedButton);
    processingLevelGroup.add(productsButton);



    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(PROCESSING_LEVEL_LABEL));
    add(rawButton);
    add(processedButton);
    add(productsButton);
  }

  public ButtonGroup getProcessingLevelGroup() {
    return processingLevelGroup;
  }

  public String getSelectedButtonText() {
    for (Enumeration<AbstractButton> buttons = processingLevelGroup.getElements(); buttons.hasMoreElements();) {
      AbstractButton button = buttons.nextElement();
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return null;
  }

  public void addActionListener(ActionListener listener) {
    rawButton.addActionListener(listener);
    processedButton.addActionListener(listener);
    productsButton.addActionListener(listener);
  }
}
