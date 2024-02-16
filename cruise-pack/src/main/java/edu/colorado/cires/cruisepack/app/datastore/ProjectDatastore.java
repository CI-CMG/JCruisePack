package edu.colorado.cires.cruisepack.app.datastore;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import edu.colorado.cires.cruisepack.xml.projects.ProjectData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class ProjectDatastore {

  public static final DropDownItem UNSELECTED_PROJECT = new DropDownItem("", "Select Project");

  private final ServiceProperties serviceProperties;
  private List<DropDownItem> projectDropDowns;

  @Autowired
  public ProjectDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path projectFile = dataDir.resolve("projects.xml");
    if (!Files.isRegularFile(projectFile)) {
      throw new IllegalStateException("Unable to read " + projectFile);
    }
    ProjectData projectData;
    try (Reader reader = Files.newBufferedReader(projectFile, StandardCharsets.UTF_8)) {
      projectData = (ProjectData) JAXBContext.newInstance(ProjectData.class).createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + projectFile, e);
    }
    projectDropDowns = new ArrayList<>(projectData.getProjects().getProjects().size() + 1);
    projectDropDowns.add(UNSELECTED_PROJECT);
    projectData.getProjects().getProjects().stream()
        .filter(Project::isUse)
        .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
        .map(ship -> new DropDownItem(ship.getUuid(), ship.getName()))
        .forEach(projectDropDowns::add);
  }

  public List<DropDownItem> getProjectDropDowns() {
    return projectDropDowns;
  }
}
