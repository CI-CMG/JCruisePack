package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Map;
import org.junit.jupiter.api.Test;

class NavigationAdditionalFieldsModelTest extends PropertyChangeModelTest<NavigationAdditionalFieldsModel> {

  @Override
  protected NavigationAdditionalFieldsModel createModel() {
    return new NavigationAdditionalFieldsModel();
  }

  @Test
  void clearErrors() {
    model.setNavDatumError("error message");
    
    clearEvents();
    
    model.clearErrors();
    
    assertChangeEvent(NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR, "error message", null);
    assertNull(model.getNavDatumError());
  }

  @Test
  void setNavDatum() {
    assertPropertyChange(
        NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM,
        model::getNavDatum,
        model::setNavDatum,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM
    );
  }

  @Test
  void setNavDatumError() {
    assertPropertyChange(
        NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR,
        model::getNavDatumError,
        model::setNavDatumError,
        "value 1",
        "value 2",
        null
    );
  }

  @Test
  void setError() {
    model.setError("navDatum", "error message");
    
    assertChangeEvent(NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR, null, "error message");
    assertEquals("error message", model.getNavDatumError());
  }

  @Test
  void transform() {
    model.setNavDatum(new DropDownItem("1", "value 1"));
    
    assertEquals(Map.of(
        "nav_datum", "value 1"
    ),  model.transform());
  }
}