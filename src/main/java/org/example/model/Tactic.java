package org.example.model;

import java.util.List;

public class Tactic {

    private String name;
    private List<PositionRole> positionRoles;
    private long teamCount;

    public Tactic(String name, List<PositionRole> positionRoles) {
        this.name = name;
        this.positionRoles = positionRoles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PositionRole> getPositionRoles() {
        return positionRoles;
    }

    public void setPositionRoles(List<PositionRole> positionRoles) {
        this.positionRoles = positionRoles;
    }

    public long getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(long teamCount) {
        this.teamCount = teamCount;
    }
}