package edu.colorado.cires.cruisepack.app.ui.view.tab;

import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.queue.QueuePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab.CruisePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.omicstab.OmicsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab.PackagePanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeoplePanel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import javax.swing.JTabbedPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class ApplicationTabs extends JTabbedPane implements ReactiveView {

  private static final String PACKAGE_TAB_NAME = "Package";
  private static final String PEOPLE_TAB_NAME = "People / Organization";
  private static final String CRUISE_TAB_NAME = "Cruise Information";
  private static final String OMICS_TAB_NAME = "Omics";
  private static final String DATASETS_TAB_NAME = "Datasets";

  private final PackagePanel packagePanel;
  private final PeoplePanel peoplePanel;
  private final CruisePanel cruisePanel;
  private final OmicsPanel omicsPanel;
  private final DatasetsPanel datasetsPanel;
  private final QueuePanel queuePanel;
  private final ReactiveViewRegistry reactiveViewRegistry;

  @Autowired
  public ApplicationTabs(
      PackagePanel packagePanel,
      PeoplePanel peoplePanel,
      CruisePanel cruisePanel,
      OmicsPanel omicsPanel,
      DatasetsPanel datasetsPanel, QueuePanel queuePanel, ReactiveViewRegistry reactiveViewRegistry) {
    this.packagePanel = packagePanel;
    this.peoplePanel = peoplePanel;
    this.cruisePanel = cruisePanel;
    this.omicsPanel = omicsPanel;
    this.datasetsPanel = datasetsPanel;
    this.queuePanel = queuePanel;
    this.reactiveViewRegistry = reactiveViewRegistry;
  }

  @PostConstruct
  public void init() {
    reactiveViewRegistry.register(this);
    
    addTab(PACKAGE_TAB_NAME, packagePanel);
    addTab(PEOPLE_TAB_NAME, peoplePanel);
    addTab(CRUISE_TAB_NAME, cruisePanel);
    addTab(OMICS_TAB_NAME, omicsPanel);
    addTab(DATASETS_TAB_NAME, datasetsPanel);
    addTab(generateQueueTabText(queuePanel.getNRows()), queuePanel);
  }
  
  private String generateQueueTabText(int queueSize) {
    return String.format(
        "Queue (%s)", queueSize
    );
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(QueueModel.EMIT_QUEUE_SIZE)) {
      int newSize = (int) evt.getNewValue();
      int oldSize = (int) evt.getOldValue();
      
      if (newSize != oldSize) {
        setTitleAt(5, generateQueueTabText(newSize));
      }
    }
  }
}
