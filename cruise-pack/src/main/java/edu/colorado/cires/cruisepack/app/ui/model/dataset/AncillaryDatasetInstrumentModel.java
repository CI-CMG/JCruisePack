package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class AncillaryDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";

  // TODO move this to datasource
  private DropDownItem instrument;
  private String comments;

  public AncillaryDatasetInstrumentModel(String instrumentGroupShortCode) {
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
    return InstrumentStatus.RAW;
  }

  public DropDownItem getInstrument() {
    return instrument;
  }

  public void setInstrument(DropDownItem instrument) {
    setIfChanged(UPDATE_INSTRUMENT, instrument, () -> this.instrument, (nv) -> this.instrument = nv);
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    setIfChanged(UPDATE_COMMENTS, comments, () -> this.comments, (nv) -> this.comments = nv);
  }
}
