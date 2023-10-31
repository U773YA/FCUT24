package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaInfoWrapper {
    private List<MetaInfo> metaRatings;

    public List<MetaInfo> getMetaRatings() {
        return metaRatings;
    }

    public void setMetaRatings(List<MetaInfo> metaRatings) {
        this.metaRatings = metaRatings;
    }
}

