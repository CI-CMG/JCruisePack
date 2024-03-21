package edu.colorado.cires.cruisepack.app.ui.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.util.Set;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;

public class ProjectModelTest extends PropertyChangeModelTest<ProjectModel> {

  @Override
  protected ProjectModel createModel() {
    return new ProjectModel();
  }
  
  @Test
  void testSetName() {
    assertPropertyChange(
        ProjectModel.UPDATE_NAME,
        model::getName,
        model::setName,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetNameError() {
    assertPropertyChange(
        ProjectModel.UPDATE_NAME_ERROR,
        model::getNameError,
        model::setNameError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetUUID() {
    assertPropertyChange(
        ProjectModel.UPDATE_UUID,
        model::getUuid,
        model::setUuid,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetUUIDError() {
    assertPropertyChange(
        ProjectModel.UPDATE_UUID_ERROR,
        model::getUuidError,
        model::setUuidError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetUse() {
    assertPropertyChange(
        ProjectModel.UPDATE_USE,
        model::isUse,
        model::setUse,
        true,
        false,
        false
    );
  }
  
  @Test
  void testEmitSaveFailure() {
    String message = "test-message";
    
    model.emitSaveFailure(message);
    assertChangeEvent(ProjectModel.EMIT_SAVE_FAILURE, null, message);
    
    clearMap();

    model.emitSaveFailure(message);
    assertChangeEvent(ProjectModel.EMIT_SAVE_FAILURE, null, message);
  }
  
  @Test
  void testClearErrors() {
    String nameError = "test-name-error";
    String uuidError = "test-uuid-error";
    
    model.setNameError(nameError);
    model.setUuidError(uuidError);
    
    clearMap();
    
    model.clearErrors();
    
    assertChangeEvent(ProjectModel.UPDATE_NAME_ERROR, nameError, null);
    assertChangeEvent(ProjectModel.UPDATE_UUID_ERROR, uuidError, null);
  }
  
  @Test
  void testRestoreDefaults() {
    String name = "test-name";
    String nameError = "test-name-error";
    String uuid = "test-uuid";
    String uuidError = "test-uuid-error";
    boolean use = true;
    
    model.setName(name);
    model.setNameError(nameError);
    model.setUuid(uuid);
    model.setUuidError(uuidError);
    model.setUse(use);
    
    clearMap();
    
    model.restoreDefaults();
    
    assertChangeEvent(ProjectModel.UPDATE_NAME, name, null);
    assertChangeEvent(ProjectModel.UPDATE_NAME_ERROR, nameError, null);
    assertChangeEvent(ProjectModel.UPDATE_UUID, uuid, null);
    assertChangeEvent(ProjectModel.UPDATE_UUID_ERROR, uuidError, null);
    assertChangeEvent(ProjectModel.UPDATE_USE, true, false);
  }
  
  @Test
  void testSetErrors() {
    model.setErrors(Set.of(
        new CustomConstrainViolation("test-name-error", "name"),
        new CustomConstrainViolation("test-uuid-error", "uuid")
    ));
    
    assertChangeEvent(ProjectModel.UPDATE_NAME_ERROR, null, "test-name-error");
    assertChangeEvent(ProjectModel.UPDATE_UUID_ERROR, null, "test-uuid-error");
  }
  
  private static class CustomConstrainViolation implements ConstraintViolation<ProjectModel> {
    
    private final String message;
    private final Path propertyPath;

    private CustomConstrainViolation(String message, String propertyPath) {
      this.message = message;
      this.propertyPath = PathImpl.createPathFromString(propertyPath);
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public String getMessageTemplate() {
      return null;
    }

    @Override
    public ProjectModel getRootBean() {
      return null;
    }

    @Override
    public Class<ProjectModel> getRootBeanClass() {
      return null;
    }

    @Override
    public Object getLeafBean() {
      return null;
    }

    @Override
    public Object[] getExecutableParameters() {
      return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
      return null;
    }

    @Override
    public Path getPropertyPath() {
      return propertyPath;
    }

    @Override
    public Object getInvalidValue() {
      return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
      return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
      return null;
    }
  }
}
