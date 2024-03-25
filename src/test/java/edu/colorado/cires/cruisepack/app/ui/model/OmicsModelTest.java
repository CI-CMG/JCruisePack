package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.ExpectedAnalyses;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsData;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsPoc;
import edu.colorado.cires.cruisepack.app.service.metadata.SamplingTypes;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OmicsModelTest extends PropertyChangeModelTest<OmicsModel> {

  @Override
  protected OmicsModel createModel() {
    return new OmicsModel();
  }

  @Test
  void restoreDefaults() {
    model.setSamplingConducted(true);
    model.setContact(new DropDownItem("contact-id-1", "contact-name"));
    model.setContactError("contact-error");
    Path trackingSheetPath = Paths.get("sample-tracking-sheet");
    model.setSampleTrackingSheet(trackingSheetPath);
    model.setSampleTrackingSheetError("sample-tracking-sheet-error");
    model.setBioProjectAccession("bio-project-accession");
    model.setBioProjectAccessionError("bio-project-accession-error");
    model.setSamplingTypesError("sampling-types-error");
    model.setExpectedAnalysesError("expected-analyses-error");
    model.setAdditionalSamplingInformation("additional-sampling-information");
    model.setAdditionalSamplingInformationError("additional-sampling-information-error");
    
    model.setWaterSamplingType(true);
    model.setSoilSedimentSamplingType(true);
    model.setOrganicTissueSamplingType(true);
    
    model.setBarcodingExpectedAnalysis(true);
    model.setGenomicsExpectedAnalysis(true);
    model.setTranscriptomicsExpectedAnalysis(true);
    model.setProteomicsExpectedAnalysis(true);
    model.setMetabolomicsExpectedAnalysis(true);
    model.setEpigeneticsExpectedAnalysis(true);
    model.setOtherExpectedAnalysis(true);
    model.setMetaBarcodingExpectedAnalysis(true);
    model.setMetaGenomicsExpectedAnalysis(true);
    model.setMetaTranscriptomicsExpectedAnalysis(true);
    model.setMetaProteomicsExpectedAnalysis(true);
    model.setMetaMetabolomicsExpectedAnalysis(true);
    model.setMicrobiomeExpectedAnalysis(true);
    
    clearEvents();
    model.restoreDefaults();
    
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLING_CONDUCTED, true, false);
    assertFalse(model.isSamplingConducted());
    assertChangeEvent(Events.UPDATE_OMICS_CONTACT, new DropDownItem("contact-id-1", "contact-name"), PersonDatastore.UNSELECTED_PERSON);
    assertEquals(PersonDatastore.UNSELECTED_PERSON, model.getContact());
    assertChangeEvent(Events.UPDATE_OMICS_CONTACT_ERROR, "contact-error", null);
    assertNull(model.getContactError());
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, trackingSheetPath, null);
    assertNull(model.getSampleTrackingSheet());
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET_ERROR, "sample-tracking-sheet-error", null);
    assertNull(model.getSampleTrackingSheetError());
    assertChangeEvent(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, "bio-project-accession", null);
    assertNull(model.getBioProjectAccession());
    assertChangeEvent(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR, "bio-project-accession-error", null);
    assertNull(model.getBioProjectAccessionError());
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLING_TYPES_ERROR, "sampling-types-error", null);
    assertNull(model.getSamplingTypesError());
    assertChangeEvent(Events.UPDATE_OMICS_EXPECTED_ANALYSES_ERROR, "expected-analyses-error", null);
    assertNull(model.getExpectedAnalysesError());
    assertChangeEvent(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, "additional-sampling-information", null);
    assertNull(model.getAdditionalSamplingInformation());
    assertChangeEvent(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR, "additional-sampling-information-error", null);
    assertNull(model.getAdditionalSamplingInformationError());
    
    assertChangeEvent(Events.UPDATE_OMICS_WATER_SAMPLING_TYPE, true, false);
    assertFalse(model.getSamplingTypes().isWater());
    assertChangeEvent(Events.UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE, true, false);
    assertFalse(model.getSamplingTypes().isSoilSediment());
    assertChangeEvent(Events.UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE, true, false);
    assertFalse(model.getSamplingTypes().isOrganicTissue());
    
    assertChangeEvent(Events.UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isBarcoding());
    assertChangeEvent(Events.UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isGenomics());
    assertChangeEvent(Events.UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isTranscriptomics());
    assertChangeEvent(Events.UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isProteomics());
    assertChangeEvent(Events.UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetabolomics());
    assertChangeEvent(Events.UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isEpigenetics());
    assertChangeEvent(Events.UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isOther());
    assertChangeEvent(Events.UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetabarcoding());
    assertChangeEvent(Events.UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetagenomics());
    assertChangeEvent(Events.UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetatranscriptomics());
    assertChangeEvent(Events.UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetaproteomics());
    assertChangeEvent(Events.UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMetametabolomics());
    assertChangeEvent(Events.UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS, true, false);
    assertFalse(model.getExpectedAnalyses().isMicrobiome());
  }

  @Test
  void updateFormState() {
    Path expectedTrackingSheetPath = Paths.get("omics-sample-tracking-sheet");
    CruiseData cruiseData = CruiseData.builder()
        .withOmics(OmicsData.builder()
            .withSamplingTypes(Arrays.stream(SamplingTypes.values())
                .map(SamplingTypes::getName)
                .toList())
            .withAnalysesTypes(Arrays.stream(ExpectedAnalyses.values())
                .map(ExpectedAnalyses::getName)
                .toList())
            .withOmicsPoc(OmicsPoc.builder()
                .withUuid(UUID.randomUUID().toString())
                .withName("omics-poc-name")
                .withEmail("omics-poc-email")
                .withPhone("omics-poc-phone")
                .build())
            .withSampleTrackingSheet(expectedTrackingSheetPath)
            .withSamplingConducted(true)
            .withNCBIAccession("omics-ncbi-accession")
            .withOmicsComment("omics-comment")
            .build())
        .build();
    
    model.updateFormState(cruiseData);
    
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLING_CONDUCTED, false, true);
    assertTrue(model.isSamplingConducted());
    assertChangeEvent(Events.UPDATE_OMICS_CONTACT, PersonDatastore.UNSELECTED_PERSON, new DropDownItem(
        cruiseData.getOmics().getOmicsPoc().getUuid(), cruiseData.getOmics().getOmicsPoc().getName()
    ));
    assertEquals(new DropDownItem(
        cruiseData.getOmics().getOmicsPoc().getUuid(), cruiseData.getOmics().getOmicsPoc().getName()
    ), model.getContact());
    assertChangeEvent(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, null, expectedTrackingSheetPath);
    assertEquals(expectedTrackingSheetPath, model.getSampleTrackingSheet());
    assertChangeEvent(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, null, model.getBioProjectAccession());
    assertEquals("omics-ncbi-accession", model.getBioProjectAccession());

    assertChangeEvent(Events.UPDATE_OMICS_WATER_SAMPLING_TYPE, false, true);
    assertTrue(model.getSamplingTypes().isWater());
    assertChangeEvent(Events.UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE, false, true);
    assertTrue(model.getSamplingTypes().isSoilSediment());
    assertChangeEvent(Events.UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE, false, true);
    assertTrue(model.getSamplingTypes().isOrganicTissue());

    assertChangeEvent(Events.UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isBarcoding());
    assertChangeEvent(Events.UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isGenomics());
    assertChangeEvent(Events.UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isTranscriptomics());
    assertChangeEvent(Events.UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isProteomics());
    assertChangeEvent(Events.UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetabolomics());
    assertChangeEvent(Events.UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isEpigenetics());
    assertChangeEvent(Events.UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isOther());
    assertChangeEvent(Events.UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetabarcoding());
    assertChangeEvent(Events.UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetagenomics());
    assertChangeEvent(Events.UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetatranscriptomics());
    assertChangeEvent(Events.UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetaproteomics());
    assertChangeEvent(Events.UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMetametabolomics());
    assertChangeEvent(Events.UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS, false, true);
    assertTrue(model.getExpectedAnalyses().isMicrobiome());
    
    assertChangeEvent(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, null, "omics-comment");
    assertEquals("omics-comment", model.getAdditionalSamplingInformation());
  }

  @Test
  void setSamplingConducted() {
    assertPropertyChange(Events.UPDATE_OMICS_SAMPLING_CONDUCTED, model::isSamplingConducted, model::setSamplingConducted, true, false, false);
  }

  @Test
  void setContact() {
    assertPropertyChange(Events.UPDATE_OMICS_CONTACT, model::getContact, model::setContact, new DropDownItem("1", "value1"), new DropDownItem("2", "value2"), PersonDatastore.UNSELECTED_PERSON);
  }

  @Test
  void setSampleTrackingSheet() {
    assertPropertyChange(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, model::getSampleTrackingSheet, model::setSampleTrackingSheet, Paths.get("path1"), Paths.get("path2"), null);
  }

  @Test
  void setBioProjectAccession() {
    assertPropertyChange(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, model::getBioProjectAccession, model::setBioProjectAccession, "value1", "value2", null);
    
    clearEvents();
    
    model.setBioProjectAccession("");
    assertChangeEvent(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, "value2", null);
    assertNull(model.getBioProjectAccession());
  }

  @Test
  void setWaterSamplingType() {
    assertPropertyChange(Events.UPDATE_OMICS_WATER_SAMPLING_TYPE, () -> model.getSamplingTypes().isWater(), model::setWaterSamplingType, true, false, false);
  }

  @Test
  void setSoilSedimentSamplingType() {
    assertPropertyChange(Events.UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE, () -> model.getSamplingTypes().isSoilSediment(), model::setSoilSedimentSamplingType, true, false, false);
  }

  @Test
  void setOrganicTissueSamplingType() {
    assertPropertyChange(Events.UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE, () -> model.getSamplingTypes().isOrganicTissue(), model::setOrganicTissueSamplingType, true, false, false);
  }

  @Test
  void setBarcodingExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isBarcoding(), model::setBarcodingExpectedAnalysis, true, false, false);
  }

  @Test
  void setGenomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isGenomics(), model::setGenomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setTranscriptomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isTranscriptomics(), model::setTranscriptomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setProteomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isProteomics(), model::setProteomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetabolomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetabolomics(), model::setMetabolomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setEpigeneticsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isEpigenetics(), model::setEpigeneticsExpectedAnalysis, true, false, false);
  }

  @Test
  void setOtherExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isOther(), model::setOtherExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetaBarcodingExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetabarcoding(), model::setMetaBarcodingExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetaGenomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetagenomics(), model::setMetaGenomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetaTranscriptomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetatranscriptomics(), model::setMetaTranscriptomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetaProteomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetaproteomics(), model::setMetaProteomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setMetaMetabolomicsExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMetametabolomics(), model::setMetaMetabolomicsExpectedAnalysis, true, false, false);
  }

  @Test
  void setMicrobiomeExpectedAnalysis() {
    assertPropertyChange(Events.UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS, () -> model.getExpectedAnalyses().isMicrobiome(), model::setMicrobiomeExpectedAnalysis, true, false, false);
  }

  @Test
  void setAdditionalSamplingInformation() {
    assertPropertyChange(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, model::getAdditionalSamplingInformation, model::setAdditionalSamplingInformation, "value1", "value2", null);
  }

  @Test
  void setAdditionalSamplingInformationError() {
    assertPropertyChange(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR, model::getAdditionalSamplingInformationError, model::setAdditionalSamplingInformationError, "value1", "value2", null);
  }

  @Test
  void setContactError() {
    assertPropertyChange(Events.UPDATE_OMICS_CONTACT_ERROR, model::getContactError, model::setContactError, "value1", "value2", null);
  }

  @Test
  void setSampleTrackingSheetError() {
    assertPropertyChange(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET_ERROR, model::getSampleTrackingSheetError, model::setSampleTrackingSheetError, "value1", "value2", null);
  }

  @Test
  void setBioProjectAccessionError() {
    assertPropertyChange(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR, model::getBioProjectAccessionError, model::setBioProjectAccessionError, "value1", "value2", null);
  }

  @Test
  void setSamplingTypesError() {
    assertPropertyChange(Events.UPDATE_OMICS_SAMPLING_TYPES_ERROR, model::getSamplingTypesError, model::setSamplingTypesError, "value1", "value2", null);
  }

  @Test
  void setExpectedAnalysesError() {
    assertPropertyChange(Events.UPDATE_OMICS_EXPECTED_ANALYSES_ERROR, model::getExpectedAnalysesError, model::setExpectedAnalysesError, "value1", "value2", null);
  }
}