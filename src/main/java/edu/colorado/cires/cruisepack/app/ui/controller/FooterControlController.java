package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.service.PackerService;
import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackStateModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FooterControlController implements PropertyChangeListener {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(FooterControlController.class);

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final FooterControlModel footerControlModel;
  private final BeanFactory beanFactory;
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
  private final PackStateModel packStateModel;
  private final ErrorModel errorModel;

  @Autowired
  public FooterControlController(ReactiveViewRegistry reactiveViewRegistry, FooterControlModel footerControlModel,
      BeanFactory beanFactory, PeopleModel peopleModel, PackageModel packageModel, DatasetsModel datasetsModel,
      CruiseInformationModel cruiseInformationModel, OmicsModel omicsModel, InstrumentDatastore instrumentDatastore, 
      CruiseDataDatastore cruiseDataDatastore, ConfigurableApplicationContext applicationContext, ProjectDatastore projectDatastore,
      PortDatastore portDatastore, ShipDatastore shipDatastore, SeaDatastore seaDatastore, PersonDatastore personDatastore,
      OrganizationDatastore organizationDatastore, PackStateModel packStateModel, ErrorModel errorModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlModel = footerControlModel;
    this.beanFactory = beanFactory;
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
    this.packStateModel = packStateModel;
    this.errorModel = errorModel;
  }

  @PostConstruct
  public void init() {
    footerControlModel.addChangeListener(this);
    packStateModel.addChangeListener(this);
    errorModel.addChangeListener(this);
  }

  public synchronized void setStopButtonEnabled(boolean stopButtonEnabled) {
    footerControlModel.setStopButtonEnabled(stopButtonEnabled);
  }

  public synchronized void setSaveButtonEnabled(boolean saveButtonEnabled) {
    footerControlModel.setSaveButtonEnabled(saveButtonEnabled);
  }

  public synchronized void setPackageButtonEnabled(boolean packageButtonEnabled) {
    footerControlModel.setPackageButtonEnabled(packageButtonEnabled);
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

  public synchronized void startPackaging() {
    beanFactory.getBean(PackerService.class).startPacking();
  }
  
  public synchronized void stopPackaging() {
    packStateModel.setProcessing(false);
  }

  public void updateFormState(Cruise cruiseMetadata) {
    packageModel.updateFormState(cruiseMetadata, projectDatastore, portDatastore, shipDatastore, seaDatastore, cruiseDataDatastore);
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
    packageModel.restoreDefaults();
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
