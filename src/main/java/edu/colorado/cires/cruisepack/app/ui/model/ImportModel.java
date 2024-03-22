package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsXLSXFormat;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ImportModel extends PropertyChangeModel {
  
  @NotNull(message = "must not be blank")
  @PathExists
  @PathIsFile
  @PathIsXLSXFormat
  private Path importPath;
  private String importPathError = null;
  
  @NotNull(message = "must not be blank")
  @PathExists
  @PathIsDirectory
  private Path destinationPath;
  private String destinationPathError = null;
  
  @NotNull
  @ValidPersonDropDownItem
  private DropDownItem metadataAuthor = PersonDatastore.UNSELECTED_PERSON;
  private String metadataAuthorError = null;
  
  public void resetState() {
    setImportPath(null);
    setImportPathError(null);
    setDestinationPath(null);
    setDestinationPathError(null);
    setMetadataAuthor(PersonDatastore.UNSELECTED_PERSON);
    setMetadataAuthorError(null);
  }
  
  public void clearErrors() {
    setImportPathError(null);
    setDestinationPathError(null);
    setMetadataAuthorError(null);
  }
  
  public void setErrors(Set<ConstraintViolation<ImportModel>> constraintViolations) {
    constraintViolations.forEach(this::setError);
  }
  
  private void setError(ConstraintViolation<ImportModel> constraintViolation) {
    String propertyPath = constraintViolation.getPropertyPath().toString();
    String message = constraintViolation.getMessage();
    
    if (propertyPath.startsWith("importPath")) {
      setImportPathError(message);
    } else if (propertyPath.startsWith("destinationPath")) {
      setDestinationPathError(message);
    } else if (propertyPath.startsWith("metadataAuthor")) {
      setMetadataAuthorError(message);
    }
  }

  public Path getImportPath() {
    return importPath;
  }

  public Path getDestinationPath() {
    return destinationPath;
  }

  public DropDownItem getMetadataAuthor() {
    return metadataAuthor;
  }

  public void setImportPath(Path importPath) {
    setIfChanged(Events.UPDATE_IMPORT_PATH, importPath, () -> this.importPath, (p) -> this.importPath = p);
  }

  public void setDestinationPath(Path destinationPath) {
    setIfChanged(Events.UPDATE_IMPORT_DESTINATION_PATH, destinationPath, () -> this.destinationPath, (p) -> this.destinationPath = p);
  }

  public void setMetadataAuthor(DropDownItem metadataAuthor) {
    setIfChanged(Events.UPDATE_IMPORT_METADATA_AUTHOR, metadataAuthor, () -> this.metadataAuthor, (d) -> this.metadataAuthor = d);
  }

  public void setImportPathError(String importPathError) {
    setIfChanged(Events.UPDATE_IMPORT_PATH_ERROR, importPathError, () -> this.importPathError, (s) -> this.importPathError = s);
  }

  public void setDestinationPathError(String destinationPathError) {
    setIfChanged(Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR, destinationPathError, () -> this.destinationPathError, (s) -> this.destinationPathError = s);
  }

  public void setMetadataAuthorError(String metadataAuthorError) {
    setIfChanged(Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR, metadataAuthorError, () -> this.metadataAuthorError, (s) -> this.metadataAuthorError = s);
  }

  public String getImportPathError() {
    return importPathError;
  }

  public String getDestinationPathError() {
    return destinationPathError;
  }

  public String getMetadataAuthorError() {
    return metadataAuthorError;
  }
}
