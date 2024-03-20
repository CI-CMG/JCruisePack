package edu.colorado.cires.cruisepack.app.ui.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ProjectModel extends PropertyChangeModel {
  
  public static final String UPDATE_NAME = "UPDATE_PROJECT_NAME";
  public static final String UPDATE_NAME_ERROR = "UPDATE_PROJECT_NAME_ERROR";
  public static final String UPDATE_UUID = "UPDATE_PROJECT_UUID";
  public static final String UPDATE_UUID_ERROR = "UPDATE_PROJECT_UUID_ERROR";
  public static final String UPDATE_USE = "UPDATE_PROJECT_USE";
  public static final String EMIT_SAVE_FAILURE = "EMIT_PROJECT_SAVE_FAILURE";
  
  @NotBlank
  private String name;
  private String nameError;
  private String uuid;
  private String uuidError;
  private boolean use = false;
  
  public void restoreDefaults() {
    setName(null);
    setUuid(null);
    setUse(false);
    
    clearErrors();
  }
  
  public void clearErrors() {
    setNameError(null);
    setUuidError(null);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    setIfChanged(UPDATE_NAME, name, () -> this.name, (n) -> this.name = n);
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    setIfChanged(UPDATE_UUID, uuid, () -> this.uuid, (u) -> this.uuid = u);
  }
  
  public void setUuidError(String error) {
    setIfChanged(UPDATE_UUID_ERROR, error, () -> this.uuidError, (e) -> this.uuidError = e);
  }

  public boolean isUse() {
    return use;
  }

  public void setUse(boolean use) {
    setIfChanged(UPDATE_USE, use, () -> this.use, (u) -> this.use = u);
  }

  public void setNameError(String nameError) {
    setIfChanged(UPDATE_NAME_ERROR, nameError, () -> this.nameError, (e) -> this.nameError = e);
  }

  private void setError(ConstraintViolation<ProjectModel> constraintViolation) {
    String propertyPath = constraintViolation.getPropertyPath().toString();
    String message = constraintViolation.getMessage();
    
    if (propertyPath.equals("name")) {
      setNameError(message);
    } else if (propertyPath.equals("uuid")) {
      setUuidError(message);
    }
  }

  public String getNameError() {
    return nameError;
  }

  public String getUuidError() {
    return uuidError;
  }

  public void setErrors(Set<ConstraintViolation<ProjectModel>> constraintViolations) {
    constraintViolations.forEach(this::setError);
  }
  
  public void emitSaveFailure(String message) {
    fireChangeEvent(EMIT_SAVE_FAILURE, null, message);
  }
}
