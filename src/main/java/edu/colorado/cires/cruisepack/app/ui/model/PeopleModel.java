package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidDropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PeopleModel extends PropertyChangeModel {
    private String scientistsError = null;
    private List<@ValidDropDownItemModel DropDownItemModel> scientists = new ArrayList<>(0);
    @NotEmpty
    private List<@ValidDropDownItemModel DropDownItemModel> sourceOrganizations = new ArrayList<>(0);
    private String sourceOrganizationsError = null;
    private List<@ValidDropDownItemModel DropDownItemModel> fundingOrganizations = new ArrayList<>(0);
    private String fundingOrganizationsError = null;
    @ValidPersonDropDownItem
    private DropDownItem metadataAuthor = PersonDatastore.UNSELECTED_PERSON;
    private String metadataAuthorError = null;

    public void restoreDefaults() {
        clearScientists();
        setScientistsError(null);
        
        clearSourceOrganizations();
        setSourceOrganizationsError(null);

        clearFundingOrganizations();
        setFundingOrganizationsError(null);

        setMetadataAuthor(PersonDatastore.UNSELECTED_PERSON);
        setMetadataAuthorError(null);
    }

    public void updateFormState(
        List<PeopleOrg> scientists,
        List<DropDownItem> peopleOptions,
        List<PeopleOrg> funders,
        List<PeopleOrg> sources,
        List<DropDownItem> organizationOptions,
        MetadataAuthor metadataAuthor
    ) {
        
        clearScientists();
        for (PeopleOrg peopleOrg : scientists) {
            DropDownItemPanel panel = new DropDownItemPanel(peopleOptions, PersonDatastore.UNSELECTED_PERSON);
            panel.getModel().setItem(new DropDownItem(peopleOrg.getUuid(), peopleOrg.getName()));
            addScientist(panel);
        }
        
        clearFundingOrganizations();
        for (PeopleOrg peopleOrg : funders) {
            DropDownItemPanel panel = new DropDownItemPanel(organizationOptions, OrganizationDatastore.UNSELECTED_ORGANIZATION);
            panel.getModel().setItem(new DropDownItem(peopleOrg.getUuid(), peopleOrg.getName()));
            addFundingOrganization(panel);
        }
        
        clearSourceOrganizations();
        for (PeopleOrg peopleOrg : sources) {
            DropDownItemPanel panel = new DropDownItemPanel(organizationOptions, OrganizationDatastore.UNSELECTED_ORGANIZATION);
            panel.getModel().setItem(new DropDownItem(peopleOrg.getUuid(), peopleOrg.getName()));
            addSourceOrganization(panel);
        }
        if (metadataAuthor != null) {
            setMetadataAuthor(new DropDownItem(metadataAuthor.getUuid(), metadataAuthor.getName()));
        }
    }
    
    public List<DropDownItemModel> getScientists() {
        return scientists;
    }

    public void setScientistsError(String scientistErrors) {
        setIfChanged(Events.UPDATE_SCIENTIST_ERROR, scientistErrors, () -> this.scientistsError, (e) -> this.scientistsError = e);
    }

    public void setSourceOrganizationsError(String sourceOrganizationErrors) {
        setIfChanged(Events.UPDATE_SOURCE_ORGANIZATION_ERROR, sourceOrganizationErrors, () -> this.sourceOrganizationsError, (e) -> this.sourceOrganizationsError = e);
    }

    public void setFundingOrganizationsError(String fundingOrganizationErrors) {
        setIfChanged(Events.UPDATE_FUNDING_ORGANIZATION_ERROR, fundingOrganizationErrors, () -> this.fundingOrganizationsError, (e) -> this.fundingOrganizationsError = e);
    }

    public List<DropDownItemModel> getSourceOrganizations() {
        return sourceOrganizations;
    }

    public List<DropDownItemModel> getFundingOrganizations() {
        return fundingOrganizations;
    }

    public DropDownItem getMetadataAuthor() {
        return metadataAuthor;
    }

    public void setMetadataAuthor(DropDownItem metadataAuthor) {
        setIfChanged(Events.UPDATE_METADATA_AUTHOR, metadataAuthor, () -> this.metadataAuthor, (a) -> this.metadataAuthor = a);
    }

    public void setMetadataAuthorError(String metadataAuthorError) {
        setIfChanged(Events.UPDATE_METADATA_AUTHOR_ERROR, metadataAuthorError, () -> this.metadataAuthorError, (e) -> this.metadataAuthorError = e);
    }

    public void setScientistErrors(List<String> errors) {
        for (int i = 0; i < errors.size(); i++) {
            scientists.get(i).setItemError(
                errors.get(i)
            );
        }
    }

    public void setSourceOrganizationErrors(List<String> errors) {
        for (int i = 0; i < errors.size(); i++) {
            sourceOrganizations.get(i).setItemError(
                errors.get(i)
            );
        }
    }

    public void setFundingOrganizationErrors(List<String> errors) {
        for (int i = 0; i < errors.size(); i++) {
            fundingOrganizations.get(i).setItemError(
                errors.get(i)
            );
        }
    }
    
    public void addScientist(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(scientists);
        DropDownItemModel model = panel.getModel();
        if (!oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(scientists);
            newModels.add(model);
            scientists = newModels;
            fireChangeEvent(Events.ADD_SCIENTIST, null, panel);
        }
    }
    
    public void removeScientist(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(scientists);
        DropDownItemModel model = panel.getModel();
        if (oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(scientists);
            newModels.remove(model);
            scientists = newModels;
            fireChangeEvent(Events.REMOVE_SCIENTIST, panel, null);
        }
    }
    
    public void clearScientists() {
        List<DropDownItemModel> oldScientists = new ArrayList<>(scientists);
        scientists = new ArrayList<>(0);
        fireChangeEvent(Events.CLEAR_SCIENTISTS, oldScientists, Collections.emptyList());
    }
    
    public void addSourceOrganization(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(sourceOrganizations);
        DropDownItemModel model = panel.getModel();
        if (!oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(sourceOrganizations);
            newModels.add(model);
            sourceOrganizations = newModels;
            fireChangeEvent(Events.ADD_SOURCE_ORGANIZATION, null, panel);
        }
    }
    
    public void removeSourceOrganization(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(sourceOrganizations);
        DropDownItemModel model = panel.getModel();
        if (oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(sourceOrganizations);
            newModels.remove(model);
            sourceOrganizations = newModels;
            fireChangeEvent(Events.REMOVE_SOURCE_ORGANIZATION, panel, null);
        }
    }
    
    public void clearSourceOrganizations() {
        List<DropDownItemModel> oldSourceOrganizations = new ArrayList<>(sourceOrganizations);
        sourceOrganizations = new ArrayList<>(0);
        fireChangeEvent(Events.CLEAR_SOURCE_ORGANIZATIONS, oldSourceOrganizations, Collections.emptyList());
    }
    
    public void addFundingOrganization(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(fundingOrganizations);
        DropDownItemModel model = panel.getModel();
        if (!oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(fundingOrganizations);
            newModels.add(model);
            fundingOrganizations = newModels;
            fireChangeEvent(Events.ADD_FUNDING_ORGANIZATION, null, panel);
        }
    }
    
    public void removeFundingOrganization(DropDownItemPanel panel) {
        List<DropDownItemModel> oldModels = new ArrayList<>(fundingOrganizations);
        DropDownItemModel model = panel.getModel();
        if (oldModels.contains(model)) {
            List<DropDownItemModel> newModels = new ArrayList<>(fundingOrganizations);
            newModels.remove(model);
            fundingOrganizations = newModels;
            fireChangeEvent(Events.REMOVE_FUNDING_ORGANIZATION, panel, null);
        }
    }
    
    public void clearFundingOrganizations() {
        List<DropDownItemModel> oldFundingOrganizations = new ArrayList<>(fundingOrganizations);
        fundingOrganizations = new ArrayList<>(0);
        fireChangeEvent(Events.CLEAR_FUNDING_ORGANIZATIONS, oldFundingOrganizations, Collections.emptyList());
    }

    public String getScientistsError() {
        return scientistsError;
    }

    public String getSourceOrganizationsError() {
        return sourceOrganizationsError;
    }

    public String getFundingOrganizationsError() {
        return fundingOrganizationsError;
    }

    public String getMetadataAuthorError() {
        return metadataAuthorError;
    }
}
