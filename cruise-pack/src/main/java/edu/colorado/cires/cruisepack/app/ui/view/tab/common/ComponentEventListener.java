package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import javax.swing.JComponent;

public interface ComponentEventListener<C extends JComponent> {
  void handle(C component);
}
