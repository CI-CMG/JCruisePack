package edu.colorado.cires.cruisepack.app.service.io;

import edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetail;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.ui.model.PackStateModel;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Metadata;
import gov.loc.repository.bagit.hash.SupportedAlgorithm;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

public class PackerFileController {
  
  private final PackStateModel packStateModel;
  private final MetadataService metadataService;

  public PackerFileController(PackStateModel packStateModel, MetadataService metadataService) {
    this.packStateModel = packStateModel;
    this.metadataService = metadataService;
  }
  
  public boolean filterTimeSize(Path source, Path target) {
    checkPackState();
    return CruisePackFileUtils.filterTimeSize(source, target);
  }
  
  public boolean filterHidden(Path path) {
    checkPackState();
    return CruisePackFileUtils.filterHidden(path);
  }
  
  public void mkDir(Path dir) {
    checkPackState();
    CruisePackFileUtils.mkDir(dir);
  }
  
  public void cleanDir(Path dir) {
    checkPackState();
    CruisePackFileUtils.cleanDir(dir);
  }
  
  public void copy(Path source, Path target) {
    checkPackState();
    CruisePackFileUtils.copy(source, target);
  }
  
  public void concatManifests(Path childManifest, Path mainBagPath, FileWriter parentManifestWriter) {
    checkPackState();
    CruisePackFileUtils.concatManifests(childManifest, mainBagPath, parentManifestWriter);
  }
  
  public void appendToManifest(Path path, Path mainBagPath, FileWriter manifestWriter) {
    checkPackState();
    CruisePackFileUtils.appendToManifest(path, mainBagPath, manifestWriter);
  }

  public CruiseMetadata createAndWriteCruiseMetadata(PackJob packJob, Path target) {
    checkPackState();
    CruiseMetadata cruiseMetadata = metadataService.createMetadata(packJob);
    metadataService.writeMetadata(cruiseMetadata, target);
    return cruiseMetadata;
  }
  
  public CruiseMetadata createAndWriteDatasetMetadata(CruiseMetadata cruiseMetadata, List<InstrumentDetail> instrumentDetails, Path target) {
    checkPackState();
    CruiseMetadata datasetMetadata = metadataService.createDatasetMetadata(cruiseMetadata, instrumentDetails);
    metadataService.writeMetadata(datasetMetadata, target);
    return datasetMetadata;
  }
  
  public void bagInPlace(Path mainBagPath, Metadata metadata, Collection<SupportedAlgorithm> algorithms) throws NoSuchAlgorithmException, IOException {
    checkPackState();
    BagCreator.bagInPlace(mainBagPath, algorithms, false, metadata);
  }
  
  private void checkPackState() {
    if (!packStateModel.isProcessing()) {
      throw new IllegalStateException("Pack job has stopped");
    }
  }
}
