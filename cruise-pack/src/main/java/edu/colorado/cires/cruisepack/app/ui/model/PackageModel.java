package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PackageModel extends PropertyChangeModel {

  private String cruiseId = "";
  private DropDownItem sea = SeaDatastore.UNSELECTED_SEA;
  private DropDownItem arrivalPort = PortDatastore.UNSELECTED_PORT;
  private DropDownItem departurePort = PortDatastore.UNSELECTED_PORT;

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    String oldCruiseId = this.cruiseId;
    if (!Objects.equals(cruiseId, oldCruiseId)) {
      this.cruiseId = cruiseId;
      fireChangeEvent(Events.UPDATE_CRUISE_ID, oldCruiseId, cruiseId);
    }
  }

  public DropDownItem getSea() {
    return sea;
  }

  public DropDownItem getArrivalPort() {
    return arrivalPort;
  }

  public void setArrivalPort(DropDownItem arrivalPort) {
    DropDownItem oldArrivalPort = this.arrivalPort;
    if (!Objects.equals(arrivalPort, oldArrivalPort)) {
      this.arrivalPort = arrivalPort;
      fireChangeEvent(Events.UPDATE_ARRIVAL_PORT, oldArrivalPort, arrivalPort);
    }
  }

  public DropDownItem getDeparturePort() {
    return departurePort;
  }

  public void setDeparturePort(DropDownItem departurePort) {
    DropDownItem oldDeparturePort = this.departurePort;
    if (!Objects.equals(departurePort, oldDeparturePort)) {
      this.departurePort = departurePort;
      fireChangeEvent(Events.UPDATE_DEPARTURE_PORT, oldDeparturePort, departurePort);
    }
  }

  public void setSea(DropDownItem sea) {
    DropDownItem oldSea = this.sea;
    if (!Objects.equals(sea, oldSea)) {
      this.sea = sea;
      fireChangeEvent(Events.UPDATE_SEA, oldSea, sea);
    }
  }
}
