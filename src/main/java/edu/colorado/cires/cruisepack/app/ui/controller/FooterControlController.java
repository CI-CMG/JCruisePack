package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.migration.SqliteMigrator;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.service.PackagingValidationService;
import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.service.pack.ClearJobsPublisher;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FooterControlController implements PropertyChangeListener {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(FooterControlController.class);

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final FooterControlModel footerControlModel;
  private final PeopleModel peopleModel;
  private final PackageModel packageModel;
  private final DatasetsModel datasetsModel;
  private final CruiseInformationModel cruiseInformationModel;
  private final OmicsModel omicsModel;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final InstrumentDatastore instrumentDatastore;
  private final ConfigurableApplicationContext applicationContext;
  private final ProjectDatastore projectDatastore;
  private final PortDatastore portDatastore;
  private final ShipDatastore shipDatastore;
  private final SeaDatastore seaDatastore;
  private final PersonDatastore personDatastore;
  private final OrganizationDatastore organizationDatastore;
  private final ErrorModel errorModel;
  private final PackagingValidationService validationService;
  private final ClearJobsPublisher clearJobsPublisher;
  private final QueueModel queueModel;
  private final SqliteMigrator sqliteMigrator;

  @Autowired
  public FooterControlController(ReactiveViewRegistry reactiveViewRegistry, FooterControlModel footerControlModel,
      PeopleModel peopleModel, PackageModel packageModel, DatasetsModel datasetsModel,
      CruiseInformationModel cruiseInformationModel, OmicsModel omicsModel, InstrumentDatastore instrumentDatastore, 
      CruiseDataDatastore cruiseDataDatastore, ConfigurableApplicationContext applicationContext, ProjectDatastore projectDatastore,
      PortDatastore portDatastore, ShipDatastore shipDatastore, SeaDatastore seaDatastore, PersonDatastore personDatastore,
      OrganizationDatastore organizationDatastore, ErrorModel errorModel, PackagingValidationService validationService,
      ClearJobsPublisher clearJobsPublisher, QueueModel queueModel, SqliteMigrator sqliteMigrator) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlModel = footerControlModel;
    this.peopleModel = peopleModel;
    this.packageModel = packageModel;
    this.datasetsModel = datasetsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.omicsModel = omicsModel;
    this.instrumentDatastore = instrumentDatastore;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.applicationContext = applicationContext;
    this.projectDatastore = projectDatastore;
    this.portDatastore = portDatastore;
    this.shipDatastore = shipDatastore;
    this.seaDatastore = seaDatastore;
    this.personDatastore = personDatastore;
    this.organizationDatastore = organizationDatastore;
    this.errorModel = errorModel;
    this.validationService = validationService;
    this.clearJobsPublisher = clearJobsPublisher;
    this.queueModel = queueModel;
    this.sqliteMigrator = sqliteMigrator;
  }

  @PostConstruct
  public void init() {
    footerControlModel.addChangeListener(this);
    errorModel.addChangeListener(this);
    cruiseDataDatastore.addChangeListener(this);
    personDatastore.addChangeListener(this);
    organizationDatastore.addChangeListener(this);
    sqliteMigrator.addChangeListener(this);
  }

  public synchronized void setStopButtonEnabled(boolean stopButtonEnabled) {
    footerControlModel.setStopButtonEnabled(stopButtonEnabled);
  }

  public synchronized void setSaveWarningDialogueVisible(boolean saveWarningDialogueVisible) {
    footerControlModel.setSaveWarningDialogueVisible(saveWarningDialogueVisible);
  }

  public synchronized void setSaveExitAppDialogueVisible(boolean saveExitAppDialogueVisible) {
    footerControlModel.setSaveExitAppDialogueVisible(saveExitAppDialogueVisible);
  }
  
  public synchronized void setSaveOrUpdateDialogVisible(boolean saveOrUpdateDialogVisible) {
    footerControlModel.setSaveOrUpdateDialogVisible(saveOrUpdateDialogVisible);
  }
  
  public synchronized void setPackageIdCollisionDialogVisible(boolean packageIdCollisionDialogVisible) {
    footerControlModel.setPackageIdCollisionDialogVisible(packageIdCollisionDialogVisible);
  }
  
  public synchronized void addToQueue() {
    validationService.validate().ifPresent(
        packJob -> queueModel.addToQueue(
            new PackJobPanel(packJob)
        )
    );
  }
  
  public synchronized void stopPackaging() {
    clearJobsPublisher.publish(this, queueModel::clearQueue);
  }

  public void updateFormState(Cruise cruiseMetadata) {
    packageModel.restoreDefaults(true);
    peopleModel.restoreDefaults();
    cruiseInformationModel.restoreDefaults();
    omicsModel.restoreDefaults();
    datasetsModel.restoreDefaults();
    
    packageModel.updateFormState(
        cruiseMetadata,
        projectDatastore.getAllProjectDropDowns(),
        portDatastore.getPortDropDowns(),
        shipDatastore.getShipDropDowns(),
        seaDatastore.getSeaDropDowns()
    );
    peopleModel.updateFormState(
        cruiseMetadata.getScientists(),
        personDatastore.getEnabledPersonDropDowns(),
        cruiseMetadata.getFunders(),
        cruiseMetadata.getSponsors(),
        organizationDatastore.getEnabledOrganizationDropDowns(),
        cruiseMetadata.getMetadataAuthor()
    );
    cruiseInformationModel.updateFormState(cruiseMetadata);
    omicsModel.updateFormState(cruiseMetadata);
    datasetsModel.updateFormState(cruiseMetadata);
  }

  public void restoreDefaultsGlobal() {
    peopleModel.restoreDefaults();
    packageModel.restoreDefaults(false);
    datasetsModel.restoreDefaults();
    cruiseInformationModel.restoreDefaults();
    omicsModel.restoreDefaults();
  }

  public synchronized void saveForms(boolean fromExitPrompt) {
    if (packageModel.getCruiseId() == null) {
      setSaveWarningDialogueVisible(true);
    } else {
      PackJob packJob = PackJobUtils.create(packageModel, peopleModel, omicsModel, cruiseInformationModel, datasetsModel, instrumentDatastore, personDatastore);
      if (packageModel.getExistingRecord() != null && !packageModel.getExistingRecord().equals(CruiseDataDatastore.UNSELECTED_CRUISE) && !packJob.getPackageId().equals(packageModel.getExistingRecord().getValue())) {
        setSaveOrUpdateDialogVisible(true);
      } else {
        boolean success = save(packJob);
        if (success) {
          postSave(packJob.getPackageId(), fromExitPrompt);
        }
      }
    }
  }
  
  public synchronized void addIgnoredWarningMessage(String message) {
    footerControlModel.addIgnoreWarningMessage(message);
  }
  
  public void create(boolean fromExitPrompt) {
    PackJob packJob = PackJobUtils.create(packageModel, peopleModel, omicsModel, cruiseInformationModel, datasetsModel, instrumentDatastore, personDatastore);
    boolean success = save(packJob);
    if (success) {
      postSave(packJob.getPackageId(), fromExitPrompt);
    }
  }
  
  public void update(boolean fromExitPrompt) {
    PackJob packJob = PackJobUtils.create(packageModel, peopleModel, omicsModel, cruiseInformationModel, datasetsModel, instrumentDatastore, personDatastore);
    boolean success = save(packJob);
    if (success) {
      delete(packageModel.getExistingRecord().getValue());
      postSave(packJob.getPackageId(), fromExitPrompt);
    }
  }
  
  private void postSave(String packageId, boolean fromExitPrompt) {
    cruiseDataDatastore.load();
    cruiseDataDatastore.getByPackageId(packageId).ifPresent(this::updateFormState);
    emitPackageId(packageId);
    if (!fromExitPrompt) {
      setSaveExitAppDialogueVisible(true);
    }
  }
  
  private boolean save(PackJob packJob) {
    String packageId = packJob.getPackageId();
    boolean exists = cruiseDataDatastore.getByPackageId(packageId).isPresent();
    String selectedValue = packageModel.getExistingRecord().getValue();
    if (exists && !packageId.equals(selectedValue)) {
      emitPackageId(packageId);
      setPackageIdCollisionDialogVisible(true);
      return false;
    }
    try {
      List<String> projects = packJob.getProjects();
      projects.forEach(project -> {
        Optional<Project> maybeExistingProject = projectDatastore.findByName(project);
        if (maybeExistingProject.isEmpty()) {
          Project newProject = new Project();
          newProject.setUuid(UUID.randomUUID().toString());
          newProject.setName(project);
          newProject.setUse(true);
          projectDatastore.save(newProject);
        }
      });
      
      cruiseDataDatastore.saveCruise(packJob);
    } catch (Exception e) {
      LOGGER.error("Failed to save cruise: {}", packageId, e);
      errorModel.emitErrorMessage(String.format(
          "Failed to save %s: %s", packageId, e.getMessage()
      ));
    }
    return true;
  }
  
  private void delete(String packageId) {
    try {
      cruiseDataDatastore.delete(packageId);
    } catch (Exception e) {
      LOGGER.error("Failed to delete cruise: {}", packageId);
      errorModel.emitErrorMessage(String.format(
          "Failed to delete %s: %s", packageId, e.getMessage()
      ));
    }
  }

  private void emitPackageId(String packageId) {
    footerControlModel.emitPackageId(packageId);
  }

  public void exitApplication() {
    int exitCode = SpringApplication.exit(applicationContext, () -> 0);
    System.exit(exitCode);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }


}
