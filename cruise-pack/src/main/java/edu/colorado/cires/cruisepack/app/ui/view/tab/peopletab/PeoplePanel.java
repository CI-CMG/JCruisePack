package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateAppendableTable;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBoxModel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PeopleController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.AppendableTableWithSelections;
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
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PersonDatastore personDatastore;
  private final OrganizationDatastore organizationDatasore;
  private final PeopleController peopleController;
  private final PeopleModel peopleModel;

  private final JComboBox<DropDownItem> metadataAuthorField = new JComboBox<>();
  private final JLabel metadataAuthorErrorLabel = new JLabel();
  private AppendableTableWithSelections scientistsField;
  private AppendableTableWithSelections sourceOrganizationsField;
  private AppendableTableWithSelections fundingOrganizationsField;
  private final EditPersonDialog editPersonDialog;
  private final EditOrgDialog editOrgDialog;

  @Autowired
  public PeoplePanel(PeopleList peopleList, OrganizationList organizationList, BeanFactory beanFactory, ReactiveViewRegistry reactiveViewRegistry, PersonDatastore personDatastore, PeopleController peopleController, PeopleModel peopleModel, OrganizationDatastore organizationDatasore, EditPersonDialog editPersonDialog, EditOrgDialog editOrgButton) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.personDatastore = personDatastore;
    this.organizationDatasore = organizationDatasore;
    this.peopleController = peopleController;
    this.peopleModel = peopleModel;
    this.editPersonDialog = editPersonDialog;
    this.editOrgDialog = editOrgButton;
  }


  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    scientistsField = new AppendableTableWithSelections(SCIENTISTS_LABEL, ADD_SCIENTIST_LABEL, PersonDatastore.UNSELECTED_PERSON, personDatastore.getEnabledPersonDropDowns());
    sourceOrganizationsField = new AppendableTableWithSelections(SOURCE_ORG_LABEL, ADD_SOURCE_ORG_LABEL, OrganizationDatastore.UNSELECTED_ORGANIZATION, organizationDatasore.getOrganizationDropDowns());
    fundingOrganizationsField = new AppendableTableWithSelections(FUNDING_ORG_LABEL, ADD_FUNDING_ORG_LABEL, OrganizationDatastore.UNSELECTED_ORGANIZATION, organizationDatasore.getOrganizationDropDowns());

    metadataAuthorField.setModel(new DefaultComboBoxModel<>(personDatastore.getEnabledPersonDropDowns().toArray(new DropDownItem[0])));
    metadataAuthorField.setSelectedItem(peopleModel.getMetadataAuthor());

    metadataAuthorErrorLabel.setText("");
    metadataAuthorErrorLabel.setForeground(new Color(Color.RED.getRGB()));
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
      editPersonDialog.addComponentListener(new ComponentListener() {

        @Override
        public void componentResized(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
          initializeFields();
        }
        
      });
      editPersonDialog.pack();
      editPersonDialog.setVisible(true);
    });
    add(editPeopleButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));
    
    JButton editOrgButton = new JButton(CREATE_ORG_LABEL);
    editOrgButton.addActionListener(e -> {
      editOrgDialog.addComponentListener(new ComponentListener() {

        @Override
        public void componentResized(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
          initializeFields();
        }
        
      });
      editOrgDialog.pack();
      editOrgDialog.setVisible(true);
    });
    add(editOrgButton, configureLayout(1, 2, c -> {
      c.weighty = 0;
    }));

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BorderLayout(10, 0));
    labelPanel.add(new JLabel(METADATA_AUTHOR_LABEL), BorderLayout.LINE_START);
    labelPanel.add(metadataAuthorErrorLabel, BorderLayout.CENTER);
    
    add(labelPanel, configureLayout(2, 1, c -> {
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
      case Events.UPDATE_SCIENTIST_ERROR:
        updateLabelText(scientistsField.getErrorLabel(), evt);
        break;
      case Events.UPDATE_SOURCE_ORGANIZATION_ERROR:
        updateLabelText(sourceOrganizationsField.getErrorLabel(), evt);
        break;
      case Events.UPDATE_FUNDING_ORGANIZATION_ERROR:
        updateLabelText(fundingOrganizationsField.getErrorLabel(), evt);
        break;
      case  Events.UPDATE_METADATA_AUTHOR_ERROR:
        updateLabelText(metadataAuthorErrorLabel, evt);
        break;
      case Events.UPDATE_PERSON_DATA_STORE:
        updateComboBoxModel(metadataAuthorField, personDatastore.getEnabledPersonDropDowns());
      default:
        break;
    }
  }
}
