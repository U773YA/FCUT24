package org.example.model;

import org.example.enums.ChemStyle;

public class DraftInput extends CardInput{

    private ChemStyle chemStyle;


    public DraftInput(Integer futBinId, Integer easySbcId, String name, Integer rating, ChemStyle chemStyle) {
        super(futBinId, easySbcId, name, rating);
        this.chemStyle = chemStyle;
    }

    public ChemStyle getChemStyle() {
        return chemStyle;
    }
}
