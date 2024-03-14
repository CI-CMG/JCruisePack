package edu.colorado.cires.cruisepack.app.migration.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORGANIZATONS")
public class OrganizationEntity {
  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "STREET")
  private String street;
  @Column(name = "CITY")
  private String city;
  @Column(name = "STATE")
  private String state;
  @Column(name = "ZIP")
  private String zip;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "UUID")
  private String uuid;
  @Column(name = "USE")
  private String use;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUse() {
    return use;
  }

  public void setUse(String use) {
    this.use = use;
  }
}
