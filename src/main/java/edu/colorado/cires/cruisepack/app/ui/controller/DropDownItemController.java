package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DropDownItemController implements PropertyChangeListener {
  
  private final DropDownItemModel model;
  private final ReactiveView panel;

  public DropDownItemController(DropDownItemModel model, ReactiveView panel) {
    this.model = model;
    this.panel = panel;
    model.addChangeListener(this);
  }
  
  public DropDownItem getItem() {
    return model.getItem();
  }
  
  public void setItem(DropDownItem item) {
    model.setItem(item);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    panel.onChange(evt);
  }
}
