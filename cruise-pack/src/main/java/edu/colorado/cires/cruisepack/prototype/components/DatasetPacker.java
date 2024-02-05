package edu.colorado.cires.cruisepack.prototype.components;

import edu.colorado.cires.cruisepack.prototype.bag.Bagger;
import edu.colorado.cires.cruisepack.prototype.components.model.InstrumentDetail;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetPacker {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatasetPacker.class);

  public static void pack(Path mainBagRootDir, Map<String, List<InstrumentDetail>> instruments) throws IOException {
    for (List<InstrumentDetail> datasets : instruments.values()) {
      String bagName = datasets.get(0).getBagName();
      LOGGER.info("Starting to pack {}", bagName);
      Path bagRootDir = mainBagRootDir.resolve("data").resolve(bagName);
      Files.createDirectories(bagRootDir);
//      Bag bag = makeInternalBag(mainBagRootDir, bagName);
      LOGGER.info("Starting packaging {}", bagName);
//      bag_log.info('CruisePack version: %s', self.ui.version)
      for(InstrumentDetail dataset : datasets) {
        LOGGER.info("Starting to pack {}", dataset.getDirName());
        // TODO do we need sqlite? Can we update the version of CruisePack to use a vendor neutral format
        // TODO copy data here

        // TODO file filtering flatening
//        Path destRoot = bagRootDir.resolve("data").resolve(dataset.getDirName()).toAbsolutePath().normalize();
        Path destRoot = bagRootDir.resolve(dataset.getDirName()).toAbsolutePath().normalize();


        Path dataPath = dataset.getDataPath().toAbsolutePath().normalize();

        try(Stream<Path> fileStream = Files.walk(dataPath, FileVisitOption.FOLLOW_LINKS)) {
          fileStream
              .filter(Files::isRegularFile)
              .map(path -> path.toAbsolutePath().normalize())
              .forEach(path -> {
                Path destPath = destRoot.resolve(dataPath.relativize(path));
                try {
                  Files.createDirectories(destPath.getParent());
                  FileUtils.copyFile(path.toFile(), destPath.toFile(), true);
                } catch (IOException e) {
                  throw new RuntimeException("Unable to copy file: " + path, e);
                }
              });
        }

        //TODO copy ancillary data

      }
      try {
        Bagger.bagInPlace(bagRootDir);
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Unable to create bag: " + bagRootDir, e);
      }
    }
  }

//  private static Bag makeInternalBag(Path mainBagRootDir, String bagName) {
//
//    //TODO will there ever be an existing bag
//    try {
//      return Bagger.readOrCreateBag(mainBagRootDir.resolve("data").resolve(bagName));
//    } catch (MaliciousPathException | UnparsableVersionException | UnsupportedAlgorithmException | InvalidBagitFileFormatException | IOException |
//             NoSuchAlgorithmException e) {
//      throw new RuntimeException("Unable to open or create internal bag " + bagName, e);
//    }
//  }

}
