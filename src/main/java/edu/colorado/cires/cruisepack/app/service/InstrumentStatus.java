package edu.colorado.cires.cruisepack.app.service;

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

  public static InstrumentStatus forValue(String strValue) {
    for (InstrumentStatus instrumentStatus : InstrumentStatus.values()) {
      if (instrumentStatus.getStrValue().equals(strValue)) {
        return instrumentStatus;
      }
    }
    return null;
  }
}
