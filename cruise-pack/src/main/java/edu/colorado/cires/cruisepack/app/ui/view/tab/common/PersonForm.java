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

import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeopleList;

public class PersonForm extends JPanel {

    private Person.Builder personBuilder;

    public PersonForm(PeopleList peopleList) {
        personBuilder = Person.builder();
        init(peopleList);
    }

    private void init(PeopleList peopleList) {
        setLayout(new GridBagLayout());
        add(new JComboBox<>(peopleList.getPeopleList()), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.add(new JLabel("Name (First Last)"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        TextFormField nameFormField = TextFormField.create(personBuilder::withName);
        namePanel.add(nameFormField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        add(namePanel, configureLayout(0, 1, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel positionOrgPanel = new JPanel();
        positionOrgPanel.setLayout(new GridBagLayout());
        positionOrgPanel.add(new JLabel("Position"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField positionFormField = TextFormField.create(personBuilder::withPosition);
        positionOrgPanel.add(positionFormField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        positionOrgPanel.add(new JLabel("Organization"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField orgFormField = TextFormField.create(personBuilder::withOrganization);
        positionOrgPanel.add(orgFormField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        add(positionOrgPanel, configureLayout(0, 2, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel streetPanel = new JPanel();
        streetPanel.setLayout(new GridBagLayout());
        streetPanel.add(new JLabel("Street"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));

        TextFormField streetFormField = TextFormField.create(personBuilder::withStreet);
        streetPanel.add(streetFormField, configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        add(streetPanel, configureLayout(0, 3, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel cityStatePanel = new JPanel();
        cityStatePanel.setLayout(new GridBagLayout());
        cityStatePanel.add(new JLabel("City"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField cityFormField = TextFormField.create(personBuilder::withCity);
        cityStatePanel.add(cityFormField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(new JLabel("State/Administrative Area"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField stateFormField = TextFormField.create(personBuilder::withState);
        cityStatePanel.add(stateFormField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        add(cityStatePanel, configureLayout(0, 4, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel postalCountryPhonePanel = new JPanel();
        postalCountryPhonePanel.setLayout(new GridBagLayout());
        postalCountryPhonePanel.add(new JLabel("Postal Code"), configureLayout(0, 0, c -> {
            c.weightx = 20;
        }));

        TextFormField zipFormField = TextFormField.create(personBuilder::withZip);
        postalCountryPhonePanel.add(zipFormField, configureLayout(0, 1, c -> {
            c.weightx = 20;
        }));
        postalCountryPhonePanel.add(new JLabel("Country"), configureLayout(1, 0, c -> {
            c.weightx = 30;
        }));
        TextFormField countryFormField = TextFormField.createWithInitialValue("USA", personBuilder::withCountry);
        postalCountryPhonePanel.add(countryFormField, configureLayout(1, 1, c -> {
            c.weightx = 30;
        }));


        postalCountryPhonePanel.add(new JLabel("Phone"), configureLayout(2, 0, c -> {
            c.weightx = 30;
        }));
        TextFormField phoneFormField = TextFormField.create(personBuilder::withPhone);
        postalCountryPhonePanel.add(phoneFormField, configureLayout(2, 1, c -> {
            c.weightx = 50;
        }));

        add(postalCountryPhonePanel, configureLayout(0, 5, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel emailOrcidIDPanel = new JPanel();
        emailOrcidIDPanel.setLayout(new GridBagLayout());
        emailOrcidIDPanel.add(new JLabel("Email"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));

        TextFormField emailFormField = TextFormField.create(personBuilder::withEmail);
        emailOrcidIDPanel.add(emailFormField, configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(new JLabel("ORCID ID"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));
        
        TextFormField orcidIDFormField = TextFormField.create(personBuilder::withOrcidID);
        emailOrcidIDPanel.add(orcidIDFormField, configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));
        
        add(emailOrcidIDPanel, configureLayout(0, 6, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new GridBagLayout());
        uuidPanel.add(new JLabel("UUID (automatically generated when you click save)"), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        JTextField uuidFormField = TextFormField.create(personBuilder::withUUID);
        uuidFormField.setEditable(false);
        uuidPanel.add(uuidFormField, configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));

        add(uuidPanel, configureLayout(0, 7, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));

        JPanel clearSaveButtonsPannel = new JPanel();
        clearSaveButtonsPannel.setLayout(new GridBagLayout());

        RadioButtonFormField displayRadioButton = new RadioButtonFormField("Display in pull-down lists:", personBuilder::withUse);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((e) -> {
            nameFormField.setText(null);
            positionFormField.setText(null);
            orgFormField.setText(null);
            streetFormField.setText(null);
            cityFormField.setText(null);
            stateFormField.setText(null);
            zipFormField.setText(null);
            countryFormField.setText("USA");
            phoneFormField.setText(null);
            emailFormField.setText(null);
            orcidIDFormField.setText(null);
            uuidFormField.setText(null);
            displayRadioButton.setSelectedValue(false);
        });

        clearSaveButtonsPannel.add(clearButton, configureLayout(0, 0, c -> {
            c.weightx = 0;
        }));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener((e) -> {
            System.out.println(personBuilder.build().toString());
        });
        clearSaveButtonsPannel.add(saveButton, configureLayout(1, 0, c -> {
            c.weightx = 0;
        }));

        JPanel footerPannel = new JPanel();
        footerPannel.setLayout(new BorderLayout());
        footerPannel.add(displayRadioButton, BorderLayout.LINE_START);
        footerPannel.add(clearSaveButtonsPannel, BorderLayout.LINE_END);

        add(footerPannel, configureLayout(0, 8, c -> {
            c.weighty = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
    }
    
}
