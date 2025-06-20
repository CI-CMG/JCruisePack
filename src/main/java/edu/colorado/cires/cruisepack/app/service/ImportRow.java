package edu.colorado.cires.cruisepack.app.service;

import java.util.List;

public interface ImportRow {
  
  String getShipName();
  String getCruiseID();
  String getLeg();
  String getChiefScientist();
  String getSourceOrganization();
  String getFundingOrganization();
  String getDeparturePort();
  String getStartDate();
  String getArrivalPort();
  String getEndDate();
  String getSeaArea();
  String getProjectName();
  String getCruiseTitle();
  String getCruisePurpose();
  List<String> getCTDInstruments();
  List<String> getMBESInstruments();
  List<String> getSBESInstruments();
  List<String> getWaterColumnInstruments();
  List<String> getADCPInstruments();
  String getComments();

}
