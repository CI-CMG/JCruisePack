package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeopleOrg peopleOrg = (PeopleOrg) o;
    return Objects.equals(name, peopleOrg.name) && Objects.equals(uuid, peopleOrg.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, uuid);
  }

  @Override
  public String toString() {
    return "PeopleOrg{" +
        "name='" + name + '\'' +
        ", uuid='" + uuid + '\'' +
        '}';
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
