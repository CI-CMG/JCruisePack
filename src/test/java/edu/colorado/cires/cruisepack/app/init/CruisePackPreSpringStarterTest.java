package edu.colorado.cires.cruisepack.app.init;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    System.clearProperty("spring.config.additional-location");
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
        testDir.resolve("config/log4j2.json"),
        testDir.resolve("data/gravityCorrectionModels.json"),
        testDir.resolve("data/instruments.json"),
        testDir.resolve("data/magneticsCorrectionModels.json"),
        testDir.resolve("data/navigationDatums.json"),
        testDir.resolve("data/organizations.json"),
        testDir.resolve("data/people.json"),
        testDir.resolve("data/ports.json"),
        testDir.resolve("data/seas.json"),
        testDir.resolve("data/ships.json"),
        testDir.resolve("data/singlebeamVerticalDatums.json"),
        testDir.resolve("data/waterColumnCalibrationStates.json")
    ));
    expected.stream().forEach(f -> {
      assertTrue(paths.contains(f));
    });
  }
}