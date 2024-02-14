package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class CruiseInformationModel extends PropertyChangeModel {
  private String cruiseTitle;
  private String cruiseTitleError;

  private String cruisePurpose;
  private String cruiseDescription;

  private Path documentsPath;
  private String documentsPathError;

  public Path getDocumentsPath() {
    return documentsPath;
  }

  public void setDocumentsPath(Path documentsPath) {
    setIfChanged(Events.UPDATE_DOCS_DIRECTORY, documentsPath, () -> this.documentsPath, (nv) -> this.documentsPath = nv);
  }

  public String getDocumentsPathError() {
    return documentsPathError;
  }

  public void setDocumentsPathError(String documentsPathError) {
    setIfChanged(Events.UPDATE_DOCS_DIRECTORY_ERROR, documentsPathError, () -> this.documentsPathError, (nv) -> this.documentsPathError = nv);
  }

  public String getCruiseTitle() {
    return cruiseTitle;
  }

  public void setCruiseTitle(String cruiseTitle) {
    cruiseTitle = normalizeString(cruiseTitle);
    setIfChanged(Events.UPDATE_CRUISE_TITLE, cruiseTitle, () -> this.cruiseTitle, (nv) -> this.cruiseTitle = nv);
  }

  public String getCruiseTitleError() {
    return cruiseTitleError;
  }

  public void setCruiseTitleError(String cruiseTitleError) {
    setIfChanged(Events.UPDATE_CRUISE_TITLE_ERROR, cruiseTitleError, () -> this.cruiseTitleError, (nv) -> this.cruiseTitleError = nv);
  }

  public String getCruisePurpose() {
    return cruisePurpose;
  }

  public void setCruisePurpose(String cruisePurpose) {
    cruisePurpose = normalizeString(cruisePurpose);
    setIfChanged(Events.UPDATE_CRUISE_PURPOSE, cruisePurpose, () -> this.cruisePurpose, (nv) -> this.cruisePurpose = nv);
  }

  public String getCruiseDescription() {
    return cruiseDescription;
  }

  public void setCruiseDescription(String cruiseDescription) {
    cruiseDescription = normalizeString(cruiseDescription);
    setIfChanged(Events.UPDATE_CRUISE_DESCRIPTION, cruiseDescription, () -> this.cruiseDescription, (nv) -> this.cruiseDescription = nv);
  }
}
