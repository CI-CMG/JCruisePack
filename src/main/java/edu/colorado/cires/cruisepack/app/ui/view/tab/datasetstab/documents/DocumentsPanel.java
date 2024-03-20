package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.documents;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.common.ComponentEventListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab.CruiseDocumentsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DocumentsPanel extends JPanel {

  private final JButton removeButton = new JButton("Remove");
  private final CruiseDocumentsPanel cruiseDocumentsPanel;
  
  private final List<ComponentEventListener<DocumentsPanel>> removedListeners = new ArrayList<>(0);

  public DocumentsPanel(CruiseDocumentsPanel cruiseDocumentsPanel) {
    this.cruiseDocumentsPanel = cruiseDocumentsPanel;
    init();
  }
  
  private void init() {
    setupLayout();
    setupMvc();
  }
  
  public void addRemovedListener(ComponentEventListener<DocumentsPanel> listener) {
    removedListeners.add(listener);
  }
  
  public void restoreDefaultState() {
    cruiseDocumentsPanel.restoreDefaultState();
  }
  
  private void setupLayout() {
    setBorder(BorderFactory.createTitledBorder(InstrumentGroupName.DOCUMENTS.getLongName()));
    setBackground(Color.WHITE);
    setLayout(new GridBagLayout());
    
    JPanel removeButtonPanel = new JPanel();
    removeButtonPanel.setLayout(new BorderLayout());
    removeButtonPanel.setBackground(Color.WHITE);
    removeButtonPanel.add(removeButton, BorderLayout.WEST);
    
    add(removeButtonPanel, configureLayout(0, 0, c -> {
      c.weighty = 0;
      c.weightx = 0;
    }));
    add(cruiseDocumentsPanel, configureLayout(0, 1, c -> {
      c.weighty = 0;
      c.weightx = 100;
    }));
  }
  
  private void setupMvc() {
    removeButton.addActionListener((evt) -> {
      for (ComponentEventListener<DocumentsPanel> listener : removedListeners) {
        listener.handle(this);
      }
    });
  }
}
