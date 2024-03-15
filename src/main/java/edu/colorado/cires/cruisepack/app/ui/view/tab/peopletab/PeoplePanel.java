package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PeopleController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemList;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditOrgDialog;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditPersonDialog;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class PeoplePanel extends JPanel implements ReactiveView {

  private static final String SCIENTISTS_LABEL = "Scientists";
  private static final String SOURCE_ORG_LABEL = "Source Organizations";
  private static final String FUNDING_ORG_LABEL = "Funding Organizations";
  private static final String ADD_SCIENTIST_LABEL = "Add Scientist";
  private static final String ADD_SOURCE_ORG_LABEL = "Add Source Organization";
  private static final String ADD_FUNDING_ORG_LABEL = "Add Funding Organization";
  private static final String METADATA_AUTHOR_LABEL = "Data Packager";
  private static final String CREATE_PEOPLE_LABEL = "Create / Edit People";
  private static final String CREATE_ORG_LABEL = "Create / Edit Organizations";
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final PersonDatastore personDatastore;
  private final OrganizationDatastore organizationDatasore;
  private final PeopleController peopleController;
  private final PeopleModel peopleModel;

  private final JComboBox<DropDownItem> metadataAuthorField = new JComboBox<>();
  private final JLabel metadataAuthorErrorLabel = new JLabel();
  private DropDownItemList scientistsField;
  private DropDownItemList sourceOrganizationsField;
  private DropDownItemList fundingOrganizationsField;
  private final EditPersonDialog editPersonDialog;
  private final EditOrgDialog editOrgDialog;

  @Autowired
  public PeoplePanel(ReactiveViewRegistry reactiveViewRegistry, PersonDatastore personDatastore, PeopleController peopleController, PeopleModel peopleModel, OrganizationDatastore organizationDatasore, EditPersonDialog editPersonDialog, EditOrgDialog editOrgButton) {
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
    scientistsField = new DropDownItemList(SCIENTISTS_LABEL, ADD_SCIENTIST_LABEL, personDatastore.getEnabledPersonDropDowns(), PersonDatastore.UNSELECTED_PERSON);
    sourceOrganizationsField = new DropDownItemList(SOURCE_ORG_LABEL, ADD_SOURCE_ORG_LABEL, organizationDatasore.getEnabledOrganizationDropDowns(), OrganizationDatastore.UNSELECTED_ORGANIZATION);
    fundingOrganizationsField = new DropDownItemList(FUNDING_ORG_LABEL, ADD_FUNDING_ORG_LABEL, organizationDatasore.getEnabledOrganizationDropDowns(), OrganizationDatastore.UNSELECTED_ORGANIZATION);

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
      editPersonDialog.pack();
      editPersonDialog.setVisible(true);
    });
    add(editPeopleButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));
    
    JButton editOrgButton = new JButton(CREATE_ORG_LABEL);
    editOrgButton.addActionListener(e -> {
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
    scientistsField.addAddItemListener(peopleController::addScientist);
    scientistsField.addRemoveItemListener(peopleController::removeScientist);
    sourceOrganizationsField.addAddItemListener(peopleController::addSourceOrganization);
    sourceOrganizationsField.addRemoveItemListener(peopleController::removeSourceOrganization);
    fundingOrganizationsField.addAddItemListener(peopleController::addFundingOrganization);
    fundingOrganizationsField.addRemoveItemListener(peopleController::removeFundingOrganization);
  }


  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.ADD_SCIENTIST:
        scientistsField.addItem((DropDownItemPanel) evt.getNewValue());
        break;
      case Events.REMOVE_SCIENTIST:
        scientistsField.removeItem((DropDownItemPanel) evt.getOldValue());
        break;
      case Events.CLEAR_SCIENTISTS:
        scientistsField.clearItems();
        break;
      case Events.ADD_SOURCE_ORGANIZATION:
        sourceOrganizationsField.addItem((DropDownItemPanel) evt.getNewValue());
        break;
      case Events.REMOVE_SOURCE_ORGANIZATION:
        sourceOrganizationsField.removeItem((DropDownItemPanel) evt.getOldValue());
        break;
      case Events.CLEAR_SOURCE_ORGANIZATIONS:
        sourceOrganizationsField.clearItems();
        break;
      case Events.ADD_FUNDING_ORGANIZATION:
        fundingOrganizationsField.addItem((DropDownItemPanel) evt.getNewValue());
        break;
      case Events.REMOVE_FUNDING_ORGANIZATION:
        fundingOrganizationsField.removeItem((DropDownItemPanel) evt.getOldValue());
        break;
      case Events.CLEAR_FUNDING_ORGANIZATIONS:
        fundingOrganizationsField.clearItems();
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
      case Events.UPDATE_METADATA_AUTHOR_ERROR:
        updateLabelText(metadataAuthorErrorLabel, evt);
        break;
      case Events.UPDATE_PERSON_DATA_STORE:
        List<DropDownItem> options = personDatastore.getEnabledPersonDropDowns();
        scientistsField.updateOptions(options);
        break;
      case Events.UPDATE_ORGANIZATION_DATA_STORE:
        options = organizationDatasore.getEnabledOrganizationDropDowns();
        fundingOrganizationsField.updateOptions(options);
        sourceOrganizationsField.updateOptions(options);
        break;
      default:
        break;
    }
  }
}
