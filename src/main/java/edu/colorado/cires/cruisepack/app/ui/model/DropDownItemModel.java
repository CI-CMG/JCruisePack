package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class DropDownItemModel extends PropertyChangeModel {
  
  public static final String UPDATE_ITEM = "UPDATE_ITEM";
  public static final String UPDATE_ITEM_ERROR = "UPDATE_ITEM_ERROR";
  
  private DropDownItem item;
  private String itemError = null;

  public DropDownItemModel(DropDownItem item) {
    this.item = item;
  }

  public DropDownItem getItem() {
    return item;
  }

  public void setItem(DropDownItem item) {
    setIfChanged(UPDATE_ITEM, item, () -> this.item, (i) -> this.item = i);
  }

  public String getItemError() {
    return itemError;
  }

  public void setItemError(String itemError) {
    setIfChanged(UPDATE_ITEM_ERROR, itemError, () -> this.itemError, (e) -> this.itemError = e);
  }
}
