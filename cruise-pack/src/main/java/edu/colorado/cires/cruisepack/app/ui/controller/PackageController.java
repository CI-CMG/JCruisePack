package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackageController implements PropertyChangeListener {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PackageModel packageModel;

  @Autowired
  public PackageController(ReactiveViewRegistry reactiveViewRegistry, PackageModel packageModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.packageModel = packageModel;
  }

  @PostConstruct
  public void init() {
    packageModel.addChangeListener(this);
  }

  public void setCruiseId(String cruiseId) {
    packageModel.setCruiseId(cruiseId);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }
}
