package edu.colorado.cires.cruisepack.prototype.ui.controller;

import edu.colorado.cires.cruisepack.prototype.ui.model.CruisePackModel;

public class DefaultController extends AbstractController {

  public void changeCruiseId(String newText) {
    getModel(CruisePackModel.class).setCruiseId(newText);
  }

  public void changeSegment(String newText) {
    getModel(CruisePackModel.class).setSegment(newText);
  }

  public void addDataset() {
    getModel(CruisePackModel.class).addInstrument();
  }

  public void removeDataset(String id) {
    getModel(CruisePackModel.class).removeDataset(id);
  }

  public void changeDataset(String id, String dataType) {
    getModel(CruisePackModel.class).changeDataset(id, dataType);
  }
}
