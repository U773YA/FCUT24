package org.example.enums;

public enum Role {
    CENTRAL_DEFENDER("cb"),
    POWERFUL_DM("power_cdm"),
    DEF_FULLBACK_R("rb"),
    DEF_FULLBACK_L("lb"),
    AGILE_STRIKER("rat"),
    BOX_TO_BOX("b2b"),
    AGILE_DM("agile_cdm"),
    ATT_FULLBACK_R("rwb"),
    ATT_FULLBACK_L("lwb"),
    TARGET_FORWARD("tank"),
    WINGER_R("rw"),
    WINGER_L("lw"),
    PLAYMAKER("cam"),
    COMPLETE_STRIKER("st"),
    KEEPER("gk");

    private final String archetypeId;

    Role(String archetypeId) {
        this.archetypeId = archetypeId;
    }

    public String getArchetypeId() {
        return archetypeId;
    }
}
