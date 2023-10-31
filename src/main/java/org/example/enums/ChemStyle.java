package org.example.enums;

public enum ChemStyle {
    BASIC(250),
    SNIPER(251),
    FINISHER(252),
    DEADEYE(253),
    MARKSMAN(254),
    HAWK(255),
    ARTIST(256),
    ARCHITECT(257),
    POWERHOUSE(258),
    MAESTRO(259),
    ENGINE(260),
    SENTINEL(261),
    GUARDIAN(262),
    GLADIATOR(263),
    BACKBONE(264),
    ANCHOR(265),
    HUNTER(266),
    CATALYST(267),
    SHADOW(268),
    WALL(269),
    SHIELD(270),
    CAT(271),
    GLOVE(272),
    BASIC_GK(273);

    private final int value;

    ChemStyle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChemStyle getChemStyle(int value) {
        for (ChemStyle c: ChemStyle.values()) {
            if (c.value == value) {
                return c;
            }
        }
        return null;
    }
}
