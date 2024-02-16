package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class NavigationDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_NAV_DATUM = "UPDATE_NAV_DATUM";


  // TODO move this to datasource
  private DropDownItem instrument;
  private String processingLevel = "Raw";
  private String comments;
  private DropDownItem navDatum;

  public NavigationDatasetInstrumentModel(String instrumentGroupShortCode) {
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

  public DropDownItem getNavDatum() {
    return navDatum;
  }

  public void setNavDatum(DropDownItem navDatum) {
    setIfChanged(UPDATE_NAV_DATUM, navDatum, () -> this.navDatum, (nv) -> this.navDatum = nv);
  }
}
