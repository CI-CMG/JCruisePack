package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import org.springframework.stereotype.Component;

@Component
public class PeopleList {
  
  private final String[] peopleList = new String[]{
      "person-1",
      "person-2"
  };

  public String[] getPeopleList() {
    return peopleList;
  }
}
