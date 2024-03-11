package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.stereotype.Component;

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
    
    public void addScientist(DropDownItemPanel panel) {
        peopleModel.addScientist(panel);
    }
    
    public void removeScientist(DropDownItemPanel panel) {
        peopleModel.removeScientist(panel);
    }
    
    public void addSourceOrganization(DropDownItemPanel panel) {
        peopleModel.addSourceOrganization(panel);
    }
    
    public void removeSourceOrganization(DropDownItemPanel panel) {
        peopleModel.removeSourceOrganization(panel);
    }
    
    public void addFundingOrganization(DropDownItemPanel panel) {
        peopleModel.addFundingOrganization(panel);
    }
    
    public void removeFundingOrganization(DropDownItemPanel panel) {
        peopleModel.removeFundingOrganization(panel);
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
