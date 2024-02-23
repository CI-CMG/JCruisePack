package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBoxModel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateStatefulRadioButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.PersonController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.xml.person.Person;
import jakarta.annotation.PostConstruct;

@Component
public class EditPersonDialog extends JDialog implements ReactiveView {

    private static final String PEOPLE_EDITOR_HEADER = "People Editor";

    private final JComboBox<DropDownItem> peopleList = new JComboBox<>();
    private final JTextField nameField = new JTextField();
    private final JLabel nameErrorLabel = createErrorLabel();
    private final JTextField positionField = new JTextField();
    private final JLabel positionErrorLabel = createErrorLabel();
    private final JTextField organizationField = new JTextField();
    private final JLabel organizationErrorLabel = createErrorLabel();
    private final JTextField streetField = new JTextField();
    private final JLabel streetErrorLabel = createErrorLabel();
    private final JTextField cityField = new JTextField();
    private final JLabel cityErrorLabel = createErrorLabel();
    private final JTextField stateField = new JTextField();
    private final JLabel stateErrorLabel = createErrorLabel();
    private final JTextField zipField = new JTextField();
    private final JLabel zipErrorLabel = createErrorLabel();
    private final JTextField countryField = new JTextField();
    private final JLabel countryErrorLabel = createErrorLabel();
    private final JTextField phoneField = new JTextField();
    private final JLabel phoneErrorLabel = createErrorLabel();
    private final JTextField emailField = new JTextField();
    private final JLabel emailErrorLabel = createErrorLabel();
    private final JTextField orcidIDField = new JTextField();
    private final JLabel orcidIDErrorLabel = createErrorLabel();
    private final JTextField uuidField = new JTextField();
    private final JLabel uuidErrorLabel = createErrorLabel();
    private final StatefulRadioButton useField = new StatefulRadioButton("Display in pull-down lists:");
    private final JButton clearButton = new JButton("Clear");
    private final JButton saveButton = new JButton("Save");
    private final SaveBeforeExitDialog saveBeforeExitDialog = new SaveBeforeExitDialog("<html><B>Save changes before closing?</B></html>");

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final PersonDatastore personDatastore;
    private final PersonController personController;
    private final PersonModel personModel;

    public EditPersonDialog(PersonDatastore personDatastore, ReactiveViewRegistry reactiveViewRegistry, PersonController personController, PersonModel personModel) {
        super((JFrame) null, PEOPLE_EDITOR_HEADER, true);
        this.personDatastore = personDatastore;
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.personController = personController;
        this.personModel = personModel;
    }

    @PostConstruct
    private void init() {
        initializeFields();
        setupLayout();
        setupMvc();
    }

    private void initializeFields() {
        peopleList.setModel(new DefaultComboBoxModel<>(personDatastore.getAllPersonDropDowns().toArray(new DropDownItem[0])));
        peopleList.setSelectedItem(PersonDatastore.UNSELECTED_PERSON);
        nameField.setText(personModel.getName());
        positionField.setText(personModel.getPosition());
        organizationField.setText(personModel.getOrganization());
        streetField.setText(personModel.getStreet());
        cityField.setText(personModel.getCity());
        stateField.setText(personModel.getState());
        zipField.setText(personModel.getZip());
        countryField.setText(personModel.getCountry());
        phoneField.setText(personModel.getPhone());
        emailField.setText(personModel.getEmail());
        orcidIDField.setText(personModel.getOrcidID());
        uuidField.setText(personModel.getUuid());
        uuidField.setEditable(false);
        useField.setSelectedValue(personModel.isUse());
    }

    private void setupLayout() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        JPanel form = new JPanel();

