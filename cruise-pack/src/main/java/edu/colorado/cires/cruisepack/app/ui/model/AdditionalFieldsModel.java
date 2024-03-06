package edu.colorado.cires.cruisepack.app.ui.model;

import java.util.Map;

public abstract class AdditionalFieldsModel extends PropertyChangeModel {
  
    protected abstract void clearErrors();
    protected abstract void setError(String propertyPath, String message);
    public abstract Map<String, Object> transform();

}
