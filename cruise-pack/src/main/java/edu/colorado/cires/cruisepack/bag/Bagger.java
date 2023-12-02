package edu.colorado.cires.cruisepack.bag;

import gov.loc.repository.bagit.conformance.BagLinter;
import gov.loc.repository.bagit.conformance.BagitWarning;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.exceptions.CorruptChecksumException;
import gov.loc.repository.bagit.exceptions.FileNotInPayloadDirectoryException;
import gov.loc.repository.bagit.exceptions.InvalidBagitFileFormatException;
import gov.loc.repository.bagit.exceptions.MaliciousPathException;
import gov.loc.repository.bagit.exceptions.MissingBagitFileException;
import gov.loc.repository.bagit.exceptions.MissingPayloadDirectoryException;
import gov.loc.repository.bagit.exceptions.MissingPayloadManifestException;
import gov.loc.repository.bagit.exceptions.UnparsableVersionException;
import gov.loc.repository.bagit.exceptions.UnsupportedAlgorithmException;
import gov.loc.repository.bagit.exceptions.VerificationException;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.reader.BagReader;
import gov.loc.repository.bagit.verify.BagVerifier;
import gov.loc.repository.bagit.writer.BagWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;

public class Bagger {

  public static Bag readOrCreateBag(Path folder)
      throws MaliciousPathException, UnparsableVersionException, UnsupportedAlgorithmException, InvalidBagitFileFormatException, IOException, NoSuchAlgorithmException {
    Path bagitFile = folder.resolve("bagit.txt");
    if (Files.exists(bagitFile)) {
      return readBag(folder);
    }
    return bagInPlace(folder);
  }

  public static Bag readBag(Path folder)
      throws MaliciousPathException, UnparsableVersionException, UnsupportedAlgorithmException, InvalidBagitFileFormatException, IOException {
    BagReader bagReader = new BagReader();
    return bagReader.read(folder);
  }

  public static Bag bagInPlace(Path folder) throws NoSuchAlgorithmException, IOException {
    StandardSupportedAlgorithms algorithm = StandardSupportedAlgorithms.MD5;
    boolean includeHiddenFiles = false;
    return BagCreator.bagInPlace(folder, Arrays.asList(algorithm), includeHiddenFiles);
  }

  public static Bag reBag(Bag bag) throws NoSuchAlgorithmException, IOException {
    // TODO is there a more efficient way to do this?
    try(Stream<Path> fileStream = Files.list(bag.getRootDir())) {
      fileStream
          .filter(p -> !p.getFileName().toString().equals("data"))
          .map(Path::toFile)
          .forEach(FileUtils::deleteQuietly);
    }
    Path dataDir = bag.getRootDir().resolve("data");
    if(Files.isDirectory(dataDir)){
      try(Stream<Path> fileStream = Files.list(dataDir)) {
        fileStream
            .map(Path::toFile)
            .forEach(file -> {
              try {
                if (file.isDirectory()) {
                  FileUtils.moveDirectoryToDirectory(file, bag.getRootDir().toFile(), false);
                } else {
                  FileUtils.moveFileToDirectory(file, bag.getRootDir().toFile(), false);
                }
              } catch (IOException e) {
                throw new RuntimeException("Unable to move file for re-bagging:  " + file, e);
              }
            });
        Files.delete(dataDir);
      }
    }
    return bagInPlace(bag.getRootDir());
  }


  public static void bagIt()
      throws NoSuchAlgorithmException, IOException, MaliciousPathException, UnparsableVersionException, UnsupportedAlgorithmException, InvalidBagitFileFormatException, MissingPayloadManifestException, MissingPayloadDirectoryException, FileNotInPayloadDirectoryException, InterruptedException, MissingBagitFileException, CorruptChecksumException, VerificationException {
    Path folder = Paths.get("FolderYouWantToBag");
    StandardSupportedAlgorithms algorithm = StandardSupportedAlgorithms.MD5;
    boolean includeHiddenFiles = false;
    Bag bag = BagCreator.bagInPlace(folder, Arrays.asList(algorithm), includeHiddenFiles);

    Path outputDir = Paths.get("WhereYouWantToWriteTheBagTo");
    BagWriter.write(bag, outputDir); //where bag is a Bag object

    boolean ignoreHiddenFiles = true;
    BagVerifier verifier = new BagVerifier();
    verifier.isValid(bag, ignoreHiddenFiles);
    verifier.isComplete(bag, ignoreHiddenFiles);

    Path rootDir = Paths.get("RootDirectoryOfExistingBag");
    Set<BagitWarning> warnings = BagLinter.lintBag(rootDir, Collections.emptyList());
  }

}
