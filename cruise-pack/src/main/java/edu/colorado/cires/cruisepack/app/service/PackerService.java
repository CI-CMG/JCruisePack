package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.copy;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterHidden;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterTimeSize;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PackerService.class);

  private final ServiceProperties serviceProperties;
  private final ObjectMapper objectMapper;
  private final PackagingValidationService validationService;
  private final FooterControlController footerControlController;

  @Autowired
  public PackerService(ServiceProperties serviceProperties, ObjectMapper objectMapper, PackagingValidationService validationService, FooterControlController footerControlController) {
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
    this.validationService = validationService;
    this.footerControlController = footerControlController;
  }

  public void startPacking() {
    footerControlController.setPackageButtonEnabled(false);
    footerControlController.setSaveButtonEnabled(false);
    footerControlController.setStopButtonEnabled(false);
    try {
      validationService.validate().ifPresent(this::startPackingThread);
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
//      setDirNames(packJob);
////    rawCheck(packJob);
        resetBagDirs(packJob);
      copyDocs(packJob);
      copyOmics(packJob);
//      packData(packJob);

      } catch (Exception e) {
        LOGGER.error("An error occurred while packing", e);
      } finally {
        footerControlController.setPackageButtonEnabled(true);
        footerControlController.setSaveButtonEnabled(true);
        footerControlController.setStopButtonEnabled(false);
      }
    }).start();
  }

  private void resetBagDirs(PackJob packJob) {
    mkDir(packJob.getPackageDirectory().resolve(packJob.getPackageId()));
    // if there is an existing bag, move everything in data dir up to top level in preparation for packing
    // TODO
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

  private void setDirNames(PackJob packJob) {
    throw new UnsupportedOperationException();
    //TODO
//    DatasetNameResolver.setDirNamesOnInstruments(packJob.getMasterBagName(), packJob.getInstruments());
  }

  /*
  def get_dir_names(self):
        """ Add data directory name to each instruments details in
        self.instruments.

        Add the appropriate directory name for the instrument package. The
        directory name depends on whether there are more than one instrument
        of a type whether there the data are raw or processed,

        """
        for package, details in self.instruments.items():
            package_parts = package.split('_')

            # If there is only one instrument for the data type then
            # directory naming and bag naming is easy.
            if len(details) == 1:
                if details[0]['status'] == 'Raw':
                    details[0]['dir_name'] = details[0]['short_name']
                elif details[0]['status'] == 'Processed':
                    details[0]['dir_name'] = (details[0]['short_name']
                                              + '_processed')
                else:
                    details[0]['dir_name'] = (details[0]['short_name']
                                             + '_products')

                if package_parts[0] == package_parts[1]:
                    details[0]['bag_name'] = '_'.join([self.main_name,
                                                       package_parts[0]])
                else:
                    details[0]['bag_name'] = '_'.join([self.main_name, package])

                continue

            # Otherwise it gets complicated. For instruments with unique
            # short names we use that as the name root, otherwise we use the
            # long instrument name to get unique data directory names.
            datasets = []
            processed = []
            products = []
            raw_count = 0
            processed_count = 0
            products_count = 0
            bag_name = ''
            name = ''

            for dataset in details:
                if dataset['instrument'] in datasets:
                    dataset['bag_name'] = bag_name
                    if dataset['status'] == 'Raw':
                        if raw_count == 0:
                            dataset['dir_name'] = name
                        else:
                            dataset['dir_name'] = '{0}-{1}'.format(
                                                        name, raw_count)
                        raw_count += 1
                    elif dataset['status'] == 'Processed':
                        if dataset['instrument'] in processed:
                            dataset['dir_name'] = (
                                '{0}_processed-{1}'.format(
                                                 name, processed_count))
                        else:
                            dataset['dir_name'] = name+'_processed'
                            processed.append(dataset['instrument'])
                        processed_count += 1
                    else:
                        if dataset['instrument'] in products:
                            dataset['dir_name'] = (
                                '{0}_products-{1}'.format(
                                                 name, products_count))
                        else:
                            dataset['dir_name'] = name+'_products'
                            products.append(dataset['instrument'])
                        products_count += 1
                else:
                    datasets.append(dataset['instrument'])

                    if package_parts[0] == package_parts[1]:
                        name = dataset['instrument'].replace(' ', '_')
                        bag_name = '_'.join([self.main_name,
                                             package_parts[0], name])
                    else:
                        name = dataset['short_name']
                        bag_name = '_'.join([self.main_name, package])

                    dataset['bag_name'] = bag_name

                    if dataset['status'] == 'Raw':
                        dataset['dir_name'] = name
                        raw_count = 1
                    elif dataset['status'] == 'Processed':
                        dataset['dir_name'] = name + '_processed'
                        processed.append(dataset['instrument'])
                        processed_count = 1
                    else:
                        dataset['dir_name'] = name+'_products'
                        products.append(dataset['instrument'])
                        products_count = 1
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
            if (filterHidden(sourceFile) && filterTimeSize(sourceFile, targetFile)) {
              mkDir(targetFile.getParent());
              copy(sourceFile, targetFile);
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
            hash_value = self.file_copy(file[0], file[1])
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
      if (filterHidden(omicsFile)) {
        mkDir(omicsDir);
        copy(omicsFile, targetFile);
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

            hash_value = self.file_copy(source_file, dest_file)
            path = os.path.relpath(dest_file, self.main_bag.bag)
            path = path.replace('\\', '/')  # Set as Unix separator
            self.main_manifest.write('{0}  {1}\n'.format(hash_value, path))
            log.info('Omics tracking sheet copied')

        except Exception as e:
            raise IOError(f' Error "{e}" occurred trying to copy omics data')
   */

  private void packData(PackJob packJob) {
    DatasetPacker.pack(serviceProperties, objectMapper, packJob);
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

}