        form.setLayout(new GridBagLayout());
        form.add(peopleList, configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.add(createLabelWithErrorPanel("Name (First Last)", nameErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        namePanel.add(nameField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        form.add(namePanel, configureLayout(0, 1, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel positionOrgPanel = new JPanel();
        positionOrgPanel.setLayout(new GridBagLayout());
        positionOrgPanel.add(createLabelWithErrorPanel("Position", positionErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        positionOrgPanel.add(positionField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        positionOrgPanel.add(createLabelWithErrorPanel("Organization", organizationErrorLabel), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        positionOrgPanel.add(organizationField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        form.add(positionOrgPanel, configureLayout(0, 2, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel streetPanel = new JPanel();
        streetPanel.setLayout(new GridBagLayout());
        streetPanel.add(createLabelWithErrorPanel("Street", streetErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        streetPanel.add(streetField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        form.add(streetPanel, configureLayout(0, 3, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel cityStatePanel = new JPanel();
        cityStatePanel.setLayout(new GridBagLayout());
        cityStatePanel.add(createLabelWithErrorPanel("City", cityErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        cityStatePanel.add(cityField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(createLabelWithErrorPanel("State/Administrative Area", stateErrorLabel), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        cityStatePanel.add(stateField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        form.add(cityStatePanel, configureLayout(0, 4, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel postalCountryPhonePanel = new JPanel();
        postalCountryPhonePanel.setLayout(new GridBagLayout());
        postalCountryPhonePanel.add(createLabelWithErrorPanel("Postal Code", zipErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 20;
        }));

        postalCountryPhonePanel.add(zipField, configureLayout(0, 1, c -> {
            c.weightx = 20;
        }));
        postalCountryPhonePanel.add(createLabelWithErrorPanel("Country", countryErrorLabel), configureLayout(1, 0, c -> {
            c.weightx = 30;
        }));

        postalCountryPhonePanel.add(countryField, configureLayout(1, 1, c -> {
            c.weightx = 30;
        }));


        postalCountryPhonePanel.add(createLabelWithErrorPanel("Phone", phoneErrorLabel), configureLayout(2, 0, c -> {
            c.weightx = 30;
        }));

        postalCountryPhonePanel.add(phoneField, configureLayout(2, 1, c -> {
            c.weightx = 50;
        }));

        form.add(postalCountryPhonePanel, configureLayout(0, 5, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel emailOrcidIDPanel = new JPanel();
        emailOrcidIDPanel.setLayout(new GridBagLayout());
        emailOrcidIDPanel.add(createLabelWithErrorPanel("Email", emailErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        emailOrcidIDPanel.add(emailField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(createLabelWithErrorPanel("ORCID ID", orcidIDErrorLabel), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        emailOrcidIDPanel.add(orcidIDField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));
        
        form.add(emailOrcidIDPanel, configureLayout(0, 6, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new GridBagLayout());
        uuidPanel.add(createLabelWithErrorPanel("UUID (automatically generated when you click save)", uuidErrorLabel), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));

        uuidPanel.add(uuidField, configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));

        form.add(uuidPanel, configureLayout(0, 7, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));

        JPanel clearSaveButtonsPannel = new JPanel();
        clearSaveButtonsPannel.setLayout(new GridBagLayout());
        clearSaveButtonsPannel.add(clearButton, configureLayout(0, 0, c -> {
            c.weightx = 0;
        }));

        clearSaveButtonsPannel.add(saveButton, configureLayout(1, 0, c -> {
            c.weightx = 0;
        }));

        JPanel footerPannel = new JPanel();
        footerPannel.setLayout(new BorderLayout());
        footerPannel.add(useField, BorderLayout.LINE_START);
        footerPannel.add(clearSaveButtonsPannel, BorderLayout.LINE_END);

        form.add(footerPannel, configureLayout(0, 8, c -> {
            c.weighty = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        add(form);
    }

    private void setupMvc() {
        reactiveViewRegistry.register(this);

        peopleList.addItemListener((evt) -> {
            DropDownItem item = (DropDownItem) evt.getItem();
            Person person = (Person) item.getRecord();
            if (person == null) {
                return;
            }
            personController.setName(person.getName());
            personController.setPosition(person.getPosition());
            personController.setOrganization(person.getOrganization());
            personController.setStreet(person.getStreet());
            personController.setCity(person.getCity());
            personController.setState(person.getState());
            personController.setZip(person.getZip());
            personController.setCountry(person.getCountry());
            personController.setPhone(person.getPhone());
            personController.setEmail(person.getEmail());
            personController.setOrcidID(person.getOrcid());
            personController.setUUID(person.getUuid());
            personController.setUse(person.isUse());
        });
        addTextListener(personController::setName, nameField);
        addTextListener(personController::setPosition, positionField);
        addTextListener(personController::setOrganization, organizationField);
        addTextListener(personController::setStreet, streetField);
        addTextListener(personController::setCity, cityField);
        addTextListener(personController::setState, stateField);
        addTextListener(personController::setZip, zipField);
        addTextListener(personController::setCountry, countryField);
        addTextListener(personController::setPhone, phoneField);
        addTextListener(personController::setEmail, emailField);
        addTextListener(personController::setOrcidID, orcidIDField);
        addTextListener(personController::setUUID, uuidField);
        useField.addValueChangeListener((use) -> personController.setUse(use));
        clearButton.addActionListener((e) -> {
            peopleList.setSelectedItem(PersonDatastore.UNSELECTED_PERSON);
            personController.restoreDefaults();
        });
        saveButton.addActionListener((e) -> personController.submit());

        saveBeforeExitDialog.addNoListener((evt) -> {
            setVisible(false);
        });
        saveBeforeExitDialog.addYesListener((evt) -> {
            boolean saved = personController.submit();
            if (saved) {
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveBeforeExitDialog.pack();
                saveBeforeExitDialog.setVisible(true);
            }
        });
    }

    private void addTextListener(Consumer<String> consumer, JTextField textField) {
        textField.getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> consumer.accept(textField.getText()));
    }

    @Override
    public void onChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Events.UPDATE_PERSON_NAME:
                updateTextField(nameField, evt);
                break;
            case Events.UPDATE_PERSON_POSITION:
                updateTextField(positionField, evt);
                break;
            case Events.UPDATE_PERSON_ORGANIZATION:
                updateTextField(organizationField, evt);
                break;
            case Events.UPDATE_PERSON_STREET:
                updateTextField(streetField, evt);
                break;
            case Events.UPDATE_PERSON_CITY:
                updateTextField(cityField, evt);
                break;
            case Events.UPDATE_PERSON_STATE:
                updateTextField(stateField, evt);
                break;
            case Events.UPDATE_PERSON_ZIP:
                updateTextField(zipField, evt);
                break;
            case Events.UPDATE_PERSON_COUNTRY:
                updateTextField(countryField, evt);
                break;
            case Events.UPDATE_PERSON_PHONE:
                updateTextField(phoneField, evt);
                break;
            case Events.UPDATE_PERSON_EMAIL:
                updateTextField(emailField, evt);
                break;
            case Events.UPDATE_PERSON_ORCIDID:
                updateTextField(orcidIDField, evt);
                break;
            case Events.UPDATE_PERSON_UUID:
                updateTextField(uuidField, evt);
                break;
            case Events.UPDATE_PERSON_USE:
                updateStatefulRadioButton(useField, evt);
                break;
            case Events.UPDATE_PERSON_NAME_ERROR:
                updateLabelText(nameErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_POSITION_ERROR:
                updateLabelText(positionErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_ORGANIZATION_ERROR:
                updateLabelText(organizationErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_STREET_ERROR:
                updateLabelText(streetErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_CITY_ERROR:
                updateLabelText(cityErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_STATE_ERROR:
                updateLabelText(stateErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_ZIP_ERROR:
                updateLabelText(zipErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_COUNTRY_ERROR:
                updateLabelText(countryErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_PHONE_ERROR:
                updateLabelText(phoneErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_EMAIL_ERROR:
                updateLabelText(emailErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_ORCID_ID_ERROR:
                updateLabelText(orcidIDErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_UUID_ERROR:
                updateLabelText(uuidErrorLabel, evt);
                break;
            case Events.UPDATE_PERSON_DATA_STORE:
                updateComboBoxModel(peopleList, personDatastore.getAllPersonDropDowns());
                break;
            default:
                break;
        }
    }


}