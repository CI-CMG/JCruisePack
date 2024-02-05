package edu.colorado.cires.cruisepack.prototype.ui.view;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.controller.Events;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class PackagePanel extends JPanel implements BaseViewPanel {

  private final DefaultController controller;

  private final JTextField cruiseIdField = new JTextField();
  private final JTextField segmentField = new JTextField();


  PackagePanel(DefaultController controller) {
    this.controller = controller;

    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    add(new JLabel("Cruise ID"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    add(new JLabel("Segment or Leg"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 0;
    add(new JLabel("Test"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    add(cruiseIdField, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 1;
    add(segmentField, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 1;
    JComboBox testList = new JComboBox(new String[]{"test", "real"});
    testList.setSelectedIndex(0);
    add(testList, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 2;
    c.gridwidth = 3;
    add(new JLabel("Destination (package will be created automatically)"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = 2;
    JTextField filePathField = new JTextField();
    add(filePathField, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 3;
    JButton dirSelectButton = new JButton("Select Directory");
    add(dirSelectButton, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 4;
    add(new JLabel("Ship"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 4;
    c.gridwidth = 2;
    add(new JLabel("Departure Port and Date"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 5;
    JComboBox shipList = new JComboBox(new String[]{"Alaska Knight"});
    shipList.setSelectedIndex(0);
    add(shipList, c);

    String[] ports = {"Aaiun, EH", "Aasiaat, GL"};

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 5;
    JComboBox portList = new JComboBox(ports);
    portList.setSelectedIndex(0);
    add(portList, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
    JTextField departureDateField = new JTextField();
    add(departureDateField, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 6;
    JComboBox seaList = new JComboBox(new String[]{"Aegean Sea"});
    seaList.setSelectedIndex(0);
    add(seaList, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 6;
    JComboBox arrivalPortList = new JComboBox(ports);
    arrivalPortList.setSelectedIndex(0);
    add(arrivalPortList, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 6;
    JTextField arrivalDateField = new JTextField();
    add(arrivalDateField, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 7;
    c.gridwidth = 3;
    add(new JLabel("Projects"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 8;
    c.gridwidth = 3;
    c.ipady = 120;
    add(new MultiComboBoxPanel(), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 9;
    JButton newProjectButton = new JButton("Add Additional Project Menu");
    add(newProjectButton, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 10;
    c.gridwidth = 3;
    add(new JLabel("Default Public Release Date"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 11;
    c.gridwidth = 3;
    JTextField releaseDateField = new JTextField();
    add(releaseDateField, c);

    initComponents();
  }

  private void initComponents() {
    cruiseIdField.addActionListener((evt) -> controller.changeCruiseId(cruiseIdField.getText()));
    segmentField.addActionListener((evt) -> controller.changeSegment(segmentField.getText()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_CRUISE_ID:
        updateTextField(cruiseIdField, evt);
        break;
      case Events.UPDATE_SEGMENT:
        updateTextField(segmentField, evt);
        break;
      default:
      break;
    }
  }

  private static void updateTextField(JTextField textField, PropertyChangeEvent evt) {
    String oldValue = textField.getText();
    String newValue = evt.getNewValue().toString();
    if (!Objects.equals(oldValue, newValue)) {
      textField.setText(newValue);
    }
  }

}
