package edu.colorado.cires.cruisepack.prototype.startup;

import edu.colorado.cires.cruisepack.prototype.datastore.DatasetDatastore;
import edu.colorado.cires.cruisepack.prototype.datastore.InstrumentDatastore;

public final class ApplicationContext {

  private static ApplicationContext applicationContext;

  public static ApplicationContext get() {
    if (applicationContext == null) {
      throw new IllegalStateException("ApplicationContext has not been initialized");
    }
    return applicationContext;
  }

  public static ApplicationContext initialize() {
    if (applicationContext == null) {
      applicationContext = new ApplicationContext();
    }
    return applicationContext;
  }

  private final UiContext uiContext;

  private ApplicationContext() {
    InstrumentDatastore instrumentDatastore = new InstrumentDatastore();
    DatasetDatastore datasetDatastore = new DatasetDatastore();
    uiContext = new UiContext(instrumentDatastore, datasetDatastore);
  }


}
