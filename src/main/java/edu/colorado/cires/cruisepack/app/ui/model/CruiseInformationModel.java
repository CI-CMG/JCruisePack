package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Component
public class CruiseInformationModel extends PropertyChangeModel {
  @NotBlank
  private String cruiseTitle;
  private String cruiseTitleError = null;
  private String cruisePurpose;
  private String cruisePurposeError = null;
  @NotBlank
  private String cruiseDescription;
  private String cruiseDescriptionError = null;

  public void restoreDefaults() {
    setCruiseTitle(null);
    setCruiseTitleError(null);

    setCruisePurpose(null);
    setCruisePurposeError(null);

    setCruiseDescription(null);
    setCruiseDescriptionError(null);
  }

  public void updateFormState(Cruise cruiseMetadata) {
    setCruiseTitle(cruiseMetadata.getCruiseTitle());
    setCruiseTitleError(null);
    setCruisePurpose(cruiseMetadata.getCruisePurpose());
    setCruisePurposeError(null);
    setCruiseDescription(cruiseMetadata.getCruiseDescription());
    setCruiseDescriptionError(null);
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

  public String getCruisePurposeError() {
    return cruisePurposeError;
  }

  public void setCruisePurposeError(String cruisePurposeError) {
    setIfChanged(Events.UPDATE_CRUISE_PURPOSE_ERROR, cruisePurposeError, () -> this.cruisePurposeError, (e) -> this.cruisePurposeError = e);
  }

  public String getCruiseDescriptionError() {
    return cruiseDescriptionError;
  }

  public void setCruiseDescriptionError(String cruiseDescriptionError) {
    setIfChanged(Events.UPDATE_CRUISE_DESCRIPTION_ERROR, cruiseDescriptionError, () -> this.cruiseDescriptionError, (e) -> this.cruiseDescriptionError = e);
  }

  
}
