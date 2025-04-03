package edu.colorado.cires.cruisepack.app.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.data.Project;
import edu.colorado.cires.cruisepack.data.ProjectData;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectDatastore extends PropertyChangeModel {
  public static final DropDownItem UNSELECTED_PROJECT = new DropDownItem("", "Select Project");

  private final ServiceProperties serviceProperties;
  private List<DropDownItem> projectDropDowns;
  private  List<Project> projects;
  private final ObjectMapper objectMapper;

  @Autowired
  public ProjectDatastore(ServiceProperties serviceProperties, ObjectMapper objectMapper) {
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void init() {
    load();
  }


  private void load() {
    projects = mergeProjects(Optional.empty(), readProjects("local-data"));
    List<DropDownItem> items = projects.stream()
        .map(o -> new DropDownItem(o.getUuid(), o.getName()))
        .collect(Collectors.toList());
    items.add(0, UNSELECTED_PROJECT);

    setProjectDropDowns(items);
  }

  private void setProjectDropDowns(List<DropDownItem> items) {
    setIfChanged(Events.UPDATE_PROJECT_DATA_STORE, items, () -> new ArrayList<DropDownItem>(), (i) -> this.projectDropDowns = i);
  }

  public List<DropDownItem> getAllProjectDropDowns() {
    return projectDropDowns;
  }

  public void save(Project project) {
    ProjectData newProjectData = new ProjectData();
    List<Project> listWithNewProject = new ArrayList<>();
    listWithNewProject.add(project);
    newProjectData.setProjects(listWithNewProject);
    List<Project> mergedProjects = mergeProjects(
        readProjects("local-data"),
        Optional.of(newProjectData)
    );

    ProjectData projectData = new ProjectData();
    projectData.setDataVersion("1.0");
    List<Project> projects = new ArrayList<>();
    projects.addAll(
        mergedProjects
    );
    projectData.setProjects(projects);

    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("local-data");
    Path projectsFile = dataDir.resolve("projects.json");

    try {
      objectMapper.writeValue(projectsFile.toFile(), newProjectData);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to save drop down items: ", e);
    }

    load();
  }


  private List<Project> mergeProjects(Optional<ProjectData> defaults, Optional<ProjectData> overrides) {
    Map<String, Project> merged = new HashMap<>(0);
    defaults.map(ProjectData::getProjects).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));
    overrides.map(ProjectData::getProjects).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));

    return merged.values().stream()
        .sorted((o1, o2) -> o1.getUuid().compareToIgnoreCase(o2.getUuid()))
        .collect(Collectors.toList());
  }

  private Optional<ProjectData> readProjects(String dir) {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve(dir);
    Path projectsFile = dataDir.resolve("projects.json");
    if (!Files.isRegularFile(projectsFile)) {
      return Optional.empty();
    }

    try {
      return Optional.of(objectMapper.readValue(projectsFile.toFile(), ProjectData.class));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse " + projectsFile, e);
    }
  }

  public Optional<Project> findByName(String name) {
    return projects.stream()
        .filter(o -> o.getName().equals(name))
        .findFirst();
  }
}
