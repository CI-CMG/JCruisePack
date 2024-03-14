package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = CruiseData.Builder.class)
public class CruiseData extends Cruise {

  private final boolean use;
  @JsonIgnore
  private final boolean delete;
  private final String documentsPath;
  private final String packageDirectory;

  private CruiseData(boolean use, boolean delete, String cruiseId, String segmentId, String packageId, String masterReleaseDate, String ship, String shipUuid,
      String departurePort, String departureDate, String arrivalPort, String arrivalDate, String seaArea, String cruiseTitle, String cruisePurpose,
      String cruiseDescription, List<PeopleOrg> sponsors, List<PeopleOrg> funders, List<PeopleOrg> scientists,
      List<String> projects, Omics omics, MetadataAuthor metadataAuthor, List<Instrument> instruments,
      Map<String, PackageInstrument> packageInstruments, String documentsPath, String packageDirectory) {
    super(cruiseId, segmentId, packageId, masterReleaseDate, ship, shipUuid, departurePort, departureDate, arrivalPort, arrivalDate, seaArea,
        cruiseTitle, cruisePurpose, cruiseDescription, sponsors, funders, scientists, projects, omics, metadataAuthor, instruments,
        packageInstruments);
    this.use = use;
    this.delete = delete;
    this.documentsPath = documentsPath;
    this.packageDirectory = packageDirectory;
  }

  public boolean isUse() {
    return use;
  }
  
  public boolean isDelete() {
    return delete;
  }

  public String getDocumentsPath() {
    return documentsPath;
  }

  public String getPackageDirectory() {
    return packageDirectory;
  }

  public static Builder builder(CruiseMetadata data) {
    return new Builder(data);
  }
  
  public static Builder builder(CruiseData data) {
    return new Builder(data);
  }
  
  public static Builder builder() {
    return new Builder();
  }


  public static class Builder {
    private boolean use = true;
    private boolean delete = false;
    private String cruiseId;
    private String segmentId;
    private String packageId;
    private String masterReleaseDate;
    private String ship;
    private String shipUuid;
    private String departurePort;
    private String departureDate;
    private String arrivalPort;
    private String arrivalDate;
    private String seaArea;
    private String cruiseTitle;
    private String cruisePurpose;
    private String cruiseDescription;
    private List<PeopleOrg> sponsors = Collections.emptyList();
    private List<PeopleOrg> funders = Collections.emptyList();
    private List<PeopleOrg> scientists = Collections.emptyList();
    private List<String> projects = Collections.emptyList();
    private Omics omics;
    private MetadataAuthor metadataAuthor;
    private List<Instrument> instruments = Collections.emptyList();
    private Map<String, PackageInstrument> packageInstruments;
    private String documentsPath;
    private String packageDirectory;

    private Builder() {

    }

    private Builder(CruiseMetadata src) {
      use = true;
      delete = false;
      documentsPath = null;
      packageDirectory = null;
      cruiseId = src.getCruiseId();
      segmentId = src.getSegmentId();
      packageId = src.getPackageId();
      masterReleaseDate = src.getMasterReleaseDate();
      ship = src.getShip();
      shipUuid = src.getShipUuid();
      departurePort = src.getDeparturePort();
      departureDate = src.getDepartureDate();
      arrivalPort = src.getArrivalPort();
      arrivalDate = src.getArrivalDate();
      seaArea = src.getSeaArea();
      cruiseTitle = src.getCruiseTitle();
      cruisePurpose = src.getCruisePurpose();
      cruiseDescription = src.getCruiseDescription();
      sponsors = src.getSponsors();
      funders = src.getFunders();
      scientists = src.getScientists();
      projects = src.getProjects();
      omics = src.getOmics();
      metadataAuthor = src.getMetadataAuthor();
      instruments = src.getInstruments();
      packageInstruments = src.getPackageInstruments();
    }

    private Builder(CruiseData src) {
      use = src.isUse();
      delete = src.isDelete();
      documentsPath = src.getDocumentsPath();
      packageDirectory = src.getPackageDirectory();
      cruiseId = src.getCruiseId();
      segmentId = src.getSegmentId();
      packageId = src.getPackageId();
      masterReleaseDate = src.getMasterReleaseDate();
      ship = src.getShip();
      shipUuid = src.getShipUuid();
      departurePort = src.getDeparturePort();
      departureDate = src.getDepartureDate();
      arrivalPort = src.getArrivalPort();
      arrivalDate = src.getArrivalDate();
      seaArea = src.getSeaArea();
      cruiseTitle = src.getCruiseTitle();
      cruisePurpose = src.getCruisePurpose();
      cruiseDescription = src.getCruiseDescription();
      sponsors = src.getSponsors();
      funders = src.getFunders();
      scientists = src.getScientists();
      projects = src.getProjects();
      omics = src.getOmics();
      metadataAuthor = src.getMetadataAuthor();
      instruments = src.getInstruments();
      packageInstruments = src.getPackageInstruments();
    }

