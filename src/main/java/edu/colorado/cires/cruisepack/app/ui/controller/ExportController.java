package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExportController implements PropertyChangeListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExportController.class);
  
  private final CruiseDataDatastore cruiseDataDatastore;
  private final PackageModel packageModel;
  private final PeopleModel peopleModel;
  private final OmicsModel omicsModel;
  private final CruiseInformationModel cruiseInformationModel;
  private final DatasetsModel datasetsModel;
  private final InstrumentDatastore instrumentDatastore;
  private final PersonDatastore personDatastore;
  
  private final FooterControlModel footerControlModel;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final ErrorModel errorModel;

  public ExportController(CruiseDataDatastore cruiseDataDatastore, PackageModel packageModel, PeopleModel peopleModel, OmicsModel omicsModel,
      CruiseInformationModel cruiseInformationModel, DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore,
      PersonDatastore personDatastore, FooterControlModel footerControlModel, ReactiveViewRegistry reactiveViewRegistry, ErrorModel errorModel) {
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.packageModel = packageModel;
    this.peopleModel = peopleModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.datasetsModel = datasetsModel;
    this.instrumentDatastore = instrumentDatastore;
    this.personDatastore = personDatastore;
    this.footerControlModel = footerControlModel;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.errorModel = errorModel;
  }
  
  @PostConstruct
  public void init() {
    errorModel.addChangeListener(this);
  }
  
  public void exportCruise(Path path) {
    if (packageModel.getCruiseId() == null) {
      footerControlModel.setSaveWarningDialogueVisible(true);
    } else {
      PackJob packJob = PackJobUtils.create(
          packageModel,
          peopleModel,
          omicsModel,
          cruiseInformationModel,
          datasetsModel,
          instrumentDatastore,
          personDatastore
      );
      try {
        cruiseDataDatastore.saveCruiseToPath(
            packJob,
            path.resolve(packJob.getPackageId() + ".json")
        );
      } catch (Exception e) {
        LOGGER.error("Failed to export cruise: {}", packJob.getPackageId(), e);
        errorModel.emitErrorMessage(String.format(
            "Failed to export %s: %s", packJob.getPackageId(), e.getMessage()
        ));
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView reactiveView : reactiveViewRegistry.getViews()) {
      reactiveView.onChange(evt);
    }
  }
}
