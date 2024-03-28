package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import edu.colorado.cires.cruisepack.xml.projects.ProjectData;
import edu.colorado.cires.cruisepack.xml.projects.ProjectList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProjectDatastoreTest extends OverridableXMLDatastoreTest<ProjectData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final ProjectDatastore datastore = new ProjectDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();
    
    assertThrows(IllegalStateException.class, () -> readFile(ProjectData.class)); // no default file
    
    assertTrue(readLocalFile(ProjectData.class).getProjects().getProjects().isEmpty());
  }

  @Test
  void save() {
    datastore.init();
    
    Project project = createProject("1", true);
    datastore.save(project);
    
    assertTrue(datastore.getAllProjectDropDowns().stream().anyMatch(
        d -> d.getId().equals(project.getUuid()) && d.getValue().equals(project.getName())
    ));

    assertThrows(IllegalStateException.class, () -> readFile(ProjectData.class)); // no default file

    Optional<Project> maybeSavedProject = readLocalFile(ProjectData.class).getProjects().getProjects().stream()
        .filter(p -> p.getUuid().equals(project.getUuid()) && p.getName().equals(project.getName()))
        .findFirst();
    assertTrue(maybeSavedProject.isPresent());
    
    Project savedProject = maybeSavedProject.get();
    assertEquals(project.getUuid(), savedProject.getUuid());
    assertEquals(project.getName(), savedProject.getName());
  }

  @Test
  void findByName() {
    datastore.init();

    Project project = createProject("1", true);
    datastore.save(project);
    
    Optional<Project> maybeProject = datastore.findByName(project.getName());
    assertTrue(maybeProject.isPresent());
    assertEquals(project.getName(), maybeProject.get().getName());
    
    assertTrue(datastore.findByName("TEST").isEmpty());
  }

  private static Project createProject(String suffix, boolean use) {
    Project project = new Project();
    
    project.setUuid(String.format("uuid-%s", suffix));
    project.setName(String.format("project-name-%s", suffix));
    project.setUse(use);
    
    return project;
  }

  @Override
  protected ProjectData createDataObject() {
    ProjectData data = new ProjectData();
    data.setDataVersion("1.0");
    data.setProjects(new ProjectList());
    return data;
  }

  @Override
  protected String getXMLFilename() {
    return "projects.xml";
  }
}