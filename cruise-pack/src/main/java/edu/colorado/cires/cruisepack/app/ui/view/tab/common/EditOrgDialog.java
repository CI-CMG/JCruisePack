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
import java.util.List;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.OrganizationController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import jakarta.annotation.PostConstruct;

@Component
public class EditOrgDialog extends JDialog implements ReactiveView {

    private static final String ORG_EDITOR_HEADER = "Organization Editor";

    private final JComboBox<DropDownItem> orgList = new JComboBox<>();
    private final JTextField nameField = new JTextField();
    private final JLabel nameErrorLabel = createErrorLabel();
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
    private final JTextField uuidField = new JTextField();
    private final JLabel uuidErrorLabel = createErrorLabel();
    private final StatefulRadioButton useField = new StatefulRadioButton("Display in pull-down lists:");
    private final JButton clearButton = new JButton("Clear");
    private final JButton saveButton = new JButton("Save");
    private final OptionDialog optionDialog = new OptionDialog(
        "<html><B>Save changes before closing?</B></html>",
        List.of("Cancel", "No", "Yes")
    );
    private final OptionDialog closeAfterSaveDialog = new OptionDialog(
        "<html><B>Organization has been updated. Do you want to exit editor?</B></html>",
        List.of("No", "Yes")
    );

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final OrganizationDatastore organizationDatastore;
    private final OrganizationController organizationController;
    private final OrganizationModel organizationModel;

    @Autowired
    public EditOrgDialog(ReactiveViewRegistry reactiveViewRegistry, OrganizationDatastore organizationDatastore, OrganizationController organizationController, OrganizationModel organizationModel) {
        super((JFrame) null, ORG_EDITOR_HEADER, true);
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.organizationDatastore = organizationDatastore;
        this.organizationController = organizationController;
        this.organizationModel = organizationModel;
    }

    @PostConstruct
    private void init() {
        initializeFields();
        setupLayout();
        setupMvc();
    }

    private void initializeFields() {
        orgList.setModel(new DefaultComboBoxModel<>(organizationDatastore.getAllOrganizationDropDowns().toArray(new DropDownItem[0])));
        orgList.setSelectedItem(OrganizationDatastore.UNSELECTED_ORGANIZATION);

        nameField.setText(organizationModel.getName());
        streetField.setText(organizationModel.getStreet());
        cityField.setText(organizationModel.getCity());
        stateField.setText(organizationModel.getState());
        zipField.setText(organizationModel.getZip());
        countryField.setText(organizationModel.getCountry());
        phoneField.setText(organizationModel.getPhone());
        emailField.setText(organizationModel.getEmail());
        uuidField.setText(organizationModel.getUuid());
        uuidField.setEditable(false);
        useField.setSelectedValue(organizationModel.isUse());
    }

    private void setupLayout() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        JPanel form = new JPanel();

