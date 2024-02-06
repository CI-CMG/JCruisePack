package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagePanel extends JPanel implements ReactiveView {

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

  private final PackageController packageController;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PackageModel packageModel;
  private final ProjectChooserPanel projectChooserPanel;
  private final ShipDatastore shipDatastore;

  private final JTextField cruiseIdField = new JTextField();
  private final JTextField segmentField = new JTextField();
  // TODO populate from data
  private final JComboBox<String> testList = new JComboBox<>(new String[]{"test", "real"});
  private final JTextField filePathField = new JTextField();
  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);
  private final JComboBox<DropDownItem> shipList = new JComboBox<>();
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
  public PackagePanel(
      PackageController packageController,
      ReactiveViewRegistry reactiveViewRegistry,
      PackageModel packageModel,
      ProjectChooserPanel projectChooserPanel,
      ShipDatastore shipDatastore) {
    this.packageController = packageController;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.packageModel = packageModel;
    this.projectChooserPanel = projectChooserPanel;
    this.shipDatastore = shipDatastore;

    // TODO set this in model
    testList.setSelectedIndex(0);
    departurePortList.setSelectedIndex(0);
    seaList.setSelectedIndex(0);
    arrivalPortList.setSelectedIndex(0);
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    cruiseIdField.setText(packageModel.getCruiseId());
    shipList.setModel(new DefaultComboBoxModel<>(shipDatastore.getShipDropDowns().toArray(new DropDownItem[0])));
    // TODO set this in model
    shipList.setSelectedIndex(0);
  }

  private void setupLayout() {
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

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    cruiseIdField.addActionListener((evt) -> packageController.setCruiseId(cruiseIdField.getText()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_CRUISE_ID:
        updateTextField(cruiseIdField, evt);
        break;
      default:
        break;
    }
  }
}
