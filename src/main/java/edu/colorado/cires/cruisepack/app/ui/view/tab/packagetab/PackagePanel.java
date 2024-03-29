package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBoxModel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateDatePicker;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.controller.ProjectController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemList;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
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
  private static final String ADDITIONAL_PROJECTS_LABEL = "Add Project";
  private static final String RELEASE_DATE_LABEL = "Default Public Release Date";

  private final PackageController packageController;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PackageModel packageModel;
  private final ShipDatastore shipDatastore;
  private final PortDatastore portDatastore;
  private final SeaDatastore seaDatastore;
  private final ProjectDatastore projectDatastore;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final FooterControlController footerControlController;
  private EditProjectDialog editProjectDialog;
  private final ProjectController projectController;

  private final DropDownItemList projectsField;
  private final JTextField cruiseIdField = new JTextField();
  private final JLabel cruiseIdErrorLabel = createErrorLabel();
  private final JTextField segmentField = new JTextField();
  private final JLabel segmentErrorLabel = createErrorLabel();
  private final JComboBox<DropDownItem> shipList = new JComboBox<>();
  private final JLabel shipErrorLabel = createErrorLabel();
  private final JComboBox<DropDownItem> departurePortList = new JComboBox<>();
  private final JLabel departurePortErrorLabel = createErrorLabel();
  private final JComboBox<DropDownItem> seaList = new JComboBox<>();
  private final JLabel seaErrorLabel = createErrorLabel();
  private final JComboBox<DropDownItem> arrivalPortList = new JComboBox<>();
  private final JLabel arrivalPortErrorLabel = createErrorLabel();
  private final DatePicker departureDateField = new DatePicker(configureDatePicker());
  private final JLabel departureDateErrorLabel = createErrorLabel();
  private final DatePicker arrivalDateField = new DatePicker(configureDatePicker());
  private final JLabel arrivalDateErrorLabel = createErrorLabel();
  private final DatePicker releaseDateField = new DatePicker(configureDatePicker());
  private final JLabel releaseDateErrorLabel = createErrorLabel();

  private final JTextField packageDirectoryField = new JTextField();
  private final JLabel packageDirectoryErrorLabel = createErrorLabel();

  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);

  private final JButton newProjectButton = new JButton("Create Project");
  private final JComboBox<DropDownItem> existingRecordList = new JComboBox<>();

  @Autowired
  public PackagePanel(
      PackageController packageController,
      ReactiveViewRegistry reactiveViewRegistry,
      PackageModel packageModel,
      ShipDatastore shipDatastore,
      PortDatastore portDatastore,
      SeaDatastore seaDatastore,
      ProjectDatastore projectDatastore,
      CruiseDataDatastore cruiseDataDatastore,
      FooterControlController footerControlController, ProjectController projectController
  ) {
    this.packageController = packageController;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.packageModel = packageModel;
    this.shipDatastore = shipDatastore;
    this.portDatastore = portDatastore;
    this.seaDatastore = seaDatastore;
    this.projectDatastore = projectDatastore;
    this.projectsField = new DropDownItemList(
      PROJECTS_LABEL,
      ADDITIONAL_PROJECTS_LABEL,
      projectDatastore.getAllProjectDropDowns(),
      ProjectDatastore.UNSELECTED_PROJECT,
        true
    );
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.footerControlController = footerControlController;
    this.projectController = projectController;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    existingRecordList.setModel(new DefaultComboBoxModel<>(cruiseDataDatastore.getDropDownItems().toArray(new DropDownItem[0])));
    existingRecordList.setSelectedItem(packageModel.getExistingRecord());

    cruiseIdField.setText(packageModel.getCruiseId());

    segmentField.setText(packageModel.getSegment());

    shipList.setModel(new DefaultComboBoxModel<>(shipDatastore.getShipDropDowns().toArray(new DropDownItem[0])));
    shipList.setSelectedItem(packageModel.getShip());

    departurePortList.setModel(new DefaultComboBoxModel<>(portDatastore.getPortDropDowns().toArray(new DropDownItem[0])));
    departurePortList.setSelectedItem(packageModel.getDeparturePort());

    arrivalPortList.setModel(new DefaultComboBoxModel<>(portDatastore.getPortDropDowns().toArray(new DropDownItem[0])));
    arrivalPortList.setSelectedItem(packageModel.getArrivalPort());

    seaList.setModel(new DefaultComboBoxModel<>(seaDatastore.getSeaDropDowns().toArray(new DropDownItem[0])));
    seaList.setSelectedItem(packageModel.getSea());

    departureDateField.setDate(packageModel.getDepartureDate());
    arrivalDateField.setDate(packageModel.getArrivalDate());
    releaseDateField.setDate(packageModel.getReleaseDate());

    packageDirectoryField.setText(packageModel.getPackageDirectory() == null ? null : packageModel.getPackageDirectory().toAbsolutePath().normalize().toString());

  }

  private void setupLayout() {
    setLayout(new GridBagLayout());

    // @formatter:off
    add(new JLabel(EXISTING_RECORD_DESCRIPTION_LABEL, null, SwingConstants.CENTER), configureLayout(0, 0, 3, c -> { c.gridwidth = GridBagConstraints.REMAINDER; c.ipady = 20;}));
    add(createLabelWithErrorPanel(CRUISE_ID_LABEL, cruiseIdErrorLabel), configureLayout(0, 1)); add(createLabelWithErrorPanel(SEGMENT_LABEL, segmentErrorLabel), configureLayout(1, 1)); add(new JLabel(EXISTING_RECORD_LABEL), configureLayout(2, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(cruiseIdField, configureLayout(0, 2)); add(segmentField, configureLayout(1, 2)); add(existingRecordList, configureLayout(2, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(createLabelWithErrorPanel(DESTINATION_LABEL, packageDirectoryErrorLabel), configureLayout(0, 3, 3, c -> { c.gridwidth = GridBagConstraints.REMAINDER; }));
    add(packageDirectoryField, configureLayout(0, 4, 2)); add(dirSelectButton, configureLayout(2, 4, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(createLabelWithErrorPanel(SHIP_LABEL, shipErrorLabel), configureLayout(0, 5)); add(createLabelWithErrorPanel(DEPARTURE_PORT_LABEL, departurePortErrorLabel), configureLayout(1, 5)); add(createLabelWithErrorPanel(DEPARTURE_DATE_LABEL, departureDateErrorLabel), configureLayout(2, 5, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(shipList, configureLayout(0, 6)); add(departurePortList, configureLayout(1, 6)); add(departureDateField, configureLayout(2, 6, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(createLabelWithErrorPanel(SEA_LABEL, seaErrorLabel), configureLayout(0, 7)); add(createLabelWithErrorPanel(ARRIVAL_PORT_LABEL, arrivalPortErrorLabel), configureLayout(1, 7)); add(createLabelWithErrorPanel(ARRIVAL_DATE_LABEL, arrivalDateErrorLabel), configureLayout(2, 7, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(seaList, configureLayout(0, 8)); add(arrivalPortList, configureLayout(1, 8)); add(arrivalDateField, configureLayout(2, 8, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    add(projectsField, configureLayout(0, 9, c -> { c.weighty = 100; c.gridwidth = GridBagConstraints.REMAINDER; c.insets = new Insets(0, 0, 0, 1); }));
    add(newProjectButton, configureLayout(0, 10, c -> { c.weightx = 100; c.weighty = 0; c.gridwidth = GridBagConstraints.REMAINDER; }));
    
    JPanel releaseDatePanel = new JPanel();
    releaseDatePanel.setLayout(new FlowLayout());
    releaseDatePanel.add(createLabelWithErrorPanel(RELEASE_DATE_LABEL, releaseDateErrorLabel));
    releaseDatePanel.add(releaseDateField);

    JPanel releaseDateWrapper = new JPanel();
    releaseDateWrapper.setLayout(new BorderLayout());
    releaseDateWrapper.add(releaseDatePanel, BorderLayout.EAST);
    
    add(releaseDateWrapper, configureLayout(0, 11, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    // @formatter:on
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);

    cruiseIdField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> packageController.setCruiseId(cruiseIdField.getText()));
    segmentField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> packageController.setSegment(segmentField.getText()));
    seaList.addItemListener((evt) -> packageController.setSea((DropDownItem) evt.getItem()));
    shipList.addItemListener((evt) -> packageController.setShip((DropDownItem) evt.getItem()));
    arrivalPortList.addItemListener((evt) -> packageController.setArrivalPort((DropDownItem) evt.getItem()));
    departurePortList.addItemListener((evt) -> packageController.setDeparturePort((DropDownItem) evt.getItem()));
    departureDateField.addDateChangeListener((evt) -> packageController.setDepartureDate(evt.getNewDate()));
    arrivalDateField.addDateChangeListener((evt) -> packageController.setArrivalDate(evt.getNewDate()));
    releaseDateField.addDateChangeListener((evt) -> packageController.setReleaseDate(evt.getNewDate()));
    dirSelectButton.addActionListener((evt) -> handleDirSelect());
    packageDirectoryField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> handleDirValue(packageDirectoryField.getText()));
    projectsField.addAddItemListener(packageController::addProject);
    projectsField.addRemoveItemListener(packageController::removeProject);
    existingRecordList.addItemListener((evt) -> packageController.setExistingRecord((DropDownItem) evt.getItem()));
    newProjectButton.addActionListener((evt) -> {
      if (editProjectDialog == null) {
        editProjectDialog = new EditProjectDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            reactiveViewRegistry,
            projectController
        );
        editProjectDialog.init();
      }
      
      editProjectDialog.pack();
      editProjectDialog.setVisible(true);
    });
  }

  private void handleDirValue(String value) {
    if (StringUtils.isBlank(value)) {
      packageController.setPackageDirectory(null);
    } else {
      Path path = Paths.get(value);
      packageController.setPackageDirectory(path.toAbsolutePath().normalize());
    }
  }

  private void handleDirSelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      packageController.setPackageDirectory(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
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
      case Events.UPDATE_SEA:
        updateComboBox(seaList, evt);
        break;
      case Events.UPDATE_ARRIVAL_PORT:
        updateComboBox(arrivalPortList, evt);
        break;
      case Events.UPDATE_DEPARTURE_PORT:
        updateComboBox(departurePortList, evt);
        break;
      case Events.UPDATE_SHIP:
        updateComboBox(shipList, evt);
        break;
      case Events.UPDATE_ARRIVAL_DATE:
        updateDatePicker(arrivalDateField, evt);
        break;
      case Events.UPDATE_DEPARTURE_DATE:
        updateDatePicker(departureDateField, evt);
        break;
      case Events.UPDATE_RELEASE_DATE:
        updateDatePicker(releaseDateField, evt);
        break;
      case Events.UPDATE_PACKAGE_DIRECTORY:
        updatePathField(packageDirectoryField, evt);
        break;
      case Events.UPDATE_CRUISE_ID_ERROR:
        updateLabelText(cruiseIdErrorLabel, evt);
        break;
      case Events.UPDATE_SEGMENT_ERROR:
        updateLabelText(segmentErrorLabel, evt);
        break;
      case Events.UPDATE_PACKAGE_DIRECTORY_ERROR:
        updateLabelText(packageDirectoryErrorLabel, evt);
        break;
      case Events.UPDATE_SEA_ERROR:
        updateLabelText(seaErrorLabel, evt);
        break;
      case Events.UPDATE_ARRIVAL_PORT_ERROR:
        updateLabelText(arrivalPortErrorLabel, evt);
        break;
      case Events.UPDATE_DEPARTURE_PORT_ERROR:
        updateLabelText(departurePortErrorLabel, evt);
        break;
      case Events.UPDATE_SHIP_ERROR:
        updateLabelText(shipErrorLabel, evt);
        break;
      case Events.UPDATE_ARRIVAL_DATE_ERROR:
        updateLabelText(arrivalDateErrorLabel, evt);
        break;
      case Events.UPDATE_DEPARTURE_DATE_ERROR:
        updateLabelText(departureDateErrorLabel, evt);
        break;
      case Events.UPDATE_RELEASE_DATE_ERROR:
        updateLabelText(releaseDateErrorLabel, evt);
        break;
      case Events.UPDATE_PROJECT_DATA_STORE:
        List<DropDownItem> options = projectDatastore.getAllProjectDropDowns();
        projectsField.updateOptions(options);
        break;
      case Events.ADD_PROJECT:
        projectsField.addItem((DropDownItemPanel) evt.getNewValue());
        break;
      case Events.REMOVE_PROJECT:
        projectsField.removeItem((DropDownItemPanel) evt.getOldValue());
        break;
      case Events.CLEAR_PROJECTS:
        projectsField.clearItems();
        break;
      case Events.UPDATE_PROJECTS_ERROR:
        updateLabelText(projectsField.getErrorLabel(), evt);
        break;
      case Events.UPDATE_CRUISE_DATA_STORE:
        updateComboBoxModel(existingRecordList, cruiseDataDatastore.getDropDownItems());
        break;
      case Events.UPDATE_EXISTING_RECORD:
        updateComboBox(existingRecordList, evt);
        DropDownItem dropDownItem = (DropDownItem) existingRecordList.getSelectedItem();
        if (dropDownItem != null) {
          Optional<CruiseData> maybeMetadata = cruiseDataDatastore.getByPackageId(dropDownItem.getId());
          if (maybeMetadata.isPresent()) {
            footerControlController.updateFormState(maybeMetadata.get());
          } else {
            footerControlController.restoreDefaultsGlobal();
          }
        }
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
