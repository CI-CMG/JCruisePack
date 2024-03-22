package edu.colorado.cires.cruisepack.app.ui.view.tab;

import edu.colorado.cires.cruisepack.app.ui.view.queue.QueuePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab.CruisePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.omicstab.OmicsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab.PackagePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeoplePanel;
import jakarta.annotation.PostConstruct;
import javax.swing.JTabbedPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class ApplicationTabs extends JTabbedPane {

  private static final String PACKAGE_TAB_NAME = "Package";
  private static final String PEOPLE_TAB_NAME = "People / Organization";
  private static final String CRUISE_TAB_NAME = "Cruise Information";
  private static final String OMICS_TAB_NAME = "Omics";
  private static final String DATASETS_TAB_NAME = "Datasets";
  private static final String QUEUE_TAB = "Queue";

  private final PackagePanel packagePanel;
  private final PeoplePanel peoplePanel;
  private final CruisePanel cruisePanel;
  private final OmicsPanel omicsPanel;
  private final DatasetsPanel datasetsPanel;
  private final QueuePanel queuePanel;

  @Autowired
  public ApplicationTabs(
      PackagePanel packagePanel,
      PeoplePanel peoplePanel,
      CruisePanel cruisePanel,
      OmicsPanel omicsPanel,
      DatasetsPanel datasetsPanel, QueuePanel queuePanel) {
    this.packagePanel = packagePanel;
    this.peoplePanel = peoplePanel;
    this.cruisePanel = cruisePanel;
    this.omicsPanel = omicsPanel;
    this.datasetsPanel = datasetsPanel;
    this.queuePanel = queuePanel;
  }

  @PostConstruct
  public void init() {
    addTab(PACKAGE_TAB_NAME, packagePanel);
    addTab(PEOPLE_TAB_NAME, peoplePanel);
    addTab(CRUISE_TAB_NAME, cruisePanel);
    addTab(OMICS_TAB_NAME, omicsPanel);
    addTab(DATASETS_TAB_NAME, datasetsPanel);
    addTab(QUEUE_TAB, queuePanel);
  }
}
