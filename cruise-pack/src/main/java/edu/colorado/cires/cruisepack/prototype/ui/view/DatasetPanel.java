package edu.colorado.cires.cruisepack.prototype.ui.view;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class DatasetPanel extends JPanel implements BaseViewPanel {

  private final DefaultController controller;
  private final String id;
  private final JButton closeButton = new JButton("Close");
  private final JButton selectDirectoryButton = new JButton("Select Directory");
  private final JRadioButton rawButton = new JRadioButton("Raw");
  private final JRadioButton processedButton = new JRadioButton("Processed");
  private final JRadioButton productsButton = new JRadioButton("Products");

  private final String dataType;

  public DatasetPanel(
      String dataType,
      DataWidgetModel model,
      DefaultController controller){
    this.dataType = dataType;
    this.controller = controller;
    id = model.getId();
    setLayout(new BorderLayout());

    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

    JPanel row1 = new JPanel();
    row1.setLayout(new BoxLayout(row1, BoxLayout.LINE_AXIS));

    JComboBox<String> dataTypeList = new JComboBox<>(model.getTypes().toArray(new String[0]));
    dataTypeList.setSelectedIndex(model.getTypes().indexOf(dataType));
    row1.add(dataTypeList);

    JComboBox<String> instrumentList = new JComboBox<>(model.getInstruments().toArray(new String[0]));
    instrumentList.setSelectedIndex(0);
    row1.add(instrumentList);

    row1.add(new JLabel("Public Release Date"));
    row1.add(new JTextField());
    row1.add(closeButton);

    content.add(row1);

    JPanel row2 = new JPanel();
    row2.setLayout(new BoxLayout(row2, BoxLayout.LINE_AXIS));

    row2.add(new JLabel("Instrument Files Path"));
    row2.add(new JTextField());
    row2.add(selectDirectoryButton);
    row2.add(rawButton);
    row2.add(processedButton);
    row2.add(productsButton);

    ButtonGroup dataLevelGroup = new ButtonGroup();
    dataLevelGroup.add(rawButton);
    dataLevelGroup.add(processedButton);
    dataLevelGroup.add(productsButton);

    content.add(row2);

    JPanel row3 = new JPanel();
    row3.setLayout(new BoxLayout(row3, BoxLayout.LINE_AXIS));

    row3.add(new JButton("Add Data Comment"));
    row3.add(new JTextArea());

    content.add(row3);

    if (model.getCustomizer() != null) {
      content.add(model.getCustomizer().customize());
    }

    add(content, BorderLayout.NORTH);


    dataTypeList.addItemListener((evt) -> controller.changeDataset(id, (String) evt.getItem()));
    closeButton.addActionListener((evt) -> controller.removeDataset(id));
    selectDirectoryButton.addActionListener((evt) -> {});

    rawButton.setSelected(true);

    rawButton.addActionListener((evt) -> {});
    processedButton.addActionListener((evt) -> {});
    productsButton.addActionListener((evt) -> {});
  }

  public String getDataType() {
    return dataType;
  }

  public String getId() {
    return id;
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }
}
