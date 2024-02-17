package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = AdditionalData.Builder.class)
public class AdditionalData {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(AdditionalData src) {
    return new Builder(src);
  }

  private final String calibrationData;
  private final String calibrationReport;

  private AdditionalData(String calibrationData, String calibrationReport) {
    this.calibrationData = calibrationData;
    this.calibrationReport = calibrationReport;
  }

  public String getCalibrationData() {
    return calibrationData;
  }

  public String getCalibrationReport() {
    return calibrationReport;
  }

  public static class Builder {

    private String calibrationData;
    private String calibrationReport;

    private Builder() {

    }

    private Builder(AdditionalData src) {
      calibrationData = src.calibrationData;
      calibrationReport = src.calibrationReport;
    }

    public Builder withCalibrationData(String calibrationData) {
      this.calibrationData = calibrationData;
      return this;
    }

    public Builder withCalibrationReport(String calibrationReport) {
      this.calibrationReport = calibrationReport;
      return this;
    }

    public AdditionalData build() {
      return new AdditionalData(calibrationData, calibrationReport);
    }
  }
}
