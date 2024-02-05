package edu.colorado.cires.cruisepack.prototype.ui.view;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;

public interface BaseViewPanel {

  void onChange(PropertyChangeEvent evt);
  default void onIndexedChange(IndexedPropertyChangeEvent evt) {
    onChange(evt);
  }


}
