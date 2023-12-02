package edu.colorado.cires.cruisepack.startup;

import edu.colorado.cires.cruisepack.datastore.DatasetDatastore;
import edu.colorado.cires.cruisepack.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.CruisePackModel;
import edu.colorado.cires.cruisepack.ui.model.DropDownModel;
import edu.colorado.cires.cruisepack.ui.view.MainFrame;

public class UiContext {

  public UiContext(InstrumentDatastore instrumentDatastore, DatasetDatastore datasetDatastore) {
    DefaultController controller = new DefaultController();
    MainFrame mainFrame = new MainFrame(controller);
    DropDownModel dropDownModel = new DropDownModel(instrumentDatastore.getDropDowns(), datasetDatastore.getDatasetTypes());
    controller.addModel(CruisePackModel.class, new CruisePackModel(dropDownModel, datasetDatastore, controller));
    controller.addModel(DropDownModel.class, dropDownModel);
    controller.addView(mainFrame);
  }
}
