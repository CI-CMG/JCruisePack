package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagePanel extends JPanel implements ReactiveView {


  private static final String DATE_FORMAT = "yyyy-MM-dd";

  private static final String CRUISE_ID_LABEL = "Cruise ID";
  private static final String SEGMENT_LABEL = "Segment or Leg";
  private static final String EXISTING_RECORD_LABEL = "Load Existing Record";
  private static final String EXISTING_RECORD_DESCRIPTION_LABEL = "Select exiting record to update or enter new cruise ID.  Enter a segment / leg name if creating multiple packages per cruise.";
  private static final String DESTINATION_LABEL = "Destination (package will be created automatically)";
  private static final String SELECT_DIR_LABEL = "Select Directory";
  private static final String SHIP_LABEL = "Ship";
  private static final String SEA_LABEL = "IHO Sea Area";
  private static final String DEPARTURE_PORT_LABEL = "Departure Port";
  private static final String DEPARTURE_DATE_LABEL = "Departure Date (YYYY-MM-DD)";
  private static final String ARRIVAL_PORT_LABEL = "Arrival Port";
  private static final String ARRIVAL_DATE_LABEL = "Arrival Date (YYYY-MM-DD)";
  private static final String PROJECTS_LABEL = "Projects";
  private static final String ADDITIONAL_PROJECTS_LABEL = "Add Additional Project Menu";
  private static final String RELEASE_DATE_LABEL = "Default Public Release Date";

  private final PackageController packageController;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PackageModel packageModel;
  private final ProjectChooserPanel projectChooserPanel;
  private final ShipDatastore shipDatastore;
  private final PortDatastore portDatastore;
  private final SeaDatastore seaDatastore;

  private final JTextField cruiseIdField = new JTextField();
  private final JTextField segmentField = new JTextField();
  private final JComboBox<String> existingRecordList = new JComboBox<>(new String[]{"test", "real"});
  private final JTextField filePathField = new JTextField();
  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);
  private final JComboBox<DropDownItem> shipList = new JComboBox<>();
  private final JComboBox<DropDownItem> departurePortList = new JComboBox<>();
  private final DatePicker departureDateField = new DatePicker(configureDatePicker());
  private final JComboBox<DropDownItem> seaList = new JComboBox<>();
  private final JComboBox<DropDownItem> arrivalPortList = new JComboBox<>();
  private final DatePicker arrivalDateField = new DatePicker(configureDatePicker());
  private final JButton newProjectButton = new JButton(ADDITIONAL_PROJECTS_LABEL);
  private final DatePicker releaseDateField = new DatePicker(configureDatePicker());

  @Autowired
  public PackagePanel(
      PackageController packageController,
      ReactiveViewRegistry reactiveViewRegistry,
      PackageModel packageModel,
      ProjectChooserPanel projectChooserPanel,
      ShipDatastore shipDatastore, PortDatastore portDatastore, SeaDatastore seaDatastore) {
    this.packageController = packageController;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.packageModel = packageModel;
    this.projectChooserPanel = projectChooserPanel;
    this.shipDatastore = shipDatastore;
    this.portDatastore = portDatastore;
    this.seaDatastore = seaDatastore;

    // TODO set this in model
    existingRecordList.setSelectedIndex(0);
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

    departurePortList.setModel(new DefaultComboBoxModel<>(portDatastore.getPortDropDowns().toArray(new DropDownItem[0])));
    departurePortList.setSelectedItem(packageModel.getDeparturePort());

    arrivalPortList.setModel(new DefaultComboBoxModel<>(portDatastore.getPortDropDowns().toArray(new DropDownItem[0])));
    arrivalPortList.setSelectedItem(packageModel.getArrivalPort());

    seaList.setModel(new DefaultComboBoxModel<>(seaDatastore.getSeaDropDowns().toArray(new DropDownItem[0])));
    seaList.setSelectedItem(packageModel.getSea());

    // TODO set this in model
    shipList.setSelectedIndex(0);
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    // @formatter:off
    add(new JLabel(EXISTING_RECORD_DESCRIPTION_LABEL, null, SwingConstants.CENTER), configureLayout(0, 0, 3, c -> { c.gridwidth = GridBagConstraints.REMAINDER; c.ipady = 20;}));
    add(new JLabel(CRUISE_ID_LABEL), configureLayout(0, 1)); add(new JLabel(SEGMENT_LABEL), configureLayout(1, 1)); add(new JLabel(EXISTING_RECORD_LABEL), configureLayout(2, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(cruiseIdField, configureLayout(0, 2)); add(segmentField, configureLayout(1, 2)); add(existingRecordList, configureLayout(2, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(new JLabel(DESTINATION_LABEL), configureLayout(0, 3, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(filePathField, configureLayout(0, 4, 2)); add(dirSelectButton, configureLayout(2, 4, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(new JLabel(SHIP_LABEL), configureLayout(0, 5)); add(new JLabel(DEPARTURE_PORT_LABEL), configureLayout(1, 5)); add(new JLabel(DEPARTURE_DATE_LABEL), configureLayout(2, 5, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(shipList, configureLayout(0, 6)); add(departurePortList, configureLayout(1, 6)); add(departureDateField, configureLayout(2, 6, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(new JLabel(SEA_LABEL), configureLayout(0, 7)); add(new JLabel(ARRIVAL_PORT_LABEL), configureLayout(1, 7)); add(new JLabel(ARRIVAL_DATE_LABEL), configureLayout(2, 7, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(seaList, configureLayout(0, 8)); add(arrivalPortList, configureLayout(1, 8)); add(arrivalDateField, configureLayout(2, 8, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(new JLabel(PROJECTS_LABEL), configureLayout(0, 9, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(projectChooserPanel, configureLayout(0, 10, 3, c -> { c.gridwidth = GridBagConstraints.REMAINDER; c.weighty = 1.0;}));
    add(newProjectButton, configureLayout(0, 11, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(new JLabel(RELEASE_DATE_LABEL, null, SwingConstants.TRAILING), configureLayout(0, 12, 2)); add(releaseDateField, configureLayout(2, 12, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    // @formatter:on
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    cruiseIdField.addActionListener((evt) -> packageController.setCruiseId(cruiseIdField.getText()));
    seaList.addItemListener((evt) -> packageController.setSea((DropDownItem) evt.getItem()));
    arrivalPortList.addItemListener((evt) -> packageController.setArrivalPort((DropDownItem) evt.getItem()));
    departurePortList.addItemListener((evt) -> packageController.setDeparturePort((DropDownItem) evt.getItem()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_CRUISE_ID:
        updateTextField(cruiseIdField, evt);
        break;
      case Events.UPDATE_SEA:
        updateComboBox(seaList, evt);
        break;
      case Events.UPDATE_ARRIVAL_PORT:
        updateComboBox(arrivalPortList, evt);
        break;
      case Events.UPDATE_DEPARTURE_PORT:
        updateComboBox(departurePortList, evt);
        break;
      default:
        break;
    }
  }

  private static DatePickerSettings configureDatePicker() {
    DatePickerSettings datePickerSettings = new DatePickerSettings();
    datePickerSettings.setFormatForDatesCommonEra(DATE_FORMAT);
    datePickerSettings.setFormatsForParsing(new ArrayList<>(List.of(DateTimeFormatter.ofPattern(DATE_FORMAT))));
    return datePickerSettings;
  }
}
