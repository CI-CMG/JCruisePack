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
        List<DropDownItem> oldScientists = List.copyOf(this.scientists);
        if (!(
            scientists.stream().map(DropDownItem::getId).collect(Collectors.toList()).equals(
                oldScientists.stream().map(DropDownItem::getId).collect(Collectors.toList())
            )
        )) {
            this.scientists = scientists;
            fireChangeEvent(Events.UPDATE_SCIENTISTS, oldScientists, scientists);
        }
    }
    public List<DropDownItem> getSourceOrganizations() {
        return sourceOrganizations;
    }
    public void setSourceOrganizations(List<DropDownItem> sourceOrganizations) {
        List<DropDownItem> oldSourceOrganizations = List.copyOf(this.sourceOrganizations);
        if (!(
            sourceOrganizations.stream().map(DropDownItem::getId).collect(Collectors.toList()).equals(
                oldSourceOrganizations.stream().map(DropDownItem::getId).collect(Collectors.toList())
            )
        )) {
            this.sourceOrganizations = sourceOrganizations;
            fireChangeEvent(Events.UPDATE_SOURCE_ORGANIZATIONS, oldSourceOrganizations, sourceOrganizations);
        }
        this.sourceOrganizations = sourceOrganizations;
    }
    public List<DropDownItem> getFundingOrganizations() {
        return fundingOrganizations;
    }
    public void setFundingOrganizations(List<DropDownItem> fundingOrganizations) {
        List<DropDownItem> oldFundingOrganizatons = List.copyOf(this.fundingOrganizations);
        if (!(
            fundingOrganizations.stream().map(DropDownItem::getId).collect(Collectors.toList()).equals(
                oldFundingOrganizatons.stream().map(DropDownItem::getId).collect(Collectors.toList())
            )
        )) {
            this.fundingOrganizations = fundingOrganizations;
            fireChangeEvent(Events.UPDATE_FUNDING_ORGANIZATIONS, oldFundingOrganizatons, fundingOrganizations);
        }
    }
    public DropDownItem getMetadataAuthor() {
        return metadataAuthor;
    }
    public void setMetadataAuthor(DropDownItem metadataAuthor) {
        DropDownItem oldMetadataAuthor = this.metadataAuthor;
        if (!Objects.equals(metadataAuthor, oldMetadataAuthor)) {
            this.metadataAuthor = metadataAuthor;
            fireChangeEvent(Events.UPDATE_METADATA_AUTHOR, oldMetadataAuthor, metadataAuthor);
        }
    }

    
}
