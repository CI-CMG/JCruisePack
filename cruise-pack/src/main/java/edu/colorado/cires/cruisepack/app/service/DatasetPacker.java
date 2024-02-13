package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.prototype.bag.Bagger;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetPacker {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatasetPacker.class);

  private static final String LOCAL_DATA = "local-data";
  private static final String PEOPLE_XML = "people.xml";
  private static final String ORGANIZATIONS_XML = "organizations.xml";

  private static void mkDir(Path dir) {
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create directory " + dir, e);
    }
  }

  private static void copy(Path source, Path target) {
    try {
      Files.copy(source, target);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to copy file " + source + " to " + target, e);
    }
  }

  private static void copyLocalData(PackJob packJob, Path destRoot) {
    Path people = packJob.getLocalDataDir().resolve(LOCAL_DATA).resolve(PEOPLE_XML);
    Path organizations = packJob.getLocalDataDir().resolve(LOCAL_DATA).resolve(ORGANIZATIONS_XML);
    Path localData = destRoot.resolve(LOCAL_DATA);
    if (Files.isRegularFile(people)) {
      mkDir(localData);
      copy(people, localData.resolve(PEOPLE_XML));
    }
    if (Files.isRegularFile(organizations)) {
      mkDir(localData);
      copy(organizations, localData.resolve(ORGANIZATIONS_XML));
    }
  }

  private static boolean filterHidden(Path path) {
    try {
      return Files.isRegularFile(path) && !Files.isHidden(path);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file " + path, e);
    }
  }

  private static boolean filterExtension(Path path, InstrumentDetail dataset) {
    if (!dataset.getExtensions().isEmpty() && InstrumentStatus.RAW == dataset.getStatus()) {
      String ext = FilenameUtils.getExtension(path.getFileName().toString());
      return dataset.getExtensions().contains(ext);
    }
    return true;
  }

  private static boolean filterTimeSize(Path source, Path target) {
    try {
      return !Files.isRegularFile(target) ||
          Files.getLastModifiedTime(source).toMillis() != Files.getLastModifiedTime(target).toMillis() ||
          Files.size(source) != Files.size(target);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file " + source + " or " + target, e);
    }
  }

  private static Path resolveFinalPath(Path destRoot, Path dataPath, Path path, InstrumentDetail dataset) {
    if (InstrumentStatus.RAW == dataset.getStatus() && dataset.isFlatten()){
      return destRoot.resolve(path.getFileName());
    }
    return destRoot.resolve(dataPath.relativize(path));
  }

  public static void pack(PackJob packJob) throws IOException {
    for (List<InstrumentDetail> datasets : packJob.getInstruments().values()) {
      String bagName = datasets.get(0).getBagName();
      LOGGER.info("Starting to pack {}", bagName);
      Path bagRootDir = packJob.getBagPath().resolve("data").resolve(bagName);
      Files.createDirectories(bagRootDir);
//      Bag bag = makeInternalBag(mainBagRootDir, bagName);
      LOGGER.info("Starting packaging {}", bagName);
//      bag_log.info('CruisePack version: %s', self.ui.version)
      for (InstrumentDetail dataset : datasets) {
        LOGGER.info("Starting to pack {}", dataset.getDirName());

        // TODO file filtering flatening
//        Path destRoot = bagRootDir.resolve("data").resolve(dataset.getDirName()).toAbsolutePath().normalize();
        Path destRoot = bagRootDir.resolve(dataset.getDirName()).toAbsolutePath().normalize();
        copyLocalData(packJob, destRoot);

        Path dataPath = dataset.getDataPath().toAbsolutePath().normalize();

        Files.walkFileTree(dataPath, new SimpleFileVisitor<>() {

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
            if (Files.isHidden(dir)) {
              return FileVisitResult.SKIP_SUBTREE;
            }
            return super.preVisitDirectory(dir, attr);
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
            Path path = file.toAbsolutePath().normalize();
            Path destPath = resolveFinalPath(destRoot, dataPath, path, dataset);
            if (filterHidden(file) && filterExtension(file, dataset) && filterTimeSize(path, destPath)) {
              try {
                Files.createDirectories(destPath.getParent());
                FileUtils.copyFile(path.toFile(), destPath.toFile(), true);
              } catch (IOException e) {
                throw new RuntimeException("Unable to copy file: " + path, e);
              }
            }
            return super.visitFile(file, attr);
          }
        });

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
