package edu.colorado.cires.cruisepack.app.ui.view.common;

import java.util.Objects;

public class DropDownItem {

  private final String id;
  private final String value;
  private final String errorMessage;

  public DropDownItem(String id, String value) {
    this.id = id;
    this.value = value;
    this.errorMessage = null;
  }

  public DropDownItem(String id, String value, String errorMessage) {
    this.id = id;
    this.value = value;
    this.errorMessage = errorMessage;
  }

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  public String getErrorMessage() {
    return errorMessage;
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
    return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(errorMessage, that.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value, errorMessage);
  }

  @Override
  public String toString() {
    return value;
  }
}
