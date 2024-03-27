package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorDialog extends java.awt.Component implements ReactiveView {
  
  private final ReactiveViewRegistry reactiveViewRegistry;

  @Autowired
  public ErrorDialog(ReactiveViewRegistry reactiveViewRegistry) {
    this.reactiveViewRegistry = reactiveViewRegistry;
  }
  
  @PostConstruct
  public void init() {
    reactiveViewRegistry.register(this);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(Events.EMIT_ERROR_MESSAGE)) {
      JOptionPane.showMessageDialog(
          SwingUtilities.getWindowAncestor(this),
          String.format(
              "<html><B>%s</B></html>", evt.getNewValue()
          ),
          "Error",
          JOptionPane.ERROR_MESSAGE
      );
    }
  }
}
