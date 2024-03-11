package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import org.springframework.stereotype.Component;

@Component
public class OrganizationList {
  
  private final String[] organizationList = new String[]{
      "organization-1",
      "organization-2"
  };

  public String[] getOrganizationList() {
    return organizationList;
  }
}
