package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DatasetPanelHeader extends JPanel {

  private static final String PUBLIC_RELEASE_DATE = "Public Release Date";
  private static final String REMOVE = "Remove";
  private static final String SELECT_DIR_LABEL = "...";
  private static final String PATH_LABEL = "Path To Data Files";

  private final String dataType;

  private final DatePicker releaseDate = new DatePicker();
  private final JButton removeButton = new JButton(REMOVE);
  private final JTextField directoryPath = new JTextField();
  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);

  public DatasetPanelHeader(String dataType) {
    this.dataType = dataType;
  }

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {

  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.setBackground(Color.WHITE);
    row1.add(removeButton, configureLayout(0, 0, c -> { c.weightx = 0; c.fill = GridBagConstraints.NONE; }));

    JPanel releaseDatePanel = new JPanel();
    releaseDatePanel.setBackground(Color.WHITE);
    releaseDatePanel.setBorder(BorderFactory.createTitledBorder(PUBLIC_RELEASE_DATE));
    releaseDatePanel.add(releaseDate);
    row1.add(releaseDatePanel, configureLayout(1, 0, c -> { c.weightx = 1; c.anchor = GridBagConstraints.LINE_END; c.fill = GridBagConstraints.NONE; }));

    add(row1, configureLayout(0, 0));

    JPanel directorySelectPanel = new JPanel();
    directorySelectPanel.setLayout(new GridBagLayout());
    directorySelectPanel.setBackground(Color.WHITE);
    directorySelectPanel.setBorder(BorderFactory.createTitledBorder(PATH_LABEL));
    directorySelectPanel.add(directoryPath, configureLayout(0, 0, c -> c.weightx = 1));
    directorySelectPanel.add(dirSelectButton, configureLayout(1, 0, c -> c.weightx = 0));
    add(directorySelectPanel, configureLayout(0, 1));


  }

  private void setupMvc() {
  }
}
