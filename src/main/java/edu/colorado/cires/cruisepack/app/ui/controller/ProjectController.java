package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.ProjectModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectController implements PropertyChangeListener {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final ProjectModel projectModel;
  private final Validator validator;
  private final ProjectDatastore projectDatastore;

  @Autowired
  public ProjectController(ReactiveViewRegistry reactiveViewRegistry, ProjectModel projectModel, Validator validator,
      ProjectDatastore projectDatastore) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.projectModel = projectModel;
    this.validator = validator;
    this.projectDatastore = projectDatastore;
  }
  
  @PostConstruct
  public void init() {
    projectModel.addChangeListener(this);
  }
  
  public String getName() {
    return projectModel.getName();
  }
  
  public void setName(String name) {
    projectModel.setName(name);
  }
  
  public String getUUID() {
    return projectModel.getUuid();
  }
  
  public void setUUID(String uuid) {
    projectModel.setUuid(uuid);
  }
  
  public boolean isUse() {
    return projectModel.isUse();
  }
  
  public void setUse(boolean use) {
    projectModel.setUse(use);
  }
  
  public void restoreDefaults() {
    projectModel.restoreDefaults();
  }
  
  public void saveProject() {
    projectModel.clearErrors();
    Set<ConstraintViolation<ProjectModel>> constraintViolations = validator.validate(
        projectModel
    );
    
    if (constraintViolations.isEmpty()) {
      Optional<Project> project = projectDatastore.findByName(projectModel.getName());
      if (project.isEmpty()) {
        projectModel.setUuid(UUID.randomUUID().toString());
        
        Project newProject = new Project();
        newProject.setName(projectModel.getName());
        newProject.setUuid(projectModel.getUuid());
        newProject.setUse(projectModel.isUse());
        
        projectDatastore.save(newProject);
      } else {
        Project existingProject = project.get();
        if (projectModel.getUuid() == null || !projectModel.getUuid().equals(existingProject.getUuid())) {
          projectModel.emitSaveFailure(String.format(
              "Project already exists with name: %s",
              projectModel.getName()
          ));
        } else {
          Project updatedProject = new Project();
          updatedProject.setUuid(existingProject.getUuid());
          updatedProject.setName(projectModel.getName());
          updatedProject.setUse(projectModel.isUse());
          
          projectDatastore.save(updatedProject);
        }
      }
      
    } else {
      projectModel.setErrors(constraintViolations);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }
}
