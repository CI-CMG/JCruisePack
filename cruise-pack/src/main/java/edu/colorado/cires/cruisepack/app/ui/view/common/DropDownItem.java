package edu.colorado.cires.cruisepack.app.ui.view.common;

public class DropDownItem {

  private final String id;
  private final String value;

  public DropDownItem(String id, String value) {
    this.id = id;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }
}
