package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.ImportRow;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CruiseDataDatastoreTest {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-dir");
  private static final MetadataService METADATA_SERVICE = mock(MetadataService.class);
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(
        TEST_PATH.resolve("work-dir").toString()
    );
  }
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setPropertyNamingStrategy(
      new PropertyNamingStrategies.SnakeCaseStrategy()
  );
  private final CruiseDataDatastore datastore = new CruiseDataDatastore(
      METADATA_SERVICE, 
      SERVICE_PROPERTIES,
      OBJECT_MAPPER,
      REACTIVE_VIEW_REGISTRY
  );
  
  @BeforeEach
  void beforeEach() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    reset(METADATA_SERVICE);
  }

  @Test
  void saveCruiseToPath() throws Exception {
    PackJob packJob = PackJob.builder()
        .setCruiseId("CRUISE-ID")
        .setPackageId("PACKAGE-ID")
        .build();
    when(METADATA_SERVICE.createData(packJob)).thenReturn(CruiseData.builder()
            .withCruiseId(packJob.getCruiseId())
            .withPackageId(packJob.getPackageId())
        .build());
    
    Path testFile = TEST_PATH.resolve("output-directory").resolve("test.json");
    FileUtils.createParentDirectories(testFile.toFile());
    datastore.saveCruiseToPath(packJob, testFile);
    
    assertTrue(testFile.toFile().exists());
    
    CruiseData cruiseData = OBJECT_MAPPER.readValue(testFile.toFile(), CruiseData.class);
    assertEquals(packJob.getCruiseId(), cruiseData.getCruiseId());
    assertEquals(packJob.getPackageId(), cruiseData.getPackageId());
  }

  @Test
  void delete() throws Exception {
    save("PACKAGE-ID-1");
    save("PACKAGE-ID-2");
    
    datastore.load();
    
    datastore.delete("PACKAGE-ID-1");
    datastore.delete("PACKAGE-ID-2");
    
    assertTrue(datastore.getCruises().isEmpty());
    
    try (Stream<Path> paths = Files.walk(TEST_PATH.resolve("work-dir").resolve("local-data").resolve("cruise-metadata"))) {
      assertEquals(0, paths.map(Path::toFile).filter(File::isFile).count());
    }
  }

  @Test
  void saveCruises() throws Exception {
    Path outputPath = TEST_PATH
        .resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve("PACKAGE-ID-1.json");
    FileUtils.createParentDirectories(outputPath.toFile());
    
    datastore.saveCruises(List.of(
        CruiseData.builder()
            .withCruiseId("CRUISE-ID")
            .withPackageId("PACKAGE-ID-1")
            .build(),
        CruiseData.builder()
            .withCruiseId("CRUISE-ID")
            .withPackageId("PACKAGE-ID-2")
            .build()
    ));

    assertTrue(outputPath.toFile().exists());

    CruiseData cruiseData = OBJECT_MAPPER.readValue(outputPath.toFile(), CruiseData.class);
    assertEquals("CRUISE-ID", cruiseData.getCruiseId());
    assertEquals("PACKAGE-ID-1", cruiseData.getPackageId());
    
    outputPath = outputPath.getParent().resolve("PACKAGE-ID-2.json");
    assertTrue(outputPath.toFile().exists());

    cruiseData = OBJECT_MAPPER.readValue(outputPath.toFile(), CruiseData.class);
    assertEquals("CRUISE-ID", cruiseData.getCruiseId());
    assertEquals("PACKAGE-ID-2", cruiseData.getPackageId());
  }

  @Test
  void saveCruiseData() throws Exception {
    Path outputPath = TEST_PATH
        .resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve("PACKAGE-ID.json");
    FileUtils.createParentDirectories(outputPath.toFile());
    
    datastore.saveCruise(CruiseData.builder()
            .withCruiseId("CRUISE-ID")
            .withPackageId("PACKAGE-ID")
            .withUse(false)
        .build());

    assertTrue(outputPath.toFile().exists());

    CruiseData cruiseData = OBJECT_MAPPER.readValue(outputPath.toFile(), CruiseData.class);
    assertEquals("CRUISE-ID", cruiseData.getCruiseId());
    assertEquals("PACKAGE-ID", cruiseData.getPackageId());
    assertFalse(cruiseData.isUse());
  }

  @Test
  void saveImportRow() throws Exception {
    Path outputPath = TEST_PATH
        .resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve("CRUISE-ID_LEG.json");
    FileUtils.createParentDirectories(outputPath.toFile());
    
    ImportRow row = new ImportRow() {
      @Override
      public String getShipName() {
        return null;
      }

      @Override
      public String getCruiseID() {
        return "CRUISE-ID";
      }

      @Override
      public String getLeg() {
        return "LEG";
      }

      @Override
      public String getChiefScientist() {
        return null;
      }

      @Override
      public String getSponsorOrganization() {
        return null;
      }

      @Override
      public String getFundingOrganization() {
        return null;
      }

      @Override
      public String getDeparturePort() {
        return null;
      }

      @Override
      public String getStartDate() {
        return null;
      }

      @Override
      public String getArrivalPort() {
        return null;
      }

      @Override
      public String getEndDate() {
        return null;
      }

      @Override
      public String getSeaArea() {
        return null;
      }

      @Override
      public String getProjectName() {
        return null;
      }

      @Override
      public String getCruiseTitle() {
        return null;
      }

      @Override
      public String getCruisePurpose() {
        return null;
      }

      @Override
      public List<String> getCTDInstruments() {
        return null;
      }

      @Override
      public List<String> getMBESInstruments() {
        return null;
      }

      @Override
      public List<String> getSBESInstruments() {
        return null;
      }

      @Override
      public List<String> getWaterColumnInstruments() {
        return null;
      }

      @Override
      public List<String> getADCPInstruments() {
        return null;
      }

      @Override
      public String getComments() {
        return null;
      }
    };
    Path packDir = TEST_PATH.resolve("pack-dir");
    String metadataAuthorName = "metadata author name";
    
    when(METADATA_SERVICE.createData(row, packDir, metadataAuthorName)).thenReturn(
        CruiseData.builder()
            .withCruiseId(row.getCruiseID())
            .withSegmentId(row.getLeg())
            .withPackageId(String.format(
                "%s_%s", row.getCruiseID(), row.getLeg()
            )).withMetadataAuthor(MetadataAuthor.builder()
                .withName(metadataAuthorName)
                .build())
            .build()
    );
    
    datastore.saveCruise(
        row,
        packDir,
        metadataAuthorName
    );

    assertTrue(outputPath.toFile().exists());

    CruiseData cruiseData = OBJECT_MAPPER.readValue(outputPath.toFile(), CruiseData.class);
    assertEquals(row.getCruiseID(), cruiseData.getCruiseId());
    assertEquals(row.getLeg(), cruiseData.getSegmentId());
    assertEquals(String.format(
        "%s_%s", row.getCruiseID(), row.getLeg()
    ), cruiseData.getPackageId());
    assertEquals(metadataAuthorName, cruiseData.getMetadataAuthor().getName());
  }

  @Test
  void init() throws Exception {
    save("PACKAGE-ID-1");
    
    datastore.init();
    
    List<CruiseData> cruises = datastore.getCruises();
    assertEquals(1, cruises.size());
    assertEquals("CRUISE-ID", cruises.get(0).getCruiseId());
    assertEquals("PACKAGE-ID-1", cruises.get(0).getPackageId());
  }

  @Test
  void load() throws Exception {
    save("PACKAGE-ID-1");
    save("PACKAGE-ID-2");
    
    datastore.load();
    
    List<CruiseData> cruises = datastore.getCruises();
    assertEquals(2, cruises.size());
    assertEquals("PACKAGE-ID-1", cruises.get(0).getPackageId());
    assertEquals("CRUISE-ID", cruises.get(0).getCruiseId());
    assertEquals("PACKAGE-ID-2", cruises.get(1).getPackageId());
    assertEquals("CRUISE-ID", cruises.get(1).getCruiseId());
  }

  @Test
  void getDropDownItems() throws Exception {
    Path outputPath = TEST_PATH
        .resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve("PACKAGE-ID-1.json");
    FileUtils.createParentDirectories(outputPath.toFile());

    datastore.saveCruise(CruiseData.builder()
        .withCruiseId("CRUISE-ID")
        .withPackageId("PACKAGE-ID-1")
        .withUse(false)
        .build());
    
    datastore.saveCruise(CruiseData.builder()
        .withCruiseId("CRUISE-ID")
        .withPackageId("PACKAGE-ID-2")
        .withUse(true)
        .build());
    
    datastore.load();
    
    List<DropDownItem> items = datastore.getDropDownItems();
    assertEquals(2, items.size());
    assertEquals(CruiseDataDatastore.UNSELECTED_CRUISE, items.get(0));
    assertEquals(new DropDownItem("PACKAGE-ID-2", "PACKAGE-ID-2"), items.get(1));
  }

  @Test
  void getByPackageId() throws Exception {
    String packageId = "PACKAGE-ID";
    Path outputPath = TEST_PATH
        .resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve(String.format(
            "%s.json", packageId
        ));
    FileUtils.createParentDirectories(outputPath.toFile());
    
    datastore.load();
    
    assertTrue(datastore.getByPackageId(packageId).isEmpty());
    
    save(packageId);
    
    datastore.load();

    Optional<CruiseData> maybeResult = datastore.getByPackageId(packageId);
    assertTrue(maybeResult.isPresent());
    assertEquals(packageId, maybeResult.get().getPackageId());
    assertEquals("CRUISE-ID", maybeResult.get().getCruiseId());
  }
  
  @Test
  void loadDirectoryDoesNotExist() {
    Path metadataDir = TEST_PATH.resolve("work-dir")
      .resolve("local-data")
      .resolve("cruise-metadata");
    Exception exception = assertThrows(IllegalStateException.class, datastore::load);
    assertEquals(String.format(
        "Cannot read cruise metadata from directory: %s", metadataDir
    ), exception.getMessage());
  }
  
  @Test
  void loadInvalidJson() throws IOException {
    Path metadataDir = TEST_PATH.resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve("instrument.json");
    FileUtils.createParentDirectories(metadataDir.toFile());
    OBJECT_MAPPER.writeValue(metadataDir.toFile(), InstrumentData.builder()
        .withUuid(UUID.randomUUID().toString())
        .withInstrument("Instrument name")
        .build());
    
    Exception exception = assertThrows(IllegalStateException.class, datastore::load);
    assertEquals(String.format(
        "Cannot read cruise metadata from file: %s", metadataDir
    ), exception.getMessage());
  }
  
  private void save(String packageId) throws Exception {
    PackJob packJob = PackJob.builder()
        .setCruiseId("CRUISE-ID")
        .setPackageId(packageId)
        .build();
    when(METADATA_SERVICE.createData(packJob)).thenReturn(CruiseData.builder()
        .withCruiseId(packJob.getCruiseId())
        .withPackageId(packJob.getPackageId())
        .build());

    Path outputPath = TEST_PATH.resolve("work-dir")
        .resolve("local-data")
        .resolve("cruise-metadata")
        .resolve(String.format(
            "%s.json", packageId
        ));
    FileUtils.createParentDirectories(outputPath.toFile());
    datastore.saveCruise(packJob);

    assertTrue(outputPath.toFile().exists());

    CruiseData cruiseData = OBJECT_MAPPER.readValue(outputPath.toFile(), CruiseData.class);
    assertEquals(packJob.getCruiseId(), cruiseData.getCruiseId());
    assertEquals(packJob.getPackageId(), cruiseData.getPackageId());
  }
}