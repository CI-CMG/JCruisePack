package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class OmicsModel extends PropertyChangeModel {

    private boolean samplingConducted = false;
    private DropDownItem contact = PersonDatastore.UNSELECTED_PERSON;
    private Path sampleTrackingSheet = null;
    private String bioProjectAccession = null;
    private SamplingTypesModel samplingTypes = new SamplingTypesModel();
    private ExpectedAnalysesModel expectedAnalyses = new ExpectedAnalysesModel();
    private String additionalSamplingInformation = null;
    
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

}
