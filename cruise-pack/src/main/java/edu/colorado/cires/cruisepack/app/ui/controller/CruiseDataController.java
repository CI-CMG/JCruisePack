package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruiseDataController {
  
  private final CruiseDataDatastore cruiseDataDatastore;

  @Autowired
  public CruiseDataController(CruiseDataDatastore cruiseDataDatastore) {
    this.cruiseDataDatastore = cruiseDataDatastore;
  }
  
  public List<CruiseData> getCruises() {
    return cruiseDataDatastore.getCruises();
  }
  
  public void updateCruises(List<CruiseData> cruises) {
    cruiseDataDatastore.saveCruises(cruises.stream());
  }
}
