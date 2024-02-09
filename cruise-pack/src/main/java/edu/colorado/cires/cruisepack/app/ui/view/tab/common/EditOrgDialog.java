package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.BeanFactory;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.OrganizationList;

public class EditOrgDialog extends JComponent {

    private static final String ORG_EDITOR_HEADER = "Organization Editor";

    public EditOrgDialog(BeanFactory beanFactory, OrganizationList organizationList) {
        this.init(beanFactory, organizationList);
    }

    private void init(BeanFactory beanFactory, OrganizationList organizationList) {
        JDialog dialog = new JDialog(beanFactory.getBean(MainFrame.class), ORG_EDITOR_HEADER, true);
        dialog.setLayout(new BorderLayout(0, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.add(new JComboBox<>(organizationList.getOrganizationList()), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.add(new JLabel("Organization Name"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));
        namePanel.add(new JTextField(), configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        formPanel.add(namePanel, configureLayout(0, 1, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel streetPanel = new JPanel();
        streetPanel.setLayout(new GridBagLayout());
        streetPanel.add(new JLabel("Street"), configureLayout(0, 0, c -> {
            c.weightx = 100;
        }));
        streetPanel.add(new JTextField(), configureLayout(0, 1, c -> {
            c.weightx = 100;
        }));

        formPanel.add(streetPanel, configureLayout(0, 2, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel cityStatePanel = new JPanel();
        cityStatePanel.setLayout(new GridBagLayout());
        cityStatePanel.add(new JLabel("City"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(new JTextField(), configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(new JLabel("State/Administrative Area"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));
        cityStatePanel.add(new JTextField(), configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));

        formPanel.add(cityStatePanel, configureLayout(0, 3, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel postalCountryPhonePanel = new JPanel();
        postalCountryPhonePanel.setLayout(new GridBagLayout());
        postalCountryPhonePanel.add(new JLabel("Postal Code"), configureLayout(0, 0, c -> {
            c.weightx = 20;
        }));
        postalCountryPhonePanel.add(new JTextField(), configureLayout(0, 1, c -> {
            c.weightx = 20;
        }));
        postalCountryPhonePanel.add(new JLabel("Country"), configureLayout(1, 0, c -> {
            c.weightx = 30;
        }));
        postalCountryPhonePanel.add(new JTextField("USA"), configureLayout(1, 1, c -> {
            c.weightx = 30;
        }));
        postalCountryPhonePanel.add(new JLabel("Phone"), configureLayout(2, 0, c -> {
            c.weightx = 50;
        }));
        postalCountryPhonePanel.add(new JTextField(), configureLayout(2, 1, c -> {
            c.weightx = 50;
        }));

        formPanel.add(postalCountryPhonePanel, configureLayout(0, 4, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel emailOrcidIDPanel = new JPanel();
        emailOrcidIDPanel.setLayout(new GridBagLayout());
        emailOrcidIDPanel.add(new JLabel("Email"), configureLayout(0, 0, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(new JTextField(), configureLayout(0, 1, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(new JLabel("ORCID ID"), configureLayout(1, 0, c -> {
            c.weightx = 50;
        }));
        emailOrcidIDPanel.add(new JTextField(), configureLayout(1, 1, c -> {
            c.weightx = 50;
        }));
        
        formPanel.add(emailOrcidIDPanel, configureLayout(0, 5, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new GridBagLayout());
        uuidPanel.add(new JLabel("UUID (automatically generated when you click save)"), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
        JTextField uuidTextField = new JTextField();
        uuidTextField.setEditable(false);
        uuidPanel.add(uuidTextField, configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));

        formPanel.add(uuidPanel, configureLayout(0, 6, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));

        JPanel clearSaveButtonsPannel = new JPanel();
        clearSaveButtonsPannel.setLayout(new GridBagLayout());
        clearSaveButtonsPannel.add(new JButton("Clear"), configureLayout(0, 0, c -> {
            c.weightx = 0;
        }));
        clearSaveButtonsPannel.add(new JButton("Save"), configureLayout(1, 0, c -> {
            c.weightx = 0;
        }));

        JPanel footerPannel = new JPanel();
        footerPannel.setLayout(new BorderLayout());
        footerPannel.add(new StatefulRadioButton("Display in pull-down lists:"), BorderLayout.LINE_START);
        footerPannel.add(clearSaveButtonsPannel, BorderLayout.LINE_END);

        formPanel.add(footerPannel, configureLayout(0, 7, c -> {
            c.weighty = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
        }));
        dialog.add(formPanel);
        dialog.pack();
        dialog.setVisible(true);
    }
    

}