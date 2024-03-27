package edu.colorado.cires.cruisepack.app.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.ImportRow;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.ResponseStatus;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class CruiseDataDatastore extends PropertyChangeModel {

  public static final DropDownItem UNSELECTED_CRUISE = new DropDownItem("", "Select Cruise");

  private final MetadataService metadataService;
  private final ServiceProperties serviceProperties;
  private final ObjectMapper objectMapper;
  private List<CruiseData> cruises;

  public CruiseDataDatastore(MetadataService metadataService, ServiceProperties serviceProperties, ObjectMapper objectMapper) {
    this.metadataService = metadataService;
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
  }
  
  public void saveCruiseToPath(PackJob packJob, Path path) throws Exception {
    saveCruise(
        metadataService.createData(packJob),
        path
    );
  }

  public void saveCruise(PackJob packJob) throws Exception {
    saveCruiseToPath(packJob, getMetadataPath(packJob.getPackageId()));
  }
  
  private Path getMetadataPath(String packageId) {
    return getCruiseMetadataDir().resolve(packageId + ".json");
  }
  
  public void delete(String packageId) throws Exception {
    saveCruises(
        cruises.stream()
            .filter(c -> c.getPackageId().equals(packageId))
            .map(c ->
                CruiseData.builder(c)
                    .withDelete(true)
                    .build()
            ).toList()
    );
  }
  
  public void saveCruises(List<CruiseData> cruiseDataList) throws Exception {
    for (CruiseData data : cruiseDataList) {
      saveCruise(data);
    }
    load();
  }
  
  private void saveCruise(CruiseData cruiseData, Path path) throws Exception {
    if (cruiseData.isDelete()) {
      Files.deleteIfExists(path);
    } else {
      try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
        objectMapper.writeValue(outputStream, cruiseData);
      }
    }
  }
  
  public void saveCruise(CruiseData cruiseData) throws Exception {
    saveCruise(cruiseData, getMetadataPath(cruiseData.getPackageId()));
  }
  
  public ResponseStatus saveCruise(ImportRow importRow, Path packageDestinationPath, String metadataAuthorName, boolean overwrite, boolean skip) {
    CruiseData cruiseData = metadataService.createData(importRow, packageDestinationPath, metadataAuthorName);
    if (!overwrite) {
      Optional<CruiseData> existing = getByPackageId(cruiseData.getPackageId());
      if (existing.isPresent()) {
        if (!skip) {
          return ResponseStatus.CONFLICT;
        } else {
          return ResponseStatus.SUCCESS;
        }
      }
    }

    try {
      saveCruise(
          cruiseData,
          getMetadataPath(cruiseData.getPackageId())
      );
      return ResponseStatus.SUCCESS;
    } catch (Exception e) {
      return ResponseStatus.ERROR;
    }
  }

  @PostConstruct
  public void init() {
    load();
  }

  public void load() {
    Path cruiseMetadataDir = getCruiseMetadataDir();
    cruises = new ArrayList<>(0);
    try (Stream<Path> paths = Files.walk(cruiseMetadataDir).filter(p -> !p.toFile().isDirectory())) {
      paths.forEach(p -> {
        try {
          cruises.add(objectMapper.readValue(p.toFile(), CruiseData.class));
        } catch (IOException e) {
          throw new IllegalStateException("Cannot read cruise metadata from file: " + p, e);
        }
      });
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read cruise metadata from directory: " + cruiseMetadataDir, e);
    }

    setCruises(cruises);
  }

  private void setCruises(List<CruiseData> cruises) {
    fireChangeEvent(Events.UPDATE_CRUISE_DATA_STORE, Collections.emptyList(), cruises);
  }

  public List<DropDownItem> getDropDownItems() {
    List<DropDownItem> items = cruises.stream()
        .filter(CruiseData::isUse)
        .sorted((c1, c2) -> c1.getPackageId().compareToIgnoreCase(c2.getPackageId()))
        .map(c -> new DropDownItem(c.getPackageId(), c.getPackageId()))
        .collect(Collectors.toList());
    items.add(0, UNSELECTED_CRUISE);
    
    return items;
  }

  private Path getCruiseMetadataDir() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("local-data");
    return dataDir.resolve("cruise-metadata");
  }
  
  public Optional<CruiseData> getByPackageId(String packageId) {
    return cruises.stream()
        .filter(m -> m.getPackageId().equals(packageId))
        .findFirst();
  }
  
  public List<CruiseData> getCruises() {
    return cruises;
  }
}
