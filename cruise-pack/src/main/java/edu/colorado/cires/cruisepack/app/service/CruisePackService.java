package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import gov.loc.repository.bagit.domain.Bag;
import java.nio.file.Path;
import java.time.Instant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruisePackService {

  private final CruiseDataDatastore cruiseDataDatastore;
  private final MetadataService metadataService;
  private final PackerService packerService;
  private final PackerStatusService packerStatusService;

  @Autowired
  public CruisePackService(CruiseDataDatastore cruiseDataDatastore, MetadataService metadataService, PackerService packerService,
      PackerStatusService packerStatusService) {
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.metadataService = metadataService;
    this.packerService = packerService;
    this.packerStatusService = packerStatusService;
  }

  private Bag newBag(Path path) {
    throw new UnsupportedOperationException();
  }

  private Bag openBag(Path path) {
    throw new UnsupportedOperationException();
  }

  public void packData(PackJobInput packJobInput) {
//    PackJob packJob = new PackJob();
//    packJob.setStartTime(Instant.now());
//    if (overwriteCheck()) {
//      if (saveData(packJobInput, packJob)) {
//        Progress progress = new Progress();
//        progress.setTotalFiles(fileCount(packJobInput, packJob));
//        if (validate(packJobInput, packJob)) {
//          CruiseMetadata cruiseMetadata = getCruiseMetadata(packJobInput, packJob);
//          validateCruiseMetadata(cruiseMetadata);
//          appendInstrumentData(cruiseMetadata);
//          packJob.setMasterBagName(packJob.getPackageId()); // TODO is this correct?
//          packJob.setBagPath(packJobInput.getDestinationPath().resolve(packJob.getMasterBagName()));
//          //TODO this validation should happen before anything else
//          Bag mainBag;
//          if (hasExistingBag(packJob)) {
//            mainBag = openBag(packJob.getBagPath());
//          } else {
//            mainBag = newBag(packJob.getBagPath());
//          }
//          metadataService.exportMetadata(mainBag, cruiseMetadata);
//          Instruments instruments = getInstruments();
//          packJob.setInstruments(instruments);
//          PackerHandle  packerHandle = packerService.startPacking(packJob);
//          packerStatusService.trackPacker(packerHandle);
//        }
//      }
//
//    }
  }

  /*
  def pack_data(self):
        """Main packaging control method.

        This method test to make sure everything is set for packing and then
        starts the packing process.

        Returns: Nothing is returned but there are several bailout returns that
        end packing if something is wrong.

        """
        self.start_time = QtCore.QDateTime.currentDateTime()

        # Check if the package id has been changed and either create new
        # record or not. If not, check for name conflict.
        if self.overwrite_check() != 'valid':
            return

        # Save data before packaging just to be safe. The 'packaging' keyword
        # suppresses the normal post-save packager exit dialog. The return
        # check bails out of packaging if there is a problem on save.
        if self.save_data('packaging') == 'save error':
            return

        # Get count of all files in all instrument source paths plus
        # documents path to initialize progress bar. RESET COPIED FILE COUNT
        # TO 0,
        self.bar_status.setText('')
        self.copied_files = 0
        self.total_files = 0
        self.pbar.setValue(0)

        self.total_files = self.file_count()
        self.pbar.setMaximum(self.total_files)
        # print(self.total_files)

        # Validate user entered information.
        critical_errors, warnings = self.validate()

        if critical_errors or warnings:
            message = ''
            msg_box = QtWidgets.QMessageBox(self)
            msg_box.setIcon(3)
            msg_box.setWindowTitle('Data Entry Errors')
            msg_box.addButton(QtWidgets.QPushButton('Fix Data'),
                              QtWidgets.QMessageBox.YesRole)
            if critical_errors:
                self.update_status('Critical errors exist and must be fixed '
                                   'before packaging.')
                warning_list = '\n\n\N{Black Small Square} '.join(critical_errors)
                message = ('The following critical data entry '
                           'errors/omissions exist. They must be corrected '
                           'before packaging can proceed.'
                           f'\n\n\N{Black Small Square}{warning_list}\n\n')

            if warnings:
                self.update_status('metadata omissions exist and should be '
                                   'fixed before packaging.')
                error_list = '\n\n\N{Black Small Square} '.join(warnings)
                message = (f'{message}These non-critical data fields are '
                           f'empty or have invalid content. They should be '
                           f'fixed before packaging.\n\n'
                           f'\N{Black Small Square} {error_list}')

                if not critical_errors:
                    msg_box.setIcon(1)
                    msg_box.addButton(QtWidgets.QPushButton('Continue'),
                                      QtWidgets.QMessageBox.NoRole)
            msg_box.setText(message)

            reply = msg_box.exec_()

            if not reply:
                # The Fix Data button was pushed so return out to allow user
                # to fix issues.
                return

        # Get metadata,
        self.cruise_metadata = self.get_cruise_metadata()

        # Get instrument metadata and raise error if there are no instruments.
        instrument_metadata = self.get_instruments(return_uuid=True)

        # Append instrument metadata to cruise metadata.
        self.cruise_metadata['instruments'] = instrument_metadata

        # We are now ready to start the packing process. Generate master bag
        # name and check to see if Bag already exists, Create if not, warn
        # user if it does.
        master_bag_name = self.cruise_metadata['package_id']
        bag_path = os.path.join(self.destination_path.text(), master_bag_name)

        if os.path.isdir(bag_path):
            reply = QtWidgets.QMessageBox.warning(
                self, 'Overwrite Danger', f'A package with ID '
                f'{self.packageID} already exists. Do you want to continue?',
                QtWidgets.QMessageBox.Yes | QtWidgets.QMessageBox.No,
                QtWidgets.QMessageBox.Yes)
            if reply == QtWidgets.QMessageBox.No:
                return
            else:
                # Open the existing bag.
                try:
                    self.main_bag = b.BagIt(bag_path)
                except Exception as e:
                    QtWidgets.QMessageBox.critical(
                        self, 'Error Creating Package Directory',
                        'Encountered the error {0}. Check the path entered '
                        'for the destination directory.'.format(e))

                    self.update_status('Packaging terminated due to "{0}"'
                                       .format(e))
                    return

        else:
            # Create a new master bag.
            try:
                self.main_bag = b.BagIt(bag_path)
            except Exception as e:
                QtWidgets.QMessageBox.critical(self, 'Error Creating Package '
                                                     'Directory',
                                               'Encountered the '
                                               'error {0}. Check the path'
                                               ' entered for the destination '
                                               'directory.'.format(e))

                self.update_status('Packaging terminated due to {0}'.format(e))

                return

        # Reset logging to make main package log file
        tools.reset_logging(log, bag_path, master_bag_name)
        log.info('Starting packaging %s', self.main_bag.bag)
        log.info('CruisePack version: %s', self.version)

        # Create package-level metadata file.
        tools.export_metadata(self.main_bag, self.cruise_metadata)

        # Get dictionary of instrument details to pass to packing thread.
        instruments = self.get_instruments_dic()

        # Create instance of Packer. This is a QThread object to run packing
        # in separate thread from main UI.
        self.packer = Packer(self, instruments)

        # Connect signals coming back from Packer.
        self.packer.signals.multi_raw.connect(self.multi_raw)
        self.packer.signals.update.connect(self.update_status)
        self.packer.finished.connect(self.packing_finished)
        self.packer.signals.packing_error.connect(self.packing_error)
        self.packer.signals.copy_error.connect(self.copy_error)

        # Set packing flag and disable package and save buttons to prevent
        # user thinking they are changing things mid-pack.
        self.packing = True
        self.finish_later.setEnabled(False)
        self.create_package.setEnabled(False)
        self.stop.setEnabled(True)
        self.packer.start()
   */

  private boolean overwriteCheck() {
    throw new UnsupportedOperationException(); // TODO
  }
  /*
      def overwrite_check(self):
        """Check if package name is new or already exists.

        Returns(str): Return valid is name is new, conflict if the name
        already exists.

        """
        # If this is the first attempt at making a package in this session,
        # try and create a new package.
        if not self.id:
            if self.new_package() != 'valid':
                return 'conflict'

        # Check whether package id is the same in the gui and cruiseData DB.
        # If not, ask whether user wants to make to make a new package.
        package_curs.execute('select * from CRUISE_DATA where ID=?', [self.id])
        package_info = package_curs.fetchone()

        if package_info['PACKAGE_ID'] != self.package_id.currentText():
            msg_box = QtWidgets.QMessageBox(self)
            msg_box.setIcon(2)  # Sets icon to warning icon
            msg_box.setWindowTitle('Overwrite Danger')
            msg_box.setText('The current package ID is different than the '
                            'saved value. Click "Update" to update current '
                            'record. Click "Create New" to create a new '
                            'record for this new package ID.')

            msg_box.addButton(QtWidgets.QPushButton('Update'),
                              QtWidgets.QMessageBox.YesRole)
            msg_box.addButton(QtWidgets.QPushButton('Create New'),
                              QtWidgets.QMessageBox.NoRole)

            reply = msg_box.exec_()

            if not reply:  # Clicking "Update" returns a value of 0.
                if (self.name_conflict_check(self.package_id.currentText())
                        != 'valid'):
                    return 'conflict'
            else:
                if self.new_package() != 'valid':
                    return 'conflict'

        return 'valid'
   */

  private void newPackage(PackJobInput input, PackJob packJob) {
    String packageId = input.getCruiseId().trim();
    if (StringUtils.isNotBlank(input.getSegmentId())) {
      packageId = packageId + "_" + input.getSegmentId().trim();
    }
    packJob.setPackageId(packageId);

    if (cruiseDataDatastore.packageIdExists(packageId)) {
      throw new CruisePackValidationException("The package name '" + packageId + "' already exists");
    }

    cruiseDataDatastore.save(packJob);

  }
  /*
   def new_package(self):
        """Create a new package entry.

        Creates new package entry in packageData SQLITE file. The package id is
        created from cruise id and segment id if provided and function tests
        whether a package with that id already exists. If so, warn user,
        otherwise create new package. self.id is set to row ID of package
        record. When updating data this value is used as reference so all
        package data, including package id can be edited.

        """
        if self.segment_id.text() != '':
            self.packageID = str(self.cruise_id.text()) + '_' + str(
                self.segment_id.text())
        elif self.cruise_id.text() != '':
            self.packageID = str(self.cruise_id.text())
        else:
            QtWidgets.QMessageBox.critical(self, 'Cruise ID Required',
                                           'The cruise ID field is empty. '
                                           'Please enter a cruise ID and '
                                           'other details before packaging.')
            return 'empty id'

        # Check if cruise id has underscores,
        message = tools.validate_cruise_id(self.cruise_id)
        if message:
            QtWidgets.QMessageBox.critical(self, 'Underscores in Cruise ID',
                                           message)
            return 'underscores'

        package = package_curs.execute(
                                    'SELECT PACKAGE_ID FROM CRUISE_DATA '
                                    'WHERE PACKAGE_ID = ?', [self.packageID])
        try:
            if package.fetchone()[0]:
                QtWidgets.QMessageBox.warning(self, 'Name Conflict',
                                              'The package name "' +
                                              self.packageID +
                                              '" already exists in the '
                                              'cruiseData SQLite file. '
                                              'Please modify the Cruise ID '
                                              'or Segment or Leg value to '
                                              'create a unique package ID.')
                return 'name conflict'
        except TypeError:
            package_curs.execute('insert into CRUISE_DATA (CRUISE_ID, '
                                 'SEGMENT_ID, PACKAGE_ID, CREATION_TIME) '
                                 'values (?,?,?,?)',
                                 [self.cruise_id.text(),
                                  self.segment_id.text(), self.packageID,
                                  QtCore.QDateTime.currentDateTime()
                                      .toString("yyyy-MM-dd hh:mm:ss")])
            package_db.commit()

            # Get new package row ID and set self.id
            package_curs.execute('select ID from CRUISE_DATA '
                                 'where PACKAGE_ID=?', [self.packageID])
            self.id = package_curs.fetchone()[0]

        # Update existing package pull-down.
        self.add_items(package_curs, 'CRUISE_DATA', 'PACKAGE_ID',
                       self.package_id)
        return 'valid'
   */

  private boolean saveData(PackJobInput input, PackJob packJob) {
    throw new UnsupportedOperationException();
  }

  /*
  def save_data(self, task='check'):
        """Save user entered data.

        Args:
            task(str): Controls behavior depending on whether save is being
            called on close, packing, or save for later.

        Returns (str): Method will return 'conflict' if there is a name clash or
            'stop' if the user has not specified the status of a dataset (
            i.e. raw, processed or products).

        """
        # Check if the package id has been changed and either create new
        # record or not. If not, check for name conflict. Skip step if being
        # called from package_data.
        if task != 'packaging':
            if self.overwrite_check() != 'valid':
                return 'conflict'

        # # Get data on datasets. This includes a validation step in
        # # get_instruments that checks that either Raw, Processed or Products
        # # is selected for each dataset.
        datasets = self.get_instruments()
        # if not datasets:
        #     self.update_status('You need to specify at least one instrument to package.')
        #     return

        # Convert dataset information to dictionary if there are datasets
        # with information to be pulled. Otherwise set to None.
        datasets = (json.dumps(datasets) if datasets else None)

        # Get dictionary of omics data:
        omics = self.omics.get_data()

        # Start updating data
        try:
            package_curs.execute(
                'update CRUISE_DATA set CRUISE_ID=?, SEGMENT_ID=?,' +
                'PACKAGE_ID=?, MASTER_RELEASE_DATE=?, DESTINATION_PATH=? where '
                'ID=?', [self.cruise_id.text(), self.segment_id.text(),
                         self.package_id.currentText(),
                         self.master_release_date.date().toString("yyyy-MM-dd"),
                         self.destination_path.text(), self.id])

            package_curs.execute('update CRUISE_DATA set CRUISE_TITLE=?, '
                                      'PURPOSE_TEXT=?, ABSTRACT_TEXT=?, '
                                      'DOCS_PATH=? where ID=?',
                                      [tools.sanitize(self.cruise_title.text()),
                                       tools.sanitize(
                                                    self.purpose.toPlainText()),
                                       tools.sanitize(self.cruise_description.
                                                      toPlainText()),
                                       self.docs_path.text(),
                                       self.id])

            # Get ship UUID from sourceData file.
            try:
                source_curs.execute('select UUID from SHIPS where NAME=?',
                                    [self.ship.currentText()])
                ship_uuid = source_curs.fetchone()[0]
            except:
                ship_uuid = ''

            package_curs.execute('update CRUISE_DATA set DEPARTURE_PORT=?,'
                                      ' DEPARTURE_TIME=?, ARRIVAL_PORT=?, '
                                      'ARRIVAL_TIME=?, SHIP=?, SHIP_UUID=?, '
                                      'SEA_AREA=? where ID=?',
                                      [self.departure_port.currentText(),
                                       self.departure_date_time.date()
                                       .toString("yyyy-MM-dd"),
                                       self.arrival_port.currentText(),
                                       self.arrival_date_time.date()
                                       .toString("yyyy-MM-dd"),
                                       self.ship.currentText(), ship_uuid,
                                       self.sea_area.currentText(), self.id])

            try:
                local_curs.execute('select UUID from PEOPLE where NAME=?',
                                        [self.metadata_author.currentText()])
                metadata_author_uuid = local_curs.fetchone()[0]
            except:
                metadata_author_uuid = ''

            package_curs.execute('update CRUISE_DATA set METADATA_AUTHOR=?, '
                                 'META_AUTHOR_UUID=? where ID=?',
                                 [self.metadata_author.currentText(),
                                  metadata_author_uuid, self.id])

            # Get data on scientists, sponsors and funders. If something is
            # returned, convert to json string otherwise set as None.
            sponsors = (json.dumps(self.sponsors.get_data()) if
                        self.sponsors.get_data() else None)
            funders = (json.dumps(self.funders.get_data()) if
                       self.funders.get_data() else None)
            scientists = (json.dumps(self.scientists.get_data()) if
                          self.scientists.get_data() else None)

            package_curs.execute('update CRUISE_DATA set FUNDERS=?, '
                                 'SPONSORS=?, SCIENTISTS=? where ID=?',
                                 [funders, sponsors, scientists, self.id])

            # Update dataset information.
            package_curs.execute('update CRUISE_DATA set DATASETS=? where '
                                  'ID=?', [datasets, self.id])

            # Update omics information
            if omics:
                try:
                    local_curs.execute('select UUID from PEOPLE where NAME=?',
                                       [omics['omics_poc']])
                    omics['omics_poc_uuid'] = local_curs.fetchone()[0]
                except:
                    omics['omics_poc_uuid'] = ''

            package_curs.execute('update CRUISE_DATA set OMICS=? '
                                 'where ID=?', [json.dumps(omics), self.id])

            # Get project names.
            projects = self.projects.get_data()
            if projects:
                package_curs.execute('update CRUISE_DATA set PROJECTS=? '
                                 'where ID=?', [json.dumps(projects), self.id])

            package_curs.execute('update CRUISE_DATA set UPDATE_TIME=? '
                                      'where ID=?',
                                      [QtCore.QDateTime.currentDateTime()
                                       .toString("yyyy-MM-dd hh:mm:ss"),
                                       self.id])

            local_db.commit()
            package_db.commit()
            self.update_status('Metadata saved.')

        except Exception as e:
            if 'locked' in str(e):
                # THe cruiseData.sqlite file is locked, or at least the system
                # thinks it's locked.
                message = ('cruiseData.sqlite file is locked, Check if another '
                           'application is using the file.'
                           '\n\nSorry but if you are trying to run multiple '
                           'CruisePack instances, Windows only allows you '
                           'to run one at a time.')
            else:
                message = (f'The error "{e}" occurred while saving to the '
                           f'database file. This often occurs due to issues '
                           f'with hidden characters in text pasted into '
                           f'fields, but can have other causes. Contact NCEI '
                           f'is issue persists')

            QtWidgets.QMessageBox.critical(self, 'Error While Writing to '
                                           'Database', message)

            self.update_status('Error saving data. please check you entries '
                               'and try again.')
            return 'save error'

        # Update lists with new values.
        self.update_ui()

        # Display message box if task = saving
        if task == 'saving':
            reply = QtWidgets.QMessageBox.information(
                                        self, 'Data Updated',
                                        f'{self.package_id.currentText()} '
                                        f'data has been updated. Do you want '
                                        f'to exit editor?',
                                        QtWidgets.QMessageBox.No |
                                        QtWidgets.QMessageBox.Yes,
                                        QtWidgets.QMessageBox.Yes)
            if reply == QtWidgets.QMessageBox.Yes:
                self.closing = True
                local_db.close()
                source_db.close()
                package_db.close()
                self.close()
   */

  private int fileCount(PackJobInput input, PackJob packJob) {
    throw new UnsupportedOperationException();
  }

  /*
      def file_count(self):
        """Get count of all files in instrument source directories and
        documents directory

        """
        path_list  = []
        ancillary_count = 0
        if self.docs_path.text():
            path_list.append(self.docs_path.text())

        instruments = self.get_instruments()

        if not instruments:
            # No instruments have been specified. Return and error check
            # will catch it later.
            return 0

        for instrument in instruments:
            path_list.append(instrument['data_path'])
            #TODO This will not count files in directory paths in additional data!!
            # If iut's a directory add it to the path'
            ancillary_count += len(instrument['additional_data'])

        file_count = tools.file_count(path_list)
        return file_count + ancillary_count
   */

  private boolean validate(PackJobInput input, PackJob packJob) {
    throw new UnsupportedOperationException();
  }
  /*
  def validate(self):
        """Validate user-entered data.

        """
        critical_errors = []
        warnings = []

        # Check that there is a metadata author, ship, and at least
        # one sponsor and one dataset set. These are the minimum requirements to
        # successfully create a package Also check other fields that should
        # have values and check all entered paths are valid.
        tools.validate_cruise_id(self.cruise_id, critical_errors)
        tools.validate_line(self.destination_path, 'package destination',
                            critical_errors, warnings, critical=True,
                            path='dir')
        tools.validate_combobox(self.ship, 'Select Ship Name', critical_errors,
                                critical=True)
        tools.validate_combobox(self.metadata_author, 'Select Metadata Author',
                                critical_errors, critical=True)

        tools.validate_people_org(self.sponsors, 'Source Organization',
                                  critical_errors, critical=True)

        instruments = self.get_instruments()
        if not instruments:
            critical_errors.append('You have not specified any '
                                   'data to package.')
            self.datasets_table.setStyleSheet(
                           'QTableWidget {border-width: 3px; '
                           'border-style: solid; border-color: red; }')
        else:
            self.datasets_table.setStyleSheet('')
        # The method get_instruments returns 'stop' if there is an instrument
        # that is not set to raw, processed or products. A warning has
        # already been shown but this terminates packing.
        if instruments == 'stop':
            critical_errors.append('raw not specified.')

        # Check if departure is before arrival and before current date..
        first = 'Departure'
        second = 'Arrival'
        tools.check_date(self.departure_date_time, self.arrival_date_time,
                         self.start_time, first, second, critical_errors)

        tools.validate_line(self.docs_path, 'documents', critical_errors,
                            warnings, path='dir', skip_empty=True)

        tools.validate_combobox(self.sea_area, 'Select Sea Name',
                                warnings)
        tools.validate_combobox(self.arrival_port, 'Select Arrival Port',
                                warnings)
        tools.validate_combobox(self.departure_port, 'Select Departure Port',
                                warnings)
        tools.validate_people_org(self.funders, 'funding organization',
                                  warnings)
        tools.validate_people_org(self.scientists, 'scientist if appropriate',
                                  warnings)

        tools.validate_line(self.cruise_title, 'cruise title',
                            critical_errors, warnings)

        # Validate dataset widgets.
        self.validate_instruments(critical_errors, warnings)

        # Validate OMics.
        self.omics.validate(critical_errors, warnings)

        return critical_errors, warnings
   */

  private CruiseMetadata getCruiseMetadata(PackJobInput input, PackJob packJob) {
    throw new UnsupportedOperationException();
  }

  /*
  def get_cruise_metadata(self):
        """Get the cruise-level metadata.

        Create a dictionary of cruise-level metadata from information entered
        in main UI.

        Returns(dic): Dictionary containing the cruise-level metadata
        information.

        """
        # Get ship UUID from sourceData file
        try:
            source_curs.execute('select UUID from SHIPS where NAME=?',
                                [self.ship.currentText()])
            ship_uuid = source_curs.fetchone()[0]
        except:
            ship_uuid = ''

        cruise_metadata = {
            'cruise_id'          : self.cruise_id.text(),
            'segment_id'         : self.segment_id.text(),
            'package_id'         : self.package_id.currentText(),
            'master_release_date': self.master_release_date.date().toString(
                "yyyy-MM-dd"),
            'ship'               : self.ship.currentText(),
            'ship_uuid'          : ship_uuid,
            'departure_port'     : self.departure_port.currentText(),
            'departure_date'     : self.departure_date_time.date().toString(
                "yyyy-MM-dd"),
            'arrival_port'       : self.arrival_port.currentText(),
            'arrival_date'       : self.arrival_date_time.date().toString(
                "yyyy-MM-dd"),
            'sea_area'           : self.sea_area.currentText(),
            'cruise_title'       : self.cruise_title.text(),
            'cruise_purpose'     : self.purpose.toPlainText(),
            'cruise_description' : self.cruise_description.toPlainText()
        }

        # Get metadata author information
        if self.metadata_author.currentText() != 'Select author':
            metadata_author_data = (local_curs.execute(
                'select * from PEOPLE where NAME=?',
                [self.metadata_author.currentText()])
                                    .fetchone())

            cruise_metadata['metadata_author'] = {
                'name' : self.metadata_author.currentText(),
                'uuid' : metadata_author_data['UUID'],
                'phone': metadata_author_data['PHONE'],
                'email': metadata_author_data['EMAIL']
            }

        # Get sponsor, funder, scientists and project information.
        if self.sponsors.get_data():
            cruise_metadata['sponsors'] = self.sponsors.get_data()

        if self.funders.get_data():
            cruise_metadata['funders'] = self.funders.get_data()

        if self.scientists.get_data():
            cruise_metadata['scientists'] = self.scientists.get_data()

        if self.projects.get_data():
            cruise_metadata['projects'] = self.projects.get_data()
        else:
            cruise_metadata['projects'] = []

        omics = self.omics.get_data()
        if omics:
            if omics['omics_poc']:
                poc_data = (local_curs.execute('select * from PEOPLE '
                                               'where NAME=?',
                                               [omics['omics_poc']]).fetchone())

                poc = {
                    'name': omics['omics_poc'],
                    'uuid': poc_data['UUID'],
                    'phone': poc_data['PHONE'],
                    'email': poc_data['EMAIL']
                    }
                omics['omics_poc'] = poc
            cruise_metadata['omics'] = omics

        return cruise_metadata
   */

  private void validateCruiseMetadata(CruiseMetadata cruiseMetadata) {
    throw new UnsupportedOperationException();
  }


  private boolean hasExistingBag(PackJob packJob) {
    throw new UnsupportedOperationException();
  }

  private void appendInstrumentData(CruiseMetadata cruiseMetadata) {
    throw new UnsupportedOperationException();
  }

//  private Instruments getInstruments() {
//    throw new UnsupportedOperationException();
//  }
  /*
  def get_instruments_dic(self):
        """Create dictionary of instruments with data to be packaged.

        Iterate over all the data widgets in the the datasets table. For
        widgets with user entered information, query the instruments table in
        the sourceData.sqlite file and append the widget's data with
        additional details.

        Returns(dic): Dictionary containing details aon all the instruments
        to be packaged. Dictionary is used by Packer class.

        """
        instruments = {}
        for n in range(self.datasets_table.rowCount()):
            data_widget = self.datasets_table.cellWidget(n, 0)
            widget_data = data_widget.get_data()

            # Add instrument to instruments (widget_data will be None for empty
            # data widgets so skip over them).
            if widget_data:
                instrument_info = source_curs.execute(
                    'select SHORT_TYPE, SHORT_NAME, '
                    'FLATTEN, FILE_EXTENSIONS, UUID from '
                    'INSTRUMENTS where NAME=? and DATA_TYPE=?',
                    [widget_data['instrument'], widget_data['type']]).fetchone()

                widget_data['short_name'] = instrument_info['SHORT_NAME']
                widget_data['type_name'] = instrument_info['SHORT_TYPE']
                if instrument_info['FLATTEN'] == 'Y':
                    flatten = True
                else:
                    flatten = False
                widget_data['flatten'] = flatten
                widget_data['extensions'] = instrument_info['FILE_EXTENSIONS']
                widget_data['uuid'] = instrument_info['UUID']

                key = '_'.join([widget_data['type_name'],
                                widget_data['short_name']])

                if key in [*instruments]:  # Command makes list of keys.
                    instruments[key].append(widget_data)
                else:
                    instruments[key] = [widget_data]

        return instruments
   */

}
