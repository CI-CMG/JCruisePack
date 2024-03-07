package edu.colorado.cires.cruisepack.app.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetail;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class CruiseDataDatastore extends PropertyChangeModel implements PropertyChangeListener {

  public static final DropDownItem UNSELECTED_CRUISE = new DropDownItem("", "Select Cruise");

  private final MetadataService metadataService;
  private final ServiceProperties serviceProperties;
  private final ObjectMapper objectMapper;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private List<CruiseData> cruises;

  public CruiseDataDatastore(MetadataService metadataService, ServiceProperties serviceProperties, ObjectMapper objectMapper, ReactiveViewRegistry reactiveViewRegistry) {
    this.metadataService = metadataService;
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
    this.reactiveViewRegistry = reactiveViewRegistry;
  }

  public void save(PackJob packJob) {
    CruiseMetadata metadata = metadataService.createMetadata(packJob); 
    CruiseData data = CruiseData.builder(metadata)
        .withInstruments(
            metadata.getInstruments().stream()
                .map(instrument -> {
                  
                  InstrumentData.Builder builder = InstrumentData.builder(instrument);

                  InstrumentDetail instrumentDetail = packJob.getInstruments().values().stream()
                      .flatMap(List::stream)
                      .filter(i -> i.getShortName().equals(instrument.getShortName()))
                      .findFirst()
                      .orElse(null);
                  
                  if (instrumentDetail == null) {
                    return builder.build();
                  }
                  
                  return builder
                      .withDataPath(instrumentDetail.getDataPath())
                      .withAncillaryDataPath(instrumentDetail.getAncillaryDataPath())
                      .build();
                }).collect(Collectors.toList())
        )
        .build();
    save(Collections.singletonList(data));
  }
  
  public void delete(String packageId) {
    save(
        cruises.stream()
            .filter(c -> c.getPackageId().equals(packageId))
            .map(c ->
                CruiseData.builder(c)
                    .withDelete(true)
                    .build()
            ).toList()
    );
  }
  
  public void save(List<CruiseData> cruiseDataList) {
    cruiseDataList.forEach(cruiseData -> {
      String packageId = cruiseData.getPackageId();
      Path metadataFile = getCruiseMetadataDir().resolve(packageId + ".json");
      if (cruiseData.isDelete()) {
        try {
          Files.deleteIfExists(metadataFile);
        } catch (IOException e) {
          throw new IllegalStateException("Unable to delete cruise data: " + metadataFile, e);
        }
      } else {
        try (OutputStream outputStream = new FileOutputStream(metadataFile.toFile())) {
          objectMapper.writeValue(outputStream, cruiseData);
        } catch (IOException e) {
          throw new IllegalStateException("Unable to write cruise data: " + metadataFile, e);
        } 
      }
    });
    
    load();
  }

  @PostConstruct
  public void init() {
    addChangeListener(this);
    load();
  }

  private void load() {
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
    fireChangeEvent(Events.UPDATE_CRUISE_DATA_STORE, null, cruises);
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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
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
