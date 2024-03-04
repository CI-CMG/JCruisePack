package edu.colorado.cires.cruisepack.app.ui.controller;

public final class Events {

  // Package
  public static final String UPDATE_CRUISE_ID = "UPDATE_CRUISE_ID";
  public static final String UPDATE_CRUISE_ID_ERROR = "UPDATE_CRUISE_ID_ERROR";
  public static final String UPDATE_SEA = "UPDATE_SEA";
  public static final String UPDATE_ARRIVAL_PORT = "UPDATE_ARRIVAL_PORT";
  public static final String UPDATE_DEPARTURE_PORT = "UPDATE_DEPARTURE_PORT";
  public static final String UPDATE_SHIP = "UPDATE_SHIP";
  public static final String UPDATE_PROJECTS = "UPDATE_PROJECTS";
  public static final String ADD_PROJECT = "ADD_PROJECT";
  public static final String REMOVE_PROJECT = "REMOVE_PROJECT";
  public static final String CLEAR_PROJECTS = "CLEAR_PROJECTS";
  public static final String UPDATE_SEGMENT = "UPDATE_SEGMENT";
  public static final String UPDATE_SEGMENT_ERROR = "UPDATE_SEGMENT_ERROR";
  public static final String UPDATE_DEPARTURE_DATE = "UPDATE_DEPARTURE_DATE";
  public static final String UPDATE_DEPARTURE_DATE_ERROR = "UPDATE_DEPARTURE_DATE_ERROR";
  public static final String UPDATE_ARRIVAL_DATE = "UPDATE_ARRIVAL_DATE";
  public static final String UPDATE_ARRIVAL_DATE_ERROR = "UPDATE_ARRIVAL_DATE_ERROR";
  public static final String UPDATE_RELEASE_DATE = "UPDATE_RELEASE_DATE";
  public static final String UPDATE_RELEASE_DATE_ERROR = "UPDATE_RELEASE_DATE_ERROR";
  public static final String UPDATE_PACKAGE_DIRECTORY = "UPDATE_PACKAGE_DIRECTORY";
  public static final String UPDATE_PACKAGE_DIRECTORY_ERROR = "UPDATE_PACKAGE_DIRECTORY_ERROR";
  public static final String UPDATE_SEA_ERROR = "UPDATE_SEA_ERROR";
  public static final String UPDATE_ARRIVAL_PORT_ERROR = "UPDATE_ARRIVAL_PORT_ERROR";
  public static final String UPDATE_DEPARTURE_PORT_ERROR = "UPDATE_DEPARTURE_PORT_ERROR";
  public static final String UPDATE_SHIP_ERROR = "UPDATE_SHIP_ERROR";
  public static final String UPDATE_PROJECTS_ERROR = "UPDATE_PROJECTS_ERROR";
  public static final String UPDATE_EXISTING_RECORD = "UPDATE_EXISTING_RECORD";

  // Footer Control
  public static final String UPDATE_JOB_ERRORS = "UPDATE_JOB_ERRORS";
  public static final String UPDATE_STOP_BUTTON_ENABLED = "UPDATE_STOP_BUTTON_ENABLED";
  public static final String UPDATE_SAVE_BUTTON_ENABLED = "UPDATE_SAVE_BUTTON_ENABLED";
  public static final String UPDATE_PACKAGE_BUTTON_ENABLED = "UPDATE_PACKAGE_BUTTON_ENABLED";
  public static final String UPDATE_SAVE_WARNING_DIALOGUE_VISIBLE = "UPDATE_SAVE_WARNING_DIALOGUE_VISIBLE";
  public static final String UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE = "UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE";
  public static final String UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE = "UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE";
  public static final String UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE = "UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE";
  public static final String EMIT_PACKAGE_ID = "EMIT_PACKAGE_ID";


  public static final String ADD_SCIENTIST = "ADD_SCIENTIST";
  public static final String REMOVE_SCIENTIST = "REMOVE_SCIENTIST";
  public static final String CLEAR_SCIENTISTS = "CLEAR_SCIENTISTS";
  public static final String ADD_SOURCE_ORGANIZATION = "ADD_SOURCE_ORGANIZATION";
  public static final String REMOVE_SOURCE_ORGANIZATION = "REMOVE_SOURCE_ORGANIZATION";
  public static final String CLEAR_SOURCE_ORGANIZATIONS = "CLEAR_SOURCE_ORGANIZATIONS";
  public static final String ADD_FUNDING_ORGANIZATION = "ADD_FUNDING_ORGANIZATION";
  public static final String REMOVE_FUNDING_ORGANIZATION = "REMOVE_FUNDING_ORGANIZATION";
  public static final String CLEAR_FUNDING_ORGANIZATIONS = "CLEAR_FUNDING_ORGANIZATIONS";
  public static final String UPDATE_METADATA_AUTHOR = "UPDATE_METADATA_AUTHOR";
  // Errors
  public static final String UPDATE_SCIENTIST_ERROR = "UPDATE_SCIENTIST_ERROR";
  public static final String UPDATE_SOURCE_ORGANIZATION_ERROR = "UPDATE_SOURCE_ORGANIZATION_ERROR";
  public static final String UPDATE_FUNDING_ORGANIZATION_ERROR = "UPDATE_FUNDING_ORGANIZATION_ERROR";
  public static final String UPDATE_METADATA_AUTHOR_ERROR = "UPDATE_METADATA_AUTHOR_ERROR";

