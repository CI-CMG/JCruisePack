package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class PropertyChangeModelTest<T extends PropertyChangeModel> {
  
  private final Map<String, PropertyChangeEvent> eventMap = new HashMap<>(0);
  
  protected abstract T createModel();
  protected T model;

  @BeforeEach
  void beforeEach() {
    clearMap();
    model = createModel();
    addChangeListener();
  }
  
  @AfterEach
  void afterEach() {
    clearMap();
  }

  private void addChangeListener() {
    model.addChangeListener(
        (evt) -> eventMap.put(
            evt.getPropertyName(), evt
        )
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
    PropertyChangeEvent event = eventMap.get(propertyName);
    assertNotNull(event);
    assertEquals(expectedOldValue, event.getOldValue());
    assertEquals(expectedNewValue, event.getNewValue());
  }
  
  protected void clearMap() {
    eventMap.clear();
  }

}
