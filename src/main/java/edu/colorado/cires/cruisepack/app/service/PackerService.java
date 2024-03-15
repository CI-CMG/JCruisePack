package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.io.PackerFileController;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.PackageInstrument;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.model.PackStateModel;
import edu.colorado.cires.cruisepack.xml.person.Person;
import gov.loc.repository.bagit.domain.Metadata;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PackerService.class);

  private static final String LOCAL_DATA = "local-data";
  private static final String PEOPLE_XML = "people.xml";
  private static final String ORGANIZATIONS_XML = "organizations.xml";

  private final ServiceProperties serviceProperties;
  private final PackagingValidationService validationService;
  private final FooterControlController footerControlController;
  private final PackStateModel packStateModel;
  private final PackerFileController packerFileController;
  private final InstrumentDatastore instrumentDatastore;

  @Autowired
  public PackerService(
      ServiceProperties serviceProperties,
      PackagingValidationService validationService,
      FooterControlController footerControlController,
      PackStateModel packStateModel, PackerFileController packerFileController, InstrumentDatastore instrumentDatastore
  ) {
    this.serviceProperties = serviceProperties;
    this.validationService = validationService;
    this.footerControlController = footerControlController;
    this.packStateModel = packStateModel;
    this.packerFileController = packerFileController;
    this.instrumentDatastore = instrumentDatastore;
  }

  public void startPacking() {
    footerControlController.setPackageButtonEnabled(false);
    footerControlController.setSaveButtonEnabled(false);
    footerControlController.setStopButtonEnabled(true);
    try {
      Optional<PackJob> maybePackJob = validationService.validate();
      if (maybePackJob.isPresent()) {
        startPackingThread(maybePackJob.get());
      } else {
        footerControlController.setPackageButtonEnabled(true);
        footerControlController.setSaveButtonEnabled(true);
        footerControlController.setStopButtonEnabled(false);
      }
    } catch (Exception e) {
      footerControlController.setPackageButtonEnabled(true);
      footerControlController.setSaveButtonEnabled(true);
      footerControlController.setStopButtonEnabled(false);
      LOGGER.error("An error occurred while initiating pack job", e);
    }
  }

  private void startPackingThread(PackJob packJob) {
    // TODO put in queue?
    new Thread(() -> {
      try {
////    rawCheck(packJob); //TODO add to validation phase
        PackJob packJobWithAncillaryInstruments = addAncillaryDataToPackJob(packJob);
        packStateModel.setProcessing(true);
        packStateModel.setProgressIncrement(100f / getTotalFiles(packJobWithAncillaryInstruments));
        resetBagDirs(packJobWithAncillaryInstruments);
        copyDocs(packJobWithAncillaryInstruments);
        copyOmics(packJobWithAncillaryInstruments);
        packData(packJobWithAncillaryInstruments);
        packMainBag(packJobWithAncillaryInstruments);
      } catch (Exception e) {
        LOGGER.error("An error occurred while packing", e);
      } finally {
        footerControlController.setPackageButtonEnabled(true);
        footerControlController.setSaveButtonEnabled(true);
        footerControlController.setStopButtonEnabled(false);
        packStateModel.setProcessing(false);
      }
    }).start();
  }
  
  public long getTotalFiles(PackJob packJob) {
    long totalFiles = 0;

    totalFiles += packerFileController.getFileCount(packJob.getOmicsSampleTrackingSheetPath());
    totalFiles += packerFileController.getFileCount(packJob.getDocumentsPath());
    totalFiles += packJob.getInstruments().values().stream()
        .flatMap(List::stream)
        .map(instrumentDetail ->
            instrumentDetail.getAdditionalFiles().stream()
                .map(AdditionalFiles::getSourceFileOrDirectory)
                .map(packerFileController::getFileCount)
                .mapToLong(Long::valueOf)
                .sum() +
                packerFileController.getFileCount(instrumentDetail.getDataPath()) +
                packerFileController.getFileCount(instrumentDetail.getAncillaryDataPath())
        )
        .mapToLong(Long::valueOf)
        .sum();
    
    return totalFiles;
  }

  private void packMainBag(PackJob packJob) {
    Path mainBagPath = packJob.getPackageDirectory().resolve(packJob.getPackageId());
    try {
      packerFileController.bagInPlace(
          mainBagPath,
          createMetadataFromPackJob(packJob),
          Collections.emptyList() // intentionally empty. Child bags have already had checksums generated. Checksums for the main bag need to be generated manually
      );
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new IllegalStateException("Unable to create main bag, ", e);
    }
    
    Path manifestFile = mainBagPath.resolve("manifest-sha256.txt");
    try (FileWriter fileWriter = new FileWriter(manifestFile.toFile(), StandardCharsets.UTF_8, true)) {

      try (Stream<Path> paths = Files.walk(mainBagPath)) {
        paths.filter(p -> p.toFile().isFile())
            .filter(p -> p.toString().endsWith("sha256.txt"))
            .filter(p -> !p.equals(manifestFile))
            .forEach(p -> packerFileController.concatManifests(p, mainBagPath, fileWriter));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      Path omicsPath = mainBagPath.resolve("data").resolve("omics");
      if (omicsPath.toFile().exists()) {
        packerFileController.appendToManifest(omicsPath, mainBagPath, fileWriter);
      }

      Path docsPath = mainBagPath.resolve("data").resolve("docs");
      if (docsPath.toFile().exists()) {
        packerFileController.appendToManifest(docsPath, mainBagPath, fileWriter);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Path tagManifestFile = mainBagPath.resolve("tagmanifest-sha256.txt");
    try (FileWriter tagManifestWriter = new FileWriter(tagManifestFile.toFile(), StandardCharsets.UTF_8, true)) {
      packerFileController.appendToManifest(mainBagPath.resolve("bag-info.txt"), mainBagPath, tagManifestWriter);
      packerFileController.appendToManifest(mainBagPath.resolve("bagit.txt"), mainBagPath, tagManifestWriter);
      packerFileController.appendToManifest(manifestFile, mainBagPath, tagManifestWriter);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void resetBagDirs(PackJob packJob) {
    Path bagDirectory = packJob.getPackageDirectory().resolve(packJob.getPackageId());
    if (!bagDirectory.toFile().exists()) {
      packerFileController.mkDir(bagDirectory.resolve(packJob.getPackageId()));
    } else {
      packerFileController.cleanDir(bagDirectory); // TODO test
    }
  }

  /*
  def run(self):
        """Main method running the packaging show in the thread.

        """
        # Get the names off all the instrument package directory names.
        self.get_dir_names()

        # Check if there are more than one raw datasets specified for any of
        # the instruments. Normally having more than one makes no sense but
        # CruisePack can handle multiple raw datasets from the same instrument.
        proceed = self.raw_check()
        if not proceed:
            return

        # Open main manifest file for appending with instrument level files.
        self.main_manifest = open(self.main_bag.manifest_file, 'w')

        # If a docs path was entered, copy documents into main bag data
        # directory.
        if self.ui.docs_path.text():
            try:
                self.copy_docs()
            except Exception as e:
                self.packing_error(IOError(f' Error "{e}" occurred trying to '
                                           f'copy document files'))
                return

        # If an omics tracking sheet path is specified, copy into main bad
        # data directory
        if self.ui.omics.tracking_path.text():
            self.copy_omics()

        # Kick off instrument data packaging.
        try:
            self.pack_data()
        except Exception as e:
            self.packing_error(IOError(f' Error "{e}" occurred while packing '
                                       f'data,'))
            return

        # Close the main manifest file after packing is complete.
        self.main_manifest.close()
   */

//  private void rawCheck(PackJob packJob) {
//    // TODO this check needs to be done up front before we get here
//    throw new UnsupportedOperationException();
//  }

  /*
      def raw_check(self):
        """Check for multiple raw datasets for an instrument.

        Check for multiple raw datasets for an instrument. If there are
        multiples, emit the multi_raw signal that the main UI picks up and
        prompts user to decide whether the proceed with packaging.

        Returns(bool): True means multiple raw datasets exists, otherwise
        False.

        """
        # Populate dictionary with True for each type + instrument with more
        # than 1 raw dataset.
        multi_raw = {}
        for package, details in self.instruments.items():
            if len(details) > 1:
                for dataset in details:
                    if dataset['status'] == 'Raw':
                        name = '{0} {1}'.format(dataset['type'],
                                                dataset['instrument'])
                        if name in [*multi_raw]:
                            multi_raw[name] = True
                        else:
                            multi_raw[name] = False

        # If there are, construct dialog box to warn user.
        if True in multi_raw.values():
            message = ('There are more than one raw datasets for the '
                       'following instruments:\n')
            for instrument, value in multi_raw.items():
                if value:
                    message = '{0}   {1}\n'.format(message, instrument)
            message = message + '\nIf this is deliberate, click "Yes" to ' \
                                'continue. Otherwise click "No" to stop ' \
                                'packaging so you can fix the issues.'

            # Pause packaging and wait for a response from the user via the
            # main UI. self.ui.packing will become true if use wants to
            # proceed. Otherwise an interruption request will be sent from main
            # UI.
            self.ui.packing = False
            self.signals.multi_raw.emit(message)
            while not self.ui.packing:
                if self.isInterruptionRequested():
                    return False
                pass

        return True
   */

  private void copyDocs(PackJob packJob) {
    if (packJob.getDocumentsPath() != null) {
      Path docsDir = packJob.getDocumentsPath().toAbsolutePath().normalize();
      Path targetDocs = packJob.getPackageDirectory().resolve(packJob.getPackageId()).resolve("docs").toAbsolutePath().normalize();
      try {
        Files.walkFileTree(docsDir, new SimpleFileVisitor<>() {

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
            Path targetFile = targetDocs.resolve(docsDir.relativize(sourceFile));
            if (packerFileController.filterHidden(sourceFile)) {
              if (packerFileController.filterTimeSize(sourceFile, targetFile)) {
                packerFileController.mkDir(targetFile.getParent());
                packerFileController.copy(sourceFile, targetFile);
              }
              packStateModel.incrementProgress();
            }
            return super.visitFile(file, attr);
          }
        });
      } catch (IOException e) {
        throw new IllegalStateException("Unable to process documents " + docsDir, e);
      }
    }
  }

  /*
      def copy_docs(self):
        """Copy document files to main bag.

        Copy documents into main package's data directory. Subdirectories in
        teh path specified in the docs path are preserved on copy.

        """
        docs_dir = os.path.normpath(self.ui.docs_path.text())

        file_list = self.copy_prep(docs_dir, self.main_bag, dir_name='docs')

        for file in file_list:
            hash_value = self.file_packerFileController.copy(file[0], file[1])
            path = os.path.relpath(file[1], self.main_bag.bag)
            path = path.replace('\\', '/')  # Set as Unix separator
            self.main_manifest.write('{0}  {1}\n'.format(hash_value, path))
        log.info(f'{len(file_list)} document files copied')
   */

  private void copyOmics(PackJob packJob) {
    if (packJob.getOmicsSampleTrackingSheetPath() != null) {
      Path omicsFile = packJob.getOmicsSampleTrackingSheetPath().toAbsolutePath().normalize();
      Path omicsDir = packJob.getPackageDirectory().resolve(packJob.getPackageId()).resolve("omics").toAbsolutePath().normalize();
      Path targetFile = omicsDir.resolve(omicsFile.getFileName());
      if (packerFileController.filterHidden(omicsFile)) {
        packerFileController.mkDir(omicsDir);
        packerFileController.copy(omicsFile, targetFile);
        packStateModel.incrementProgress();
      }
    }
  }
  /*
      def copy_omics(self):
        """Copy omics data to bag.

        """
        try:
            omics_dest = os.path.join(self.main_bag.data_directory, 'omics')
            self.make_dir(omics_dest)

            source_file = self.ui.omics.tracking_path.text()
            dest_file = os.path.join(omics_dest, os.path.basename(source_file))

            hash_value = self.file_packerFileController.copy(source_file, dest_file)
            path = os.path.relpath(dest_file, self.main_bag.bag)
            path = path.replace('\\', '/')  # Set as Unix separator
            self.main_manifest.write('{0}  {1}\n'.format(hash_value, path))
            log.info('Omics tracking sheet copied')

        except Exception as e:
            raise IOError(f' Error "{e}" occurred trying to copy omics data')
   */

  private void packData(PackJob packJob) {
    Path mainBagDataDir = packJob.getPackageDirectory().resolve(packJob.getPackageId()).toAbsolutePath().normalize();

    CruiseMetadata cruiseMetadata = packerFileController.createAndWriteCruiseMetadata(
        packJob,
        mainBagDataDir.resolve(packJob.getPackageId() + "-metadata.json")
    );

    for (List<InstrumentDetail> instruments : packJob.getInstruments().values()) {
      String instrumentBagName = instruments.get(0).getBagName();
      Path instrumentBagRootDir = mainBagDataDir.resolve(instrumentBagName).toAbsolutePath().normalize();

      packerFileController.mkDir(instrumentBagRootDir);

      boolean bagContainsData = false;
      for (InstrumentDetail dataset : instruments) {
        Path datasetDir = instrumentBagRootDir.resolve(dataset.getDirName());

        copyMainDatasetFiles(datasetDir, dataset);
        dataset.getAdditionalFiles().forEach(additionalFile -> copyAdditionalFiles(datasetDir, additionalFile));
        
        bagContainsData = datasetDir.toFile().exists();
      }

      if (bagContainsData) {
        copyLocalData(serviceProperties, instrumentBagRootDir);
        CruiseMetadata datasetMetadata = packerFileController.createAndWriteDatasetMetadata(
            cruiseMetadata,
            instruments,
            instrumentBagRootDir.resolve(instrumentBagName + "-metadata.json")
        );

        try {
          packerFileController.bagInPlace(
              instrumentBagRootDir,
              createMetadataFromCruiseMetadata(datasetMetadata),
              Collections.singletonList(StandardSupportedAlgorithms.SHA256)
          );
        } catch (NoSuchAlgorithmException | IOException e) {
          throw new RuntimeException("Unable to create bag: " + instrumentBagRootDir, e);
        }
      } else {
        try {
          FileUtils.deleteDirectory(instrumentBagRootDir.toFile());
        } catch (IOException e) {
          throw new IllegalStateException("Unable to delete empty bag: " + instrumentBagRootDir, e);
        }
      }
    }
  }
  
  private PackJob addAncillaryDataToPackJob(PackJob packJob) {
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> packJobInstruments = new HashMap<>(packJob.getInstruments());
    for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : packJob.getInstruments().entrySet()) {
      List<InstrumentDetail> ancillaryDetails = new ArrayList<>(0);
      String ancillaryGroupShortType = "ANCILLARY";
      for (InstrumentDetail instrumentDetail : entry.getValue()) {
        Path ancillaryPath = instrumentDetail.getAncillaryDataPath();
        if (ancillaryPath != null) {
          String ancillaryInstrumentName = String.format(
              "%s Ancillary",
              instrumentDatastore.getNameForShortCode(entry.getKey().getInstrumentGroupShortType())
          );
          instrumentDatastore.getInstrumentFromShortGroupTypeAndInstrumentName(ancillaryGroupShortType, ancillaryInstrumentName).ifPresent(
              referenceInstrument -> ancillaryDetails.add(
                  InstrumentDetail.builder()
                      .setInstrument(ancillaryInstrumentName)
                      .setStatus(instrumentDetail.getStatus())
                      .setDataPath(ancillaryPath)
                      .setDataComment(instrumentDetail.getAncillaryDataDetails())
                      .setReleaseDate(instrumentDetail.getReleaseDate())
                      .setShortName(referenceInstrument.getShortName())
                      .setBagName(String.format(
                          "%s_ANCILLARY_%s",
                          packJob.getCruiseId(), referenceInstrument.getShortName()
                      )).setDirName(referenceInstrument.getShortName())
                      .build()
              )
          );
        }
      }

      if (!ancillaryDetails.isEmpty()) {
        packJobInstruments.put(
            new InstrumentDetailPackageKey(
                ancillaryGroupShortType,
                ancillaryDetails.get(0).getShortName()
            ),
            ancillaryDetails
        );
      }
    }
    
    return PackJob.builder(packJob)
        .setInstruments(packJobInstruments)
        .build();
  }

  /*
   def pack_data(self):
        """Main method to pack data.

        This method packs the data. It loops through all the datasets for a
        all the instruments. Make a new bag for the instrument and then loop
        through all the datasets for that instrument. Create an instance of
        the FileOps thread classes, handles logging and other aspects of
        bagging.

        """
        for datasets in self.instruments.values():
            start_time = QtCore.QDateTime.currentDateTime()
            bag_name = datasets[0]['bag_name']
            log.info('Starting to pack %s', bag_name)

            bag = self.make_bag(datasets[0])
            self.bag = bag

            # Wire signals.
            bag.signals.update.connect(self.update_forward)

            # We need to keep entries in an existing manifest to preserve
            # checksum values for files that were copied in an earlier run.
            # Read the current manifest into a dictionary stored at
            # instrument.bag.manifest_contents.
            bag.read_manifest_to_dict()

            # Get logging going for this instrument package.
            tools.reset_logging(bag_log, bag.bag, bag_name)
            bag_log.info('Starting packaging %s', bag_name)
            bag_log.info('CruisePack version: %s', self.ui.version)

            # Loop through the datasets for this instrument.
            self.ui.cruise_metadata['package_instruments'] = {}
            for dataset in datasets:
                self.dataset = dataset
                bag_log.info('Starting to pack %s', dataset['dir_name'])

                self.copy_local(bag)

                self.copy_dataset()

                tools.export_metadata(bag, self.ui.cruise_metadata, dataset)

                # Append instrument bag manifest contents to main manifest
                for item, checksum in bag.manifest_contents.items():
                    full_path = os.path.join('data', bag_name, item)
                    full_path = full_path.replace('\\', '/')
                    self.main_manifest.write('{0}  {1}\n'.format(checksum,
                                                                 full_path))
                bag_log.info('Packing %s complete', dataset['dir_name'])

            bag.write_tagmanifest()
            bag.write_baginfo(self.ui.cruise_metadata,
                              dataset['instrument'])

            # Calculate and log execution time.
            now = QtCore.QDateTime.currentDateTime()
            total_time = start_time.secsTo(now)
            total_time = total_time / 60
            bag_log.info('Execution time %s minutes', total_time)

            # Update main log and update this bag.
            log.info('%s packaging complete', bag.bag)
            bag.update()

            # Append instrument bag manifest contents to main manifest
            for item, checksum in bag.manifest_contents.items():
                full_path = os.path.join('data', bag_name, item)
                full_path = full_path.replace('\\', '/')
                self.main_manifest.write('{0}  {1}\n'.format(checksum,
                                                             full_path))
   */

  private void copyLocalData(ServiceProperties serviceProperties, Path instrumentBagDataDir) {
    Path systemLocalData = Paths.get(serviceProperties.getWorkDir()).resolve(LOCAL_DATA);
    Path people = systemLocalData.resolve(PEOPLE_XML);
    Path organizations = systemLocalData.resolve(ORGANIZATIONS_XML);
    Path localData = instrumentBagDataDir.resolve(LOCAL_DATA);
    if (Files.isRegularFile(people)) {
      packerFileController.mkDir(localData);
      packerFileController.copy(people, localData.resolve(PEOPLE_XML));
    }
    if (Files.isRegularFile(organizations)) {
      packerFileController.mkDir(localData);
      packerFileController.copy(organizations, localData.resolve(ORGANIZATIONS_XML));
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

  private void copyMainDatasetFiles(Path datasetDir, InstrumentDetail dataset) {
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
          if (packerFileController.filterHidden(sourceFile)) {
            if (filterExtension(sourceFile, dataset) && packerFileController.filterTimeSize(sourceFile, targetFile)) {
              packerFileController.mkDir(targetFile.getParent());
              packerFileController.copy(sourceFile, targetFile);
            }
            packStateModel.incrementProgress();
          }
          return super.visitFile(file, attr);
        }
      });
    } catch (IOException e) {
      throw new IllegalStateException("Unable to process files " + sourceDataDir, e);
    }
  }

  private void copyAdditionalFiles(Path datasetDir, AdditionalFiles additionalFile) {
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
            if (packerFileController.filterHidden(sourceFile)) {
              if (packerFileController.filterTimeSize(sourceFile, targetFile)) {
                packerFileController.mkDir(targetFile.getParent());
                packerFileController.copy(sourceFile, targetFile);
              }
              packStateModel.incrementProgress();
            }
            return super.visitFile(file, attr);
          }
        });
      } else if (Files.isRegularFile(sourceDataPath) && !Files.isHidden(sourceDataPath)) {
        Path targetFile = resolveFinalPath(resolvedDatasetDir, sourceDataPath, sourceDataPath);
        if (packerFileController.filterTimeSize(sourceDataPath, targetFile)) {
          packerFileController.mkDir(targetFile.getParent());
          packerFileController.copy(sourceDataPath, targetFile);
        }
        packStateModel.incrementProgress();
      } else {
        throw new IllegalStateException("Unable to read " + sourceDataPath);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Unable to process files " + sourceDataPath, e);
    }
  }
  
  private Metadata createMetadataFromPackJob(PackJob packJob) {
    Person metadataAuthor = packJob.getMetadataAuthor();

    Metadata metadata = new Metadata();
    metadata.addAll(List.of(
        new SimpleImmutableEntry<>(
            "Source-Organization",
            String.join(
                ", ",
                packJob.getSources().stream()
                    .map(PeopleOrg::getName)
                    .collect(Collectors.toSet())
            )
        ),
        new SimpleImmutableEntry<>(
            "Contact-Name",
            metadataAuthor == null ? null : metadataAuthor.getName()
        ),
        new SimpleImmutableEntry<>(
            "Contact-Phone",
            metadataAuthor == null ? null : metadataAuthor.getPhone()
        ),
        new SimpleImmutableEntry<>(
            "Contact-Email",
            metadataAuthor == null ? null : metadataAuthor.getEmail()
        ),
        new SimpleImmutableEntry<>(
            "External-Description",
            packJob.getCruiseDescription()
        ),
        new SimpleImmutableEntry<>(
            "External-Identifier",
            packJob.getCruiseId()
        ),
        new SimpleImmutableEntry<>(
            "Cruise Name",
            packJob.getCruiseTitle()
        )
    ));
    return metadata;
  }

  private Metadata createMetadataFromCruiseMetadata(CruiseMetadata cruiseMetadata) {
    MetadataAuthor metadataAuthor = cruiseMetadata.getMetadataAuthor();
    
    Metadata metadata = new Metadata();
    metadata.addAll(List.of(
        new SimpleImmutableEntry<>(
            "Source-Organization",
            String.join(
                ", ",
                cruiseMetadata.getSponsors().stream()
                    .map(PeopleOrg::getName)
                    .collect(Collectors.toSet())
            )
        ),
        new SimpleImmutableEntry<>(
            "Contact-Name",
            metadataAuthor == null ? null : metadataAuthor.getName()
        ),
        new SimpleImmutableEntry<>(
            "Contact-Phone",
            metadataAuthor == null ? null : metadataAuthor.getPhone()
        ),
        new SimpleImmutableEntry<>(
            "Contact-Email",
            metadataAuthor == null ? null : metadataAuthor.getEmail()
        ),
        new SimpleImmutableEntry<>(
            "External-Description",
            cruiseMetadata.getCruiseDescription()
        ),
        new SimpleImmutableEntry<>(
            "External-Identifier",
            cruiseMetadata.getCruiseId()
        ),
        new SimpleImmutableEntry<>(
            "Cruise Name",
            cruiseMetadata.getCruiseTitle()
        ),
        new SimpleImmutableEntry<>(
            "Instrument",
            String.join(
                ", ",
                cruiseMetadata.getPackageInstruments().values().stream()
                    .map(PackageInstrument::getInstrument)
                    .map(Instrument::getInstrument)
                    .collect(Collectors.toSet())
            )
        )
    ));
    return metadata;
  }

}
