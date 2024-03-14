package edu.colorado.cires.cruisepack.app.migration.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROJECTS")
public class ProjectEntity {

  @Id
  @Column(name = "NAME")
  private String name;
  @Column(name = "USE")
  private String  use;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUse() {
    return use;
  }

  public void setUse(String use) {
    this.use = use;
  }
}
