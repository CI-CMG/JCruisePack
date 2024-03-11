package edu.colorado.cires.cruisepack.app.service.metadata;

public enum SamplingTypes {

    WATER("Water"),
    SOIL_SEDIMENT("Soil/Sediment"),
    ORGANIC_TISSUE("Organic Tissue");

    private final String name;

    SamplingTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
