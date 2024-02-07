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
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeopleList;

public class EditPersonDialog extends JComponent {

    private static final String PEOPLE_EDITOR_HEADER = "People Editor";

    
    public EditPersonDialog(BeanFactory beanFactory, PeopleList peopleList) {
        this.init(beanFactory, peopleList);
    }

    private void init(BeanFactory beanFactory, PeopleList peopleList) {
        JDialog dialog = new JDialog(beanFactory.getBean(MainFrame.class), PEOPLE_EDITOR_HEADER, true);
      dialog.setLayout(new BorderLayout(5, 5));

      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      panel.add(new JComboBox<>(peopleList.getPeopleList()), configureLayout(0, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Name (First Last)"), configureLayout(0, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JLabel("Position"), configureLayout(0, 3));
      panel.add(new JLabel("Organization"), configureLayout(1, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 4));
      panel.add(new JTextField(), configureLayout(1, 4, c -> c.gridwidth = GridBagConstraints.REMAINDER));
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
      panel.add(new JLabel("ORCID ID"), configureLayout(1, 11, c -> c.gridwidth = GridBagConstraints.REMAINDER));
      panel.add(new JTextField(), configureLayout(0, 12));
      panel.add(new JTextField(), configureLayout(1, 12, c -> c.gridwidth = GridBagConstraints.REMAINDER));
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