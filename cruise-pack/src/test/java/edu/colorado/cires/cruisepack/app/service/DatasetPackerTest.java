package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class DatasetPackerTest {
//  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//  private Path mainBagRootDir = Paths.get("target/TST200400");
//
//  @BeforeEach
//  public void beforeEach() throws Exception{
//    FileUtils.deleteQuietly(mainBagRootDir.toFile());
//    Files.createDirectories(mainBagRootDir.resolve("data"));
//  }
//
//
//  @Test
//  public void test() throws Exception {
//    CruiseMetadata cruiseMetadata = new CruiseMetadata();
//    List<Instrument> instrumentsMetadata = new ArrayList<>();
//
//
//    Map<String, List<InstrumentDetail>> instruments = new HashMap<>();
//    InstrumentDetail em122Raw = new InstrumentDetail(
//        InstrumentStatus.RAW,
//        "longem122",
//        "EM122",
//        Collections.emptySet(),
//        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122"),
//        false, null);
//    em122Raw.setDirName("EM122");
//    em122Raw.setBagName("TST200400_MB-BATHY_EM122");
//
//    Instrument instrument = new Instrument();
//    instrument.setInstrument(em122Raw.getInstrument());
//    instrument.setShortName(em122Raw.getShortName());
//    instrument.setDataPath(em122Raw.getDataPath().toString());
//    instrumentsMetadata.add(instrument);
//
//    InstrumentDetail em122Processed = new InstrumentDetail(
//        InstrumentStatus.PROCESSED,
//        "longem122",
//        "EM122",
//        Collections.emptySet(),
//        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed"),
//        false, null);
//    em122Processed.setDirName("EM122_processed");
//    em122Processed.setBagName("TST200400_MB-BATHY_EM122");
//
//    instrument = new Instrument();
//    instrument.setInstrument(em122Processed.getInstrument());
//    instrument.setShortName(em122Processed.getShortName());
//    instrument.setDataPath(em122Processed.getDataPath().toString());
//    instrumentsMetadata.add(instrument);
//
//    InstrumentDetail em122Processed1 = new InstrumentDetail(
//        InstrumentStatus.PROCESSED,
//        "longem122",
//        "EM122",
//        Collections.emptySet(),
//        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_processed-1"),
//        false, null);
//    em122Processed1.setDirName("EM122_processed-1");
//    em122Processed1.setBagName("TST200400_MB-BATHY_EM122");
//
//    instrument = new Instrument();
//    instrument.setInstrument(em122Processed1.getInstrument());
//    instrument.setShortName(em122Processed1.getShortName());
//    instrument.setDataPath(em122Processed1.getDataPath().toString());
//    instrumentsMetadata.add(instrument);
//
//    InstrumentDetail em122Products = new InstrumentDetail(
//        InstrumentStatus.PRODUCTS,
//        "longem122",
//        "EM122",
//        Collections.emptySet(),
//        Paths.get("src/test/resources/test-src/TST200400/data/TST200400_MB-BATHY_EM122/data/EM122_products"),
//        false, null);
//    em122Products.setDirName("EM122_products");
//    em122Products.setBagName("TST200400_MB-BATHY_EM122");
//
//    instrument = new Instrument();
//    instrument.setInstrument(em122Products.getInstrument());
//    instrument.setShortName(em122Products.getShortName());
//    instrument.setDataPath(em122Products.getDataPath().toString());
//    instrumentsMetadata.add(instrument);
//
//    instruments.put("MB-BATHY_EM122", Arrays.asList(em122Raw, em122Processed, em122Processed1, em122Products));
//
//    cruiseMetadata.setInstruments(instrumentsMetadata);
//    Files.createDirectories(mainBagRootDir);
//
//
//    PackJob packJob = null;
////    PackJob packJob = new PackJob();
////    packJob.setStartTime(Instant.now());
////    packJob.setPackageId("TST200400");
////    packJob.setMasterBagName("TST200400");
////    packJob.setBagPath(mainBagRootDir);
////    packJob.setInstruments(instruments);
////    packJob.setCruisePackDataDir(Paths.get("../sample-work-dir"));
////    packJob.setCruiseMetadata(cruiseMetadata);
////    private Path docsDir;
////    private Path omicsFile;
//
//
//    DatasetPacker.pack(null, OBJECT_MAPPER, packJob);
//
//
//
//  }
}