  // OmicsModel
  public static final String UPDATE_OMICS_SAMPLING_CONDUCTED = "UPDATE_OMICS_SAMPLING_CONDUCTED";
  public static final String UPDATE_OMICS_CONTACT = "UPDATE_OMICS_CONTACT";
  public static final String UPDATE_OMICS_CONTACT_ERROR = "UPDATE_OMICS_CONTACT_ERROR";
  public static final String UPDATE_OMICS_SAMPLE_TRACKING_SHEET = "UPDATE_OMICS_SAMPLE_TRACKING_SHEET";
  public static final String UPDATE_OMICS_SAMPLE_TRACKING_SHEET_ERROR = "UPDATE_SAMPLE_TRACKING_SHEET_ERROR";
  public static final String UPDATE_OMICS_BIO_PROJECT_ACCESSION = "UPDATE_OMICS_BIO_PROJECT_ACCESSION";
  public static final String UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR = "UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR";
  public static final String UPDATE_OMICS_WATER_SAMPLING_TYPE = "UPDATE_OMICS_WATER_SAMPLING_TYPE";
  public static final String UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE = "UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE";
  public static final String UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE = "UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE";
  public static final String UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS = "UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS = "UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS = "UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS = "UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS = "UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS";
  public static final String UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION = "UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION";
  public static final String UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR = "UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR";
  public static final String UPDATE_OMICS_SAMPLING_TYPES_ERROR = "UPDATE_OMICS_SAMPLING_TYPES_ERROR";
  public static final String UPDATE_OMICS_EXPECTED_ANALYSES_ERROR = "UPDATE_OMICS_EXPECTED_ANALYSES_ERROR";
  // Cruise Information
  public static final String UPDATE_CRUISE_TITLE = "UPDATE_CRUISE_TITLE";
  public static final String UPDATE_CRUISE_TITLE_ERROR = "UPDATE_CRUISE_TITLE_ERROR";
  public static final String UPDATE_CRUISE_PURPOSE = "UPDATE_CRUISE_PURPOSE";
  public static final String UPDATE_CRUISE_PURPOSE_ERROR = "UPDATE_CRUISE_PURPOSE_ERROR";
  public static final String UPDATE_CRUISE_DESCRIPTION = "UPDATE_CRUISE_DESCRIPTION";
  public static final String UPDATE_CRUISE_DESCRIPTION_ERROR = "UPDATE_CRUISE_DESCRIPTION_ERROR";
  public static final String UPDATE_DOCS_DIRECTORY = "UPDATE_DOCS_DIRECTORY";
  public static final String UPDATE_DOCS_DIRECTORY_ERROR = "UPDATE_DOCS_DIRECTORY_ERROR";

  // Datasets
  public static final String CLEAR_DATASET_LIST = "UPDATE_DATASET_LIST";
  public static final String ADD_DATASET = "ADD_DATASET";
  public static final String REMOVE_DATASET = "REMOVE_DATASET";
  public static final String UPDATE_DATASET_DATA_PREFIX = "UPDATE_DATASET_DATA_";
  public static final String UPDATE_DATASETS_ERROR = "UPDATE_DATASETS_ERROR";

