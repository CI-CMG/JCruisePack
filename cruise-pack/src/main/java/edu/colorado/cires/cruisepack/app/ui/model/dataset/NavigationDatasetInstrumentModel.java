package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidNavigationDatumDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.HashMap;

public class NavigationDatasetInstrumentModel extends BaseDatasetInstrumentModel {
  public static final String UPDATE_NAV_DATUM = "UPDATE_NAV_DATUM";
  public static final String UPDATE_NAV_DATUM_ERROR = "UPDATE_NAV_DATUM_ERROR";
  @ValidNavigationDatumDropDownItem
  private DropDownItem navDatum = NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM;
  private String navDatumError = null;

  public NavigationDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
  }

  @Override
  public void clearErrors() {
    setPublicReleaseDateError(null);
    setDataPathError(null);
    setInstrumentError(null);
    setProcessingLevelError(null);
    setCommentsError(null);
    setNavDatumError(null);
  }
  
  @Override
  protected HashMap<String, Object> getAdditionalFields() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("nav_datum", navDatum.getValue());
    return map;
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
  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("processingLevel")) {
      setProcessingLevelError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    } else if (propertyPath.endsWith("navDatum")) {
      setNavDatumError(message);
    }
  }
}
