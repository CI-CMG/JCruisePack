package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class PropertyChangeModel {

  protected final PropertyChangeSupport propertyChangeSupport;

  public PropertyChangeModel() {
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void removeChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  protected void fireChangeEvent(String eventId, Object oldValue, Object newValue) {
    propertyChangeSupport.firePropertyChange(eventId, oldValue, newValue);
  }

  protected void fireIndexedChangeEvent(String eventId, int index, Object oldValue, Object newValue) {
    propertyChangeSupport.fireIndexedPropertyChange(eventId, index, oldValue, newValue);
  }

  protected <T> void setIfChanged(String event, T newValue, Supplier<T> getOldValue, Consumer<T> setNewValue) {
    T oldValue = getOldValue.get();
    if (!Objects.equals(newValue, oldValue)) {
      setNewValue.accept(newValue);
      fireChangeEvent(event, oldValue, newValue);
    }
  }
}
