package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DatabaseObjectMapperFactory {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .setDefaultPropertyInclusion(Include.NON_NULL)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

}
