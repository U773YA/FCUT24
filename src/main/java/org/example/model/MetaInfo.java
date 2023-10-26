package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaInfo {
    private double metaRating;
    private Integer chemstyleId;
    private String archetypeId;
    private Integer chemistry;
    @JsonProperty("isBestChemstyleAtChem")
    private boolean isBestChemstyleAtChem;

    public MetaInfo() {
    }

    public MetaInfo(double metaRating, Integer chemstyleId, String archetypeId, Integer chemistry, boolean isBestChemstyleAtChem) {
        this.metaRating = metaRating;
        this.chemstyleId = chemstyleId;
        this.archetypeId = archetypeId;
        this.chemistry = chemistry;
        this.isBestChemstyleAtChem = isBestChemstyleAtChem;
    }

    public double getMetaRating() {
        return metaRating;
    }

    public Integer getChemstyleId() {
        return chemstyleId;
    }

    public String getArchetypeId() {
        return archetypeId;
    }

    public Integer getChemistry() {
        return chemistry;
    }

    public boolean isBestChemstyleAtChem() {
        return isBestChemstyleAtChem;
    }
}
