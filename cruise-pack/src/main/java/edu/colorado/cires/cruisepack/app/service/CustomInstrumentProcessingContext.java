package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;

public class CustomInstrumentProcessingContext {

  private final PackJob packJob;
  private final InstrumentDetail dataset;
  private final Path mainBagDataDir;
  private final Path datasetBagRootDir;


  public CustomInstrumentProcessingContext(PackJob packJob, InstrumentDetail dataset, Path mainBagDataDir, Path datasetBagDataDir) {
    this.packJob = packJob;
    this.dataset = dataset;
    this.mainBagDataDir = mainBagDataDir;
    this.datasetBagRootDir = datasetBagDataDir;
  }

  public PackJob getPackJob() {
    return packJob;
  }

  public InstrumentDetail getDataset() {
    return dataset;
  }

  public Path getMainBagDataDir() {
    return mainBagDataDir;
  }

  public Path getDatasetBagRootDir() {
    return datasetBagRootDir;
  }
}
