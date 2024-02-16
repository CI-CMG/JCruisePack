package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class DatasetNameResolverTest {

  @Test
  public void test() throws Exception {
    String mainName = "TST200400";
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> instruments = new HashMap<>();
    InstrumentDetailPackageKey key = new InstrumentDetailPackageKey("MB-BATHY", "EM122");
    instruments.put(key, Arrays.asList(
        new InstrumentNameHolder("longem122", "EM122", InstrumentStatus.RAW,
            Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122")),
        new InstrumentNameHolder("longem122", "EM122", InstrumentStatus.PROCESSED,
            Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed")),
        new InstrumentNameHolder("longem122", "EM122", InstrumentStatus.PROCESSED,
            Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1")),
        new InstrumentNameHolder("longem122", "EM122", InstrumentStatus.PRODUCTS,
            Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"))
        ));

    DatasetNameResolver.setDirNamesOnInstruments(mainName, instruments);

    assertEquals(
        Arrays.asList("EM122", "EM122_processed", "EM122_processed-1", "EM122_products"),
        instruments.get(key).stream().map(InstrumentNameHolder::getDirName).collect(Collectors.toList())
    );

    assertEquals(
        Arrays.asList("TST200400_MB-BATHY_EM122", "TST200400_MB-BATHY_EM122", "TST200400_MB-BATHY_EM122", "TST200400_MB-BATHY_EM122"),
        instruments.get(key).stream().map(InstrumentNameHolder::getBagName).collect(Collectors.toList())
    );
  }
}