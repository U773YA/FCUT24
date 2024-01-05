package org.example.enums;

public enum League {
    ONEA_PRO_LEAGUE(4),
    THREE_LIGA(2076),
    THREEF_SUPERLIGA(1),
    A_LEAGUE(351),
    ALLSVENSKAN(56),
    BARCLAYS_WSL(2216),
    BUNDESLIGA(19),
    BUNDESLIGA_2(20),
    CALCIO_A_FEMMINILE(2236),
    CESKA_LIGA(319),
    CESKA_LIGA_ZEN(2230),
    CINCH_PREM(50),
    CSL(2012),
    CSSL(189),
    D1_ARKEMA(2218),
    EFL_CHAMPIONSHIP(14),
    EFL_LEAGUE_ONE(60),
    EFL_LEAGUE_TWO(61),
    ELITESERIEN(41),
    EREDIVISIE(10),
    FINNLIIGA(322),
    GPFBL(2215),
    HELLAS_LIGA(63),
    HERO_ISL(2149),
    ICONS(2118),
    K_LEAGUE_1(83),
    LA_LEAGUE_EA_SPORTS(53),
    LALIGA_HYPERMOTION(54),
    LIBERTADORES(1003),
    LIGA_CYPRUS(2210),
    LIGA_F(2222),
    LIGA_HRVATSKA(317),
    LIGA_PORTUGAL(308),
    LIGA_PORTUGAL_FEMININO(2228),
    LIGUE_1_UBER_EATS(16),
    LIGUE_2_BKT(17),
    LPF(353),
    MAGYAR_LIGA(2211),
    MLS(39),
    NEDERLAND_VROUWEN_LIGA(2229),
    NWSL(2221),
    O_BUNDESLIGA(80),
    PKO_BP_EKSTRAKLASA(66),
    PREMIER_LEAGUE(13),
    ROSHN_SAUDI_LEAGUE(350),
    SCHWEIZER_DAMEN_LIGA(2231),
    SCOTTISH_WOMEN_S_LEAGUE(2233),
    SERIA_A_TIM(31),
    SERIE_BKT(32),
    SSE_AIRTRICITY_PD(65),
    SUDAMERICANA(1014),
    SUPERLIGA(330),
    SVERIGE_LIGA(2232),
    TRENDYOL_SUPER_LIG(68),
    UKRAYINA_LIHA(332),
    UNITED_EMIRATES_LEAGUE(2172);

    private final int value;

    League(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static League getLeague(int value) {
        for (League c: League.values()) {
            if (c.value == value) {
                return c;
            }
        }
        return null;
    }
}
