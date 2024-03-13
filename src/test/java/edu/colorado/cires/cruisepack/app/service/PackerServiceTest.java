package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.init.CruisePackPreSpringStarter;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
    properties = {
        "cruise-pack.work-dir=target/PackerServiceTest",
        "cruise-pack.ui=false"
    },
    useMainMethod = UseMainMethod.ALWAYS
)
@DirtiesContext
public class PackerServiceTest {

  private Path mainBagRootDir = Paths.get("target/test-output").toAbsolutePath().normalize();

  private static final Path workDir = Paths.get("target/PackerServiceTest");

  @Autowired
  private PackerService packerService;

  @MockBean
  private MetadataService metadataService;

  @MockBean
  private FooterControlController footerControlController;

  @MockBean
  private PackagingValidationService validationService;

  @BeforeAll
  public static void beforeAll() {
    System.setProperty("cruise-pack.work-dir", workDir.toAbsolutePath().normalize().toString());

  }

  @AfterAll
  public static void afterAll() {
    System.clearProperty("cruise-pack.work-dir");
    System.clearProperty("spring.config.additional-location");
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    FileUtils.deleteQuietly(workDir.toFile());
    Files.createDirectories(workDir.resolve("local-data"));
    Files.createFile(workDir.resolve("local-data").resolve("people.xml"));
    Files.createFile(workDir.resolve("local-data").resolve("organizations.xml"));
    FileUtils.deleteQuietly(mainBagRootDir.toFile());
    Files.createDirectories(mainBagRootDir);
    CruisePackPreSpringStarter.start();
  }

  @AfterEach
  public void afterEach() {
    FileUtils.deleteQuietly(workDir.toFile());
  }

