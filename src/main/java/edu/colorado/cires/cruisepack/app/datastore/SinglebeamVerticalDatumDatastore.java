package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.DatabaseObjectMapperFactory;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.data.SinglebeamVerticalDatumData;
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
public class SinglebeamVerticalDatumDatastore {

  public static final DropDownItem UNSELECTED_VERTICAL_DATUM = new DropDownItem("", "Select Vertical Datum");

  private final ServiceProperties serviceProperties;

  private List<DropDownItem> verticalDatumDropDowns;

  @Autowired
  public SinglebeamVerticalDatumDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path verticalDatumsFile = dataDir.resolve("singlebeamVerticalDatums.json");
    if (!Files.isRegularFile(verticalDatumsFile)) {
      throw new IllegalStateException("Unable to read " + verticalDatumsFile);
    }

    SinglebeamVerticalDatumData data;
    try {
      data = DatabaseObjectMapperFactory.getObjectMapper().readValue(verticalDatumsFile.toFile(), SinglebeamVerticalDatumData.class);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse " + verticalDatumsFile, e);
    }

    verticalDatumDropDowns = new ArrayList<>(data.getSinglebeamVerticalDatums().size() + 1);
    verticalDatumDropDowns.add(UNSELECTED_VERTICAL_DATUM);
    data.getSinglebeamVerticalDatums().stream()
        .sorted((d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName()))
        .map(d -> new DropDownItem(d.getUuid(), d.getName()))
        .forEach(verticalDatumDropDowns::add);
  }

  public List<DropDownItem> getVerticalDatumDropDowns() {
    return verticalDatumDropDowns;
  }

}
