package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.copy;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterHidden;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterTimeSize;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetPacker {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatasetPacker.class);

  private static final String LOCAL_DATA = "local-data";
  private static final String PEOPLE_XML = "people.xml";
  private static final String ORGANIZATIONS_XML = "organizations.xml";


  private static void copyLocalData(ServiceProperties serviceProperties, PackJob packJob, Path instrumentBagDataDir) {
    Path systemLocalData = Paths.get(serviceProperties.getWorkDir()).resolve(LOCAL_DATA);
    Path people = systemLocalData.resolve(PEOPLE_XML);
    Path organizations = systemLocalData.resolve(ORGANIZATIONS_XML);
    Path localData = instrumentBagDataDir.resolve(LOCAL_DATA);
    if (Files.isRegularFile(people)) {
      mkDir(localData);
      copy(people, localData.resolve(PEOPLE_XML));
    }
    if (Files.isRegularFile(organizations)) {
      mkDir(localData);
      copy(organizations, localData.resolve(ORGANIZATIONS_XML));
    }
  }


  private static boolean filterExtension(Path path, InstrumentDetail dataset) {
    if (!dataset.getExtensions().isEmpty() && InstrumentStatus.RAW == dataset.getStatus()) {
      String ext = FilenameUtils.getExtension(path.getFileName().toString());
      return dataset.getExtensions().contains(ext);
    }
    return true;
  }

  private static Path resolveFinalPath(Path datasetDir, Path sourceDataDir, Path sourceFile) {
    return datasetDir.resolve(sourceDataDir.relativize(sourceFile));
  }

  private static Path resolveFinalPath(Path datasetDir, Path sourceDataDir, Path sourceFile, InstrumentDetail dataset) {
    if (InstrumentStatus.RAW == dataset.getStatus() && dataset.isFlatten()) {
      return datasetDir.resolve(sourceFile.getFileName());
    }
    return datasetDir.resolve(sourceDataDir.relativize(sourceFile));
  }


  public static void pack(ServiceProperties serviceProperties, PackJob packJob, MetadataService metadataService) {
    Path mainBagDataDir = packJob.getPackageDirectory().resolve(packJob.getPackageId()).toAbsolutePath().normalize();

    CruiseMetadata cruiseMetadata = metadataService.createMetadata(packJob);
    metadataService.writeMetadata(cruiseMetadata, mainBagDataDir.resolve(packJob.getPackageId() + "-metadata.json"));

    for (List<InstrumentDetail> instruments : packJob.getInstruments().values()) {
      String instrumentBagName = instruments.get(0).getBagName();
      Path instrumentBagRootDir = mainBagDataDir.resolve(instrumentBagName).toAbsolutePath().normalize();

      mkDir(instrumentBagRootDir);
      copyLocalData(serviceProperties, packJob, instrumentBagRootDir);

      for (InstrumentDetail dataset : instruments) {
        Path datasetDir = instrumentBagRootDir.resolve(dataset.getDirName());

        CruiseMetadata packageMetadata = metadataService.getPackageMetadata(cruiseMetadata, dataset);
        metadataService.writeMetadata(packageMetadata, instrumentBagRootDir.resolve(instrumentBagName + "-metadata.json"));

        copyMainDatasetFiles(datasetDir, dataset);
        dataset.getAdditionalFiles().forEach(additionalFile -> copyAdditionalFiles(datasetDir, additionalFile));
      }

      try {
        BagCreator.bagInPlace(instrumentBagRootDir, Arrays.asList(StandardSupportedAlgorithms.MD5), false);
      } catch (NoSuchAlgorithmException | IOException e) {
        throw new RuntimeException("Unable to create bag: " + instrumentBagRootDir, e);
      }

    }
  }

  private static void copyMainDatasetFiles(Path datasetDir, InstrumentDetail dataset) {
    Path sourceDataDir = dataset.getDataPath().toAbsolutePath().normalize();
    try {
      Files.walkFileTree(sourceDataDir, new SimpleFileVisitor<>() {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
          if (Files.isHidden(dir)) {
            return FileVisitResult.SKIP_SUBTREE;
          }
          return super.preVisitDirectory(dir, attr);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
          Path sourceFile = file.toAbsolutePath().normalize();
          Path targetFile = resolveFinalPath(datasetDir, sourceDataDir, sourceFile, dataset);
          if (filterHidden(sourceFile) && filterExtension(sourceFile, dataset) && filterTimeSize(sourceFile, targetFile)) {
            mkDir(targetFile.getParent());
            copy(sourceFile, targetFile);
          }
          return super.visitFile(file, attr);
        }
      });
    } catch (IOException e) {
      throw new IllegalStateException("Unable to process files " + sourceDataDir, e);
    }
  }

  private static void copyAdditionalFiles(Path datasetDir, AdditionalFiles additionalFile) {
    Path sourceDataPath = additionalFile.getSourceFileOrDirectory().toAbsolutePath().normalize();
    Path resolvedDatasetDir = datasetDir.resolve(additionalFile.getRelativeDestinationDirectory()).toAbsolutePath().normalize();
    try {
      if (Files.isDirectory(sourceDataPath)) {
        Files.walkFileTree(sourceDataPath, new SimpleFileVisitor<>() {

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
            if (Files.isHidden(dir)) {
              return FileVisitResult.SKIP_SUBTREE;
            }
            return super.preVisitDirectory(dir, attr);
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
            Path sourceFile = file.toAbsolutePath().normalize();
            Path targetFile = resolveFinalPath(resolvedDatasetDir, sourceDataPath, sourceFile);
            if (filterHidden(sourceFile) && filterTimeSize(sourceFile, targetFile)) {
              mkDir(targetFile.getParent());
              copy(sourceFile, targetFile);
            }
            return super.visitFile(file, attr);
          }
        });
      } else if (Files.isRegularFile(sourceDataPath) && !Files.isHidden(sourceDataPath)) {
        Path targetFile = resolveFinalPath(resolvedDatasetDir, sourceDataPath, sourceDataPath);
        if (filterTimeSize(sourceDataPath, targetFile)) {
          mkDir(targetFile.getParent());
          copy(sourceDataPath, targetFile);
        }
      } else {
        throw new IllegalStateException("Unable to read " + sourceDataPath);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Unable to process files " + sourceDataPath, e);
    }
  }

}
