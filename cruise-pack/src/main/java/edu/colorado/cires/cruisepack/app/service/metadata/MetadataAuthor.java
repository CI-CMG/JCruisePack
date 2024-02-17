package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = MetadataAuthor.Builder.class)
public class MetadataAuthor {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(MetadataAuthor src) {
    return new Builder(src);
  }

  private final String name;
  private final String uuid;
  private final String phone;
  private final String email;

  private MetadataAuthor(String name, String uuid, String phone, String email) {
    this.name = name;
    this.uuid = uuid;
    this.phone = phone;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getUuid() {
    return uuid;
  }

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }

  public static class Builder {
    private String name;
    private String uuid;
    private String phone;
    private String email;

    private Builder() {

    }

    private Builder(MetadataAuthor src) {
      name = src.name;
      uuid = src.uuid;
      phone = src.phone;
      email = src.email;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder withPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder withEmail(String email) {
      this.email = email;
      return this;
    }

    public MetadataAuthor build() {
      return new MetadataAuthor(name, uuid, phone, email);
    }
  }
}
