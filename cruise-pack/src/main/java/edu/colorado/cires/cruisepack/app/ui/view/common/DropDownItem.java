package edu.colorado.cires.cruisepack.app.ui.view.common;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DropDownItem that = (DropDownItem) o;
    return Objects.equals(id, that.id) && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value);
  }

  @Override
  public String toString() {
    return value;
  }
}
