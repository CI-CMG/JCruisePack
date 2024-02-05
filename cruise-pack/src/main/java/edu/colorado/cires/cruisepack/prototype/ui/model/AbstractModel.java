package edu.colorado.cires.cruisepack.prototype.ui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {

  protected final PropertyChangeSupport propertyChangeSupport;

  public AbstractModel() {
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
}
