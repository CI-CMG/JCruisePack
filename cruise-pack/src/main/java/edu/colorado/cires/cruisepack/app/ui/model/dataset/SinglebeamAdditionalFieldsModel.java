package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSinglebeamVerticalDatumDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class SinglebeamAdditionalFieldsModel extends AdditionalFieldsModel {
  public static final String UPDATE_VERTICAL_DATUM = "UPDATE_VERTICAL_DATUM";
  public static final String UPDATE_VERTICAL_DATUM_ERROR = "UPDATE_VERTICAL_DATUM_ERROR";
  public static final String UPDATE_OBS_RATE = "UPDATE_OBS_RATE";
  public static final String UPDATE_OBS_RATE_ERROR = "UPDATE_OBS_RATE_ERROR";
  public static final String UPDATE_SOUND_VELOCITY = "UPDATE_SOUND_VELOCITY";
  public static final String UPDATE_SOUND_VELOCITY_ERROR = "UPDATE_SOUND_VELOCITY_ERROR";

  @ValidSinglebeamVerticalDatumDropDownItem
  private DropDownItem verticalDatum = SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM;
  private String verticalDatumError = null;
  @NotBlank
  private String obsRate;
  private String obsRateError = null;
  @NotBlank
  private String soundVelocity;
  private String soundVelocityError = null;

  @Override
  public void clearErrors() {
    setVerticalDatumError(null);
    setObsRateError(null);
    setSoundVelocityError(null);
  }

  @Override
  protected void setError(String propertyPath, String message) {
    if (propertyPath.endsWith("verticalDatum")) {
      setVerticalDatumError(message);
    } else if (propertyPath.endsWith("obsRate")) {
      setObsRateError(message);
    } else if (propertyPath.endsWith("soundVelocity")) {
      setSoundVelocityError(message);
    }
  }

  @Override
  public Map<String, Object> transform() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("vertical_datum", verticalDatum.getValue());
    map.put("obs_rate", obsRate);
    map.put("sound_velocity", soundVelocity);
    return map;
  }

  public DropDownItem getVerticalDatum() {
    return verticalDatum;
  }

  public void setVerticalDatum(DropDownItem verticalDatum) {
    setIfChanged(UPDATE_VERTICAL_DATUM, verticalDatum, () -> this.verticalDatum, (nv) -> this.verticalDatum = nv);
  }

  public String getObsRate() {
    return obsRate;
  }

  public void setObsRate(String obsRate) {
    setIfChanged(UPDATE_OBS_RATE, obsRate, () -> this.obsRate, (nv) -> this.obsRate = nv);
  }

  public String getSoundVelocity() {
    return soundVelocity;
  }

  public void setSoundVelocity(String soundVelocity) {
    setIfChanged(UPDATE_SOUND_VELOCITY, soundVelocity, () -> this.soundVelocity, (nv) -> this.soundVelocity = nv);
  }

  private void setVerticalDatumError(String message) {
    setIfChanged(UPDATE_VERTICAL_DATUM_ERROR, message, () -> this.verticalDatumError, (e) -> this.verticalDatumError = e);
  }

  private void setObsRateError(String message) {
    setIfChanged(UPDATE_OBS_RATE_ERROR, message, () -> this.obsRateError, (e) -> this.obsRateError = e);
  }

  private void setSoundVelocityError(String message) {
    setIfChanged(UPDATE_SOUND_VELOCITY_ERROR, message, () -> this.soundVelocityError, (e) -> this.soundVelocityError = e);
  }
}
