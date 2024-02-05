package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagePanel extends JPanel {

  private static final String CRUISE_ID_LABEL = "Cruise ID";
  private static final String SEGMENT_LABEL = "Segment or Leg";
  private static final String TEST_LABEL = "Test";
  private static final String DESTINATION_LABEL = "Destination (package will be created automatically)";
  private static final String SELECT_DIR_LABEL = "Select Directory";
  private static final String SHIP_LABEL = "Ship";
  private static final String DEPARTURE_PORT_DATE_LABEL = "Departure Port and Date";
  private static final String PROJECTS_LABEL = "Projects";
  private static final String ADDITIONAL_PROJECTS_LABEL = "Add Additional Project Menu";
  private static final String RELEASE_DATE_LABEL = "Default Public Release Date";

  private final ProjectChooserPanel projectChooserPanel;

  private final JTextField cruiseIdField = new JTextField();
  private final JTextField segmentField = new JTextField();
  // TODO populate from data
  private final JComboBox<String> testList = new JComboBox<>(new String[]{"test", "real"});
  private final JTextField filePathField = new JTextField();
  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);
  // TODO populate from data
  private final JComboBox<String> shipList = new JComboBox<>(new String[]{"Alaska Knight"});
  // TODO populate from data
  private final String[] ports = new String[]{"Aaiun, EH", "Aasiaat, GL"};
  private final JComboBox<String> departurePortList = new JComboBox<>(ports);
  private final JTextField departureDateField = new JTextField();
  // TODO populate from data
  private final JComboBox<String> seaList = new JComboBox<>(new String[]{"Aegean Sea"});
  private final JComboBox<String> arrivalPortList = new JComboBox<>(ports);
  private final JTextField arrivalDateField = new JTextField();
  private final JButton newProjectButton = new JButton(ADDITIONAL_PROJECTS_LABEL);
  private final JTextField releaseDateField = new JTextField();

  @Autowired
  public PackagePanel(ProjectChooserPanel projectChooserPanel) {
    this.projectChooserPanel = projectChooserPanel;
  }

  @PostConstruct
  public void init() {
    testList.setSelectedIndex(0);
    shipList.setSelectedIndex(0);
    departurePortList.setSelectedIndex(0);
    seaList.setSelectedIndex(0);
    arrivalPortList.setSelectedIndex(0);

    setLayout(new GridBagLayout());
    add(new JLabel(CRUISE_ID_LABEL), configureLayout(0, 0));
    add(new JLabel(SEGMENT_LABEL), configureLayout(1, 0));
    add(new JLabel(TEST_LABEL), configureLayout(2, 0));
    add(cruiseIdField, configureLayout(0, 1));
    add(segmentField, configureLayout(1, 1));
    add(testList, configureLayout(2, 1));
    add(new JLabel(DESTINATION_LABEL), configureLayout(0, 2, 3));
    add(filePathField, configureLayout(0, 3, 2));
    add(dirSelectButton, configureLayout(2, 3));
    add(new JLabel(SHIP_LABEL), configureLayout(0, 4));
    add(new JLabel(DEPARTURE_PORT_DATE_LABEL), configureLayout(1, 4, 2));
    add(shipList, configureLayout(0, 5));
    add(departurePortList, configureLayout(1, 5));
    add(departureDateField, configureLayout(2, 5));
    add(seaList, configureLayout(0, 6));
    add(arrivalPortList, configureLayout(2, 5));
    add(arrivalDateField, configureLayout(2, 6));
    add(new JLabel(PROJECTS_LABEL), configureLayout(0, 7, 3));
    add(projectChooserPanel, configureLayout(0, 8, 3, c -> c.ipady = 120));
    add(newProjectButton, configureLayout(0, 9));
    add(new JLabel(RELEASE_DATE_LABEL), configureLayout(0, 10, 3));
    add(releaseDateField, configureLayout(0, 11, 3));
  }

}