  // Person
  public static final String UPDATE_PERSON_NAME = "UPDATE_PERSON_NAME";
  public static final String EMIT_PERSON_NAME = "EMIT_PERSON_NAME";
  public static final String UPDATE_PERSON_POSITION = "UPDATE_PERSON_POSITION";
  public static final String UPDATE_PERSON_ORGANIZATION = "UPDATE_PERSON_ORGANIZATION";
  public static final String UPDATE_PERSON_STREET = "UPDATE_PERSON_STREET";
  public static final String UPDATE_PERSON_CITY = "UPDATE_PERSON_CITY";
  public static final String UPDATE_PERSON_STATE = "UPDATE_PERSON_STATE";
  public static final String UPDATE_PERSON_ZIP = "UPDATE_PERSON_ZIP";
  public static final String UPDATE_PERSON_COUNTRY = "UPDATE_PERSON_COUNTRY";
  public static final String UPDATE_PERSON_PHONE = "UPDATE_PERSON_PHONE";
  public static final String UPDATE_PERSON_EMAIL = "UPDATE_PERSON_EMAIL";
  public static final String UPDATE_PERSON_ORCIDID = "UPDATE_PERSON_ORCIDID";
  public static final String UPDATE_PERSON_UUID = "UPDATE_PERSON_UUID";
  public static final String UPDATE_PERSON_USE = "UPDATE_PERSON_USE";
  public static final String UPDATE_PERSON_NAME_ERROR = "UPDATE_PERSON_NAME_ERROR";
  public static final String UPDATE_PERSON_POSITION_ERROR = "UPDATE_PERSON_POSITION_ERROR";
  public static final String UPDATE_PERSON_ORGANIZATION_ERROR = "UPDATE_PERSON_ORGANIZATION_ERROR";
  public static final String UPDATE_PERSON_STREET_ERROR = "UPDATE_PERSON_STREET_ERROR";
  public static final String UPDATE_PERSON_CITY_ERROR = "UPDATE_PERSON_CITY_ERROR";
  public static final String UPDATE_PERSON_STATE_ERROR = "UPDATE_PERSON_STATE_ERROR";
  public static final String UPDATE_PERSON_ZIP_ERROR = "UPDATE_PERSON_ZIP_ERROR";
  public static final String UPDATE_PERSON_COUNTRY_ERROR = "UPDATE_PERSON_COUNTRY_ERROR";
  public static final String UPDATE_PERSON_PHONE_ERROR = "UPDATE_PERSON_PHONE_ERROR";
  public static final String UPDATE_PERSON_EMAIL_ERROR = "UPDATE_PERSON_EMAIL_ERROR";
  public static final String UPDATE_PERSON_ORCID_ID_ERROR = "UPDATE_ORCID_ID_ERROR";
  public static final String UPDATE_PERSON_UUID_ERROR = "UPDATE_PERSON_UUID_ERROR";

  // Organization
  public static final String UPDATE_ORG_NAME = "UPDATE_ORG_NAME";
  public static final String EMIT_ORG_NAME = "EMIT_ORG_NAME";
  public static final String UPDATE_ORG_STREET = "UPDATE_ORG_STREET";
  public static final String UPDATE_ORG_CITY = "UPDATE_ORG_CITY";
  public static final String UPDATE_ORG_STATE = "UPDATE_ORG_STATE";
  public static final String UPDATE_ORG_ZIP = "UPDATE_ORG_ZIP";
  public static final String UPDATE_ORG_COUNTRY = "UPDATE_ORG_COUNTRY";
  public static final String UPDATE_ORG_PHONE = "UPDATE_ORG_PHONE";
  public static final String UPDATE_ORG_EMAIL = "UPDATE_ORG_EMAIL";
  public static final String UPDATE_ORG_ORCID_ID = "UPDATE_ORG_ORCID_ID";
  public static final String UPDATE_ORG_UUID = "UPDATE_ORG_UUID";
  public static final String UPDATE_ORG_USE = "UPDATE_ORG_USE";
  public static final String UPDATE_ORG_NAME_ERROR = "UPDATE_ORG_NAME_ERROR";
  public static final String UPDATE_ORG_STREET_ERROR = "UPDATE_ORG_STREET_ERROR";
  public static final String UPDATE_ORG_CITY_ERROR = "UPDATE_ORG_CITY_ERROR";
  public static final String UPDATE_ORG_STATE_ERROR = "UPDATE_ORG_STATE_ERROR";
  public static final String UPDATE_ORG_ZIP_ERROR = "UPDATE_ORG_ZIP_ERROR";
  public static final String UPDATE_ORG_COUNTRY_ERROR = "UPDATE_ORG_COUNTRY_ERROR";
  public static final String UPDATE_ORG_PHONE_ERROR = "UPDATE_ORG_PHONE_ERROR";
  public static final String UPDATE_ORG_EMAIL_ERROR = "UPDATE_ORG_EMAIL_ERROR";
  public static final String UPDATE_ORG_UUID_ERROR = "UPDATE_ORG_UUID_ERROR";

  public static final String UPDATE_CRUISE_DATA_STORE = "UPDATE_CRUISE_DATA_STORE";
  public static final String UPDATE_PERSON_DATA_STORE = "UPDATE_PERSON_DATA_STORE";
  public static final String UPDATE_ORGANIZATION_DATA_STORE = "UPDATE_ORGANIZATION_DATA_STORE";

  private Events() {

  }

}
