package edu.colorado.cires.cruisepack.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ImportRow {
  
  String getShipName();
  String getCruiseID();
  String getLeg();
  String getChiefScientist();
  String getSponsorOrganization();
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