        form.setLayout(new GridBagLayout());
        form.add(orgList, configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.add(createLabelWithErrorPanel("Organization Name", nameErrorLabel), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        namePanel.add(nameField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        form.add(namePanel, configureLayout(0, 1, c -> {
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
            c.weightx = 100;
        }));

        emailOrcidIDPanel.add(emailField, configureLayout(0, 1, c -> {
            c.weightx = 100;
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

        orgList.addItemListener((evt) -> {
            DropDownItem item = (DropDownItem) evt.getItem();
            organizationDatastore.getByUUID(item.getId()).ifPresent(organization -> {
                organizationController.setName(organization.getName());
                organizationController.setStreet(organization.getStreet());
                organizationController.setCity(organization.getCity());
                organizationController.setState(organization.getState());
                organizationController.setZip(organization.getZip());
                organizationController.setCountry(organization.getCountry());
                organizationController.setPhone(organization.getPhone());
                organizationController.setEmail(organization.getEmail());
                organizationController.setUuid(organization.getUuid());
                organizationController.setUse(organization.isUse()); 
            });
        });

        addTextListener(organizationController::setName, nameField);
        addTextListener(organizationController::setStreet, streetField);
        addTextListener(organizationController::setCity, cityField);
        addTextListener(organizationController::setState, stateField);
        addTextListener(organizationController::setZip, zipField);
        addTextListener(organizationController::setCountry, countryField);
        addTextListener(organizationController::setPhone, phoneField);
        addTextListener(organizationController::setEmail, emailField);
        addTextListener(organizationController::setUuid, uuidField);
        useField.addValueChangeListener((v) -> organizationController.setUse(v));

        clearButton.addActionListener((e) -> {
            orgList.setSelectedItem(OrganizationDatastore.UNSELECTED_ORGANIZATION);
            organizationController.restoreDefaults();
        });
        
        closeAfterSaveDialog.addListener("Yes", (evt) -> setVisible(false));
        
        saveButton.addActionListener((e) -> {
            boolean success = organizationController.submit();
            if (success) {
                closeAfterSaveDialog.pack();
                closeAfterSaveDialog.setVisible(true);
            }
        });

        optionDialog.addListener("No", (evt) -> setVisible(false));
        optionDialog.addListener("Yes", (evt) -> {
            boolean saved = organizationController.submit();
            if (saved) {
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                optionDialog.pack();
                optionDialog.setVisible(true);
            }
        });
    }

    private void addTextListener(Consumer<String> consumer, JTextField textField) {
        textField.getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> consumer.accept(textField.getText()));
    }

    @Override
    public void onChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Events.UPDATE_ORG_NAME:
                updateTextField(nameField, evt);
                break;
            case Events.UPDATE_ORG_STREET:
                updateTextField(streetField, evt);
                break;
            case Events.UPDATE_ORG_CITY:
                updateTextField(cityField, evt);
                break;
            case Events.UPDATE_ORG_STATE:
                updateTextField(stateField, evt);
                break;
            case Events.UPDATE_ORG_ZIP:
                updateTextField(zipField, evt);
                break;
            case Events.UPDATE_ORG_COUNTRY:
                updateTextField(countryField, evt);
                break;
            case Events.UPDATE_ORG_PHONE:
                updateTextField(phoneField, evt);
                break;
            case Events.UPDATE_ORG_EMAIL:
                updateTextField(emailField, evt);
                break;
            case Events.UPDATE_ORG_UUID:
                updateTextField(uuidField, evt);
                break;
            case Events.UPDATE_ORG_USE:
                updateStatefulRadioButton(useField, evt);
                break;
            case Events.UPDATE_ORG_NAME_ERROR:
                updateLabelText(nameErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_STREET_ERROR:
                updateLabelText(streetErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_CITY_ERROR:
                updateLabelText(cityErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_STATE_ERROR:
                updateLabelText(stateErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_ZIP_ERROR:
                updateLabelText(zipErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_COUNTRY_ERROR:
                updateLabelText(countryErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_PHONE_ERROR:
                updateLabelText(phoneErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_EMAIL_ERROR:
                updateLabelText(emailErrorLabel, evt);
                break;
            case Events.UPDATE_ORG_UUID_ERROR:
                updateLabelText(uuidErrorLabel, evt);
                break;
            case Events.UPDATE_ORGANIZATION_DATA_STORE:
                updateComboBoxModel(orgList, organizationDatastore.getAllOrganizationDropDowns());
                break;
            case Events.EMIT_ORG_NAME:
                updateLabelText(closeAfterSaveDialog.getLabel(), new PropertyChangeEvent(
                    evt,
                    "UPDATE_CLOSE_AFTER_SAVE_LABEL",
                    closeAfterSaveDialog.getLabel().getText(),
                    String.format("<html><B>%s has been updated. Do you want to exit editor?</B></html>", evt.getNewValue())
                ));
                break;
            default:
                break;
        }
    }
    

}