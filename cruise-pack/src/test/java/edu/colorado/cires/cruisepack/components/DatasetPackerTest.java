package edu.colorado.cires.cruisepack.components;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.prototype.components.DatasetPacker;
import edu.colorado.cires.cruisepack.prototype.components.model.InstrumentDetail;
import edu.colorado.cires.cruisepack.prototype.components.model.InstrumentStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatasetPackerTest {

  private Path mainBagRootDir = Paths.get("target/TST200400");

  @BeforeEach
  public void beforeEach() throws Exception{
    FileUtils.deleteQuietly(mainBagRootDir.toFile());
    Files.createDirectories(mainBagRootDir.resolve("data"));
  }

//  @AfterEach
//  public void afterEach() {
//    FileUtils.deleteQuietly(mainBagRootDir.toFile());
//  }

  //TODO test flatten

  @Test
  public void test() throws Exception {
    Map<String, List<InstrumentDetail>> instruments = new HashMap<>();
    InstrumentDetail em122Raw = new InstrumentDetail(
        InstrumentStatus.RAW,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122")
    );
    em122Raw.setDirName("EM122");
    em122Raw.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122Processed = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed")
    );
    em122Processed.setDirName("EM122_processed");
    em122Processed.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122Processed1 = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1")
    );
    em122Processed1.setDirName("EM122_processed-1");
    em122Processed1.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122Products = new InstrumentDetail(
        InstrumentStatus.PRODUCTS,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products")
    );
    em122Products.setDirName("EM122_products");
    em122Products.setBagName("TST200400_MB-BATHY_EM122");

    instruments.put("MB-BATHY_EM122", Arrays.asList(em122Raw, em122Processed, em122Processed1, em122Products));


    Files.createDirectories(mainBagRootDir);
    DatasetPacker.pack(mainBagRootDir, instruments);

    Path expectedRoot = Paths.get("src/test/resources/test-bags/TST200400/data/TST200400_MB-BATHY_EM122");
    TreeSet<Path> expected = new TreeSet<>();
    try(Stream<Path> fileStream = Files.walk(expectedRoot)) {
      fileStream.filter(Files::isRegularFile).map(expectedRoot::relativize).forEach(expected::add);
    }

    Path actualRoot = mainBagRootDir.resolve("data/TST200400_MB-BATHY_EM122");
    TreeSet<Path> actual = new TreeSet<>();
    try(Stream<Path> fileStream = Files.walk(actualRoot)) {
      fileStream.filter(Files::isRegularFile).map(actualRoot::relativize).forEach(actual::add);
    }

    assertEquals(expected, actual);

  }

}