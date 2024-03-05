package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;

public class AncillaryDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public AncillaryDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
  }

  @Override
  protected InstrumentStatus getSelectedInstrumentProcessingLevel() {
    return InstrumentStatus.RAW;
  }

  @Override
  public void clearErrors() {
    setPublicReleaseDateError(null);
    setDataPathError(null);
    setInstrumentError(null);
    setCommentsError(null);
  }

  @Override
  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    }
  }
}
