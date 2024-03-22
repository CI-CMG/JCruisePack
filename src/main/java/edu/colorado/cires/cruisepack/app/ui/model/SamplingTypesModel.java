package edu.colorado.cires.cruisepack.app.ui.model;

public class SamplingTypesModel {

    private boolean water = false;
    private boolean soilSediment = false;
    private boolean organicTissue = false;
    
    public boolean isWater() {
        return water;
    }
    public void setWater(boolean water) {
        this.water = water;
    }
    public boolean isSoilSediment() {
        return soilSediment;
    }
    public void setSoilSediment(boolean soilSediment) {
        this.soilSediment = soilSediment;
    }
    public boolean isOrganicTissue() {
        return organicTissue;
    }
    public void setOrganicTissue(boolean organicTissue) {
        this.organicTissue = organicTissue;
    }
    
}
