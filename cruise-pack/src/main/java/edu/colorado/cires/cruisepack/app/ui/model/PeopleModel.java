package edu.colorado.cires.cruisepack.app.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidOrganizationDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotEmpty;

@Component
public class PeopleModel extends PropertyChangeModel {

    @NotEmpty
    private List<@ValidPersonDropDownItem DropDownItem> scientists = new ArrayList<>(0);
    private String scientistsError = null;
    @NotEmpty
    private List<@ValidOrganizationDropDownItem DropDownItem> sourceOrganizations = new ArrayList<>(0);
    private String sourceOrganizationsError = null;
    @NotEmpty
    private List<@ValidOrganizationDropDownItem DropDownItem> fundingOrganizations = new ArrayList<>(0);
    private String fundingOrganizationsError = null;
    @ValidPersonDropDownItem
    private DropDownItem metadataAuthor = PersonDatastore.UNSELECTED_PERSON;
    private String metadataAuthorError = null;

    public void restoreDefaults() {
        setScientists(Collections.emptyList());
        setScientistError(null);
        
        setSourceOrganizations(Collections.emptyList());
        setSourceOrganizationError(null);

        setFundingOrganizations(Collections.emptyList());
        setFundingOrganizationError(null);

        setMetadataAuthor(PersonDatastore.UNSELECTED_PERSON);
        setMetadataAuthorError(null);
    }

    public void updateFormState(CruiseMetadata cruiseMetadata) {
        setScientists(cruiseMetadata.getScientists().stream().map((po) -> new DropDownItem(po.getUuid(), po.getName())).toList());
        setFundingOrganizations(cruiseMetadata.getFunders().stream().map((po) -> new DropDownItem(po.getUuid(), po.getName())).toList());
        setSourceOrganizations(cruiseMetadata.getSponsors().stream().map((po) -> new DropDownItem(po.getUuid(), po.getName())).toList());
        if (cruiseMetadata.getMetadataAuthor() != null) {
            setMetadataAuthor(new DropDownItem(cruiseMetadata.getMetadataAuthor().getUuid(), cruiseMetadata.getMetadataAuthor().getName()));
        }
    }
    
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

    public void setScientistErrors(List<String> errors) {
        List<DropDownItem> newItems = new ArrayList<>(0);
        for (int i = 0; i < errors.size(); i++) {
            String errorMessage = errors.get(i);
            DropDownItem dropDownItem = scientists.get(i);
            if (errorMessage != null) {
                dropDownItem = new DropDownItem(dropDownItem.getId(), dropDownItem.getValue(), errorMessage);
            }
            newItems.add(dropDownItem);
        }
        setScientists(newItems);
    }

    public void setSourceOrganizationErrors(List<String> errors) {
        List<DropDownItem> newItems = new ArrayList<>(0);
        for (int i = 0; i < errors.size(); i++) {
            String errorMessage = errors.get(i);
            DropDownItem dropDownItem = sourceOrganizations.get(i);
            if (errorMessage != null) {
                dropDownItem = new DropDownItem(dropDownItem.getId(), dropDownItem.getValue(), errorMessage);
            }
            newItems.add(dropDownItem);
        }
        setSourceOrganizations(newItems);
    }

    public void setFundingOrganizationErrors(List<String> errors) {
        List<DropDownItem> newItems = new ArrayList<>(0);
        for (int i = 0; i < errors.size(); i++) {
            String errorMessage = errors.get(i);
            DropDownItem dropDownItem = fundingOrganizations.get(i);
            if (errorMessage != null) {
                dropDownItem = new DropDownItem(dropDownItem.getId(), dropDownItem.getValue(), errorMessage);
            }
            newItems.add(dropDownItem);
        }
        setFundingOrganizations(newItems);
    }
    
}
