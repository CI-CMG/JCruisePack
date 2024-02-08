package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DatasetPanelHeader extends JPanel {

  private static final String PUBLIC_RELEASE_DATE = "Public Release Date";
  private static final String REMOVE = "Remove";

  private final String dataType;

  private final DatePicker releaseDate = new DatePicker();
  private final JButton removeButton = new JButton(REMOVE);

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
    add(new JLabel(dataType), configureLayout(0, 0));
    add(new JLabel(PUBLIC_RELEASE_DATE, null, SwingConstants.RIGHT), configureLayout(1, 0, c -> c.weightx = 0));
    add(releaseDate, configureLayout(2, 0, c -> c.weightx = 0));
    add(removeButton, configureLayout(3, 0, c -> c.weightx = 0));
  }

  private void setupMvc() {
  }
}