    public Builder withUse(boolean use) {
      this.use = use;
      return this;
    }
    
    public Builder withDelete(boolean delete) {
      this.delete = delete;
      return this;
    }

    public Builder withCruiseId(String cruiseId) {
      this.cruiseId = cruiseId;
      return this;
    }

    public Builder withSegmentId(String segmentId) {
      this.segmentId = segmentId;
      return this;
    }

    public Builder withPackageId(String packageId) {
      this.packageId = packageId;
      return this;
    }

    public Builder withMasterReleaseDate(String masterReleaseDate) {
      this.masterReleaseDate = masterReleaseDate;
      return this;
    }

    public Builder withShip(String ship) {
      this.ship = ship;
      return this;
    }

    public Builder withShipUuid(String shipUuid) {
      this.shipUuid = shipUuid;
      return this;
    }

    public Builder withDeparturePort(String departurePort) {
      this.departurePort = departurePort;
      return this;
    }

    public Builder withDepartureDate(String departureDate) {
      this.departureDate = departureDate;
      return this;
    }

    public Builder withArrivalPort(String arrivalPort) {
      this.arrivalPort = arrivalPort;
      return this;
    }

    public Builder withArrivalDate(String arrivalDate) {
      this.arrivalDate = arrivalDate;
      return this;
    }

    public Builder withSeaArea(String seaArea) {
      this.seaArea = seaArea;
      return this;
    }

    public Builder withCruiseTitle(String cruiseTitle) {
      this.cruiseTitle = cruiseTitle;
      return this;
    }

    public Builder withCruisePurpose(String cruisePurpose) {
      this.cruisePurpose = cruisePurpose;
      return this;
    }

    public Builder withCruiseDescription(String cruiseDescription) {
      this.cruiseDescription = cruiseDescription;
      return this;
    }

    public Builder withSponsors(List<PeopleOrg> sponsors) {
      if (sponsors == null) {
        sponsors = new ArrayList<>(0);
      }
      this.sponsors = Collections.unmodifiableList(new ArrayList<>(sponsors));
      return this;
    }

    public Builder withFunders(List<PeopleOrg> funders) {
      if (funders == null) {
        funders = new ArrayList<>(0);
      }
      this.funders = Collections.unmodifiableList(new ArrayList<>(funders));
      return this;
    }

    public Builder withScientists(List<PeopleOrg> scientists) {
      if (scientists == null) {
        scientists = new ArrayList<>(0);
      }
      this.scientists = Collections.unmodifiableList(new ArrayList<>(scientists));
      return this;
    }

    public Builder withProjects(List<String> projects) {
      if (projects == null) {
        projects = new ArrayList<>(0);
      }
      this.projects = Collections.unmodifiableList(new ArrayList<>(projects));
      return this;
    }

    public Builder withOmics(Omics omics) {
      this.omics = omics;
      return this;
    }

    public Builder withMetadataAuthor(MetadataAuthor metadataAuthor) {
      this.metadataAuthor = metadataAuthor;
      return this;
    }

    public Builder withInstruments(List<InstrumentData> instruments) {
      if (instruments == null) {
        instruments = new ArrayList<>(0);
      }
      this.instruments = Collections.unmodifiableList(new ArrayList<>(instruments));
      return this;
    }

    public Builder withPackageInstruments(Map<String, PackageInstrument> packageInstruments) {
      if (packageInstruments == null) {
        // Allow this to be null
        this.packageInstruments = packageInstruments;
      } else {
        this.packageInstruments = Collections.unmodifiableMap(new LinkedHashMap<>(packageInstruments));
      }
      return this;
    }
    
    public Builder withDocumentsPath(String documentsPath) {
      this.documentsPath = documentsPath;
      return this;
    }
    
    public Builder withPackageDirectory(String packageDirectory) {
      this.packageDirectory = packageDirectory;
      return this;
    }

    public CruiseData build() {
      return new CruiseData(use, delete, cruiseId, segmentId, packageId, masterReleaseDate, ship, shipUuid,
          departurePort, departureDate, arrivalPort, arrivalDate, seaArea, cruiseTitle, cruisePurpose,
          cruiseDescription, sponsors, funders, scientists, projects, omics,
          metadataAuthor, instruments, packageInstruments, documentsPath, packageDirectory);
    }
  }
}
