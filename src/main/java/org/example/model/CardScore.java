package org.example.model;

import org.example.enums.ChemStyle;
import org.example.enums.Position;
import org.example.enums.Role;

public class CardScore {

    private Integer cardId;
    private Double score;
    private ChemStyle chemStyle;
    private final Role role;

    public CardScore(Integer cardId, Double score, ChemStyle chemStyle, Role role) {
        this.cardId = cardId;
        this.score = score;
        this.chemStyle = chemStyle;
        this.role = role;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ChemStyle getChemStyle() {
        return chemStyle;
    }

    public void setChemStyle(ChemStyle chemStyle) {
        this.chemStyle = chemStyle;
    }

    public Role getRole() {
        return role;
    }
}
