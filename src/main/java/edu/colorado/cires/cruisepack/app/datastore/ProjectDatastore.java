package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import edu.colorado.cires.cruisepack.xml.projects.ProjectData;
import edu.colorado.cires.cruisepack.xml.projects.ProjectList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
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

  public List<DropDownItem> getProjectDropDownsMatchingNames(List<String> names) {
    return projectDropDowns.stream()
        .filter((dd) -> names.contains(dd.getValue()))
        .toList();
  }

  private List<Project> mergeProjects(Optional<ProjectData> defaults, Optional<ProjectData> overrides) {
    Map<String, Project> merged = new HashMap<>(0);
    defaults.map(od -> od.getProjects().getProjects()).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));
    overrides.map(od -> od.getProjects().getProjects()).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));

    return merged.values().stream()
        .sorted((o1, o2) -> o1.getUuid().compareToIgnoreCase(o2.getUuid()))
        .collect(Collectors.toList());
  }

  private Optional<ProjectData> readProjects(String dir) {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve(dir);
    Path peopleFile = dataDir.resolve("projects.xml");
    if (!Files.isRegularFile(peopleFile)) {
      return Optional.empty();
    }
    ProjectData projectData;
    try (Reader reader = Files.newBufferedReader(peopleFile, StandardCharsets.UTF_8)) {
      projectData = (ProjectData) JAXBContext.newInstance(ProjectData.class)
          .createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + peopleFile, e);
    }
    return Optional.of(projectData);
  }

  public void save(Project project) {
    ProjectData newProjectData = new ProjectData();
    ProjectList newProjectList = new ProjectList();
    List<Project> listWithNewProject = newProjectList.getProjects();
    listWithNewProject.add(project);
    newProjectData.setProjects(newProjectList);
    List<Project> mergedProjects = mergeProjects(
        readProjects("local-data"),
        Optional.of(newProjectData)
    );

    ProjectData projectData = new ProjectData();
    projectData.setDataVersion("1.0");
    ProjectList projectList = new ProjectList();
    List<Project> projects = projectList.getProjects();
    projects.addAll(mergedProjects);
    projectData.setProjects(projectList);

    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("local-data");
    Path projectsFile = dataDir.resolve("projects.xml");

    try (OutputStream outputStream = new FileOutputStream(projectsFile.toFile())) {
      JAXB.marshal(projectData, outputStream);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to save drop down items: ", e);
    }

    load();
  }

  // TODO
//  public void save(OrganizationModel organizationModel) {
//    save(organizationFromModel(organizationModel));
//  }

  private void load() {
    //TODO
//    organizations = mergeOrganizations(readOrganizations("data"), readOrganizations("local-data"));
//    List<DropDownItem> items = organizations.stream()
//        .map(o -> new DropDownItem(o.getUuid(), o.getName()))
//        .collect(Collectors.toList());
//    items.add(0, UNSELECTED_ORGANIZATION);
//
//    setOrganizationDropDowns(items);
  }


}
