package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.InstrumentNameHolder;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BaseDatasetInstrumentModelTest extends PropertyChangeModelTest<BaseDatasetInstrumentModel<AdditionalFieldsModel>> {
  
  private static final String INSTRUMENT_SHORT_NAME = InstrumentGroupName.XBT.getShortName();

  @Override
  protected BaseDatasetInstrumentModel<AdditionalFieldsModel> createModel() {
    return new BaseDatasetInstrumentModel<>(INSTRUMENT_SHORT_NAME) {};
  }

  @Test
  void getSelectedInstrument() {
    DropDownItem instrument = new DropDownItem("1", "instrument 1");
    model.setInstrument(instrument);

    Optional<DropDownItem> maybeInstrument = model.getSelectedInstrument();
    assertTrue(maybeInstrument.isPresent());
    assertEquals(instrument, maybeInstrument.get());
  }

  @Test
  void setInstrument() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_INSTRUMENT,
        model::getInstrument,
        model::setInstrument,
        new DropDownItem("1", "instrument 1"),
        new DropDownItem("2", "instrument 2"),
        InstrumentDatastore.UNSELECTED_INSTRUMENT
    );
  }

  @Test
  void setProcessingLevel() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL,
        model::getProcessingLevel,
        model::setProcessingLevel,
        "value1",
        "value2",
        "Raw"
    );
  }

  @Test
  void setComments() {
    assertPropertyChange(BaseDatasetInstrumentModel.UPDATE_COMMENTS, model::getComments, model::setComments, "value1", "value2", null);
  }

  @Test
  void clearErrors() {
    String publicReleaseDateError = "public release date error";
    String dataPathError = "data path error";
    String instrumentError = "instrument error";
    String processingLevelError = "processing level error";
    String commentsError = "comments error";
    String ancillaryPathError = "ancillary path error";
    String ancillaryDetailsError = "ancillary details error";
    String navDatumError = "nav datum error";
    
    NavigationAdditionalFieldsModel additionalFieldsModel = new NavigationAdditionalFieldsModel();
    addChangeListener(additionalFieldsModel);
    model.setAdditionalFieldsModel(additionalFieldsModel);
    
    model.setPublicReleaseDateError(publicReleaseDateError);
    model.setDataPathError(dataPathError);
    model.setInstrumentError(instrumentError);
    model.setProcessingLevelError(processingLevelError);
    model.setCommentsError(commentsError);
    model.setAncillaryPathError(ancillaryPathError);
    model.setAncillaryDetailsError(ancillaryDetailsError);
    additionalFieldsModel.setError("navDatum", navDatumError);
    
    clearEvents();
    model.clearErrors();
    
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR, publicReleaseDateError, null);
    assertNull(model.getPublicReleaseDateError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_DATASET_DATA_PATH_ERROR, dataPathError, null);
    assertNull(model.getDataPathError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR, instrumentError, null);
    assertNull(model.getInstrumentError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR, processingLevelError, null);
    assertNull(model.getProcessingLevelError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_COMMENTS_ERROR, commentsError, null);
    assertNull(model.getCommentsError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH_ERROR, ancillaryPathError, null);
    assertNull(model.getAncillaryPathError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS_ERROR, ancillaryDetailsError, null);
    assertNull(model.getAncillaryDetailsError());
    assertChangeEvent(NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR, navDatumError, null);
    assertNull(additionalFieldsModel.getNavDatumError());
  }

  @Test
  void getInstrumentGroupShortCode() {
    assertEquals(INSTRUMENT_SHORT_NAME, model.getInstrumentGroupShortCode());
  }

  @Test
  void setPublicReleaseDate() {
    assertPropertyChange(BaseDatasetInstrumentModel.UPDATE_PUBLIC_RELEASE_DATE, model::getPublicReleaseDate, model::setPublicReleaseDate, LocalDate.now(), LocalDate.now().plusDays(1), null);
  }

  @Test
  void setDataPath() {
    assertPropertyChange(BaseDatasetInstrumentModel.UPDATE_DATA_PATH, model::getDataPath, model::setDataPath, Paths.get("path 1"), Paths.get("path 2"), null);
  }

  @Test
  void getPackageKey() {
    Optional<InstrumentDetailPackageKey> maybeKey = model.getPackageKey();
    assertTrue(maybeKey.isEmpty());
    
    model.setInstrument(new DropDownItem("1", "instrument 1"));
    maybeKey = model.getPackageKey();
    assertTrue(maybeKey.isPresent());
    assertEquals(new InstrumentDetailPackageKey(INSTRUMENT_SHORT_NAME, "instrument 1"), maybeKey.get());
  }

  @Test
  void getInstrumentNameHolder() {
    Instrument referenceInstrument = new Instrument();
    referenceInstrument.setUuid(UUID.randomUUID().toString());
    referenceInstrument.setName("instrument 1");
    
    Optional<InstrumentNameHolder> maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertTrue(maybeNameHolder.isEmpty());
    
    DropDownItem selectedInstrument = new DropDownItem("", "");
    model.setInstrument(selectedInstrument);
    maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertTrue(maybeNameHolder.isEmpty());
    
    selectedInstrument = new DropDownItem(referenceInstrument.getUuid(), "does not match");
    model.setInstrument(selectedInstrument);
    maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertTrue(maybeNameHolder.isEmpty());
    
    selectedInstrument = new DropDownItem("does not match", referenceInstrument.getName());
    model.setInstrument(selectedInstrument);
    maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertTrue(maybeNameHolder.isEmpty());
    
    model.setDataPath(Paths.get("data path"));
    model.setPublicReleaseDate(LocalDate.now());
    model.setComments("comment");
    model.setAncillaryPath(Paths.get("ancillary path"));
    model.setAncillaryDetails("ancillary details");
    model.setAdditionalFieldsModel(new AdditionalFieldsModel() {
      @Override
      protected void clearErrors() {
        
      }

      @Override
      protected void setError(String propertyPath, String message) {

      }

      @Override
      public Map<String, Object> transform() {
        return Map.of(
            "test additional field", "test additional value"
        );
      }
    });
    
    selectedInstrument = new DropDownItem(referenceInstrument.getUuid(), referenceInstrument.getName());
    model.setInstrument(selectedInstrument);
    maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertFalse(maybeNameHolder.isEmpty());
    
    InstrumentNameHolder nameHolder = maybeNameHolder.get();
    assertEquals(referenceInstrument.getUuid(), nameHolder.getUuid());
    assertEquals(referenceInstrument.getName(), nameHolder.getInstrument());
    assertEquals(referenceInstrument.getShortName(), nameHolder.getShortName());
    assertEquals(InstrumentStatus.forValue(model.getProcessingLevel()), nameHolder.getStatus());
    assertEquals(model.getDataPath(), nameHolder.getDataPath());
    assertEquals(Collections.emptyList(), nameHolder.getAdditionalFiles());
    assertEquals(model.getPublicReleaseDate(), nameHolder.getReleaseDate());
    assertEquals(model.getComments(), nameHolder.getDataComment());
    assertEquals(model.getAdditionalFieldsModel().transform(), nameHolder.getAdditionalFields());
    assertEquals(model.getAncillaryPath(), nameHolder.getAncillaryDataPath());
    assertEquals(model.getAncillaryDetails(), nameHolder.getAncillaryDataDetails());
    
    model.setAdditionalFieldsModel(null);
    maybeNameHolder = model.getInstrumentNameHolder(referenceInstrument);
    assertFalse(maybeNameHolder.isEmpty());

    nameHolder = maybeNameHolder.get();
    assertEquals(referenceInstrument.getUuid(), nameHolder.getUuid());
    assertEquals(referenceInstrument.getName(), nameHolder.getInstrument());
    assertEquals(referenceInstrument.getShortName(), nameHolder.getShortName());
    assertEquals(InstrumentStatus.forValue(model.getProcessingLevel()), nameHolder.getStatus());
    assertEquals(model.getDataPath(), nameHolder.getDataPath());
    assertEquals(Collections.emptyList(), nameHolder.getAdditionalFiles());
    assertEquals(model.getPublicReleaseDate(), nameHolder.getReleaseDate());
    assertEquals(model.getComments(), nameHolder.getDataComment());
    assertNull(nameHolder.getAdditionalFields());
    assertEquals(model.getAncillaryPath(), nameHolder.getAncillaryDataPath());
    assertEquals(model.getAncillaryDetails(), nameHolder.getAncillaryDataDetails());
  }

  @Test
  void setAncillaryPath() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH, 
        model::getAncillaryPath,
        model::setAncillaryPath,
        Paths.get("ancillary path 1"),
        Paths.get("ancillary path 2"),
        null
    );
  }

  @Test
  void setAncillaryPathError() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH_ERROR,
        model::getAncillaryPathError,
        model::setAncillaryPathError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setAncillaryDetails() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS,
        model::getAncillaryDetails,
        model::setAncillaryDetails,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setAncillaryDetailsError() {
    assertPropertyChange(
        BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS_ERROR,
        model::getAncillaryDetailsError,
        model::setAncillaryDetailsError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setModelErrors() {
    NavigationAdditionalFieldsModel additionalFieldsModel = new NavigationAdditionalFieldsModel();
    addChangeListener(additionalFieldsModel);
    model.setAdditionalFieldsModel(additionalFieldsModel);
    model.setModelErrors("publicReleaseDate", "release date error");
    model.setModelErrors("dataPath", "data path error");
    model.setModelErrors("ancillaryPath", "ancillary path error");
    model.setModelErrors("ancillaryDetails", "ancillary details error");
    model.setModelErrors("instrument", "instrument error");
    model.setModelErrors("processingLevel", "processing level error");
    model.setModelErrors("comments", "comments error");
    model.setModelErrors(".additionalFieldsModel.navDatum", "nav datum error");
    
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR, null, "release date error");
    assertEquals("release date error", model.getPublicReleaseDateError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_DATASET_DATA_PATH_ERROR, null, "data path error");
    assertEquals("data path error", model.getDataPathError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH_ERROR, null, "ancillary path error");
    assertEquals("ancillary path error", model.getAncillaryPathError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS_ERROR, null, "ancillary details error");
    assertEquals("ancillary details error", model.getAncillaryDetailsError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR, null, "instrument error");
    assertEquals("instrument error", model.getInstrumentError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR, null, "processing level error");
    assertEquals("processing level error", model.getProcessingLevelError());
    assertChangeEvent(BaseDatasetInstrumentModel.UPDATE_COMMENTS_ERROR, null, "comments error");
    assertEquals("comments error", model.getCommentsError());
    assertChangeEvent(NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR, null, "nav datum error");
    assertEquals("nav datum error", additionalFieldsModel.getNavDatumError());
  }

  @Test
  void setAdditionalFieldsModel() {
    AdditionalFieldsModel additionalFieldsModel = new GravityAdditionalFieldsModel();
    model.setAdditionalFieldsModel(additionalFieldsModel);
    
    assertEquals(additionalFieldsModel, model.getAdditionalFieldsModel());
  }
}