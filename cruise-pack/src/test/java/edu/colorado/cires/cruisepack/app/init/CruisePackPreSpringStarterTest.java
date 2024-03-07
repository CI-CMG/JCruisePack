package edu.colorado.cires.cruisepack.app.init;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruisePackPreSpringStarterTest {

  private static final Path testDir = Paths.get("target/CruisePackPreSpringStarterTest");

  @BeforeEach
  public void beforeEach() throws Exception {
    System.setProperty("cruise-pack.work-dir", testDir.toAbsolutePath().normalize().toString());
    FileUtils.deleteQuietly(testDir.toFile());
  }

  @AfterEach
  public void afterEach() throws Exception {
    System.clearProperty("cruise-pack.work-dir");
    FileUtils.deleteQuietly(testDir.toFile());
  }

  @Test
  public void testCreateAll() throws Exception {
    CruisePackPreSpringStarter.start();
    Set<Path> paths = new TreeSet<>();
    try(Stream<Path> pathStream = Files.walk(testDir)) {
      paths.addAll(pathStream.collect(Collectors.toList()));
    }
    Set<Path> expected = new TreeSet<>(Arrays.asList(
        testDir,
        testDir.resolve("log"),
        testDir.resolve("local-data"),
        testDir.resolve("local-data/cruise-metadata"),
        testDir.resolve("config"),
        testDir.resolve("data"),
        testDir.resolve("config/application.properties"),
        testDir.resolve("config/log4j2.xml"),
        testDir.resolve("data/gravityCorrectionModels.xml"),
        testDir.resolve("data/instruments.xml"),
        testDir.resolve("data/magneticsCorrectionModels.xml"),
        testDir.resolve("data/navigationDatums.xml"),
        testDir.resolve("data/organizations.xml"),
        testDir.resolve("data/people.xml"),
        testDir.resolve("data/ports.xml"),
        testDir.resolve("data/projects.xml"),
        testDir.resolve("data/seas.xml"),
        testDir.resolve("data/ships.xml"),
        testDir.resolve("data/singlebeamVerticalDatums.xml"),
        testDir.resolve("data/waterColumnCalibrationStates.xml")
    ));
    assertEquals(expected, paths);
  }
}