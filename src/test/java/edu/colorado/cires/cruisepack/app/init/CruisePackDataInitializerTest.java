package edu.colorado.cires.cruisepack.app.init;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

public class CruisePackDataInitializerTest {

  private static final Path testDir = Paths.get("target/CruisePackDataInitializerTest");

  @BeforeEach
  public void beforeEach() throws Exception {
    FileUtils.deleteQuietly(testDir.toFile());
    Files.createDirectories(testDir);
  }

  @AfterEach
  public void afterEach() throws Exception {
    FileUtils.deleteQuietly(testDir.toFile());
  }


  @Test
  public void test() {
    List<String> files = Arrays.stream(CruisePackDataInitializer.getPackagedData()).map(Resource::getFilename).sorted().collect(Collectors.toList());
    assertEquals(Arrays.asList(
        "gravityCorrectionModels.xml",
        "instruments.xml",
        "magneticsCorrectionModels.xml",
        "navigationDatums.xml",
        "organizations.xml",
        "people.xml",
        "ports.xml",
        "seas.xml",
        "ships.xml",
        "singlebeamVerticalDatums.xml",
        "waterColumnCalibrationStates.xml"
    ), files);

  }
}