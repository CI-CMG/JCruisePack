package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.service.ImportService;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportController implements PropertyChangeListener {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);
  
  private final ImportModel importModel;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final ImportService importService;
  private final ErrorModel errorModel;

  @Autowired
  public ImportController(ImportModel importModel, ReactiveViewRegistry reactiveViewRegistry, ImportService importService, ErrorModel errorModel) {
    this.importModel = importModel;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.importService = importService;
    this.errorModel = errorModel;
  }
  
  @PostConstruct
  public void init() {
    importModel.addChangeListener(this);
    errorModel.addChangeListener(this);
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
    try {
      return importService.importCruises(importModel);
    } catch (Exception e) {
      LOGGER.error("Failed to import cruises", e);
      errorModel.emitErrorMessage(String.format(
          "Failed to import cruises: %s", e.getMessage()
      ));
    }
    return false;
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
    try {
      importService.saveTemplate(path);
    } catch (IOException e) {
      LOGGER.error("Failed to save import template", e);
      errorModel.emitErrorMessage(String.format(
          "Failed to save import template: %s", e.getMessage()
      ));
    }
  }
}
