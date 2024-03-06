package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidNavigationDatumDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.HashMap;
import java.util.Map;

public class NavigationAdditionalFieldsModel extends AdditionalFieldsModel {
  public static final String UPDATE_NAV_DATUM = "UPDATE_NAV_DATUM";
  public static final String UPDATE_NAV_DATUM_ERROR = "UPDATE_NAV_DATUM_ERROR";
  @ValidNavigationDatumDropDownItem
  private DropDownItem navDatum = NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM;
  private String navDatumError = null;

  public void clearErrors() {
    setNavDatumError(null);
  }

  public DropDownItem getNavDatum() {
    return navDatum;
  }

  public void setNavDatum(DropDownItem navDatum) {
    setIfChanged(UPDATE_NAV_DATUM, navDatum, () -> this.navDatum, (nv) -> this.navDatum = nv);
  }

  private void setNavDatumError(String message) {
    setIfChanged(UPDATE_NAV_DATUM_ERROR, message, () -> this.navDatumError, (e) -> this.navDatumError = e);
  }

  @Override
  protected void setError(String propertyPath, String message) {
    if (propertyPath.endsWith("navDatum")) {
      setNavDatumError(message);
    }
  }

  @Override
  public Map<String, Object> transform() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("nav_datum", navDatum.getValue());
    return map;
  }
}
