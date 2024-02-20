package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    classes = {
        MetadataService.class,
        JacksonAutoConfiguration.class,
        ServiceProperties.class,
        ShipDatastore.class,
        PortDatastore.class,
        SeaDatastore.class,
        InstrumentDatastore.class
    }
)
@ActiveProfiles("test")
public class MetadataServiceTest {

  private Path output = Paths.get("target/metadata.json").toAbsolutePath().normalize();
  private Path metadata1 = Paths.get("src/test/resources/metadata1.json");


  @BeforeEach
  public void beforeEach() throws Exception {
    FileUtils.deleteQuietly(output.toFile());
    Files.createDirectories(output.getParent());
  }

  @Autowired
  private MetadataService metadataService;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testGenerateCruiseMetadata() throws Exception {

    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments = new LinkedHashMap<>();

    instruments.put(new InstrumentDetailPackageKey("MB-BATHY", "EM122"), Arrays.asList(
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.RAW)
            .setUuid("9da1f3f0-9ec8-11e1-a8b0-0800200c9a66")
            .setReleaseDate(LocalDate.of(2023, 2, 20))
            .setInstrument("Kongsberg EM122")
            .setShortName("EM122")
            .setDataPath(output.resolve("data1").toAbsolutePath().normalize())
            .setDirName("EM122")
            .setBagName("FOOBARGE123_SEG1-BATHY_EM122")
            .setExtensions(new LinkedHashSet<>(Arrays.asList("bat", "tst")))
            .setFlatten(true)
            .setDataComment("comment1")
            .setAdditionalFiles(Arrays.asList(
                AdditionalFiles.builder()
                    .setSourceFileOrDirectory(output.resolve("src1").toAbsolutePath().normalize())
                    .setRelativeDestinationDirectory(output.resolve("dest1").toAbsolutePath().normalize())
                    .build(),
                AdditionalFiles.builder()
                    .setSourceFileOrDirectory(output.resolve("src2").toAbsolutePath().normalize())
                    .setRelativeDestinationDirectory(output.resolve("dest2").toAbsolutePath().normalize())
                    .build()
            ))
            .build(),
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.PROCESSED)
            .setUuid("9da1f3f0-9ec8-11e1-a8b0-0800200c9a66")
            .setReleaseDate(LocalDate.of(2023, 2, 21))
            .setInstrument("Kongsberg EM122")
            .setShortName("EM122")
            .setDataPath(output.resolve("data2").toAbsolutePath().normalize())
            .setDirName("EM122_processed")
            .setBagName("FOOBARGE123_SEG1-BATHY_EM122")
            .setFlatten(false)
            .setDataComment("comment2")
            .build()
    ));

    instruments.put(new InstrumentDetailPackageKey("WCSD", "Hydrosweep-DS-2"), Arrays.asList(
        InstrumentDetail.builder()
            .setStatus(InstrumentStatus.RAW)
            .setUuid("7318b3fd-823d-42a8-8a74-293c88256f87")
            .setReleaseDate(LocalDate.of(2023, 2, 22))
            .setInstrument("Atlas Hydrosweep DS-2")
            .setShortName("Hydrosweep-DS-2")
            .setDataPath(output.resolve("data3").toAbsolutePath().normalize())
            .setDirName("Hydrosweep-DS-2")
            .setBagName("FOOBARGE123_SEG1-WCSD_Hydrosweep-DS-2")
            .setDataComment("comment3")
            .build()
    ));

    PackJob packJob = PackJob.builder()
        .setCruiseId("FOOBARGE123")
        .setSegment("SEG1")
        .setPackageId("FOOBARGE123_SEG1")
        .setSeaUuid("40b4d5d3-15a4-466d-9980-30e0983e952c")
        .setArrivalPortUuid("28d56cfe-dbf2-4a65-a144-90cc13df894b")
        .setDeparturePortUuid("baf8405c-1937-44f3-a9be-c1b9c13e8942")
        .setShipUuid("632f234e-e9a8-4683-8fdb-c3cff0ee5a68")
        .setDepartureDate(LocalDate.of(2023, 2, 17))
        .setArrivalDate(LocalDate.of(2023, 2, 18))
        .setReleaseDate(LocalDate.of(2023, 2, 19))
        .setPackageDirectory(output.resolve("pack").toAbsolutePath().normalize())
        .setCruiseTitle("Foo Barge Cruise")
        .setCruisePurpose("Explore Foo Barginess")
        .setCruiseDescription("To boldly go where no Foo Barge has gone before")
        .setDocumentsPath(output.resolve("docs").toAbsolutePath().normalize())
        .setOmicsSamplingConducted(true)
        .setOmicsContactUuid("cca227a1-f644-4028-b01f-ebb7bc58eff9")
        .setOmicsSampleTrackingSheetPath(output.resolve("omics").toAbsolutePath().normalize())
        .setOmicsBioProjectAccession("OmicsBioProjectAccession")
        .setOmicsSamplingTypes(Arrays.asList("sample1", "sample2"))
        .setOmicsExpectedAnalyses(Arrays.asList("Analysis1", "Analysis2"))
        .setOmicsAdditionalSamplingInformation("AdditionalSamplingInformation")
        .setInstruments(instruments)
        .build();

    CruiseMetadata cruiseMetadata = metadataService.createMetadata(packJob);

    metadataService.writeMetadata(cruiseMetadata, output);

    cruiseMetadata = objectMapper.readValue(output.toFile(), CruiseMetadata.class);
    CruiseMetadata expected = objectMapper.readValue(metadata1.toFile(), CruiseMetadata.class);

    assertEquals(expected, cruiseMetadata);
  }
}