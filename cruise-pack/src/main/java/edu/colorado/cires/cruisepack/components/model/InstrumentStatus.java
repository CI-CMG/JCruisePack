package edu.colorado.cires.cruisepack.components.model;

public enum InstrumentStatus {
  RAW("Raw"),
  PROCESSED("Processed"),
  PRODUCTS("Products");

  private final String strValue;

  InstrumentStatus(String strValue) {
    this.strValue = strValue;
  }

  public String getStrValue() {
    return strValue;
  }
}
