package edu.colorado.cires.cruisepack.app.ui.util;

import java.awt.GridBagConstraints;
import java.util.function.Consumer;

public final class LayoutUtils {

  public static GridBagConstraints configureLayout(int x, int y) {
    return configureLayout(x, y, null, null);
  }

  public static GridBagConstraints configureLayout(int x, int y, Integer width) {
    return configureLayout(x, y, width, null);
  }

  public static GridBagConstraints configureLayout(int x, int y, Consumer<GridBagConstraints> customizer) {
    return configureLayout(x, y, null, customizer);
  }

  public static GridBagConstraints configureLayout(int x, int y, Integer width, Consumer<GridBagConstraints> customizer) {
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.gridx = x;
    gridBagConstraints.gridy = y;
    if (width != null) {
      gridBagConstraints.gridwidth = width;
    }
    if (customizer != null) {
      customizer.accept(gridBagConstraints);
    }
    return gridBagConstraints;
  }

  private LayoutUtils() {

  }
}
