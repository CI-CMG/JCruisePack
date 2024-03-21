package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class PeopleModelTest extends PropertyChangeModelTest<PeopleModel> {

  @Override
  protected PeopleModel createModel() {
    return new PeopleModel();
  }
  
  private DropDownItemPanel createDropDownItemPanel(DropDownItem dropDownItem) {
    DropDownItemPanel panel = new DropDownItemPanel(
        List.of(
            new DropDownItem("1", "value1"),
            new DropDownItem("2", "value2"),
            dropDownItem
        ),
        new DropDownItem("", "defaultValue")
    );
    
    panel.getModel().setItem(dropDownItem);
    
    return panel;
  }

  @Test
  void restoreDefaults() {
    model.addScientist(createDropDownItemPanel(
        new DropDownItem("id1", "scientist-1")
    ));
    model.addScientist(createDropDownItemPanel(
        new DropDownItem("id2", "scientist-2")
    ));
    model.setScientistsError("scientists-error");
    model.addSourceOrganization(createDropDownItemPanel(
        new DropDownItem("id1", "organization-1")
    ));
    model.addSourceOrganization(createDropDownItemPanel(
        new DropDownItem("id2", "organization-2")
    ));
    model.setSourceOrganizationsError("source-organizations-error");
    model.addFundingOrganization(createDropDownItemPanel(
        new DropDownItem("id1", "organization-1")
    ));
    model.addFundingOrganization(createDropDownItemPanel(
        new DropDownItem("id2", "organization-2")
    ));
    model.setFundingOrganizationsError("funding-organization-error");
    model.setMetadataAuthor(
        new DropDownItem("1", "person-1")
    );
    model.setMetadataAuthorError("metadata-author-error");
    
    clearMap();
    
    List<DropDownItemModel> scientists = model.getScientists();
    String scientistsError = model.getScientistsError();
    
    List<DropDownItemModel> sourceOrganizations = model.getSourceOrganizations();
    String sourceOrganizationsError = model.getSourceOrganizationsError();
    
    List<DropDownItemModel> fundingOrganizations = model.getFundingOrganizations();
    String fundingOrganizationsError = model.getFundingOrganizationsError();
    
    DropDownItem metadataAuthor = model.getMetadataAuthor();
    String metadataAuthorError = model.getMetadataAuthorError();
    
    model.restoreDefaults();
    
    assertChangeEvent(Events.CLEAR_SCIENTISTS, scientists, Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getScientists());
    assertChangeEvent(Events.UPDATE_SCIENTIST_ERROR, scientistsError, null);
    assertNull(model.getScientistsError());
    assertChangeEvent(Events.CLEAR_SOURCE_ORGANIZATIONS, sourceOrganizations, Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getSourceOrganizations());
    assertChangeEvent(Events.UPDATE_SOURCE_ORGANIZATION_ERROR, sourceOrganizationsError, null);
    assertNull(model.getSourceOrganizationsError());
    assertChangeEvent(Events.CLEAR_FUNDING_ORGANIZATIONS, fundingOrganizations, Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getFundingOrganizations());
    assertChangeEvent(Events.UPDATE_FUNDING_ORGANIZATION_ERROR, fundingOrganizationsError, null);
    assertNull(model.getFundingOrganizationsError());
    assertChangeEvent(Events.UPDATE_METADATA_AUTHOR, metadataAuthor, PersonDatastore.UNSELECTED_PERSON);
    assertEquals(PersonDatastore.UNSELECTED_PERSON, model.getMetadataAuthor());
    assertChangeEvent(Events.UPDATE_METADATA_AUTHOR_ERROR, metadataAuthorError, null);
    assertNull(model.getMetadataAuthorError());
  }

  @Test
  void updateFormState() {
    List<PeopleOrg> scientists = List.of(
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("scientist-1")
            .build(),
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("scientist-2")
            .build()
    );
    List<PeopleOrg> funders = List.of(
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("organization-1")
            .build(),
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("organization-2")
            .build()
    );
    List<PeopleOrg> sponsors = List.of(
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("organization-3")
            .build(),
        PeopleOrg.builder()
            .withUuid(UUID.randomUUID().toString())
            .withName("organization-4")
            .build()
    );
    MetadataAuthor metadataAuthor = MetadataAuthor.builder()
        .withUuid(UUID.randomUUID().toString())
        .withName("metadata-author")
        .build();
    
    model.updateFormState(
        scientists,
        scientists.stream()
            .map(p -> new DropDownItem(p.getUuid(), p.getName()))
            .toList(),
        funders,
        sponsors,
        Stream.concat(funders.stream(), sponsors.stream())
            .map(p -> new DropDownItem(p.getUuid(), p.getName()))
            .toList(),
        metadataAuthor
    );
    
    assertChangeEvents(
        Events.ADD_SCIENTIST,
        scientists.stream().map(p -> (DropDownItemPanel) null).toList(),
        scientists.stream()
            .map(p -> createDropDownItemPanel(
                new DropDownItem(p.getUuid(), p.getName())
            ))
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(
        scientists.stream()
            .map(p -> new DropDownItem(p.getUuid(), p.getName()))
            .toList(),
        model.getScientists().stream()
            .map(DropDownItemModel::getItem)
            .toList()
    );

    assertChangeEvents(
        Events.ADD_SOURCE_ORGANIZATION,
        sponsors.stream().map(p -> (DropDownItemPanel) null).toList(),
        sponsors.stream()
            .map(p -> createDropDownItemPanel(
                new DropDownItem(p.getUuid(), p.getName())
            ))
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(
        sponsors.stream()
            .map(p -> new DropDownItem(p.getUuid(), p.getName()))
            .toList(),
        model.getSourceOrganizations().stream()
            .map(DropDownItemModel::getItem)
            .toList()
    );

    assertChangeEvents(
        Events.ADD_FUNDING_ORGANIZATION,
        funders.stream().map(p -> (DropDownItemPanel) null).toList(),
        funders.stream()
            .map(p -> createDropDownItemPanel(
                new DropDownItem(p.getUuid(), p.getName())
            ))
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(
        funders.stream()
            .map(p -> new DropDownItem(p.getUuid(), p.getName()))
            .toList(),
        model.getFundingOrganizations().stream()
            .map(DropDownItemModel::getItem)
            .toList()
    );
    
    assertChangeEvent(Events.UPDATE_METADATA_AUTHOR, PersonDatastore.UNSELECTED_PERSON, new DropDownItem(metadataAuthor.getUuid(), metadataAuthor.getName()));
  }

  @Test
  void setScientistsError() {
    assertPropertyChange(Events.UPDATE_SCIENTIST_ERROR, model::getScientistsError, model::setScientistsError, "value1", "value2", null);
  }

  @Test
  void setSourceOrganizationsError() {
    assertPropertyChange(Events.UPDATE_SOURCE_ORGANIZATION_ERROR, model::getSourceOrganizationsError, model::setSourceOrganizationsError, "value1", "value2", null);
  }

  @Test
  void setFundingOrganizationsError() {
    assertPropertyChange(Events.UPDATE_FUNDING_ORGANIZATION_ERROR, model::getFundingOrganizationsError, model::setFundingOrganizationsError, "value1", "value2", null);
  }

  @Test
  void setMetadataAuthor() {
    assertPropertyChange(
        Events.UPDATE_METADATA_AUTHOR, 
        model::getMetadataAuthor,
        model::setMetadataAuthor,
        new DropDownItem("1", "value1"),
        new DropDownItem("1", "value2"),
        PersonDatastore.UNSELECTED_PERSON 
    );
  }

  @Test
  void setMetadataAuthorError() {
    assertPropertyChange(
        Events.UPDATE_METADATA_AUTHOR_ERROR,
        model::getMetadataAuthorError,
        model::setMetadataAuthorError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setScientistErrors() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "person-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "person-2")
        )
    );
    
    panels.forEach(model::addScientist);
    
    model.getScientists().forEach(m -> m.addChangeListener((evt) -> {
      String propertyName = evt.getPropertyName();
      Map<String, List<PropertyChangeEvent>> eventMap = getEventMap();
      List<PropertyChangeEvent> value = eventMap.get(propertyName);
      if (value == null) {
        List<PropertyChangeEvent> events = new ArrayList<>(0);
        events.add(evt);
        eventMap.put(
            propertyName, events
        );
      } else {
        value.add(evt);
        eventMap.put(
            propertyName, value
        );
      }
    }));
    
    List<String> errors = List.of(
        "error-1", "error-2"
    );
    model.setScientistErrors(errors);
    
    assertChangeEvents(
        DropDownItemModel.UPDATE_ITEM_ERROR,
        errors.stream()
            .map(v -> (String) null)
            .toList(),
        errors,
        (v) -> v
    );
    assertEquals("error-1", model.getScientists().get(0).getItemError());
    assertEquals("error-2", model.getScientists().get(1).getItemError());
  }

  @Test
  void setSourceOrganizationErrors() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addSourceOrganization);

    model.getSourceOrganizations().forEach(m -> m.addChangeListener((evt) -> {
      String propertyName = evt.getPropertyName();
      Map<String, List<PropertyChangeEvent>> eventMap = getEventMap();
      List<PropertyChangeEvent> value = eventMap.get(propertyName);
      if (value == null) {
        List<PropertyChangeEvent> events = new ArrayList<>(0);
        events.add(evt);
        eventMap.put(
            propertyName, events
        );
      } else {
        value.add(evt);
        eventMap.put(
            propertyName, value
        );
      }
    }));

    List<String> errors = List.of(
        "error-1", "error-2"
    );
    model.setSourceOrganizationErrors(errors);

    assertChangeEvents(
        DropDownItemModel.UPDATE_ITEM_ERROR,
        errors.stream()
            .map(v -> (String) null)
            .toList(),
        errors,
        (v) -> v
    );
    assertEquals("error-1", model.getSourceOrganizations().get(0).getItemError());
    assertEquals("error-2", model.getSourceOrganizations().get(1).getItemError());
  }

  @Test
  void setFundingOrganizationErrors() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addFundingOrganization);

    model.getFundingOrganizations().forEach(m -> m.addChangeListener((evt) -> {
      String propertyName = evt.getPropertyName();
      Map<String, List<PropertyChangeEvent>> eventMap = getEventMap();
      List<PropertyChangeEvent> value = eventMap.get(propertyName);
      if (value == null) {
        List<PropertyChangeEvent> events = new ArrayList<>(0);
        events.add(evt);
        eventMap.put(
            propertyName, events
        );
      } else {
        value.add(evt);
        eventMap.put(
            propertyName, value
        );
      }
    }));

    List<String> errors = List.of(
        "error-1", "error-2"
    );
    model.setFundingOrganizationErrors(errors);

    assertChangeEvents(
        DropDownItemModel.UPDATE_ITEM_ERROR,
        errors.stream()
            .map(v -> (String) null)
            .toList(),
        errors,
        (v) -> v
    );
    assertEquals("error-1", model.getFundingOrganizations().get(0).getItemError());
    assertEquals("error-2", model.getFundingOrganizations().get(1).getItemError());
  }

  @Test
  void addScientist() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "person-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "person-2")
        )
    );
    
    panels.forEach(model::addScientist);
    
    assertChangeEvents(
        Events.ADD_SCIENTIST,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        panels,
        (p) -> p == null ? null : p.getModel().getItem()
    );
    
    assertEquals(
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        model.getScientists()
    );
  }

  @Test
  void removeScientist() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "person-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "person-2")
        )
    );

    panels.forEach(model::addScientist);
    clearMap();
    panels.forEach(model::removeScientist);

    assertChangeEvents(
        Events.REMOVE_SCIENTIST,
        panels,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(Collections.emptyList(), model.getScientists());
  }

  @Test
  void clearScientists() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "person-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "person-2")
        )
    );

    panels.forEach(model::addScientist);
    clearMap();
    
    model.clearScientists();
    
    assertChangeEvent(
        Events.CLEAR_SCIENTISTS,
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        Collections.emptyList()
    );
    assertEquals(Collections.emptyList(), model.getScientists());
  }

  @Test
  void addSourceOrganization() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addSourceOrganization);

    assertChangeEvents(
        Events.ADD_SOURCE_ORGANIZATION,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        panels,
        (p) -> p == null ? null : p.getModel().getItem()
    );

    assertEquals(
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        model.getSourceOrganizations()
    );
  }

  @Test
  void removeSourceOrganization() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addSourceOrganization);
    clearMap();
    panels.forEach(model::removeSourceOrganization);

    assertChangeEvents(
        Events.REMOVE_SOURCE_ORGANIZATION,
        panels,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(Collections.emptyList(), model.getSourceOrganizations());
  }

  @Test
  void clearSourceOrganizations() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addSourceOrganization);
    clearMap();

    model.clearSourceOrganizations();

    assertChangeEvent(
        Events.CLEAR_SOURCE_ORGANIZATIONS,
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        Collections.emptyList()
    );
    assertEquals(Collections.emptyList(), model.getSourceOrganizations());
  }

  @Test
  void addFundingOrganization() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addFundingOrganization);

    assertChangeEvents(
        Events.ADD_FUNDING_ORGANIZATION,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        panels,
        (p) -> p == null ? null : p.getModel().getItem()
    );

    assertEquals(
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        model.getFundingOrganizations()
    );
  }

  @Test
  void removeFundingOrganization() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addFundingOrganization);
    clearMap();
    panels.forEach(model::removeFundingOrganization);

    assertChangeEvents(
        Events.REMOVE_FUNDING_ORGANIZATION,
        panels,
        panels.stream()
            .map(p -> (DropDownItemPanel) null)
            .toList(),
        (p) -> p == null ? null : p.getModel().getItem()
    );
    assertEquals(Collections.emptyList(), model.getFundingOrganizations());
  }

  @Test
  void clearFundingOrganizations() {
    List<DropDownItemPanel> panels = List.of(
        createDropDownItemPanel(
            new DropDownItem("id1", "organization-1")
        ),
        createDropDownItemPanel(
            new DropDownItem("id2", "organization-2")
        )
    );

    panels.forEach(model::addFundingOrganization);
    clearMap();

    model.clearFundingOrganizations();

    assertChangeEvent(
        Events.CLEAR_FUNDING_ORGANIZATIONS,
        panels.stream()
            .map(DropDownItemPanel::getModel)
            .toList(),
        Collections.emptyList()
    );
    assertEquals(Collections.emptyList(), model.getFundingOrganizations());
  }
}