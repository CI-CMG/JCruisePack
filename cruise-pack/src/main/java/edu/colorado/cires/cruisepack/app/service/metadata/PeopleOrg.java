package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = PeopleOrg.Builder.class)
public class PeopleOrg {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(PeopleOrg src) {
    return new Builder(src);
  }

  private final String name;
  private final String uuid;

  private PeopleOrg(String name, String uuid) {
    this.name = name;
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public String getUuid() {
    return uuid;
  }

  public static class Builder {
    private String name;
    private String uuid;

    private Builder() {

    }

    private Builder(PeopleOrg src) {
      name = src.name;
      uuid = src.uuid;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public PeopleOrg build() {
      return new PeopleOrg(name, uuid);
    }
  }
}
