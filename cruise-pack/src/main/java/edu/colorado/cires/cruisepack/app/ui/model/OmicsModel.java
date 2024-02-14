package edu.colorado.cires.cruisepack.app.ui.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.privatejgoodies.common.base.Objects;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

@Component
public class OmicsModel extends PropertyChangeModel {

    private boolean samplingConducted = false;
    private DropDownItem contact = PersonDatastore.UNSELECTED_PERSON;
    private String sampleTrackingSheet = "";
    private String bioProjectAccession = "";
    private List<String> samplingTypes = new ArrayList<>(0);
    private List<String> expectedAnalyses = new ArrayList<>(0);
    private String additionalSamplingInformation = "";
    
    public boolean isSamplingConducted() {
        return samplingConducted;
    }
    public void setSamplingConducted(boolean samplingConducted) {
        boolean oldSamplingConducted = this.samplingConducted;
        if (oldSamplingConducted != samplingConducted) {
            this.samplingConducted = samplingConducted;
            fireChangeEvent(Events.UPDATE_OMICS_SAMPLING_CONDUCTED, oldSamplingConducted, samplingConducted);
        }
        this.samplingConducted = samplingConducted;
    }
    public DropDownItem getContact() {
        return contact;
    }
    public void setContact(DropDownItem contact) {
        DropDownItem oldContact = this.contact;
        if (!Objects.equals(contact, oldContact)) {
            this.contact = contact;
            fireChangeEvent(Events.UPDATE_OMICS_CONTACT, oldContact, contact);
        }
    }
    public String getSampleTrackingSheet() {
        return sampleTrackingSheet;
    }
    public void setSampleTrackingSheet(String sampleTrackingSheet) {
        String oldSampleTrackingSheet = this.sampleTrackingSheet;
        if (!oldSampleTrackingSheet.equals(sampleTrackingSheet)) {
            this.sampleTrackingSheet = sampleTrackingSheet;
            fireChangeEvent(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, oldSampleTrackingSheet, sampleTrackingSheet);
        }
    }
    public String getBioProjectAccession() {
        return bioProjectAccession;
    }
    public void setBioProjectAccession(String bioProjectAccession) {
        String oldBioProjectAccession = this.bioProjectAccession;
        if (!oldBioProjectAccession.equals(bioProjectAccession)) {
            this.bioProjectAccession = bioProjectAccession;
            fireChangeEvent(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, oldBioProjectAccession, bioProjectAccession);
        }
    }
    public List<String> getSamplingTypes() {
        return samplingTypes;
    }
    public void setSamplingTypes(List<String> samplingTypes) {
        List<String> oldSamplingTypes = List.copyOf(this.samplingTypes);
        if (!(new HashSet<>(oldSamplingTypes).equals(new HashSet<>(samplingTypes)))) {
            this.samplingTypes = samplingTypes;
            fireChangeEvent(Events.UPDATE_OMICS_SAMPLING_TYPES, oldSamplingTypes, samplingTypes);
        }
    }
    public List<String> getExpectedAnalyses() {
        return expectedAnalyses;
    }
    public void setExpectedAnalyses(List<String> expectedAnalyses) {
        List<String> oldExpectedAnalyses = List.copyOf(this.expectedAnalyses);
        if (!(new HashSet<>(oldExpectedAnalyses).equals(new HashSet<>(expectedAnalyses)))) {
            this.expectedAnalyses = expectedAnalyses;
            fireChangeEvent(Events.UPDATE_OMICS_EXPECTED_ANALYSES, oldExpectedAnalyses, expectedAnalyses);
        }
    }
    public String getAdditionalSamplingInformation() {
        return additionalSamplingInformation;
    }
    public void setAdditionalSamplingInformation(String additionalSamplingInformation) {
        String oldAdditionalSamplingInformation = this.additionalSamplingInformation;
        if (!oldAdditionalSamplingInformation.equals(additionalSamplingInformation)) {
            this.additionalSamplingInformation = additionalSamplingInformation;
            fireChangeEvent(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, oldAdditionalSamplingInformation, additionalSamplingInformation);
        }
    }

    
    
}
