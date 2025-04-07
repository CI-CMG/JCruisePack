package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.DatabaseObjectMapperFactory;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.data.WaterColumnCalibrationStateData;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterColumnCalibrationStateDatastore {

  public static final DropDownItem UNSELECTED_CALIBRATION_STATE = new DropDownItem("", "Select Calibration State");

  private final ServiceProperties serviceProperties;
  private List<DropDownItem> calibrationStateDropDowns;

  @Autowired
  public WaterColumnCalibrationStateDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path calibrationStatesFile = dataDir.resolve("waterColumnCalibrationStates.json");
    if (!Files.isRegularFile(calibrationStatesFile)) {
      throw new IllegalStateException("Unable to read " + calibrationStatesFile);
    }

    WaterColumnCalibrationStateData data;
    try {
      data = DatabaseObjectMapperFactory.getObjectMapper().readValue(calibrationStatesFile.toFile(), WaterColumnCalibrationStateData.class);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse " + calibrationStatesFile, e);
    }

    calibrationStateDropDowns = new ArrayList<>(data.getWaterColumnCalibrationStates().size() + 1);
    calibrationStateDropDowns.add(UNSELECTED_CALIBRATION_STATE);
    data.getWaterColumnCalibrationStates().stream()
        .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
        .map(c -> new DropDownItem(c.getUuid(), c.getName()))
        .forEach(calibrationStateDropDowns::add);
  }

  public List<DropDownItem> getCalibrationStateDropDowns() {
    return calibrationStateDropDowns;
  }

}
