package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class ExportController {
  private final CruiseDataDatastore cruiseDataDatastore;
  private final PackageModel packageModel;
  private final PeopleModel peopleModel;
  private final OmicsModel omicsModel;
  private final CruiseInformationModel cruiseInformationModel;
  private final DatasetsModel datasetsModel;
  private final InstrumentDatastore instrumentDatastore;
  private final PersonDatastore personDatastore;
  
  private final FooterControlModel footerControlModel;

  public ExportController(CruiseDataDatastore cruiseDataDatastore, PackageModel packageModel, PeopleModel peopleModel, OmicsModel omicsModel,
      CruiseInformationModel cruiseInformationModel, DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore,
      PersonDatastore personDatastore, FooterControlModel footerControlModel) {
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.packageModel = packageModel;
    this.peopleModel = peopleModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.datasetsModel = datasetsModel;
    this.instrumentDatastore = instrumentDatastore;
    this.personDatastore = personDatastore;
    this.footerControlModel = footerControlModel;
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
      cruiseDataDatastore.saveCruiseToPath(
          packJob,
          path.resolve(packJob.getPackageId() + ".json")
      );
    }
  }
}
