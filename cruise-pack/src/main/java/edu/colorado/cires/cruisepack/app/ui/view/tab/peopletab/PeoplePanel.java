package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditOrgDialog;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditPersonDialog;

@Component
public class PeoplePanel extends JPanel {

  private static final String SCIENTISTS_LABEL = "Scientists";
  private static final String SOURCE_ORG_LABEL = "Source Organizations";
  private static final String FUNDING_ORG_LABEL = "Funding Organizations";
  private static final String ADD_SCIENTIST_LABEL = "Add Scientist";
  private static final String ADD_SOURCE_ORG_LABEL = "Add Source Organization";
  private static final String ADD_FUNDING_ORG_LABEL = "Add Funding Organization";
  private static final String SELECT_SCIENTIST_LABEL = "Select Scientist";
  private static final String SELECT_SOURCE_ORG_LABEL = "Select Source Organization";
  private static final String SELECT_FUNDING_ORG_LABEL = "Select Funding Organization";
  private static final String METADATA_AUTHOR_LABEL = "Metadata Author";
  private static final String CREATE_PEOPLE_LABEL = "Create / Edit People";
  private static final String CREATE_ORG_LABEL = "Create / Edit Organizations";
  
  private final PeopleList peopleList;
  private final OrganizationList organizationList;
  private final BeanFactory beanFactory;


  @Autowired
  public PeoplePanel(PeopleList peopleList, OrganizationList organizationList, BeanFactory beanFactory) {
    this.peopleList = peopleList;
    this.organizationList = organizationList;
    this.beanFactory = beanFactory;
  }


  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(new AppendableTableWithSelections(
        peopleList.getPeopleList(),
        SCIENTISTS_LABEL,
        ADD_SCIENTIST_LABEL,
        SELECT_SCIENTIST_LABEL
    ), configureLayout(0, 0));
    add(new AppendableTableWithSelections(
        organizationList.getOrganizationList(),
        SOURCE_ORG_LABEL,
        ADD_SOURCE_ORG_LABEL,
        SELECT_SOURCE_ORG_LABEL
    ), configureLayout(1, 0));
    add(new AppendableTableWithSelections(
        organizationList.getOrganizationList(),
       FUNDING_ORG_LABEL,
        ADD_FUNDING_ORG_LABEL,
        SELECT_FUNDING_ORG_LABEL
        
    ), configureLayout(2, 0));
    
    JButton editPeopleButton = new JButton(CREATE_PEOPLE_LABEL);
    editPeopleButton.addActionListener(e -> {
      new EditPersonDialog(beanFactory, peopleList);
    });
    add(editPeopleButton, configureLayout(0, 2));
    
    JButton editOrgButton = new JButton(CREATE_ORG_LABEL);
    editOrgButton.addActionListener(e -> {
      new EditOrgDialog(beanFactory, organizationList);
    });
    add(editOrgButton, configureLayout(1, 2));
    
    add(new JLabel(METADATA_AUTHOR_LABEL), configureLayout(2, 1));
    add(new JComboBox<>(peopleList.getPeopleList()), configureLayout(2, 2));
  }
}
