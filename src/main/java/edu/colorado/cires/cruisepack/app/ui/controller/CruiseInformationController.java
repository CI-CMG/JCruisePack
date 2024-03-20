package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruiseInformationController implements PropertyChangeListener {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final CruiseInformationModel cruiseInformationModel;

  @Autowired
  public CruiseInformationController(ReactiveViewRegistry reactiveViewRegistry, CruiseInformationModel cruiseInformationModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.cruiseInformationModel = cruiseInformationModel;
  }

  @PostConstruct
  public void init() {
    cruiseInformationModel.addChangeListener(this);
  }

  public void setCruiseTitle(String cruiseTitle) {
    cruiseInformationModel.setCruiseTitle(cruiseTitle);
  }

  public void setCruiseTitleError(String cruiseTitleError) {
    cruiseInformationModel.setCruiseTitleError(cruiseTitleError);
  }

  public void setCruisePurpose(String cruisePurpose) {
    cruiseInformationModel.setCruisePurpose(cruisePurpose);
  }

  public void setCruiseDescription(String cruiseDescription) {
    cruiseInformationModel.setCruiseDescription(cruiseDescription);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }


}