  @Test
  public void testSingleDataset() throws Exception {
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments = new LinkedHashMap<>();
    List<InstrumentDetail> instrumentDetails = Arrays.asList(
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.RAW)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122"))
            .setDirName("EM122")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PROCESSED)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed"))
            .setDirName("EM122_processed")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PROCESSED)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1"))
            .setDirName("EM122_processed-1")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PRODUCTS)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"))
            .setDirName("EM122_products")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build()
    );
    instruments.put(new InstrumentDetailPackageKey("MB-BATHY", "EM122"), instrumentDetails);

    PackJob packJob = PackJob.builder()
        .setCruiseId("TST200400")
        .setPackageId("TST200400")
        .setReleaseDate(LocalDate.now())
        .setPackageDirectory(mainBagRootDir)
        .setInstruments(instruments)
        .setDocumentsPath(Paths.get("src/test/resources/test-src/TST200400/data/documents"))
        .setOmicsSampleTrackingSheetPath(Paths.get("src/test/resources/test-src/TST200400/data/omics-sheet/omics-file.txt"))
        .build();

    doReturn(Optional.of(packJob)).when(validationService).validate();

    CruiseMetadata cruiseMetadata = mock(CruiseMetadata.class);
    when(metadataService.createMetadata(packJob)).thenReturn(cruiseMetadata);

    Path metadataPath = mainBagRootDir.resolve("TST200400/TST200400-metadata.json");
    doAnswer(invocation -> {
      Path path = invocation.getArgument(1, Path.class);
      FileUtils.write(path.toFile(), "", StandardCharsets.UTF_8);
      return null;
    }).when(metadataService).writeMetadata(eq(cruiseMetadata), eq(metadataPath));

    CruiseMetadata instrumentMetadata = mock(CruiseMetadata.class);
    when(metadataService.createDatasetMetadata(cruiseMetadata, instrumentDetails)).thenReturn(instrumentMetadata);

    Path datasetMetadataPath = mainBagRootDir.resolve("TST200400/TST200400_MB-BATHY_EM122/TST200400_MB-BATHY_EM122-metadata.json");
    doAnswer(invocation -> {
      Path path = invocation.getArgument(1, Path.class);
      FileUtils.write(path.toFile(), "", StandardCharsets.UTF_8);
      return null;
    }).when(metadataService).writeMetadata(eq(instrumentMetadata), eq(datasetMetadataPath));

    packerService.startPacking();

    Thread.sleep(1000); //TODO be smarter with wait

    verify(metadataService).writeMetadata(eq(cruiseMetadata), eq(metadataPath));

    Path expectedRoot = Paths.get("src/test/resources/test-bags/TST200400/data/TST200400_MB-BATHY_EM122");
    TreeSet<Path> expected = new TreeSet<>();
    try (Stream<Path> fileStream = Files.walk(expectedRoot)) {
      fileStream.filter(Files::isRegularFile).map(expectedRoot::relativize).forEach(expected::add);
    }

    Path actualRoot = mainBagRootDir.resolve("TST200400/data/TST200400_MB-BATHY_EM122");
    TreeSet<Path> actual = new TreeSet<>();
    try (Stream<Path> fileStream = Files.walk(actualRoot)) {
      fileStream.filter(Files::isRegularFile).map(actualRoot::relativize).forEach(actual::add);
    }

    assertEquals(expected, actual);

  }

  @Test
  public void testEmptyDataset() throws Exception {
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments = new LinkedHashMap<>();
    List<InstrumentDetail> instrumentDetails = Arrays.asList(
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.RAW)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122"))
            .setDirName("EM122")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PROCESSED)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed"))
            .setDirName("EM122_processed")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PROCESSED)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1"))
            .setDirName("EM122_processed-1")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PRODUCTS)
            .setInstrument("longem122")
            .setShortName("EM122")
            .setDataPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"))
            .setDirName("EM122_products")
            .setBagName("TST200400_MB-BATHY_EM122")
            .build()
    );
    instruments.put(new InstrumentDetailPackageKey("MB-BATHY", "EM122"), instrumentDetails);

    PackJob packJob = PackJob.builder()
        .setCruiseId("TST200400")
        .setPackageId("TST200400")
        .setReleaseDate(LocalDate.now())
        .setPackageDirectory(mainBagRootDir)
        .setInstruments(instruments)
        .setDocumentsPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/documents"))
        .setOmicsSampleTrackingSheetPath(Paths.get("src/test/resources/test-src-empty/TST200400/data/omics-sheet/omics-file.txt"))
        .build();

    doReturn(Optional.of(packJob)).when(validationService).validate();

    CruiseMetadata cruiseMetadata = mock(CruiseMetadata.class);
    when(metadataService.createMetadata(packJob)).thenReturn(cruiseMetadata);

    Path metadataPath = mainBagRootDir.resolve("TST200400/TST200400-metadata.json");
    doAnswer(invocation -> {
      Path path = invocation.getArgument(1, Path.class);
      FileUtils.write(path.toFile(), "", StandardCharsets.UTF_8);
      return null;
    }).when(metadataService).writeMetadata(eq(cruiseMetadata), eq(metadataPath));

    CruiseMetadata instrumentMetadata = mock(CruiseMetadata.class);
    when(metadataService.createDatasetMetadata(cruiseMetadata, instrumentDetails)).thenReturn(instrumentMetadata);

    Path datasetMetadataPath = mainBagRootDir.resolve("TST200400/TST200400_MB-BATHY_EM122/TST200400_MB-BATHY_EM122-metadata.json");
    doAnswer(invocation -> {
      Path path = invocation.getArgument(1, Path.class);
      FileUtils.write(path.toFile(), "", StandardCharsets.UTF_8);
      return null;
    }).when(metadataService).writeMetadata(eq(instrumentMetadata), eq(datasetMetadataPath));

    packerService.startPacking();

    Thread.sleep(1000); //TODO be smarter with wait

    verify(metadataService).writeMetadata(eq(cruiseMetadata), eq(metadataPath));

    Path actualRoot = mainBagRootDir.resolve("TST200400/data/TST200400_MB-BATHY_EM122");
    assertFalse(actualRoot.toFile().exists());

  }

}