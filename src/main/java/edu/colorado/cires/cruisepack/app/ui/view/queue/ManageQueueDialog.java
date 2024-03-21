package edu.colorado.cires.cruisepack.app.ui.view.queue;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class ManageQueueDialog extends JDialog {
  
  private final QueuePanel queuePanel;

  @Autowired
  public ManageQueueDialog(QueuePanel queuePanel) {
    super((JFrame) null, "Queue", false);
    this.queuePanel = queuePanel;
  }
  
  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {}
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(queuePanel, configureLayout(0, 0, c -> { c.weighty = 100; c.weightx = 100; }));
  }
  
  private void setupMvc() {}
}
