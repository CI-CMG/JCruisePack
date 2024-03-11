package edu.colorado.cires.cruisepack.app.service.metadata;

public enum ExpectedAnalyses {

    BARCODING("Barcoding"),
    GENOMICS("Genomics"),
    TRANSCRIPOMICS("Transcriptomics"),
    PROTEOMICS("Proteomics"),
    METABOLOMICS("Metabolomics"),
    EPIGENETICS("Epigenetics"),
    OTHER("Other"),
    METABARCODING("Metabarcoding"),
    METAGENOMICS("Metagenomics"),
    METATRANSCRIPTOMICS("Metatranscriptomics"),
    METAPROTEOMICS("Metaproteomics"),
    METAMETABOLOMICS("Metametabolomics"),
    MICROBIOME("Microbiome");

    private final String name;

    ExpectedAnalyses(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
