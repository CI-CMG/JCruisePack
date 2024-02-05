package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PeoplePanel extends JPanel {

  private static final String SCIENTISTS_LABEL = "Scientists";
  private static final String SPONSOR_ORG_LABEL = "Sponsor Organizations";
  private static final String FUNDING_ORG_LABEL = "Funding Organizations";
  private static final String ADD_SCIENTIST_LABEL = "Add Scientist";
  private static final String ADD_SPONSOR_ORG_LABEL = "Add Sponsor Organization";
  private static final String ADD_FUNDING_ORG_LABEL = "Add Funding Organization";
  private static final String METADATA_AUTHOR_LABEL = "Metadata Author";
  private static final String CREATE_PEOPLE_LABEL = "Create / Edit People";
  private static final String CREATE_ORG_LABEL = "Create / Edit Organizations";

  private final ScientistMultiComboBoxPanel scientistMultiComboBoxPanel;
  private final SponsorOrganizationMultiComboBoxPanel sponsorOrganizationMultiComboBoxPanel;
  private final FundingOrganizationMultiComboBoxPanel fundingOrganizationMultiComboBoxPanel;

  private final JButton addScientistButton = new JButton(ADD_SCIENTIST_LABEL);
  private final JButton addSponsorOrgButton = new JButton(ADD_SPONSOR_ORG_LABEL);
  private final JButton addFundingOrgButton = new JButton(ADD_FUNDING_ORG_LABEL);
  // TODO populate from data
  private final JComboBox<String> authorList = new JComboBox<>(new String[]{"Select Metadata Author"});


  @Autowired
  public PeoplePanel(ScientistMultiComboBoxPanel scientistMultiComboBoxPanel,
      SponsorOrganizationMultiComboBoxPanel sponsorOrganizationMultiComboBoxPanel,
      FundingOrganizationMultiComboBoxPanel fundingOrganizationMultiComboBoxPanel) {
    this.scientistMultiComboBoxPanel = scientistMultiComboBoxPanel;
    this.sponsorOrganizationMultiComboBoxPanel = sponsorOrganizationMultiComboBoxPanel;
    this.fundingOrganizationMultiComboBoxPanel = fundingOrganizationMultiComboBoxPanel;
  }


  @PostConstruct
  public void init() {
    authorList.setSelectedIndex(0);

    setLayout(new GridBagLayout());
    add(new JLabel(SCIENTISTS_LABEL), configureLayout(0, 0));
    add(new JLabel(SPONSOR_ORG_LABEL), configureLayout(1, 0));
    add(new JLabel(FUNDING_ORG_LABEL), configureLayout(2, 0));
    add(scientistMultiComboBoxPanel, configureLayout(0, 1, c -> c.ipady = 240));
    add(sponsorOrganizationMultiComboBoxPanel, configureLayout(1, 1, c -> c.ipady = 240));
    add(fundingOrganizationMultiComboBoxPanel, configureLayout(2, 1, c -> c.ipady = 240));
    add(addScientistButton, configureLayout(0, 2));
    add(addSponsorOrgButton, configureLayout(1, 2));
    add(addFundingOrgButton, configureLayout(2, 2));
    add(new JPanel(), configureLayout(0, 3, 2));
    add(new JLabel(METADATA_AUTHOR_LABEL), configureLayout(2, 3));
    add(new JLabel(CREATE_PEOPLE_LABEL), configureLayout(0, 4));
    add(new JLabel(CREATE_ORG_LABEL), configureLayout(1, 4));
    add(authorList, configureLayout(2, 4));

  }
}
