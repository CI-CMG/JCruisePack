package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ImportModelTest extends PropertyChangeModelTest<ImportModel> {

  @Override
  protected ImportModel createModel() {
    return new ImportModel();
  }

  @Test
  void resetState() {
    Path importPath = Paths.get("import-path");
    String importPathError = "import-path-error";
    Path destinationPath = Paths.get("destination-path");
    String destinationPathError = "destination-path-error";
    DropDownItem metadataAuthor = new DropDownItem("1", "metadata-author");
    String metadataAuthorError = "metadata-author-error";
    
    model.setImportPath(importPath);
    model.setImportPathError(importPathError);
    model.setDestinationPath(destinationPath);
    model.setDestinationPathError(destinationPathError);
    model.setMetadataAuthor(metadataAuthor);
    model.setMetadataAuthorError(metadataAuthorError);
    
    clearEvents();
    
    model.resetState();
    
    assertChangeEvent(Events.UPDATE_IMPORT_PATH, importPath, null);
    assertNull(model.getImportPath());
    assertChangeEvent(Events.UPDATE_IMPORT_PATH_ERROR, importPathError, null);
    assertNull(model.getImportPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_DESTINATION_PATH, destinationPath, null);
    assertNull(model.getDestinationPath());
    assertChangeEvent(Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR, destinationPathError, null);
    assertNull(model.getDestinationPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_METADATA_AUTHOR, metadataAuthor, PersonDatastore.UNSELECTED_PERSON);
    assertEquals(PersonDatastore.UNSELECTED_PERSON, model.getMetadataAuthor());
    assertChangeEvent(Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR, metadataAuthorError, null);
    assertNull(model.getMetadataAuthorError());
  }

  @Test
  void clearErrors() {
    String importPathError = "import-path-error";
    String destinationPathError = "destination-path-error";
    String metadataAuthorError = "metadata-author-error";
    
    model.setImportPathError(importPathError);
    model.setDestinationPathError(destinationPathError);
    model.setMetadataAuthorError(metadataAuthorError);
    
    clearEvents();
    model.clearErrors();
    
    assertChangeEvent(Events.UPDATE_IMPORT_PATH_ERROR, importPathError, null);
    assertNull(model.getImportPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR, destinationPathError, null);
    assertNull(model.getDestinationPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR, metadataAuthorError, null);
    assertNull(model.getMetadataAuthorError());
  }

  @Test
  void setErrors() {
    model.setErrors(Set.of(
        new CustomConstraintViolation("import-path-error", "importPath"),
        new CustomConstraintViolation("destination-path-error", "destinationPath"),
        new CustomConstraintViolation("metadata-author-error", "metadataAuthor")
    ));
    
    assertChangeEvent(Events.UPDATE_IMPORT_PATH_ERROR, null, "import-path-error");
    assertEquals("import-path-error", model.getImportPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR, null, "destination-path-error");
    assertEquals("destination-path-error", model.getDestinationPathError());
    assertChangeEvent(Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR, null, "metadata-author-error");
    assertEquals("metadata-author-error", model.getMetadataAuthorError());
  }

  @Test
  void setImportPath() {
    assertPropertyChange(Events.UPDATE_IMPORT_PATH, model::getImportPath, model::setImportPath, Paths.get("path1"), Paths.get("path2"), null);
  }

  @Test
  void setDestinationPath() {
    assertPropertyChange(Events.UPDATE_IMPORT_DESTINATION_PATH, model::getDestinationPath, model::setDestinationPath, Paths.get("path1"), Paths.get("path2"), null);
  }

  @Test
  void setMetadataAuthor() {
    assertPropertyChange(Events.UPDATE_IMPORT_METADATA_AUTHOR, model::getMetadataAuthor, model::setMetadataAuthor, new DropDownItem("1", "value1"), new DropDownItem("1", "value2"), PersonDatastore.UNSELECTED_PERSON);
  }

  @Test
  void setImportPathError() {
    assertPropertyChange(Events.UPDATE_IMPORT_PATH_ERROR, model::getImportPathError, model::setImportPathError, "value1", "value2", null);
  }

  @Test
  void setDestinationPathError() {
    assertPropertyChange(Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR, model::getDestinationPathError, model::setDestinationPathError, "value1", "value2", null);
  }

  @Test
  void setMetadataAuthorError() {
    assertPropertyChange(Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR, model::getMetadataAuthorError, model::setMetadataAuthorError, "value1", "value2", null);
  }
}