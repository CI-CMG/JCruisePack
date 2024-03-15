package edu.colorado.cires.cruisepack.app.migration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SqliteMigratorTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .setDefaultPropertyInclusion(Include.NON_NULL)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
      .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);


  @Test
  public void test() throws Exception {
    Path oldCp = Paths.get("src/test/resources/oldcp");
    Path expected = oldCp.resolve("expected");

    CruiseDataDatastore cruiseDataDatastore = mock(CruiseDataDatastore.class);
    OrganizationDatastore organizationDatastore = mock(OrganizationDatastore.class);
    PersonDatastore personDatastore = mock(PersonDatastore.class);
    ProjectDatastore projectDatastore = mock(ProjectDatastore.class);
    SqliteMigrator migrator = new SqliteMigrator(
        objectMapper,
        cruiseDataDatastore,
        organizationDatastore,
        personDatastore,
        projectDatastore);
    migrator.migrate(oldCp);

    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2120.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2102.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("Cruise01.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("test4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2126.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1506_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1506_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1603_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1603_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_5.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1701.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1703.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1907.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2006.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2102_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2102_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2103_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2103_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("sdsdsd_tttttt.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("sdf_sdd.json").toFile(), CruiseData.class));

    verifyNoMoreInteractions(cruiseDataDatastore);
//    verifyNoMoreInteractions(cruiseDataDatastore, organizationDatastore, personDatastore, projectDatastore);

  }
}