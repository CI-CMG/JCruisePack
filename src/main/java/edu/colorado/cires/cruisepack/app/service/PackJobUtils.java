package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.ExpectedAnalyses;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.service.metadata.SamplingTypes;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.ExpectedAnalysesModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.model.SamplingTypesModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.FileExtensionList;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.person.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class PackJobUtils {

  private static String resolveDropDownItemUuid(DropDownItem ddi) {
    if (ddi == null || ddi.getId().isEmpty()) {
      return null;
    }
    return ddi.getId();
  }

  public static String resolvePackageId(PackageModel packageModel) {
    return resolvePackageId(packageModel.getCruiseId(), packageModel.getSegment());
  }
  
  public static String resolvePackageId(String cruiseId, String segment) {
    if (cruiseId != null) {
      String packageId = cruiseId;
      if (segment != null) {
        packageId = packageId + "_" + segment;
      }
      return packageId;
    }
    return null;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> getDirNames(DatasetsModel datasetsModel, String packageId, InstrumentDatastore instrumentDatastore) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = new LinkedHashMap<>();
    for (BaseDatasetInstrumentModel<?> instrumentModel : datasetsModel.getDatasets()) {
      instrumentModel.getPackageKey().ifPresent(key -> {
        instrumentDatastore.getInstrument(key).flatMap(instrumentModel::getInstrumentNameHolder).ifPresent(nameHolder -> {
          List<InstrumentNameHolder> holders = namers.computeIfAbsent(key, k -> new ArrayList<>());
          holders.add(nameHolder);
        });
      });
    }
    DatasetNameResolver.setDirNamesOnInstruments(packageId, namers);
    return namers;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentDetail>> getInstruments(DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore, String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = getDirNames(datasetsModel, packageId, instrumentDatastore);
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> map = new LinkedHashMap<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentNameHolder>> entry : namers.entrySet()) {
      InstrumentDetailPackageKey pkg = entry.getKey();
      List<InstrumentDetail> instrumentDetails = new ArrayList<>(entry.getValue().size());
      for (InstrumentNameHolder nameHolder : entry.getValue()) {
        Instrument instrument = instrumentDatastore.getInstrument(pkg)
            .orElseThrow(() -> new IllegalStateException("Unable to find instrument " + pkg));
        Set<String> exts = new LinkedHashSet<>();
        FileExtensionList fileExtensionList = instrument.getFileExtensions();
        if (fileExtensionList != null) {
          List<String> extList = fileExtensionList.getFileExtensions();
          if (extList != null) {
            exts.addAll(extList);
          }
        }

        InstrumentDetail.Builder builder = InstrumentDetail.builder()
            .setUuid(nameHolder.getUuid())
            .setStatus(nameHolder.getStatus())
            .setInstrument(nameHolder.getInstrument())
            .setShortName(nameHolder.getShortName())
            .setExtensions(exts)
            .setDataPath(nameHolder.getDataPath() == null ? null : nameHolder.getDataPath().toString())
            .setFlatten(instrument.isFlatten())
            .setDirName(nameHolder.getDirName())
            .setBagName(nameHolder.getBagName())
            .setAdditionalFiles(nameHolder.getAdditionalFiles())
            .setReleaseDate(nameHolder.getReleaseDate())
            .setDataComment(nameHolder.getDataComment())
            .setAncillaryDataPath(nameHolder.getAncillaryDataPath() == null ? null : nameHolder.getAncillaryDataPath().toString())
            .setAncillaryDataDetails(nameHolder.getAncillaryDataDetails());

        if (nameHolder.getAdditionalFields() != null) {
          for (Entry<String, Object> e : nameHolder.getAdditionalFields().entrySet()) {
            builder = builder.setAdditionalField(e);
          }
        }

        instrumentDetails.add(
            builder.build()
        );

      }
      map.put(pkg, Collections.unmodifiableList(instrumentDetails));
    }
    return map;
  }

  public static PackJob create(PackageModel packageModel, PeopleModel peopleModel, OmicsModel omicsModel, CruiseInformationModel cruiseInformationModel, DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore, PersonDatastore personDatastore) {
    String packageId = resolvePackageId(packageModel);
    Person omicsContact = personDatastore.getByUUID(omicsModel.getContact().getId()).orElse(null);

    List<String> omicsSamplingTypes = new ArrayList<>(0);
    SamplingTypesModel samplingTypesModel = omicsModel.getSamplingTypes();
    if (samplingTypesModel != null) {
      if (samplingTypesModel.isWater()) {
        omicsSamplingTypes.add(SamplingTypes.WATER.getName());
      }

      if (samplingTypesModel.isSoilSediment()) {
        omicsSamplingTypes.add(SamplingTypes.SOIL_SEDIMENT.getName());
      }

      if (samplingTypesModel.isOrganicTissue()) {
        omicsSamplingTypes.add(SamplingTypes.ORGANIC_TISSUE.getName());
      }
    }

    List<String> omicsExpectedAnalyses = new ArrayList<>(0);
    ExpectedAnalysesModel expectedAnalysesModel = omicsModel.getExpectedAnalyses();
    if (expectedAnalysesModel != null) {
      if (expectedAnalysesModel.isBarcoding()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.BARCODING.getName());
      }

      if (expectedAnalysesModel.isGenomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.GENOMICS.getName());
      }

      if (expectedAnalysesModel.isTranscriptomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.TRANSCRIPOMICS.getName());
      }

      if (expectedAnalysesModel.isProteomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.PROTEOMICS.getName());
      }

      if (expectedAnalysesModel.isMetabolomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METABOLOMICS.getName());
      }

      if (expectedAnalysesModel.isEpigenetics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.EPIGENETICS.getName());
      }

      if (expectedAnalysesModel.isOther()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.OTHER.getName());
      }

      if (expectedAnalysesModel.isMetabarcoding()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METABARCODING.getName());
      }

      if (expectedAnalysesModel.isMetagenomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAGENOMICS.getName());
      }

      if (expectedAnalysesModel.isMetatranscriptomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METATRANSCRIPTOMICS.getName());
      }

      if (expectedAnalysesModel.isMetaproteomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAPROTEOMICS.getName());
      }

      if (expectedAnalysesModel.isMetametabolomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAMETABOLOMICS.getName());
      }

      if (expectedAnalysesModel.isMicrobiome()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.MICROBIOME.getName());
      }
    }

    return PackJob.builder()
        .setCruiseId(packageModel.getCruiseId())
        .setSegment(packageModel.getSegment())
        .setSeaUuid(resolveDropDownItemUuid(packageModel.getSea()))
        .setArrivalPortUuid(resolveDropDownItemUuid(packageModel.getArrivalPort()))
        .setDeparturePortUuid(resolveDropDownItemUuid(packageModel.getDeparturePort()))
        .setShipUuid(resolveDropDownItemUuid(packageModel.getShip()))
        .setDepartureDate(packageModel.getDepartureDate())
        .setArrivalDate(packageModel.getArrivalDate())
        .setProjects(packageModel.getProjects().stream().map(DropDownItemModel::getItem).map(DropDownItem::getValue).toList())
        .setReleaseDate(packageModel.getReleaseDate())
        .setPackageDirectory(packageModel.getPackageDirectory())
        .setCruiseTitle(cruiseInformationModel.getCruiseTitle())
        .setCruisePurpose(cruiseInformationModel.getCruisePurpose())
        .setCruiseDescription(cruiseInformationModel.getCruiseDescription())
        .setDocumentsPath(datasetsModel.getDocumentsPath())
        .setOmicsSamplingConducted(omicsModel.isSamplingConducted())
        .setOmicsContactUuid(omicsContact != null ? omicsContact.getUuid() : null)
        .setOmicsContactName(omicsContact != null ? omicsContact.getName() : null)
        .setOmicsContactEmail(omicsContact != null ? omicsContact.getEmail() : null)
        .setOmicsContactPhone(omicsContact != null ? omicsContact.getPhone() : null)
        .setOmicsSampleTrackingSheetPath(omicsModel.getSampleTrackingSheet())
        .setOmicsBioProjectAccession(omicsModel.getBioProjectAccession())
        .setOmicsSamplingTypes(omicsSamplingTypes)
        .setOmicsExpectedAnalyses(omicsExpectedAnalyses)
        .setOmicsAdditionalSamplingInformation(omicsModel.getAdditionalSamplingInformation())
        .setPackageId(packageId)
        .setInstruments(getInstruments(datasetsModel, instrumentDatastore, packageId))
        .setScientists(peopleModel.getScientists().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setFunders(peopleModel.getFundingOrganizations().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setSources(peopleModel.getSourceOrganizations().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setMetadataAuthor(peopleModel.getMetadataAuthor() == null ? null : personDatastore.getByUUID(peopleModel.getMetadataAuthor().getId()).orElse(null))
        .build();
  }

}
