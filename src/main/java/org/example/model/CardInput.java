package org.example.model;

public class CardInput {

    private final Integer futBinId;
    private final Integer easySbcId;
    private final String name;
    private final Integer rating;
    private final String evoId;

    public CardInput(Integer futBinId, Integer easySbcId, String name, Integer rating) {
        this.futBinId = futBinId;
        this.easySbcId = easySbcId;
        this.name = name;
        this.rating = rating;
        this.evoId = "";
    }

    public CardInput(Integer futBinId, Integer easySbcId, String name, Integer rating, String evoId) {
        this.futBinId = futBinId;
        this.easySbcId = easySbcId;
        this.name = name;
        this.rating = rating;
        this.evoId = evoId;
    }

    public Integer getFutBinId() {
        return futBinId;
    }

    public Integer getEasySbcId() {
        return easySbcId;
    }

    public String getName() {
        return name;
    }

    public Integer getRating() {
        return rating;
    }

    public String getEvoId() {
        return evoId;
    }
}
