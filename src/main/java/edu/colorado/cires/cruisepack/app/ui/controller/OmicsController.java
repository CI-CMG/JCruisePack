package edu.colorado.cires.cruisepack.app.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;

@Component
public class OmicsController implements PropertyChangeListener {

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final OmicsModel omicsModel;

    @Autowired
    public OmicsController(ReactiveViewRegistry reactiveViewRegistry, OmicsModel omicsModel) {
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.omicsModel = omicsModel;
    }

    @PostConstruct
    public void init() {
        omicsModel.addChangeListener(this);
    }

    public void setSamplingConducted(boolean samplingConducted) {
        omicsModel.setSamplingConducted(samplingConducted);
    }

    public void setContact(DropDownItem contact) {
        omicsModel.setContact(contact);
    }

    public void setSampleTrackingSheet(Path sampleTrackingSheet) {
        omicsModel.setSampleTrackingSheet(sampleTrackingSheet);
    }

    public void setBioProjectAccession(String bioProjectAccession) {
        omicsModel.setBioProjectAccession(bioProjectAccession);
    }

    public void setWaterSamplingType(Boolean waterSamplingType) {
        omicsModel.setWaterSamplingType(waterSamplingType);
    }

    public void setSoilSedimentSamplingType(Boolean soilSedimentSamplingType) {
        omicsModel.setSoilSedimentSamplingType(soilSedimentSamplingType);
    }

    public void setOrganicTissueSamplingType(Boolean organicTissueSamplingType) {
        omicsModel.setOrganicTissueSamplingType(organicTissueSamplingType);
    }

    public void setBarcodingExpectedAnalysis(Boolean barcodingExpectedAnalysis) {
        omicsModel.setBarcodingExpectedAnalysis(barcodingExpectedAnalysis);
    }

    public void setGenomicsExpectedAnalysis(Boolean genomicsExpectedAnalysis) {
        omicsModel.setGenomicsExpectedAnalysis(genomicsExpectedAnalysis);
    }

    public void setTranscriptomicsExpectedAnalysis(Boolean transcriptomicsExpectedAnalysis) {
        omicsModel.setTranscriptomicsExpectedAnalysis(transcriptomicsExpectedAnalysis);
    }

    public void setProteomicsExpectedAnalysis(Boolean proteomicsExpectedAnalysis) {
        omicsModel.setProteomicsExpectedAnalysis(proteomicsExpectedAnalysis);
    }

    public void setMetabolomicsExpectedAnalysis(Boolean metabolomicsExpectedAnalysis) {
        omicsModel.setMetabolomicsExpectedAnalysis(metabolomicsExpectedAnalysis);
    }

    public void setEpigeneticsExpectedAnalysis(Boolean epigeneticsExpectedAnalysis) {
        omicsModel.setEpigeneticsExpectedAnalysis(epigeneticsExpectedAnalysis);
    }

    public void setOtherExpectedAnalysis(Boolean otherExpectedAnalysis) {
        omicsModel.setOtherExpectedAnalysis(otherExpectedAnalysis);
    }

    public void setMetaBarcodingExpectedAnalysis(Boolean metabarcodingExpectedAnalysis) {
        omicsModel.setMetaBarcodingExpectedAnalysis(metabarcodingExpectedAnalysis);
    }

    public void setMetaGenomicsExpectedAnalysis(Boolean metaGenomicsExpectedAnalysis) {
        omicsModel.setMetaGenomicsExpectedAnalysis(metaGenomicsExpectedAnalysis);
    }

    public void setMetatranscriptomicsExpectedAnalysis(Boolean metatranscriptomicsExpectedAnalysis) {
        omicsModel.setMetaTranscriptomicsExpectedAnalysis(metatranscriptomicsExpectedAnalysis);
    }

    public void setMetaproteomicsExpectedAnalysis(Boolean metaproteomicsExpectedAnalysis) {
        omicsModel.setMetaProteomicsExpectedAnalysis(metaproteomicsExpectedAnalysis);
    }

    public void setMetametabolomicsExpectedAnalysis(Boolean metametabolomicsExpectedAnalysis) {
        omicsModel.setMetaMetabolomicsExpectedAnalysis(metametabolomicsExpectedAnalysis);
    }

    public void setMicrobiomeExpectedAnalysis(Boolean microbiomeExpectedAnalysis) {
        omicsModel.setMicrobiomeExpectedAnalysis(microbiomeExpectedAnalysis);
    }

    public void setAdditionalSamplingInformation(String additionalSamplingInformation) {
        omicsModel.setAdditionalSamplingInformation(additionalSamplingInformation);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }
    
}
