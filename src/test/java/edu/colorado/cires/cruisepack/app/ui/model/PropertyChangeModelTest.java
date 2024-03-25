package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class PropertyChangeModelTest<T extends PropertyChangeModel> {
  
  private final Map<String, List<PropertyChangeEvent>> eventMap = new HashMap<>(0);
  
  protected abstract T createModel();
  protected T model;

  @BeforeEach
  void beforeEach() {
    clearEvents();
    model = createModel();
    addChangeListener();
  }
  
  @AfterEach
  void afterEach() {
    clearEvents();
  }

  public Map<String, List<PropertyChangeEvent>> getEventMap() {
    return eventMap;
  }

  private void addChangeListener() {
    model.addChangeListener(
        (evt) -> {
          String propertyName = evt.getPropertyName();
          List<PropertyChangeEvent> value = eventMap.get(propertyName);
          if (value == null) {
            List<PropertyChangeEvent> events = new ArrayList<>(0);
            events.add(evt);
            eventMap.put(
                propertyName, events 
            );
          } else {
            value.add(evt);
            eventMap.put(
                propertyName, value
            );
          }
        }
    );
  }
  
  protected void addChangeListener(PropertyChangeModel model) {
    model.addChangeListener(
        (evt) -> {
          String propertyName = evt.getPropertyName();
          List<PropertyChangeEvent> value = eventMap.get(propertyName);
          if (value == null) {
            List<PropertyChangeEvent> events = new ArrayList<>(0);
            events.add(evt);
            eventMap.put(
                propertyName, events
            );
          } else {
            value.add(evt);
            eventMap.put(
                propertyName, value
            );
          }
        }
    );
  }
  
  protected <V> void assertPropertyChange(String eventName, Supplier<V> getter, Consumer<V> setter, V value1, V value2, V defaultValue) {
    setter.accept(value1);
    assertEquals(getter.get(), value1);
    assertChangeEvent(eventName, defaultValue, value1);
    
    eventMap.clear();
    setter.accept(value1);
    assertEquals(getter.get(), value1);
    assertNull(eventMap.get(eventName));
    
    setter.accept(value2);
    assertEquals(getter.get(), value2);
    assertChangeEvent(eventName, value1, value2);
  }
  
  protected <V> void assertChangeEvent(String propertyName, V expectedOldValue, V expectedNewValue) {
    List<PropertyChangeEvent> events = eventMap.get(propertyName);
    assertEquals(1, events.size());
    PropertyChangeEvent event = events.get(0);
    assertNotNull(event);
    assertEquals(expectedOldValue, event.getOldValue());
    assertEquals(expectedNewValue, event.getNewValue());
  }
  
  protected <V, T> void assertChangeEvents(String propertyName, List<V> expectedOldValues, List<V> expectedNewValues, Function<V, T> assertionValueTransform) {
    assertEquals(expectedOldValues.size(), expectedNewValues.size());
    List<PropertyChangeEvent> events = eventMap.get(propertyName);
    assertEquals(expectedOldValues.size(), events.size());
    
    for (int i = 0; i < expectedOldValues.size(); i++) {
      PropertyChangeEvent event = events.get(i);
      
      V expectedOldValue = expectedOldValues.get(i);
      V oldValue = (V) event.getOldValue();
      V expectedNewValue = expectedNewValues.get(i);
      V newValue = (V) event.getNewValue();
      
      assertEquals(
          assertionValueTransform.apply(expectedOldValue),
          assertionValueTransform.apply(oldValue)
      );
      assertEquals(
          assertionValueTransform.apply(expectedNewValue),
          assertionValueTransform.apply(newValue)
      );
    }
  }
  
  protected void clearEvents() {
    eventMap.clear();
  }

  protected class CustomConstraintViolation implements ConstraintViolation<T> {
    
    private final String message;
    private final Path propertyPath;

    protected CustomConstraintViolation(String message, String propertyPath) {
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
    public T getRootBean() {
      return null;
    }

    @Override
    public Class<T> getRootBeanClass() {
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
