package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class ProjectChooserPanel extends JPanel implements ReactiveView {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final UiRefresher uiRefresher;

  private final List<ProjectRow> rows = new ArrayList<>();

  private final JPanel fluff = new JPanel();

  @Autowired
  public ProjectChooserPanel(ReactiveViewRegistry reactiveViewRegistry, UiRefresher uiRefresher) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.uiRefresher = uiRefresher;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {

  }

  private void addFluff() {
    add(fluff, configureLayout(0, rows.size(), c -> {
      c.fill = GridBagConstraints.BOTH;
      c.weighty = 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }

  private void setupLayout() {
    fluff.setBackground(Color.WHITE);

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    addFluff();

  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }

  public void addRow() {
    ProjectRow row = new ProjectRow(this);
    row.init();
    remove(fluff);
    add(row, configureLayout(0, rows.size(), c -> {
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weighty = 0;
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    rows.add(row);
    addFluff();
    uiRefresher.refresh();
  }

  public void removeRow(ProjectRow row) {
    remove(row);
    rows.remove(row);
    uiRefresher.refresh();
  }
}
