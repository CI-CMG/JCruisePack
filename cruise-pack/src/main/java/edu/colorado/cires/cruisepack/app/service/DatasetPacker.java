package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.copy;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterHidden;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterTimeSize;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.service.CruiseMetadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.CruiseMetadata.PackageInstrument;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetPacker {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatasetPacker.class);

  private static final String LOCAL_DATA = "local-data";
  private static final String PEOPLE_XML = "people.xml";
  private static final String ORGANIZATIONS_XML = "organizations.xml";



  private static void copyLocalData(PackJob packJob, Path instrumentBagDataDir) {
    Path people = packJob.getCruisePackDataDir().resolve(LOCAL_DATA).resolve(PEOPLE_XML);
    Path organizations = packJob.getCruisePackDataDir().resolve(LOCAL_DATA).resolve(ORGANIZATIONS_XML);
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



  private static Path resolveFinalPath(Path datasetDir, Path sourceDataDir, Path sourceFile, InstrumentDetail dataset) {
    if (InstrumentStatus.RAW == dataset.getStatus() && dataset.isFlatten()){
      return datasetDir.resolve(sourceFile.getFileName());
    }
    return datasetDir.resolve(sourceDataDir.relativize(sourceFile));
  }

  private static void writeMetadata(ObjectMapper objectMapper, CruiseMetadata cruiseMetadata, Path file) {
    try (OutputStream outputStream = Files.newOutputStream(file)) {
      objectMapper.writeValue(outputStream, cruiseMetadata);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to write metadata " + file, e);
    }
  }

  private static Instrument resolveInstrument(CruiseMetadata cruiseMetadata, InstrumentDetail dataset) {
    return cruiseMetadata.getInstruments().stream()
        .filter(instrument -> Paths.get(instrument.getDataPath()).equals(dataset.getDataPath()))
        .findFirst().orElseThrow(() -> new IllegalStateException("Metadata for " + dataset.getDataPath() + " was not found"));
  }

  private static CruiseMetadata getPackageMetadata(CruiseMetadata cruiseMetadata, InstrumentDetail dataset) {
    CruiseMetadata packageMetadata = cruiseMetadata.shallowCopy();
    Map<String, PackageInstrument> packageInstruments = new LinkedHashMap<>();
    Instrument instrument = resolveInstrument(cruiseMetadata, dataset);
    PackageInstrument packageInstrument = new PackageInstrument();
    packageInstrument.setInstrument(instrument);
    packageInstrument.setTypeName(dataset.getShortName());
    packageInstrument.setFlatten(dataset.isFlatten());
    packageInstrument.setExtensions(new ArrayList<>(dataset.getExtensions()));
    packageInstrument.setDirName(dataset.getDirName());
    packageInstrument.setBagName(dataset.getBagName());

    packageInstruments.put(dataset.getDirName(), packageInstrument);

    packageMetadata.setPackageInstruments(packageInstruments);
    return packageMetadata;
  }

  public static void pack(ObjectMapper objectMapper, PackJob packJob) {
    Path mainBagDataDir = packJob.getBagPath().resolve("data").toAbsolutePath().normalize();

    writeMetadata(objectMapper, packJob.getCruiseMetadata(), mainBagDataDir.resolve(packJob.getPackageId() + "-metadata.json"));

    for (List<InstrumentDetail> instruments : packJob.getInstruments().values()) {
      String instrumentBagName = instruments.get(0).getBagName();
      Path instrumentBagRootDir = mainBagDataDir.resolve(instrumentBagName).toAbsolutePath().normalize();

      mkDir(instrumentBagRootDir);

      copyLocalData(packJob, instrumentBagRootDir);

      for (InstrumentDetail dataset : instruments) {
        Path datasetDir = instrumentBagRootDir.resolve(dataset.getDirName());
        Path sourceDataDir = dataset.getDataPath().toAbsolutePath().normalize();

        CruiseMetadata packageMetadata = getPackageMetadata(packJob.getCruiseMetadata(), dataset);
        writeMetadata(objectMapper, packageMetadata, instrumentBagRootDir.resolve(instrumentBagName + "-metadata.json"));


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

//        if (dataset.getCustomHandler() != null) {
//          CustomInstrumentProcessingContext customProcessingContext = new CustomInstrumentProcessingContext(packJob, dataset, mainBagDataDir,
//              datasetBagRootDir);
//
//          dataset.getCustomHandler().accept(customProcessingContext);
//        }
      }

      try {
        BagCreator.bagInPlace(instrumentBagRootDir, Arrays.asList(StandardSupportedAlgorithms.MD5), false);
      } catch (NoSuchAlgorithmException | IOException e) {
        throw new RuntimeException("Unable to create bag: " + instrumentBagRootDir, e);
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