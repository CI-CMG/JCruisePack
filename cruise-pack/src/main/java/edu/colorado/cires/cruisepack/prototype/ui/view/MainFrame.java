package edu.colorado.cires.cruisepack.prototype.ui.view;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame implements BaseViewPanel {

  private final DefaultController controller;

  public MainFrame(DefaultController controller) {
    super("CruisePack");
    this.controller = controller;

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(800, 800));

    JPanel rootPanel = new JPanel();
    rootPanel.setLayout(new BorderLayout());
    rootPanel.add(new Tabs(), BorderLayout.CENTER);
    rootPanel.add(new FooterPanel(), BorderLayout.SOUTH);

    add(rootPanel);

    pack();
    setVisible(true);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }

  private class Tabs extends JTabbedPane {

    private final PackagePanel packagePanel = new PackagePanel(controller);
    private final PeoplePanel peoplePanel = new PeoplePanel();
    private final CruiseInformationPanel cruiseInformationPanel = new CruiseInformationPanel();
    private final OmicsPanel omicsPanel = new OmicsPanel();
    private final DatasetsPanel datasetsPanel = new DatasetsPanel(controller);

    private Tabs() {
      controller.addView(packagePanel);
      controller.addView(peoplePanel);
      controller.addView(cruiseInformationPanel);
      controller.addView(omicsPanel);
      controller.addView(datasetsPanel);

      addTab("Package", packagePanel);
      addTab("People / Organizations", peoplePanel);
      addTab("Cruise Information", cruiseInformationPanel);
      addTab("Omics", omicsPanel);
      addTab("Datasets", datasetsPanel);
    }
  }

}
