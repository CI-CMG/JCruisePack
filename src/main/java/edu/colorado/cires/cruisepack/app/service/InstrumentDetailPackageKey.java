package edu.colorado.cires.cruisepack.app.service;

import java.util.Objects;

public class InstrumentDetailPackageKey {

  private final String instrumentGroupShortType;
  private final String instrumentShortCode;

  public InstrumentDetailPackageKey(String instrumentGroupShortType, String instrumentShortCode) {
    this.instrumentGroupShortType = instrumentGroupShortType;
    this.instrumentShortCode = instrumentShortCode;
  }

  public String getInstrumentGroupShortType() {
    return instrumentGroupShortType;
  }

  public String getInstrumentShortCode() {
    return instrumentShortCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstrumentDetailPackageKey that = (InstrumentDetailPackageKey) o;
    return Objects.equals(instrumentGroupShortType, that.instrumentGroupShortType) && Objects.equals(instrumentShortCode,
        that.instrumentShortCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instrumentGroupShortType, instrumentShortCode);
  }

  @Override
  public String toString() {
    return instrumentGroupShortType + "_" + instrumentShortCode;
  }
}
