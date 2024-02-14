package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OmicsModel extends PropertyChangeModel {

  private boolean samplingConducted = false;
  private DropDownItem contact = PersonDatastore.UNSELECTED_PERSON;
  private Path sampleTrackingSheet = null;
  private String bioProjectAccession = null;
  private List<String> samplingTypes = new ArrayList<>(0);
  private List<String> expectedAnalyses = new ArrayList<>(0);
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
    setIfChanged(Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET, sampleTrackingSheet, () -> this.sampleTrackingSheet,
        (nv) -> this.sampleTrackingSheet = nv);
  }

  public String getBioProjectAccession() {
    return bioProjectAccession;
  }

  public void setBioProjectAccession(String bioProjectAccession) {
    bioProjectAccession = normalizeString(bioProjectAccession);
    setIfChanged(Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION, bioProjectAccession, () -> this.bioProjectAccession,
        (nv) -> this.bioProjectAccession = nv);
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
    additionalSamplingInformation = normalizeString(additionalSamplingInformation);
    setIfChanged(Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION, additionalSamplingInformation, () -> this.additionalSamplingInformation,
        (nv) -> this.additionalSamplingInformation = nv);
  }


}
