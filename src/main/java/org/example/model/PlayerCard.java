package org.example.model;

import org.example.enums.Position;

import java.util.ArrayList;
import java.util.List;

public class PlayerCard {
    private Integer futBinId;
    private Integer easySbcId;
    private String name;
    private Integer rating;
    private String evoId;
    private String nation;
    private Integer id;
    private Integer clubId;
    private Integer leagueId;
    private List<Position> positions = new ArrayList<>();
    private List<MetaInfo> metaInfoList = new ArrayList<>();

    public Integer getFutBinId() {
        return futBinId;
    }

    public void setFutBinId(Integer futBinId) {
        this.futBinId = futBinId;
    }

    public Integer getEasySbcId() {
        return easySbcId;
    }

    public void setEasySbcId(Integer easySbcId) {
        this.easySbcId = easySbcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getEvoId() {
        return evoId;
    }

    public void setEvoId(String evoId) {
        this.evoId = evoId;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<MetaInfo> getMetaInfoList() {
        return metaInfoList;
    }

    public void setMetaInfoList(List<MetaInfo> metaInfoList) {
        this.metaInfoList = metaInfoList;
    }
}
