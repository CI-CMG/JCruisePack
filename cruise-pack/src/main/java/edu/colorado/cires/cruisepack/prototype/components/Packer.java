package edu.colorado.cires.cruisepack.prototype.components;

import edu.colorado.cires.cruisepack.app.service.DatasetNameResolver;
import edu.colorado.cires.cruisepack.app.service.DatasetPacker;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetail;
import gov.loc.repository.bagit.domain.Bag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packer {

//  private static final Logger LOGGER = LoggerFactory.getLogger(Packer.class);
//
//  private final Map<String, List<InstrumentDetail>> instruments;
//  // Nullable
//  private final Path docsDir;
//  // Nullable
//  private final Path omicsFile;
//  private final String mainName;
//  private final Bag mainBag;
//
//  public Packer(Map<String, List<InstrumentDetail>> instruments, /*Nullable*/ Path docsDir, /*Nullable*/ Path omicsFile, String mainName, Bag mainBag) {
//    this.instruments = instruments;
//    this.docsDir = docsDir;
//    this.omicsFile = omicsFile;
//    this.mainName = mainName;
//    this.mainBag = mainBag;
//  }
//
//  public void run() throws IOException {
//    DatasetNameResolver.setDirNamesOnInstruments(mainName, instruments);
//    rawCheck();
//    copyDocs();
//    copyOmics();
////    DatasetPacker.pack(mainBag.getRootDir(), instruments);
//  }
//
//  private void rawCheck() {
//    //TODO
//  }
//
//  private void copyDocs() throws IOException {
//    // TODO copy prep?
//    if (docsDir != null) {
//      AtomicInteger count = new AtomicInteger(0);
//      try(Stream<Path> fileStream = Files.list(docsDir)) {
//        fileStream
//            .map(Path::toFile)
//            .forEach(file -> {
//              try {
//                Path data = mainBag.getRootDir().resolve("data");
//                Files.createDirectories(data);
//                if (file.isDirectory()) {
//                  FileUtils.copyDirectoryToDirectory(file, data.toFile());
//                } else {
//                  FileUtils.copyFileToDirectory(file, data.toFile());
//                }
//                count.incrementAndGet();
//              } catch (IOException e) {
//                throw new RuntimeException("Unable to move file for re-bagging:  " + file, e);
//              }
//            });
//      }
//      LOGGER.info("{} document files copied", count.get());
//    }
//  }
//
//  private void copyOmics() throws IOException {
//    if (omicsFile != null) {
//      Path omicsDir = mainBag.getRootDir().resolve("data").resolve("omics");
//      Files.createDirectories(omicsDir);
//      FileUtils.copyFileToDirectory(omicsFile.toFile(), omicsDir.toFile());
//    }
//  }

}
