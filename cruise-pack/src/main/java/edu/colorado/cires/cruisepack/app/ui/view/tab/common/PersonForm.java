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

    private REMOVePerson.Builder personBuilder;

    public PersonForm(PeopleList peopleList) {
        personBuilder = REMOVePerson.builder();
        init(peopleList);
    }

    private void init(PeopleList peopleList) {
        
    }
    
}
