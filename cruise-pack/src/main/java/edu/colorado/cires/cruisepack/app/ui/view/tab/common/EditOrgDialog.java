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
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.springframework.beans.factory.BeanFactory;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.OrganizationList;

public class EditOrgDialog extends JComponent {

    private static final String ORG_EDITOR_HEADER = "Organization Editor";

    public EditOrgDialog(BeanFactory beanFactory, OrganizationList organizationList) {
        this.init(beanFactory, organizationList);
    }

    private void init(BeanFactory beanFactory, OrganizationList organizationList) {
        JDialog dialog = new JDialog(beanFactory.getBean(MainFrame.class), ORG_EDITOR_HEADER, true);
      dialog.setLayout(new BorderLayout(10, 10));

      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      panel.add(new JComboBox<>(organizationList.getOrganizationList()), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Organization Name"), configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Street"), configureLayout(0, 5, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 6, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("City"), configureLayout(0, 7));
      panel.add(new JLabel("State/Administrative Area"), configureLayout(1, 7, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 8));
      panel.add(new JTextField(), configureLayout(1, 8, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Postal Code"), configureLayout(0, 9));
      panel.add(new JLabel("Country"), configureLayout(1, 9));
      panel.add(new JLabel("Phone"), configureLayout(2, 9, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 10));
      panel.add(new JTextField("USA"), configureLayout(1, 10));
      panel.add(new JTextField(), configureLayout(2, 10, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Email"), configureLayout(0, 11));
      panel.add(new JTextField(), configureLayout(0, 12, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("UUID (automatically generated when you click save)"), configureLayout(0, 13, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      JTextField uuidTextField = new JTextField();
      uuidTextField.setEditable(false);
      panel.add(uuidTextField, configureLayout(0, 14, c -> c.gridwidth = GridBagConstraints.REMAINDER));

      JPanel subPanel = new JPanel();
      subPanel.setLayout(new GridBagLayout());
      subPanel.add(new JLabel("Display in pull-down lists:"), configureLayout(0, 0));
      subPanel.add(new JRadioButton("Yes"), configureLayout(1, 0));
      subPanel.add(new JRadioButton("No"), configureLayout(2, 0));
      subPanel.add(new JButton("Clear"), configureLayout(3, 0));
      subPanel.add(new JButton("Save"), configureLayout(4, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      dialog.add(panel, BorderLayout.PAGE_START);
      dialog.add(subPanel, BorderLayout.PAGE_END);
      dialog.pack();
      dialog.setVisible(true);
    }
    

}