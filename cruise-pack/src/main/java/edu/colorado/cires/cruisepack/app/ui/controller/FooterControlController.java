package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.service.PackerService;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FooterControlController implements PropertyChangeListener {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final FooterControlModel footerControlModel;
  private final BeanFactory beanFactory;

  @Autowired
  public FooterControlController(ReactiveViewRegistry reactiveViewRegistry, FooterControlModel footerControlModel,
      BeanFactory beanFactory) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlModel = footerControlModel;
    this.beanFactory = beanFactory;
  }

  @PostConstruct
  public void init() {
    footerControlModel.addChangeListener(this);
  }

  public synchronized void setStopButtonEnabled(boolean stopButtonEnabled) {
    footerControlModel.setStopButtonEnabled(stopButtonEnabled);
  }

  public synchronized void setSaveButtonEnabled(boolean saveButtonEnabled) {
    footerControlModel.setSaveButtonEnabled(saveButtonEnabled);
  }

  public synchronized void setPackageButtonEnabled(boolean packageButtonEnabled) {
    footerControlModel.setPackageButtonEnabled(packageButtonEnabled);
  }

  public synchronized void startPackaging() {
    beanFactory.getBean(PackerService.class).startPacking();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }


}
