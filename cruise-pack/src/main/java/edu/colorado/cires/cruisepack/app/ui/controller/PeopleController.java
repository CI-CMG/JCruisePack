package edu.colorado.cires.cruisepack.app.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;

@Component
public class PeopleController implements PropertyChangeListener {

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final PeopleModel peopleModel;

    public PeopleController(ReactiveViewRegistry reactiveViewRegistry, PeopleModel peopleModel) {
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.peopleModel = peopleModel;
    }

    @PostConstruct
    public void init() {
        peopleModel.addChangeListener(this);
    }

    public void setScientists(List<DropDownItem> scientsts) {
        peopleModel.setScientists(scientsts);
    }

    public void setSourceOrganizations(List<DropDownItem> sourceOrganizations) {
        peopleModel.setSourceOrganizations(sourceOrganizations);
    }

    public void setFundingOrganizations(List<DropDownItem> fundingOrganizations) {
        peopleModel.setFundingOrganizations(fundingOrganizations);
    }

    public void setMetadataAuthor(DropDownItem metadataAuthor) {
        peopleModel.setMetadataAuthor(metadataAuthor);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }
    
}
