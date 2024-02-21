package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidInstrumentDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class SinglebeamDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_INSTRUMENT_ERROR = "UPDATE_INSTRUMENT_ERROR";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING_LEVEL";
  public static final String UPDATE_PROCESSING_LEVEL_ERROR = "UPDATE_PROCESSING_LEVEL_ERROR";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_COMMENTS_ERROR = "UPDATE_COMMENTS_ERROR";
  public static final String UPDATE_VERTICAL_DATUM = "UPDATE_VERTICAL_DATUM";
  public static final String UPDATE_VERTICAL_DATUM_ERROR = "UPDATE_VERTICAL_DATUM_ERROR";
  public static final String UPDATE_OBS_RATE = "UPDATE_OBS_RATE";
  public static final String UPDATE_OBS_RATE_ERROR = "UPDATE_OBS_RATE_ERROR";
  public static final String UPDATE_SOUND_VELOCITY = "UPDATE_SOUND_VELOCITY";
  public static final String UPDATE_SOUND_VELOCITY_ERROR = "UPDATE_SOUND_VELOCITY_ERROR";

  // TODO move this to datasource
  @NotNull @ValidInstrumentDropDownItem
  private DropDownItem instrument;
  private String instrumentError = null;
  @NotBlank
  private String processingLevel = "Raw";
  private String processingLevelError = null;
  @NotBlank
  private String comments;
  private String commentsError = null;
  @NotNull @ValidInstrumentDropDownItem
  private DropDownItem verticalDatum;
  private String verticalDatumError = null;
  @NotBlank
  private String obsRate;
  private String obsRateError = null;
  @NotBlank
  private String soundVelocity;
  private String soundVelocityError = null;

  public SinglebeamDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
  }

  @Override
  public Optional<DropDownItem> getSelectedInstrument() {
    if (instrument == null || StringUtils.isBlank(instrument.getId())) {
      return Optional.empty();
    }
    return Optional.of(instrument);
  }

  @Override
  protected InstrumentStatus getSelectedInstrumentProcessingLevel() {
    return InstrumentStatus.forValue(processingLevel);
  }

  public DropDownItem getInstrument() {
    return instrument;
  }

  public void setInstrument(DropDownItem instrument) {
    setIfChanged(UPDATE_INSTRUMENT, instrument, () -> this.instrument, (nv) -> this.instrument = nv);
  }

  public String getProcessingLevel() {
    return processingLevel;
  }

  public void setProcessingLevel(String processingLevel) {
    setIfChanged(UPDATE_PROCESSING_LEVEL, processingLevel, () -> this.processingLevel, (nv) -> this.processingLevel = nv);
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    setIfChanged(UPDATE_COMMENTS, comments, () -> this.comments, (nv) -> this.comments = nv);
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

  private void setInstrumentError(String message) {
    setIfChanged(UPDATE_INSTRUMENT_ERROR, message, () -> this.instrumentError, (e) -> this.instrumentError = e);
  }

  private void setProcessingLevelError(String message) {
    setIfChanged(UPDATE_PROCESSING_LEVEL_ERROR, message, () -> this.processingLevelError, (e) -> this.processingLevelError = e);
  }

  private void setCommentsError(String message) {
    setIfChanged(UPDATE_COMMENTS_ERROR, message, () -> this.commentsError, (e) -> this.commentsError = e);
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

  @Override
  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("processingLevel")) {
      setProcessingLevelError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    } else if (propertyPath.endsWith("verticalDatum")) {
      setVerticalDatumError(message);
    } else if (propertyPath.endsWith("obsRate")) {
      setObsRateError(message);
    } else if (propertyPath.endsWith("soundVelocity")) {
      setSoundVelocityError(message);
    }
  }
}
