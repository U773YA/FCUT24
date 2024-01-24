package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceResult {

    private List<RatingPrices> prices;
    private String status;

    public List<RatingPrices> getPrices() {
        return prices;
    }

    public void setPrices(List<RatingPrices> prices) {
        this.prices = prices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
