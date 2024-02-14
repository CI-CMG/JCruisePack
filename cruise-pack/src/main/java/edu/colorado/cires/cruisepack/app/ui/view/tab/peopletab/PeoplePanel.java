package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateAppendableTable;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatasore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PeopleController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditOrgDialog;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditPersonDialog;

@Component
public class PeoplePanel extends JPanel implements ReactiveView {

  private static final String SCIENTISTS_LABEL = "Scientists";
  private static final String SOURCE_ORG_LABEL = "Source Organizations";
  private static final String FUNDING_ORG_LABEL = "Funding Organizations";
  private static final String ADD_SCIENTIST_LABEL = "Add Scientist";
  private static final String ADD_SOURCE_ORG_LABEL = "Add Source Organization";
  private static final String ADD_FUNDING_ORG_LABEL = "Add Funding Organization";
  private static final String METADATA_AUTHOR_LABEL = "Metadata Author";
  private static final String CREATE_PEOPLE_LABEL = "Create / Edit People";
  private static final String CREATE_ORG_LABEL = "Create / Edit Organizations";
  
  private final PeopleList peopleList;
  private final OrganizationList organizationList;
  private final BeanFactory beanFactory;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PersonDatastore personDatastore;
  private final PeopleController peopleController;
  private final PeopleModel peopleModel;

  private final JComboBox<DropDownItem> metadataAuthorField = new JComboBox<>();
  private final AppendableTableWithSelections scientistsField;
  private final AppendableTableWithSelections sourceOrganizationsField;
  private final AppendableTableWithSelections fundingOrganizationsField;


  @Autowired
  public PeoplePanel(PeopleList peopleList, OrganizationList organizationList, BeanFactory beanFactory, ReactiveViewRegistry reactiveViewRegistry, PersonDatastore personDatastore, PeopleController peopleController, PeopleModel peopleModel, OrganizationDatasore organizationDatasore) {
    this.peopleList = peopleList;
    this.organizationList = organizationList;
    this.beanFactory = beanFactory;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.personDatastore = personDatastore;
    this.peopleController = peopleController;
    this.peopleModel = peopleModel;
    this.scientistsField = new AppendableTableWithSelections(SCIENTISTS_LABEL, ADD_SCIENTIST_LABEL, PersonDatastore.UNSELECTED_PERSON, personDatastore.getPersonDropDowns());
    this.sourceOrganizationsField = new AppendableTableWithSelections(SOURCE_ORG_LABEL, ADD_SOURCE_ORG_LABEL, OrganizationDatasore.UNSELECTED_ORGANIZATION, organizationDatasore.getOrganizationDropDowns());
    this.fundingOrganizationsField = new AppendableTableWithSelections(FUNDING_ORG_LABEL, ADD_FUNDING_ORG_LABEL, OrganizationDatasore.UNSELECTED_ORGANIZATION, organizationDatasore.getOrganizationDropDowns());
  }


  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    metadataAuthorField.setModel(new DefaultComboBoxModel<>(personDatastore.getPersonDropDowns().toArray(new DropDownItem[0])));
    metadataAuthorField.setSelectedItem(peopleModel.getMetadataAuthor());
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(scientistsField, configureLayout(0, 0, c -> {
      c.weighty = 100;
      c.insets = new Insets(0, 0, 0, 20);
    }));
    add(sourceOrganizationsField, configureLayout(1, 0, c -> {
      c.weighty = 100;
      c.insets = new Insets(0, 0, 0, 20);
    }));
    add(fundingOrganizationsField, configureLayout(2, 0, c -> {
      c.weighty = 100;
    }));
    
    JButton editPeopleButton = new JButton(CREATE_PEOPLE_LABEL);
    editPeopleButton.addActionListener(e -> {
      new EditPersonDialog(beanFactory, peopleList);
    });
    add(editPeopleButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));
    
    JButton editOrgButton = new JButton(CREATE_ORG_LABEL);
    editOrgButton.addActionListener(e -> {
      new EditOrgDialog(beanFactory, organizationList);
    });
    add(editOrgButton, configureLayout(1, 2, c -> {
      c.weighty = 0;
    }));
    
    add(new JLabel(METADATA_AUTHOR_LABEL), configureLayout(2, 1, c -> {
      c.weighty = 0;
    }));
    add(metadataAuthorField, configureLayout(2, 2, c -> {
      c.weighty = 0;
    }));
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);

    metadataAuthorField.addItemListener((e) -> peopleController.setMetadataAuthor((DropDownItem) e.getItem()));
    scientistsField.addValuesChangedListener((i) -> peopleController.setScientists(i));
    sourceOrganizationsField.addValuesChangedListener((i) -> peopleController.setSourceOrganizations(i));
    fundingOrganizationsField.addValuesChangedListener((i) -> peopleController.setFundingOrganizations(i));
  }


  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_SCIENTISTS:
        updateAppendableTable(scientistsField, evt);
        break;
      case Events.UPDATE_SOURCE_ORGANIZATIONS:
        updateAppendableTable(sourceOrganizationsField, evt);
        break;
      case Events.UPDATE_FUNDING_ORGANIZATIONS:
        updateAppendableTable(fundingOrganizationsField, evt);
        break;
      case Events.UPDATE_METADATA_AUTHOR:
        updateComboBox(metadataAuthorField, evt);
        break;
      default:
        break;
    }
  }
}
