package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

public enum InstrumentGroupName {
  XBT("XBT", "XBT"),
  SUB_BOTTOM("SUB-BOTTOM", "Sub Bottom"),
  WATER_COLUMN("WCSD", "Water Column Sonar"),
  SIDE_SCAN("SIDE-SCAN", "Side Scan Sonar"),
  SINGLE_BEAM("SB-BATHY", "Singlebeam Bathymetry"),
  OTHER("OTHER", "Other"),
  NAVIGATION("NAV", "Navigation"),
  MULTIBEAM("MB-BATHY", "Multibeam Bathymetry"),
  MAGNETICS("MAG", "Magnetics"),
  GRAVITY("GRAV", "Gravity"),
  CTD("CTD", "CTD"),
  ADCP("ADCP", "ADCP"),
  DOCUMENTS("DOCUMENTS", "Documents Data");

  private final String shortName;
  private final String longName;
  
  InstrumentGroupName(String shortName, String longName) {
    this.shortName = shortName;
    this.longName = longName;
  }
  
  public static InstrumentGroupName fromShortName(String shortName) {
    if (shortName.equals(XBT.shortName)) {
      return XBT;
    } else if (shortName.equals(SUB_BOTTOM.shortName)) {
      return SUB_BOTTOM;
    } else if (shortName.equals(WATER_COLUMN.shortName)) {
      return WATER_COLUMN;
    } else if (shortName.equals(SIDE_SCAN.shortName)) {
      return SIDE_SCAN;
    } else if (shortName.equals(SINGLE_BEAM.shortName)) {
      return SINGLE_BEAM;
    } else if (shortName.equals(OTHER.shortName)) {
      return OTHER;
    } else if (shortName.equals(NAVIGATION.shortName)) {
      return NAVIGATION;
    } else if (shortName.equals(MULTIBEAM.shortName)) {
      return MULTIBEAM;
    } else if (shortName.equals(MAGNETICS.shortName)) {
      return MAGNETICS;
    } else if (shortName.equals(GRAVITY.shortName)) {
      return GRAVITY;
    } else if (shortName.equals(CTD.shortName)) {
      return CTD;
    } else if (shortName.equals(ADCP.shortName)) {
      return ADCP;
    } else if (shortName.equals(DOCUMENTS.shortName)) {
      return DOCUMENTS;
    } else {
      throw new IllegalArgumentException("Instrument group not found for short name: " + shortName);
    }
  }
  
  public static InstrumentGroupName fromLongName(String longName) {
    if (longName.equals(XBT.longName)) {
      return XBT;
    } else if (longName.equals(SUB_BOTTOM.longName)) {
      return SUB_BOTTOM;
    } else if (longName.equals(WATER_COLUMN.longName)) {
      return WATER_COLUMN;
    } else if (longName.equals(SIDE_SCAN.longName)) {
      return SIDE_SCAN;
    } else if (longName.equals(SINGLE_BEAM.longName)) {
      return SINGLE_BEAM;
    } else if (longName.equals(OTHER.longName)) {
      return OTHER;
    } else if (longName.equals(NAVIGATION.longName)) {
      return NAVIGATION;
    } else if (longName.equals(MULTIBEAM.longName)) {
      return MULTIBEAM;
    } else if (longName.equals(MAGNETICS.longName)) {
      return MAGNETICS;
    } else if (longName.equals(GRAVITY.longName)) {
      return GRAVITY;
    } else if (longName.equals(CTD.longName)) {
      return CTD;
    } else if (longName.equals(ADCP.longName)) {
      return ADCP;
    } else if (longName.equals(DOCUMENTS.longName)) {
      return DOCUMENTS;
    } else {
      throw new IllegalArgumentException(
          String.format(
              "Instrument group not found for long name: %s",
              longName
          )
      );
    }
  }

  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return longName;
  }
}
