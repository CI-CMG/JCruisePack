package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.service.metadata.ExpectedAnalyses;
import edu.colorado.cires.cruisepack.app.service.metadata.Omics;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsPoc;
import edu.colorado.cires.cruisepack.app.service.metadata.SamplingTypes;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidExpectedAnalyses;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSamplingTypes;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class OmicsModel extends PropertyChangeModel {

    private boolean samplingConducted = false;
    @ValidPersonDropDownItem
    private DropDownItem contact = PersonDatastore.UNSELECTED_PERSON;
    private String contactError = null;
    @NotNull(message = "must not be blank")
    @PathExists
    @PathIsFile
    private Path sampleTrackingSheet = null;
    private String sampleTrackingSheetError = null;
    @NotBlank
    private String bioProjectAccession = null;
    private String bioProjectAcessionError = null;
    @ValidSamplingTypes
    private SamplingTypesModel samplingTypes = new SamplingTypesModel();
    private String samplingTypesError = null;
    @ValidExpectedAnalyses
    private ExpectedAnalysesModel expectedAnalyses = new ExpectedAnalysesModel();
    private String expectedAnalysesError = null;
    @NotBlank
    private String additionalSamplingInformation = null;
    private String additionalSamplingInformationError = null;

    public void restoreDefaults() {
        setSamplingConducted(false);
        
        setContact(PersonDatastore.UNSELECTED_PERSON);
        setContactError(null);

        setSampleTrackingSheet(null);
        setSampleTrackingSheetError(null);

        setBioProjectAccession(null);
        setBioProjectAcessionError(null);

        setWaterSamplingType(false);
        setSoilSedimentSamplingType(false);
        setOrganicTissueSamplingType(false);
        setSamplingTypesError(null);

        setBarcodingExpectedAnalysis(false);
        setGenomicsExpectedAnalysis(false);
        setTranscriptomicsExpectedAnalysis(false);
        setProteomicsExpectedAnalysis(false);
        setMetabolomicsExpectedAnalysis(false);
        setEpigeneticsExpectedAnalysis(false);
        setOtherExpectedAnalysis(false);
        setMetaBarcodingExpectedAnalysis(false);
        setMetaGenomicsExpectedAnalysis(false);
        setMetatranscriptomicsExpectedAnalysis(false);
        setMetaproteomicsExpectedAnalysis(false);
        setMetametabolomicsExpectedAnalysis(false);
        setMicrobiomeExpectedAnalysis(false);
        setExpectedAnalysesError(null);

        setAdditionalSamplingInformation(null);
        setAdditionalSamplingInformationError(null);
    }

    public void updateFormState(Cruise cruiseMetadata) {
        Omics omicsMetadata = cruiseMetadata.getOmics();
        if (omicsMetadata != null) {
            for (String samplingType : omicsMetadata.samplingTypes()) {
                if (samplingType.equals(SamplingTypes.WATER.getName())) {
                    setWaterSamplingType(true);
                } else if (samplingType.equals(SamplingTypes.SOIL_SEDIMENT.getName())) {
                    setSoilSedimentSamplingType(true);
                } else if (samplingType.equals(SamplingTypes.ORGANIC_TISSUE.getName())) {
                    setOrganicTissueSamplingType(true);
                }
            }

            for (String analysis : omicsMetadata.analysesTypes()) {
                if (analysis.equals(ExpectedAnalyses.BARCODING.getName())) {
                    setBarcodingExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.GENOMICS.getName())) {
                    setGenomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.TRANSCRIPOMICS.getName())) {
                    setTranscriptomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.PROTEOMICS.getName())) {
                    setProteomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METABOLOMICS.getName())) {
                    setMetabolomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.EPIGENETICS.getName())) {
                    setEpigeneticsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.OTHER.getName())) {
                    setOtherExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METABARCODING.getName())) {
                    setMetaBarcodingExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METAGENOMICS.getName())) {
                    setMetaGenomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METATRANSCRIPTOMICS.getName())) {
                    setMetatranscriptomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METAPROTEOMICS.getName())) {
                    setMetaproteomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.METAMETABOLOMICS.getName())) {
                    setMetametabolomicsExpectedAnalysis(true);
                } else if (analysis.equals(ExpectedAnalyses.MICROBIOME.getName())) {
                    setMicrobiomeExpectedAnalysis(true);
                }
            }

            OmicsPoc poc = omicsMetadata.omicsPoc();
            if (poc != null) {
                setContact(new DropDownItem(poc.getUuid(), poc.getName()));
            }
            // TODO: setSampleTrackingSheet
            setBioProjectAccession(omicsMetadata.ncbiAccession());
            setAdditionalSamplingInformation(omicsMetadata.omicsComment());
            // TODO: setSamplingConducted
        }
    }
    
    public boolean isSamplingConducted() {
        return samplingConducted;
    }
    public void setSamplingConducted(boolean samplingConducted) {
        setIfChanged(Events.UPDATE_OMICS_SAMPLING_CONDUCTED, samplingConducted, () -> this.samplingConducted, (nv) -> this.samplingConducted = nv);
    }
    public DropDownItem getContact() {
        return contact;
    }
    public void setContact(DropDownItem contact) {
        setIfChanged(Events.UPDATE_OMICS_CONTACT, contact, () -> this.contact, (nv) -> this.contact = nv);
    }
    public Path getSampleTrackingSheet() {
        return sampleTrackingSheet;
    }
    public void setSampleTrackingSheet(Path sampleTrackingSheet) {
        setIfChanged(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, sampleTrackingSheet, () -> this.sampleTrackingSheet, (nv) -> this.sampleTrackingSheet = nv);
    }
    public String getBioProjectAccession() {
        return bioProjectAccession;
    }
    public void setBioProjectAccession(String bioProjectAccession) {
        bioProjectAccession = normalizeString(bioProjectAccession);
        setIfChanged(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, bioProjectAccession, () -> this.bioProjectAccession, (nv) -> this.bioProjectAccession = nv);
    }

    public SamplingTypesModel getSamplingTypes() {
        return samplingTypes;
    }

    public void setWaterSamplingType(boolean waterSamplingType) {
        boolean oldWaterSamplingType = this.samplingTypes.isWater();
        if (oldWaterSamplingType != waterSamplingType) {
            this.samplingTypes.setWater(waterSamplingType);
            fireChangeEvent(Events.UPDATE_OMICS_WATER_SAMPLING_TYPE, oldWaterSamplingType, waterSamplingType);
        }
    }

    public void setSoilSedimentSamplingType(boolean soilSedimentSamplingType) {
        boolean oldSoilSedimentSamplingType = this.samplingTypes.isSoilSediment();
        if (oldSoilSedimentSamplingType != soilSedimentSamplingType) {
            this.samplingTypes.setSoilSediment(soilSedimentSamplingType);
            fireChangeEvent(Events.UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE, oldSoilSedimentSamplingType, soilSedimentSamplingType);
        }
    }

    public void setOrganicTissueSamplingType(boolean organicTissueSamplingType) {
        boolean oldOrganicTissueSamplingType = this.samplingTypes.isOrganicTissue();
        if (oldOrganicTissueSamplingType != organicTissueSamplingType) {
            this.samplingTypes.setOrganicTissue(organicTissueSamplingType);
            fireChangeEvent(Events.UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE, oldOrganicTissueSamplingType, organicTissueSamplingType);
        }
    }

    public ExpectedAnalysesModel getExpectedAnalyses() {
        return expectedAnalyses;
    }

    public void setBarcodingExpectedAnalysis(boolean barcodingExpectedAnalysis) {
        boolean oldBarcodingExpectedAnalysis = this.expectedAnalyses.isBarcoding();
        if (oldBarcodingExpectedAnalysis != barcodingExpectedAnalysis) {
            this.expectedAnalyses.setBarcoding(barcodingExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS, oldBarcodingExpectedAnalysis, barcodingExpectedAnalysis);
        }
    }

    public void setGenomicsExpectedAnalysis(boolean genomicsExpectedAnalysis) {
        boolean oldGenomicsExpectedAnalysis = this.expectedAnalyses.isGenomics();
        if (oldGenomicsExpectedAnalysis != genomicsExpectedAnalysis) {
            this.expectedAnalyses.setGenomics(genomicsExpectedAnalysis);
        fireChangeEvent(Events.UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS, oldGenomicsExpectedAnalysis, genomicsExpectedAnalysis);
        }
    }

    public void setTranscriptomicsExpectedAnalysis(boolean transcriptomicsExpectedAnalysis) {
        boolean oldTranscriptomicsExpectedAnalysis = this.expectedAnalyses.isTranscriptomics();
        if (oldTranscriptomicsExpectedAnalysis != transcriptomicsExpectedAnalysis) {
            this.expectedAnalyses.setTranscriptomics(transcriptomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS, oldTranscriptomicsExpectedAnalysis, transcriptomicsExpectedAnalysis);
        }
    }

    public void setProteomicsExpectedAnalysis(boolean proteomicsExpectedAnalysis) {
        boolean oldProteomicsExpectedAnalysis = this.expectedAnalyses.isProteomics();
        if (oldProteomicsExpectedAnalysis != proteomicsExpectedAnalysis) {
            this.expectedAnalyses.setProteomics(proteomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS, oldProteomicsExpectedAnalysis, proteomicsExpectedAnalysis);
        }
    }

    public void setMetabolomicsExpectedAnalysis(boolean metabolomicsExpectedAnalysis) {
        boolean oldMetabolomicsExpectedAnalysis = this.expectedAnalyses.isMetabolomics();
        if (oldMetabolomicsExpectedAnalysis != metabolomicsExpectedAnalysis) {
            this.expectedAnalyses.setMetabolomics(metabolomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS, oldMetabolomicsExpectedAnalysis, metabolomicsExpectedAnalysis);
        }
    }

    public void setEpigeneticsExpectedAnalysis(boolean epigeneticsExpectedAnalysis) {
        boolean oldEpigeneticsExpectedAnalysis = this.expectedAnalyses.isEpigenetics();
        if (oldEpigeneticsExpectedAnalysis != epigeneticsExpectedAnalysis) {
            this.expectedAnalyses.setEpigenetics(epigeneticsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS, oldEpigeneticsExpectedAnalysis, epigeneticsExpectedAnalysis);
        }
    }

    public void setOtherExpectedAnalysis(boolean otherExpectedAnalysis) {
        boolean oldOtherExpectedAnalysis = this.expectedAnalyses.isOther();
        if (oldOtherExpectedAnalysis != otherExpectedAnalysis) {
            this.expectedAnalyses.setOther(otherExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS, oldOtherExpectedAnalysis, otherExpectedAnalysis);
        }
    }

    public void setMetaBarcodingExpectedAnalysis(boolean metabarcodingExpectedAnalysis) {
        boolean oldMetaborcodingExpectedAnalysis = this.expectedAnalyses.isMetabarcoding();
        if (oldMetaborcodingExpectedAnalysis != metabarcodingExpectedAnalysis) {
            this.expectedAnalyses.setMetabarcoding(metabarcodingExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS, oldMetaborcodingExpectedAnalysis, metabarcodingExpectedAnalysis);
        }
    }

    public void setMetaGenomicsExpectedAnalysis(boolean metaGenomicsExpectedAnalysis) {
        boolean oldMetaGenomicsExpectedAnalysis = this.expectedAnalyses.isMetagenomics();
        if (oldMetaGenomicsExpectedAnalysis != metaGenomicsExpectedAnalysis) {
            this.expectedAnalyses.setMetagenomics(metaGenomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS, oldMetaGenomicsExpectedAnalysis, metaGenomicsExpectedAnalysis);
        }
    }

    public void setMetatranscriptomicsExpectedAnalysis(boolean metatranscriptomicsExpectedAnalysis) {
        boolean oldMetatranscriptomicsExpectedAnalysis = this.expectedAnalyses.isMetatranscriptomics();
        if (oldMetatranscriptomicsExpectedAnalysis != metatranscriptomicsExpectedAnalysis) {
            this.expectedAnalyses.setMetatranscriptomics(metatranscriptomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS, oldMetatranscriptomicsExpectedAnalysis, metatranscriptomicsExpectedAnalysis);
        }
    }

    public void setMetaproteomicsExpectedAnalysis(boolean metaproteomicsExpectedAnalysis) {
        boolean oldMetaproteomicsExpectedAnalysis = this.expectedAnalyses.isMetaproteomics();
        if (oldMetaproteomicsExpectedAnalysis != metaproteomicsExpectedAnalysis) {
            this.expectedAnalyses.setMetaproteomics(metaproteomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS, oldMetaproteomicsExpectedAnalysis, metaproteomicsExpectedAnalysis);
        }
    }

    public void setMetametabolomicsExpectedAnalysis(boolean metametabolomicsExpectedAnalysis) {
        boolean oldMetametabolomicsExpectedAnalysis = this.expectedAnalyses.isMetametabolomics();
        if (oldMetametabolomicsExpectedAnalysis != metametabolomicsExpectedAnalysis) {
            this.expectedAnalyses.setMetametabolomics(metametabolomicsExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS, oldMetametabolomicsExpectedAnalysis, metametabolomicsExpectedAnalysis);
        }
    }

    public void setMicrobiomeExpectedAnalysis(boolean microbiomeExpectedAnalysis) {
        boolean oldMicrobiomeExpectedAnalysis = this.expectedAnalyses.isMicrobiome();
        if (oldMicrobiomeExpectedAnalysis != microbiomeExpectedAnalysis) {
            this.expectedAnalyses.setMicrobiome(microbiomeExpectedAnalysis);
            fireChangeEvent(Events.UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS, oldMicrobiomeExpectedAnalysis, microbiomeExpectedAnalysis);
        }
    }

    public String getAdditionalSamplingInformation() {
        return additionalSamplingInformation;
    }
    public void setAdditionalSamplingInformation(String additionalSamplingInformation) {
        additionalSamplingInformation = normalizeString(additionalSamplingInformation);
        setIfChanged(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, additionalSamplingInformation, () -> this.additionalSamplingInformation, (nv) -> this.additionalSamplingInformation = nv);
    }

    public String getAdditionalSamplingInformationError() {
        return additionalSamplingInformationError;
    }

    public void setAdditionalSamplingInformationError(String additionalSamplingInformationError) {
        setIfChanged(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR, additionalSamplingInformationError, () -> this.additionalSamplingInformationError, (e) -> this.additionalSamplingInformationError = e);
    }
    public String getContactError() {
        return contactError;
    }
    
    public void setContactError(String contactError) {
        setIfChanged(Events.UPDATE_OMICS_CONTACT_ERROR, contactError, () -> this.contactError, (e) -> this.contactError = contactError);
    }
    
    public String getSampleTrackingSheetError() {
        return sampleTrackingSheetError;
    }
    
    public void setSampleTrackingSheetError(String sampleTrackingSheetError) {
        setIfChanged(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET_ERROR, sampleTrackingSheetError, () -> this.sampleTrackingSheetError, (e) -> this.sampleTrackingSheetError = e);
    }
    
    public String getBioProjectAcessionError() {
        return bioProjectAcessionError;
    }
    
    public void setBioProjectAcessionError(String bioProjectAcessionError) {
        setIfChanged(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR, bioProjectAcessionError, () -> this.bioProjectAcessionError, (e) -> this.bioProjectAcessionError = e);
    }
    
    public String getSamplingTypesError() {
        return samplingTypesError;
    }
    
    public void setSamplingTypesError(String samplingTypesError) {
        setIfChanged(Events.UPDATE_OMICS_SAMPLING_TYPES_ERROR, samplingTypesError, () -> this.samplingTypesError, (e) -> this.samplingTypesError = e);
    }
    
    public String getExpectedAnalysesError() {
        return expectedAnalysesError;
    }
    
    public void setExpectedAnalysesError(String expectedAnalysesError) {
        setIfChanged(Events.UPDATE_OMICS_EXPECTED_ANALYSES_ERROR, expectedAnalysesError, () -> this.expectedAnalysesError, (e) -> this.expectedAnalysesError = e);
    }

    
}
