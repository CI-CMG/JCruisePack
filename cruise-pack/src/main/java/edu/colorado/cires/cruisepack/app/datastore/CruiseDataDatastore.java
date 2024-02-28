package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CruiseDataDatastore extends PropertyChangeModel implements PropertyChangeListener {

  public static final DropDownItem UNSELECTED_CRUISE = new DropDownItem("", "Select Cruise");

  private final MetadataService metadataService;
  private final ServiceProperties serviceProperties;
  private final ObjectMapper objectMapper;
  private List<DropDownItem> dropDownItems;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private List<CruiseMetadata> cruises;

  public CruiseDataDatastore(MetadataService metadataService, ServiceProperties serviceProperties, ObjectMapper objectMapper, ReactiveViewRegistry reactiveViewRegistry) {
    this.metadataService = metadataService;
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
    this.reactiveViewRegistry = reactiveViewRegistry;
  }

  public void save(PackJob packJob) {
    CruiseMetadata metadata = metadataService.createMetadata(packJob);
    Path metadataFile = getCruiseMetadataDir().resolve(metadata.getPackageId() + ".json");
    metadataService.writeMetadata(metadata, metadataFile);
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
          cruises.add(objectMapper.readValue(p.toFile(), CruiseMetadata.class));
        } catch (IOException e) {
          throw new IllegalStateException("Cannot read cruise metadata from file: " + p, e);
        }
      });
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read cruise metadata from directory: " + cruiseMetadataDir, e);
    }

    List<DropDownItem> items = cruises.stream()
      .map(m -> new DropDownItem(m.getPackageId(), m.getPackageId()))
      .sorted((c1, c2) -> c1.getValue().compareToIgnoreCase(c2.getValue()))
      .collect(Collectors.toList());

    items.add(0, UNSELECTED_CRUISE);

    setDropDownItems(items);
  }

  private void setDropDownItems(List<DropDownItem> items) {
    this.dropDownItems = items;
    fireChangeEvent(Events.UPDATE_CRUISE_DATA_STORE, Collections.emptyList(), items);
  }

  public List<DropDownItem> getDropDownItems() {
    return dropDownItems;
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
  
  public Optional<CruiseMetadata> getByPackageId(String packageId) {
    return cruises.stream()
        .filter(m -> m.getPackageId().equals(packageId))
        .findFirst();
  }
}
