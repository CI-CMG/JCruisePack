package edu.colorado.cires.cruisepack.app.ui.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidOrganizationDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotEmpty;

@Component
public class PeopleModel extends PropertyChangeModel {

    @NotEmpty
    private List<@ValidPersonDropDownItem DropDownItem> scientists = new ArrayList<>(0);
    private String scientistsError = "";
    @NotEmpty
    private List<@ValidOrganizationDropDownItem DropDownItem> sourceOrganizations = new ArrayList<>(0);
    private String sourceOrganizationsError = "";
    @NotEmpty
    private List<@ValidOrganizationDropDownItem DropDownItem> fundingOrganizations = new ArrayList<>(0);
    private String fundingOrganizationsError = "";
    @ValidPersonDropDownItem
    private DropDownItem metadataAuthor = PersonDatastore.UNSELECTED_PERSON;
    private String metadataAuthorError;
    
    public List<DropDownItem> getScientists() {
        return scientists;
    }

    public void setScientists(List<DropDownItem> scientists) {
        setIfChanged(Events.UPDATE_SCIENTISTS, scientists, () -> this.scientists, (s) -> this.scientists = s);
    }

    public String getScientistsError() {
        return scientistsError;
    }

    public void setScientistError(String scientistErrors) {
        setIfChanged(Events.UPDATE_SCIENTIST_ERROR, scientistErrors, () -> this.scientistsError, (e) -> this.scientistsError = e);
    }
    
    public String getSourceOrganizationsError() {
        return sourceOrganizationsError;
    }

    public void setSourceOrganizationError(String sourceOrganizationErrors) {
        setIfChanged(Events.UPDATE_SOURCE_ORGANIZATION_ERROR, sourceOrganizationErrors, () -> this.sourceOrganizationsError, (e) -> this.sourceOrganizationsError = e);
    }

    public String getFundingOrganizationsError() {
        return fundingOrganizationsError;
    }

    public void setFundingOrganizationError(String fundingOrganizationErrors) {
        setIfChanged(Events.UPDATE_FUNDING_ORGANIZATION_ERROR, fundingOrganizationErrors, () -> this.fundingOrganizationsError, (e) -> this.fundingOrganizationsError = e);
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

    public String getMetadatAuthorError() {
        return metadataAuthorError;
    }

    public void setMetadataAuthorError(String metadataAuthorError) {
        setIfChanged(Events.UPDATE_METADATA_AUTHOR_ERROR, metadataAuthorError, () -> this.metadataAuthorError, (e) -> this.metadataAuthorError = e);
    }

    
}
