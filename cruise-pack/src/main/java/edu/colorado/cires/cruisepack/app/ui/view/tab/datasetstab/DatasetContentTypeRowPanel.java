package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DatasetContentTypeRowPanel extends JPanel {

  private static final String RELEASE_DATE_LABEL = "Public Release Date";
  private static final String CLOSE_LABEL = "Close";

  private final JButton closeButton = new JButton(CLOSE_LABEL);
  private final JTextField releaseDateField = new JTextField();

  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

//    JComboBox<String> dataTypeList = new JComboBox<>(model.getTypes().toArray(new String[0]));
//    dataTypeList.setSelectedIndex(model.getTypes().indexOf(dataType));
//    add(dataTypeList);

//    JComboBox<String> instrumentList = new JComboBox<>(model.getInstruments().toArray(new String[0]));
//    instrumentList.setSelectedIndex(0);
//    add(instrumentList);

    add(new JLabel(CLOSE_LABEL));
    add(releaseDateField);
    add(closeButton);
  }
}
