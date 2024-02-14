package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.OrganizationList;

public class OrgForm extends JPanel {

    private Organization.Builder orgBuilder;

    public OrgForm(OrganizationList organizationList) {
        orgBuilder = Organization.builder();
        init(organizationList);
    }

    private void init(OrganizationList organizationList) {
        setLayout(new GridBagLayout());
        add(new JComboBox<>(organizationList.getOrganizationList()), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.add(new JLabel("Organization Name"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        TextFormField orgNameFormField = TextFormField.create(orgBuilder::withOrgnaizationName);
        namePanel.add(orgNameFormField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        add(namePanel, configureLayout(0, 1, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel streetPanel = new JPanel();
        streetPanel.setLayout(new GridBagLayout());
        streetPanel.add(new JLabel("Street"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        TextFormField streetFormField = TextFormField.create(orgBuilder::withStreet);
        streetPanel.add(streetFormField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        add(streetPanel, configureLayout(0, 2, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel cityStatePanel = new JPanel();
        cityStatePanel.setLayout(new GridBagLayout());
        cityStatePanel.add(new JLabel("City"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField cityFormField = TextFormField.create(orgBuilder::withCity);
        cityStatePanel.add(cityFormField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(new JLabel("State/Administrative Area"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField stateFormField = TextFormField.create(orgBuilder::withState);
        cityStatePanel.add(stateFormField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        add(cityStatePanel, configureLayout(0, 3, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel postalCountryPhonePanel = new JPanel();
        postalCountryPhonePanel.setLayout(new GridBagLayout());
        postalCountryPhonePanel.add(new JLabel("Postal Code"), configureLayout(0, 0, c -> {
            c.weightx = 20;
        }));

        TextFormField zipFormField = TextFormField.create(orgBuilder::withZip);
        postalCountryPhonePanel.add(zipFormField, configureLayout(0, 1, c -> {
            c.weightx = 20;
        }));
        postalCountryPhonePanel.add(new JLabel("Country"), configureLayout(1, 0, c -> {
            c.weightx = 30;
        }));

        TextFormField countryFormField = TextFormField.createWithInitialValue("USA", orgBuilder::withCountry);
        postalCountryPhonePanel.add(countryFormField, configureLayout(1, 1, c -> {
            c.weightx = 30;
        }));
        postalCountryPhonePanel.add(new JLabel("Phone"), configureLayout(2, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField phoneFormField = TextFormField.create(orgBuilder::withPhone);
        postalCountryPhonePanel.add(phoneFormField, configureLayout(2, 1, c -> {
            c.weightx = 50;
        }));

        add(postalCountryPhonePanel, configureLayout(0, 4, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel emailOrcidIDPanel = new JPanel();
        emailOrcidIDPanel.setLayout(new GridBagLayout());
        emailOrcidIDPanel.add(new JLabel("Email"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField emailFormField = TextFormField.create(orgBuilder::withEmail);
        emailOrcidIDPanel.add(emailFormField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(new JLabel("ORCID ID"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField orcidID = TextFormField.create(orgBuilder::withOrdcidID);
        emailOrcidIDPanel.add(orcidID, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));
        
        add(emailOrcidIDPanel, configureLayout(0, 5, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new GridBagLayout());
        uuidPanel.add(new JLabel("UUID (automatically generated when you click save)"), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        JTextField uuidTextField = TextFormField.create(orgBuilder::withUUID);
        uuidTextField.setEditable(false);
        uuidPanel.add(uuidTextField, configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));

        add(uuidPanel, configureLayout(0, 6, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));

        JPanel clearSaveButtonsPannel = new JPanel();
        clearSaveButtonsPannel.setLayout(new GridBagLayout());

        StatefulRadioButton useRadioButton = new RadioButtonFormField("Display in pull-down lists:", orgBuilder::withUse);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((e) -> {
            orgNameFormField.setText(null);
            streetFormField.setText(null);
            cityFormField.setText(null);
            stateFormField.setText(null);
            zipFormField.setText(null);
            countryFormField.setText("USA");
            phoneFormField.setText(null);
            emailFormField.setText(null);
            orcidID.setText(null);
            uuidTextField.setText(null);
            // useRadioButton.setSelectedValue(false);
        });
        clearSaveButtonsPannel.add(clearButton, configureLayout(0, 0, c -> {
            c.weightx = 0;
        }));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener((e) -> {
            System.out.println(orgBuilder.build().toString());
        });
        clearSaveButtonsPannel.add(saveButton, configureLayout(1, 0, c -> {
            c.weightx = 0;
        }));

        JPanel footerPannel = new JPanel();
        footerPannel.setLayout(new BorderLayout());
        footerPannel.add(useRadioButton, BorderLayout.LINE_START);
        footerPannel.add(clearSaveButtonsPannel, BorderLayout.LINE_END);

        add(footerPannel, configureLayout(0, 7, c -> {
            c.weighty = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
    }
    
}
