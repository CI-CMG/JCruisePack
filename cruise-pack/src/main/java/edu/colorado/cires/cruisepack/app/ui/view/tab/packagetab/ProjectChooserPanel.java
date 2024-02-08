package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectChooserPanel extends JPanel implements ReactiveView {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final UiRefresher uiRefresher;

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

  private void setupLayout() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.WHITE);
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
    add(row);
    uiRefresher.refresh();
  }

  public void removeRow(ProjectRow row) {
    remove(row);
    uiRefresher.refresh();
  }
}
