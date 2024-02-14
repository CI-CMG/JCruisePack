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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SamplingTypesModel other = (SamplingTypesModel) obj;
        if (water != other.water)
            return false;
        if (soilSediment != other.soilSediment)
            return false;
        if (organicTissue != other.organicTissue)
            return false;
        return true;
    }

    
    
}
