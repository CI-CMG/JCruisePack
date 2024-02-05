package edu.colorado.cires.cruisepack.prototype.ui.model;

import java.util.List;
import java.util.UUID;

public class DataWidgetModel {

  private final String id;
  private final String type;
  private final List<String> types;
  private final List<String> instruments;
  private DataWidgetModelCustomizer customizer;

  public DataWidgetModel(String type, List<String> types, List<String> instruments) {
    this.id = UUID.randomUUID().toString();
    this.type = type;
    this.types = types;
    this.instruments = instruments;
  }

  public List<String> getTypes() {
    return types;
  }

  public List<String> getInstruments() {
    return instruments;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public DataWidgetModelCustomizer getCustomizer() {
    return customizer;
  }

  public void setCustomizer(DataWidgetModelCustomizer customizer) {
    this.customizer = customizer;
  }
}
