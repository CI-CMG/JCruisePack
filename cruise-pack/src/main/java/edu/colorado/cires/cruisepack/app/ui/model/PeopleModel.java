package edu.colorado.cires.cruisepack.app.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.privatejgoodies.common.base.Objects;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

@Component
public class PeopleModel extends PropertyChangeModel {

    private List<DropDownItem> scientists = new ArrayList<>(0);
    private List<DropDownItem> sourceOrganizations = new ArrayList<>(0);
    private List<DropDownItem> fundingOrganizations = new ArrayList<>(0);
    private DropDownItem metadataAuthor = PersonDatastore.UNSELECTED_PERSON;
    
    public List<DropDownItem> getScientists() {
        return scientists;
    }
    public void setScientists(List<DropDownItem> scientists) {
        setIfChanged(Events.UPDATE_SCIENTISTS, scientists, () -> this.scientists, (s) -> this.scientists = s);
    }
    public List<DropDownItem> getSourceOrganizations() {
        return sourceOrganizations;
    }
    public void setSourceOrganizations(List<DropDownItem> sourceOrganizations) {
        setIfChanged(Events.UPDATE_SOURCE_ORGANIZATIONS, sourceOrganizations, () -> this.sourceOrganizations, (o) -> this.sourceOrganizations = o);
    }
    public List<DropDownItem> getFundingOrganizations() {
        return fundingOrganizations;
    }
    public void setFundingOrganizations(List<DropDownItem> fundingOrganizations) {
        setIfChanged(Events.UPDATE_FUNDING_ORGANIZATIONS, fundingOrganizations, () -> this.fundingOrganizations, (o) -> this.fundingOrganizations = o);
    }
    public DropDownItem getMetadataAuthor() {
        return metadataAuthor;
    }
    public void setMetadataAuthor(DropDownItem metadataAuthor) {
        setIfChanged(Events.UPDATE_METADATA_AUTHOR, metadataAuthor, () -> this.metadataAuthor, (a) -> this.metadataAuthor = a);
    }

    
}
