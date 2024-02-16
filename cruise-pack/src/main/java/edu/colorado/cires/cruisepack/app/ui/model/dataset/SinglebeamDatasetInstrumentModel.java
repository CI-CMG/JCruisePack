package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class SinglebeamDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_VERTICAL_DATUM = "UPDATE_VERTICAL_DATUM";
  public static final String UPDATE_OBS_RATE = "UPDATE_OBS_RATE";
  public static final String UPDATE_SOUND_VELOCITY = "UPDATE_SOUND_VELOCITY";

  // TODO move this to datasource
  private DropDownItem instrument;
  private String processingLevel = "Raw";
  private String comments;
  private DropDownItem verticalDatum;
  private String obsRate;
  private String soundVelocity;

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
}
