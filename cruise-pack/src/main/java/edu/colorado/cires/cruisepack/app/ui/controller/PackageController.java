package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackageController implements PropertyChangeListener {

  private final Collection<ReactiveView> reactiveViews;
  private final PackageModel packageModel;

  @Autowired
  public PackageController(
      Collection<ReactiveView> reactiveViews,
      PackageModel packageModel) {
    this.reactiveViews = reactiveViews;
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
    for (ReactiveView view : reactiveViews) {
      view.onChange(evt);
    }
/*
    changing = true;
    try {
      for (BaseViewPanel view: registeredViews) {
        view.onChange(evt);
      }
      registeredViews.removeAll(viewsToRemove);
      registeredViews.addAll(viewsToAdd);
      viewsToRemove.clear();
      viewsToAdd.clear();
    } finally {
      changing = false;
    }
 */
  }
}
