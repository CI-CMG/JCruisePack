package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DatasetNameResolverTest {

  @Test
  public void test() throws Exception {
    String mainName = "TST200400";
    Map<String, List<InstrumentDetail>> instruments = new HashMap<>();

    InstrumentDetail em122Raw = new InstrumentDetail(
        InstrumentStatus.RAW,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122"),
        flatten);

    InstrumentDetail em122Processed = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed"),
        flatten);

    InstrumentDetail em122Processed1 = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1"),
        flatten);

    InstrumentDetail em122Products = new InstrumentDetail(
        InstrumentStatus.PRODUCTS,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"),
        flatten);

    instruments.put("MB-BATHY_EM122", Arrays.asList(em122Raw, em122Processed, em122Processed1, em122Products));

    DatasetNameResolver.setDirNamesOnInstruments(mainName, instruments);

    Map<String, List<InstrumentDetail>> expected = new HashMap<>();
    InstrumentDetail em122RawExpected = new InstrumentDetail(
        InstrumentStatus.RAW,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122"),
        flatten);
    em122RawExpected.setDirName("EM122");
    em122RawExpected.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122ProcessedExpected = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed"),
        flatten);
    em122ProcessedExpected.setDirName("EM122_processed");
    em122ProcessedExpected.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122Processed1Expected = new InstrumentDetail(
        InstrumentStatus.PROCESSED,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1"),
        flatten);
    em122Processed1Expected.setDirName("EM122_processed-1");
    em122Processed1Expected.setBagName("TST200400_MB-BATHY_EM122");

    InstrumentDetail em122ProductsExpected = new InstrumentDetail(
        InstrumentStatus.PRODUCTS,
        "longem122",
        "EM122",
        Collections.emptySet(),
        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"),
        flatten);
    em122ProductsExpected.setDirName("EM122_products");
    em122ProductsExpected.setBagName("TST200400_MB-BATHY_EM122");

    expected.put("MB-BATHY_EM122", Arrays.asList(em122RawExpected, em122ProcessedExpected, em122Processed1Expected, em122ProductsExpected));

    assertEquals(expected, instruments);
  }
}