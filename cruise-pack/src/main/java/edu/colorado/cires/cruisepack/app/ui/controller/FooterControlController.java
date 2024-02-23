package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackerService;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FooterControlController implements PropertyChangeListener {

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

  @Autowired
  public FooterControlController(ReactiveViewRegistry reactiveViewRegistry, FooterControlModel footerControlModel,
      BeanFactory beanFactory, PeopleModel peopleModel, PackageModel packageModel, DatasetsModel datasetsModel,
      CruiseInformationModel cruiseInformationModel, OmicsModel omicsModel, InstrumentDatastore instrumentDatastore, CruiseDataDatastore cruiseDataDatastore, ConfigurableApplicationContext applicationContext) {
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
  }

  @PostConstruct
  public void init() {
    footerControlModel.addChangeListener(this);
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

  public synchronized void startPackaging() {
    beanFactory.getBean(PackerService.class).startPacking();
  }

  public void updateFormState(CruiseMetadata cruiseMetadata) {
  }

  public void restoreDefaultsGlobal() {
    peopleModel.restoreDefaults();
    packageModel.restoreDefaults();
    datasetsModel.restoreDefaults();
    cruiseInformationModel.restoreDefaults();
    omicsModel.restoreDefaults();
  }

  public synchronized void saveForms() {
    if (packageModel.getCruiseId() == null) {
      setSaveWarningDialogueVisible(true);
    } else {
      PackJob packJob = PackJob.create(packageModel, omicsModel, cruiseInformationModel, datasetsModel, instrumentDatastore);
      cruiseDataDatastore.save(packJob);
      setSaveExitAppDialogueVisible(true);
    }
  }

  public void exitApplication() {
    int exitCode = SpringApplication.exit(applicationContext, new ExitCodeGenerator[] {
      new ExitCodeGenerator() {

        @Override
        public int getExitCode() {
          return 0;
        }
        
      }
    });
    System.exit(exitCode);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }


}
