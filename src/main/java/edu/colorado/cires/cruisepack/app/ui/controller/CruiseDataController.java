package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruiseDataController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CruiseDataController.class);
  
  private final CruiseDataDatastore cruiseDataDatastore;

  @Autowired
  public CruiseDataController(CruiseDataDatastore cruiseDataDatastore) {
    this.cruiseDataDatastore = cruiseDataDatastore;
  }
  
  public List<CruiseData> getCruises() {
    return cruiseDataDatastore.getCruises();
  }
  
  public void updateCruises(List<CruiseData> cruises) {
    try {
      cruiseDataDatastore.saveCruises(cruises);
    } catch (Exception e) {
      LOGGER.error("Failed to save cruises", e);
      
    }
  }
}
