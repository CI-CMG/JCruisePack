package edu.colorado.cires.cruisepack.app.ui.model;

public class ExpectedAnalysesModel {

    private boolean barcoding = false;
    private boolean genomics = false;
    private boolean transcriptomics = false;
    private boolean proteomics = false;
    private boolean metabolomics = false;
    private boolean epigenetics = false;
    private boolean other = false;
    private boolean metabarcoding = false;
    private boolean metagenomics = false;
    private boolean metatranscriptomics = false;
    private boolean metaproteomics = false;
    private boolean metametabolomics = false;
    private boolean microbiome = false;
    
    public boolean isBarcoding() {
        return barcoding;
    }
    public void setBarcoding(boolean barcoding) {
        this.barcoding = barcoding;
    }
    public boolean isGenomics() {
        return genomics;
    }
    public void setGenomics(boolean genomics) {
        this.genomics = genomics;
    }
    public boolean isTranscriptomics() {
        return transcriptomics;
    }
    public void setTranscriptomics(boolean transcriptomics) {
        this.transcriptomics = transcriptomics;
    }
    public boolean isProteomics() {
        return proteomics;
    }
    public void setProteomics(boolean proteomics) {
        this.proteomics = proteomics;
    }
    public boolean isMetabolomics() {
        return metabolomics;
    }
    public void setMetabolomics(boolean metabolomics) {
        this.metabolomics = metabolomics;
    }
    public boolean isEpigenetics() {
        return epigenetics;
    }
    public void setEpigenetics(boolean epigenetics) {
        this.epigenetics = epigenetics;
    }
    public boolean isOther() {
        return other;
    }
    public void setOther(boolean other) {
        this.other = other;
    }
    public boolean isMetabarcoding() {
        return metabarcoding;
    }
    public void setMetabarcoding(boolean metabarcoding) {
        this.metabarcoding = metabarcoding;
    }
    public boolean isMetagenomics() {
        return metagenomics;
    }
    public void setMetagenomics(boolean metagenomics) {
        this.metagenomics = metagenomics;
    }
    public boolean isMetatranscriptomics() {
        return metatranscriptomics;
    }
    public void setMetatranscriptomics(boolean metatranscriptomics) {
        this.metatranscriptomics = metatranscriptomics;
    }
    public boolean isMetaproteomics() {
        return metaproteomics;
    }
    public void setMetaproteomics(boolean metaproteomics) {
        this.metaproteomics = metaproteomics;
    }
    public boolean isMetametabolomics() {
        return metametabolomics;
    }
    public void setMetametabolomics(boolean metametabolomics) {
        this.metametabolomics = metametabolomics;
    }
    public boolean isMicrobiome() {
        return microbiome;
    }
    public void setMicrobiome(boolean microbiome) {
        this.microbiome = microbiome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpectedAnalysesModel other = (ExpectedAnalysesModel) obj;
        if (barcoding != other.barcoding)
            return false;
        if (genomics != other.genomics)
            return false;
        if (transcriptomics != other.transcriptomics)
            return false;
        if (proteomics != other.proteomics)
            return false;
        if (metabolomics != other.metabolomics)
            return false;
        if (epigenetics != other.epigenetics)
            return false;
        if (this.other != other.other)
            return false;
        if (metabarcoding != other.metabarcoding)
            return false;
        if (metagenomics != other.metagenomics)
            return false;
        if (metatranscriptomics != other.metatranscriptomics)
            return false;
        if (metaproteomics != other.metaproteomics)
            return false;
        if (metametabolomics != other.metametabolomics)
            return false;
        if (microbiome != other.microbiome)
            return false;
        return true;
    }

    
    
}
