package edu.colorado.cires.cruisepack.app.ui.view.queue;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.ComponentEventListener;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class PackJobPanel extends JPanel {
  
  private final PackJob packJob;
  private final String processId;
  
  private final List<ComponentEventListener<PackJobPanel>> removeListeners = new ArrayList<>(0);
  private final List<ComponentEventListener<PackJobPanel>> stopListeners = new ArrayList<>(0);
  
  private final JButton removeButton = new JButton("Remove");
  private final JButton stopButton = new JButton("Stop Packaging");
  private final JProgressBar progressBar = new JProgressBar();

  public PackJobPanel(PackJob packJob) {
    this.packJob = packJob;
    this.processId = UUID.randomUUID().toString();
  }

  public String getProcessId() {
    return processId;
  }

  public BoundedRangeModel getProgressBarModel() {
    return progressBar.getModel();
  }

  public JButton getRemoveButton() {
    return removeButton;
  }

  public JButton getStopButton() {
    return stopButton;
  }

  public PackJob getPackJob() {
    return packJob;
  }
  
  public void addRemoveListener(ComponentEventListener<PackJobPanel> listener) {
    removeListeners.add(listener);
  }
  
  public void addStopListener(ComponentEventListener<PackJobPanel> listener) {
    stopListeners.add(listener);
  }
  
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    stopButton.setEnabled(false);
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    
    JPanel row1 = new JPanel();
    row1.setLayout(new BorderLayout());
    row1.add(removeButton, BorderLayout.WEST);
    
    JPanel packAndStopButtons = new JPanel();
    packAndStopButtons.setLayout(new BorderLayout());
    packAndStopButtons.add(stopButton, BorderLayout.EAST);
    row1.add(packAndStopButtons, BorderLayout.EAST);
    
    add(new JLabel(packJob.getPackageId()), configureLayout(0, 0, c -> c.insets = new Insets(0, 0, 10, 0)));
    add(row1, configureLayout(0, 1));
    add(progressBar, configureLayout(0, 2));
  }
  
  private void setupMvc() {
    removeButton.addActionListener((evt) -> removeListeners.forEach(
        listener -> listener.handle(this)
    ));
    stopButton.addActionListener((evt) -> stopListeners.forEach(
        listener -> listener.handle(this)
    ));
  }
}
