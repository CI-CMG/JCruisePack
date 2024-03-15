package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.service.ImportService;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportController implements PropertyChangeListener {
  
  private final ImportModel importModel;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final ImportService importService;

  @Autowired
  public ImportController(ImportModel importModel, ReactiveViewRegistry reactiveViewRegistry, ImportService importService) {
    this.importModel = importModel;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.importService = importService;
  }
  
  @PostConstruct
  public void init() {
    importModel.addChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }
  
  public Path getImportPath() {
    return importModel.getImportPath();
  }
  
  public Path getDestinationPath() {
    return importModel.getDestinationPath();
  }
  
  public DropDownItem getMetadataAuthor() {
    return importModel.getMetadataAuthor();
  }
  
  public boolean importCruises() {
    return importService.importCruises(importModel);
  }
  
  public void resetState() {
    importModel.resetState();
  }
  
  public void setImportPath(Path importPath) {
    importModel.setImportPath(importPath);
  }
  
  public void setDestinationPath(Path destinationPath) {
    importModel.setDestinationPath(destinationPath);
  }
  
  public void setMetadataAuthor(DropDownItem metadataAuthor) {
    importModel.setMetadataAuthor(metadataAuthor);
  }
  
  public void saveTemplate(Path path) {
    importService.saveTemplate(path);
  }
}
