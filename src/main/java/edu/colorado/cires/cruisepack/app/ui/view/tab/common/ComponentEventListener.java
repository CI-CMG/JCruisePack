package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import java.awt.Container;
import javax.swing.JComponent;

public interface ComponentEventListener<C extends Container> {
  void handle(C component);
}
