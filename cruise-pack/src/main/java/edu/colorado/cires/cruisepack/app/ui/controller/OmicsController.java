package edu.colorado.cires.cruisepack.app.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.List;

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

    public void setSamplingTypes(List<String> samplingTypes) {
        omicsModel.setSamplingTypes(samplingTypes);
    }

    public void setExpectedAnalyses(List<String> expectedAnalyses) {
        omicsModel.setExpectedAnalyses(expectedAnalyses);
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
