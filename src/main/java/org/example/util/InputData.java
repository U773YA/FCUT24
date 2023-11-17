package org.example.util;

import org.example.enums.Position;
import org.example.enums.Role;
import org.example.model.CardInput;
import org.example.model.Manager;
import org.example.model.PlayerCard;
import org.example.model.PositionRole;
import org.example.model.Tactic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.example.enums.Position.CAM;
import static org.example.enums.Position.CB;
import static org.example.enums.Position.CDM;
import static org.example.enums.Position.CF;
import static org.example.enums.Position.CM;
import static org.example.enums.Position.GK;
import static org.example.enums.Position.LB;
import static org.example.enums.Position.LM;
import static org.example.enums.Position.LW;
import static org.example.enums.Position.LWB;
import static org.example.enums.Position.RB;
import static org.example.enums.Position.RM;
import static org.example.enums.Position.RW;
import static org.example.enums.Position.RWB;
import static org.example.enums.Position.ST;
import static org.example.enums.Role.AGILE_STRIKER;
import static org.example.enums.Role.ATT_FULLBACK_L;
import static org.example.enums.Role.ATT_FULLBACK_R;
import static org.example.enums.Role.BOX_TO_BOX;
import static org.example.enums.Role.CENTRAL_DEFENDER;
import static org.example.enums.Role.COMPLETE_STRIKER;
import static org.example.enums.Role.DEF_FULLBACK_L;
import static org.example.enums.Role.DEF_FULLBACK_R;
import static org.example.enums.Role.KEEPER;
import static org.example.enums.Role.PLAYMAKER;
import static org.example.enums.Role.POWERFUL_DM;
import static org.example.enums.Role.WINGER_L;
import static org.example.enums.Role.WINGER_R;

public class InputData {
    protected static final List<CardInput> playerCardInputList = new ArrayList<>();
    protected static final Map<Integer, PlayerCard> playerCardMap = new LinkedHashMap<>();
    protected static final List<Tactic> tacticList = new ArrayList<>();
    protected static final List<Manager> managerList = new ArrayList<>();
    protected static final Map<Position, List<Role>> positionRoleMap = new HashMap<>();
    protected static Map<Position, List<Integer>> playerPositionMap = new HashMap<>();
    protected static final List<List<Integer>> cb2 = new ArrayList<>();
    protected static final List<List<Integer>> cb3 = new ArrayList<>();
    protected static final List<List<Integer>> cm2 = new ArrayList<>();
    protected static final List<List<Integer>> st2 = new ArrayList<>();
    protected static final List<List<Integer>> cf2 = new ArrayList<>();
    protected static final List<List<Integer>> cdm2 = new ArrayList<>();
    protected static final List<List<Integer>> cam2 = new ArrayList<>();
    protected static final List<List<Integer>> cm3 = new ArrayList<>();
    protected static final long startTime = System.nanoTime();
    protected static long allTeamsCount = 1;
    protected static long teamCounter = 0;
    protected static List<Integer> mandatoryPlayers = List.of();
    protected static final List<Position> duplicatePositions = List.of(CB, CM, ST, CF, CDM, CAM);

    static {
        positionRoleMap.put(GK, List.of(KEEPER));
        positionRoleMap.put(RB, List.of(DEF_FULLBACK_R));
        positionRoleMap.put(LB, List.of(DEF_FULLBACK_L));
        positionRoleMap.put(CB, List.of(CENTRAL_DEFENDER));
        positionRoleMap.put(RWB, List.of(ATT_FULLBACK_R));
        positionRoleMap.put(LWB, List.of(ATT_FULLBACK_L));
        positionRoleMap.put(CDM, List.of(POWERFUL_DM, BOX_TO_BOX));
        positionRoleMap.put(RM, List.of(WINGER_R, ATT_FULLBACK_R));
        positionRoleMap.put(LM, List.of(WINGER_L, ATT_FULLBACK_L));
        positionRoleMap.put(CM, List.of(BOX_TO_BOX, POWERFUL_DM, PLAYMAKER));
        positionRoleMap.put(RW, List.of(WINGER_R, AGILE_STRIKER));
        positionRoleMap.put(LW, List.of(WINGER_L, AGILE_STRIKER));
        positionRoleMap.put(CAM, List.of(PLAYMAKER, WINGER_R, WINGER_L));
        positionRoleMap.put(CF, List.of(WINGER_R, WINGER_L, COMPLETE_STRIKER, PLAYMAKER, AGILE_STRIKER));
        positionRoleMap.put(ST, List.of(COMPLETE_STRIKER, AGILE_STRIKER));
    }

    protected static void populatePlayerInput() {
        playerCardInputList.add(new CardInput(230, 194765, "GRIEZMANN", 88));
        playerCardInputList.add(new CardInput(336, 226161, "LLORENTE", 84));
        playerCardInputList.add(new CardInput(32, 227125, "KERR", 90));

        playerCardInputList.add(new CardInput(19654, 50512578, "DZEKO RTTK", 89));
        playerCardInputList.add(new CardInput(47, 236479, "MAPI LEON", 89));
        playerCardInputList.add(new CardInput(49, 231866, "RODRI", 89));

        playerCardInputList.add(new CardInput(300, 212622, "KIMMICH", 88));
        playerCardInputList.add(new CardInput(20089, 50596900, "NEVILLE", 88));
//        playerCardInputList.add(new CardInput(106, 226302, "POPP", 88));
//        playerCardInputList.add(new CardInput(397, 227316, "RENARD", 88));
        playerCardInputList.add(new CardInput(70, 218667, "SILVA", 88));

        playerCardInputList.add(new CardInput(190, 227246, "BRONZE", 87));
        playerCardInputList.add(new CardInput(423, 230621, "DONNARUMMA", 87));
//        playerCardInputList.add(new CardInput(19864, 50524277, "IAGO ASPAS FLASHBACK", 87));
        playerCardInputList.add(new CardInput(301, 235073, "KOBEL", 87));
        playerCardInputList.add(new CardInput(20092, 67329561, "MADDISON TRIPLE THREAT", 87));
        playerCardInputList.add(new CardInput(449, 215698, "MAIGNAN", 87));
        playerCardInputList.add(new CardInput(226, 177003, "MODRIC", 87));
        playerCardInputList.add(new CardInput(953302, 223197, "UNAL CENTURIONS EVO", 87, "2-17"));
        playerCardInputList.add(new CardInput(20023, 50495888, "THIAGO SILVA FUT CENTURIONS", 87));

        playerCardInputList.add(new CardInput(19991, 50556019, "BOWEN FUT CENTURIONS", 86));
        playerCardInputList.add(new CardInput(19908, 50532536, "DANILO PEREIRA FUT CENTURIONS", 86));
        playerCardInputList.add(new CardInput(303, 235243, "DE LIGT", 86));
        playerCardInputList.add(new CardInput(462, 211110, "DYBALA", 86));
        playerCardInputList.add(new CardInput(404, 237197, "GEYORO", 86));
//        playerCardInputList.add(new CardInput(236, 186942, "GUNDOGAN", 86));
//        playerCardInputList.add(new CardInput(228, 182521, "KROOS", 86));
        playerCardInputList.add(new CardInput(19956, 50596887, "MCCABE TOTW", 86));
        playerCardInputList.add(new CardInput(268, 223848, "MILINKOVIC-SAVIC", 86));
//        playerCardInputList.add(new CardInput(238, 189513, "PAREJO", 86));
        playerCardInputList.add(new CardInput(19976, 50574782, "RAMAZANI", 86));
        playerCardInputList.add(new CardInput(74, 216267, "ROBERTSON", 86));
        playerCardInputList.add(new CardInput(19854, 50537217, "WARD-PROWSE TRAILBLAZERS", 86));
        playerCardInputList.add(new CardInput(19782, 50528626, "WILSON TRAILBLAZERS", 86));

        playerCardInputList.add(new CardInput(270, 209981, "BOUNOU", 85));
        playerCardInputList.add(new CardInput(2511, 235657, "DALY", 85));
        playerCardInputList.add(new CardInput(76, 237692, "FODEN", 85));
        playerCardInputList.add(new CardInput(332, 247789, "FROHMS", 85));
        playerCardInputList.add(new CardInput(392, 226177, "SAUERBRUNN", 85));
        playerCardInputList.add(new CardInput(105, 246219, "SHAW", 85));
        playerCardInputList.add(new CardInput(81, 186345, "TRIPPIER", 85));
        playerCardInputList.add(new CardInput(19973, 50565815, "VAN DER HEYDEN", 85));
        playerCardInputList.add(new CardInput(2515, 242830, "WALSH", 85));
        playerCardInputList.add(new CardInput(20031, 50530365, "ZAHA TOTW", 85));
//        playerCardInputList.add(new CardInput(163, 265242, "ZINSBERGER", 85));

        playerCardInputList.add(new CardInput(469, 227127, "ALEX REMIRO", 84));
        playerCardInputList.add(new CardInput(387, 267215, "BIXBY", 84));
        playerCardInputList.add(new CardInput(309, 212194, "BRANDT", 84));
        playerCardInputList.add(new CardInput(2524, 247851, "BRUNO GUIMARAES", 84));
        playerCardInputList.add(new CardInput(656148, 244193, "CABRAL CENTURIONS EVOLUTION", 84, "15"));
        playerCardInputList.add(new CardInput(389, 238212, "CAMPBELL", 84));
        playerCardInputList.add(new CardInput(20101, 50578294, "CAQUERET TRIPLE THREAT", 84));
        playerCardInputList.add(new CardInput(19127, 50540066, "CARRASCO", 84));
        playerCardInputList.add(new CardInput(402, 227327, "DABRITZ", 84));
        playerCardInputList.add(new CardInput(331, 202556, "DEPAY", 84));
        playerCardInputList.add(new CardInput(87, 241852, "DIABY", 84));
        playerCardInputList.add(new CardInput(403, 264884, "DUDEK", 84));
//        playerCardInputList.add(new CardInput(262, 209499, "FABINHO", 84));
        playerCardInputList.add(new CardInput(2521, 232580, "GABRIEL", 84));
        playerCardInputList.add(new CardInput(2522, 230666, "GABRIEL JESUS", 84));
        playerCardInputList.add(new CardInput(2527, 251566, "GABRIEL MARTINELLI", 84));
        playerCardInputList.add(new CardInput(311, 207862, "GINTER", 84));
        playerCardInputList.add(new CardInput(313, 210035, "GRIMALDO", 84));
        playerCardInputList.add(new CardInput(314, 185122, "GULACSI", 84));
//        playerCardInputList.add(new CardInput(415, 220814, "HERNANDEZ", 84));
//        playerCardInputList.add(new CardInput(117, 213648, "HOJBJERG", 84));
        playerCardInputList.add(new CardInput(83, 238074, "JAMES", 84));
        playerCardInputList.add(new CardInput(19091, 50566800, "KADIOGLU FOUNDATIONS", 84));
//        playerCardInputList.add(new CardInput(333, 193747, "KOKE", 84));
        playerCardInputList.add(new CardInput(19261, 50569327, "KOLO MUANI", 84));
//        playerCardInputList.add(new CardInput(450, 216435, "LOBOTKA", 84));
//        playerCardInputList.add(new CardInput(452, 198706, "LUIS ALBERTO", 84));
        playerCardInputList.add(new CardInput(19103, 50524153, "LUKAKU", 84));
        playerCardInputList.add(new CardInput(192, 227191, "MARTA TORREJON", 84));
//        playerCardInputList.add(new CardInput(316, 189596, "MULLER", 84));
        playerCardInputList.add(new CardInput(468, 267275, "NAGASATO", 84));
        playerCardInputList.add(new CardInput(19661, 50552822, "ORSIC FOUNDATIONS", 84));
        playerCardInputList.add(new CardInput(455, 210008, "RABIOT", 84));
//        playerCardInputList.add(new CardInput(317, 222492, "SANE", 84));
//        playerCardInputList.add(new CardInput(470, 227410, "SCOTT", 84));
        playerCardInputList.add(new CardInput(456, 189881, "SMALLING", 84));
        playerCardInputList.add(new CardInput(318, 212190, "SULE", 84));
        playerCardInputList.add(new CardInput(487296, 226491, "TIERNEY EVOLUTIONS III", 84, "11"));
//        playerCardInputList.add(new CardInput(2516, 189509, "THIAGO", 84));
        playerCardInputList.add(new CardInput(85, 164240, "THIAGO SILVA", 84));
        playerCardInputList.add(new CardInput(19790, 50571352, "WAGNER TRAILBLAZERS", 84));
        playerCardInputList.add(new CardInput(19087, 50585065, "WILMS FOUNDATIONS", 84));

        playerCardInputList.add(new CardInput(471, 199845, "ACERBI", 83));
        playerCardInputList.add(new CardInput(194, 246630, "AJIBADE", 83));
        playerCardInputList.add(new CardInput(416, 220834, "ASENSIO", 83));
        playerCardInputList.add(new CardInput(473, 247797, "BLUNDELL", 83));
        playerCardInputList.add(new CardInput(497, 224179, "BORJA IGLESIAS", 83));
        playerCardInputList.add(new CardInput(475, 251809, "BOTMAN", 83));
        playerCardInputList.add(new CardInput(479, 266933, "CARUSO", 83));
        playerCardInputList.add(new CardInput(19663, 50542612, "CECCHERINI FOUNDATIONS", 83));
        playerCardInputList.add(new CardInput(481, 213661, "CHRISTENSEN", 83));
        playerCardInputList.add(new CardInput(193, 262531, "CLAUDIA PINA", 83));
        playerCardInputList.add(new CardInput(510, 244260, "DANI OLMO", 83));
        playerCardInputList.add(new CardInput(486, 190460, "ERIKSEN", 83));
        playerCardInputList.add(new CardInput(345, 226998, "ERIKSSON", 83));
        playerCardInputList.add(new CardInput(490, 232077, "FRANCH", 83));
        playerCardInputList.add(new CardInput(359, 261773, "FREIGANG", 83));
        playerCardInputList.add(new CardInput(512, 264240, "GAVI", 83));
        playerCardInputList.add(new CardInput(494, 247513, "GILLES", 83));
        playerCardInputList.add(new CardInput(328, 216460, "GIMENEZ", 83));
        playerCardInputList.add(new CardInput(358, 205693, "HALLER", 83));
        playerCardInputList.add(new CardInput(364, 248715, "HEGERING", 83));
        playerCardInputList.add(new CardInput(496, 178603, "HUMMELS", 83));
        playerCardInputList.add(new CardInput(489, 205498, "JORGINHO", 83));
        playerCardInputList.add(new CardInput(500, 208574, "KOSTIC", 83));
        playerCardInputList.add(new CardInput(426, 193301, "LACAZETTE", 83));
        playerCardInputList.add(new CardInput(118, 167948, "LLORIS", 83));
        playerCardInputList.add(new CardInput(502, 229582, "MANCINI", 83));
        playerCardInputList.add(new CardInput(504, 227346, "MBOCK", 83));
        playerCardInputList.add(new CardInput(329, 201153, "MORATA", 83));
        playerCardInputList.add(new CardInput(246, 200724, "NACHO", 83));
        playerCardInputList.add(new CardInput(20035, 50557507, "NIAKHATE TOTW", 83));
        playerCardInputList.add(new CardInput(509, 226318, "O'HARA", 83));
        playerCardInputList.add(new CardInput(322, 204638, "ORBAN", 83));
        playerCardInputList.add(new CardInput(511, 230142, "OYARZABAL", 83));
        playerCardInputList.add(new CardInput(491, 241464, "PAU TORRES", 83));
        playerCardInputList.add(new CardInput(19618, 50603899, "PATRI OJEDA FOUNDATIONS", 83));
        playerCardInputList.add(new CardInput(515, 246178, "PEYRAUD-MAGNIN", 83));
        playerCardInputList.add(new CardInput(517, 233838, "RAUCH", 83));
        playerCardInputList.add(new CardInput(20119, 50596533, "REVELLI FOUNDATIONS", 83));
        playerCardInputList.add(new CardInput(519, 210413, "ROMAGNOLI", 83));
        playerCardInputList.add(new CardInput(19662, 50529791, "SAMU SAIZ FOUNDATIONS", 83));
//        playerCardInputList.add(new CardInput(522, 234236, "SCHICK", 83));
//        playerCardInputList.add(new CardInput(523, 227405, "SCHMIDT", 83));
//        playerCardInputList.add(new CardInput(524, 262058, "SELLNER", 83));
//        playerCardInputList.add(new CardInput(477, 189511, "SERGIO BUSQUETS", 83));
//        playerCardInputList.add(new CardInput(525, 205988, "SHAW", 83));
//        playerCardInputList.add(new CardInput(526, 230869, "UNAI SIMON", 83));
//        playerCardInputList.add(new CardInput(19794, 50558849, "VICKY LOSADA FOUNDATIONS", 83));
//        playerCardInputList.add(new CardInput(530, 246430, "VLAHOVIC", 83));

        playerCardInputList.add(new CardInput(535, 229237, "AKANJI", 82));
        playerCardInputList.add(new CardInput(539, 261733, "BALTIMORE", 82));
        playerCardInputList.add(new CardInput(121, 227535, "BENTANCUR", 82));
        playerCardInputList.add(new CardInput(252, 248243, "CAMAVINGA", 82));
        playerCardInputList.add(new CardInput(546, 246242, "DIA", 82));
        playerCardInputList.add(new CardInput(548, 226268, "DIMARCO", 82));
        playerCardInputList.add(new CardInput(552, 247811, "ENDO", 82));
        playerCardInputList.add(new CardInput(551, 235410, "EN-NESYRI", 82));
        playerCardInputList.add(new CardInput(624, 241461, "FERRAN TORRES", 82));
        playerCardInputList.add(new CardInput(562, 192318, "GOTZE", 82));
        playerCardInputList.add(new CardInput(563, 241546, "GREBOVAL", 82));
        playerCardInputList.add(new CardInput(564, 212096, "GRIFO", 82));
        playerCardInputList.add(new CardInput(568, 243263, "HATCH", 82));
        playerCardInputList.add(new CardInput(569, 235790, "HAVERTZ", 82));
        playerCardInputList.add(new CardInput(570, 242024, "HUERTA", 82));
        playerCardInputList.add(new CardInput(595, 232498, "ISI", 82));
        playerCardInputList.add(new CardInput(353, 231591, "JAVI GALAN", 82));
        playerCardInputList.add(new CardInput(590, 212814, "JOAO MARIO", 82));
        playerCardInputList.add(new CardInput(537, 223334, "JOELINTON", 82));
        playerCardInputList.add(new CardInput(577, 219522, "LEDESMA", 82));
        playerCardInputList.add(new CardInput(335, 213565, "LEMAR", 82));
        playerCardInputList.add(new CardInput(579, 241671, "LIVAKOVIC", 82));
        playerCardInputList.add(new CardInput(580, 222077, "LOCATELLI", 82));
        playerCardInputList.add(new CardInput(439, 199482, "LOPES", 82));
        playerCardInputList.add(new CardInput(582, 239761, "MAANUM", 82));
        playerCardInputList.add(new CardInput(583, 239837, "MAC ALLISTER", 82));
        playerCardInputList.add(new CardInput(584, 267339, "MACE", 82));
        playerCardInputList.add(new CardInput(267, 234642, "MENDY", 82));
        playerCardInputList.add(new CardInput(352, 233084, "MOLINA", 82));
        playerCardInputList.add(new CardInput(275, 210411, "OTAVIO", 82));
        playerCardInputList.add(new CardInput(598, 236428, "PERISSET", 82));
        playerCardInputList.add(new CardInput(327, 204639, "SAVIC", 82));
        playerCardInputList.add(new CardInput(611, 226985, "SCHOUGH", 82));
        playerCardInputList.add(new CardInput(613, 226991, "SEMBRANT", 82));
        playerCardInputList.add(new CardInput(617, 245839, "SUGITA", 82));
        playerCardInputList.add(new CardInput(618, 236772, "SZOBOSZLAI", 82));
        playerCardInputList.add(new CardInput(625, 244369, "TSYGANKOV", 82));
        playerCardInputList.add(new CardInput(630, 212188, "WERNER", 82));
//        playerCardInputList.add(new CardInput(628, 233751, "VAN DE DONK", 82));
//        playerCardInputList.add(new CardInput(124, 240091, "VICARIO", 82));

        playerCardInputList.add(new CardInput(634, 208920, "AKE", 81));
        playerCardInputList.add(new CardInput(643, 263578, "BALDE", 81));
        playerCardInputList.add(new CardInput(673, 251892, "BETO", 81));
        playerCardInputList.add(new CardInput(645, 265857, "BILBAULT", 81));
        playerCardInputList.add(new CardInput(646, 233364, "BLACKSTENIUS", 81));
        playerCardInputList.add(new CardInput(700, 235944, "BRAIS MENDEZ", 81));
        playerCardInputList.add(new CardInput(648, 228881, "CALABRIA", 81));
        playerCardInputList.add(new CardInput(650, 272010, "CALLIGARIS", 81));
        playerCardInputList.add(new CardInput(90, 229984, "CHILWELL", 81));
        playerCardInputList.add(new CardInput(652, 246172, "CHUKWUEZE", 81));
        playerCardInputList.add(new CardInput(653, 197655, "COATES", 81));
        playerCardInputList.add(new CardInput(656, 264963, "CORBOZ", 81));
        playerCardInputList.add(new CardInput(658, 199304, "DANILO", 81));
        playerCardInputList.add(new CardInput(660, 236276, "DANJUMA", 81));
        playerCardInputList.add(new CardInput(722, 219693, "DIEGO CARLOS", 81));
        playerCardInputList.add(new CardInput(717, 226271, "FABIAN RUIZ", 81));
        playerCardInputList.add(new CardInput(672, 239791, "GLAS", 81));
        playerCardInputList.add(new CardInput(733, 216201, "INAKI WILLIAMS", 81));
        playerCardInputList.add(new CardInput(680, 198219, "INSIGNE", 81));
        playerCardInputList.add(new CardInput(683, 232730, "KAMADA", 81));
        playerCardInputList.add(new CardInput(687, 216354, "KRAMARIC", 81));
        playerCardInputList.add(new CardInput(685, 179645, "KJAER", 81));
        playerCardInputList.add(new CardInput(686, 237678, "KONATE", 81));
        playerCardInputList.add(new CardInput(693, 192563, "LENO", 81));
        playerCardInputList.add(new CardInput(694, 226316, "LEROUX", 81));
        playerCardInputList.add(new CardInput(123, 226226, "LO CELSO", 81));
        playerCardInputList.add(new CardInput(19170, 50553640, "LOZANO", 81));
        playerCardInputList.add(new CardInput(254, 208618, "LUCAS VAZQUEZ", 81));
        playerCardInputList.add(new CardInput(726, 204614, "MARIO RUI", 81));
        playerCardInputList.add(new CardInput(701, 175943, "MERTENS", 81));
        playerCardInputList.add(new CardInput(702, 267272, "MILAZZO", 81));
        playerCardInputList.add(new CardInput(703, 205175, "MILIK", 81));
        playerCardInputList.add(new CardInput(18904, 50586648, "MOFFI TOTW", 81));
        playerCardInputList.add(new CardInput(706, 189575, "MUNIAIN", 81));
        playerCardInputList.add(new CardInput(711, 235659, "PARRIS", 81));
        playerCardInputList.add(new CardInput(120, 243576, "PEDRO PORRO", 81));
        playerCardInputList.add(new CardInput(696, 120533, "PEPE", 81));
        playerCardInputList.add(new CardInput(713, 216409, "POLITANO", 81));
        playerCardInputList.add(new CardInput(715, 168651, "RAKITIC", 81));
        playerCardInputList.add(new CardInput(351, 236045, "REINILDO", 81));
        playerCardInputList.add(new CardInput(719, 244675, "SANCET", 81));
        playerCardInputList.add(new CardInput(723, 233195, "SCHLAGER", 81));
        playerCardInputList.add(new CardInput(19094, 50495235, "SCHMEICHEL", 81));
        playerCardInputList.add(new CardInput(730, 247263, "TAPSOBA", 81));
        playerCardInputList.add(new CardInput(429, 236786, "TERRIER", 81));
        playerCardInputList.add(new CardInput(731, 207421, "TROSSARD", 81));
//        playerCardInputList.add(new CardInput(422, 253306, "UGARTE", 81));
//        playerCardInputList.add(new CardInput(19144, 50543638, "VLACHODIMOS", 81));
        playerCardInputList.add(new CardInput(19155, 50546981, "ZAPATA", 81));
    }

    protected static void populateTactics() {
        int index = 0;
        tacticList.add(index++, new Tactic( "4-1-2-1-2(2)" , Arrays.asList(new PositionRole(GK, KEEPER),
                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
                new PositionRole(CM, BOX_TO_BOX), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CAM, PLAYMAKER)
                , new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-1-3-2" , Arrays.asList(new PositionRole(GK, KEEPER),
                new PositionRole(RB,DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
                new PositionRole(RM, WINGER_R), new PositionRole(CM, BOX_TO_BOX), new PositionRole(LM, WINGER_L),
                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-2-2-2" , Arrays.asList(new PositionRole(GK, KEEPER),
                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
                new PositionRole(CDM, BOX_TO_BOX), new PositionRole(CAM, WINGER_R), new PositionRole(CAM, WINGER_L),
                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
//        tacticList.add(index++, new Tactic( "4-2-4" , Arrays.asList(new PositionRole(GK, KEEPER),
//                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
//                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CM, POWERFUL_DM),
//                new PositionRole(CM, BOX_TO_BOX), new PositionRole(RW, WINGER_R), new PositionRole(LW, WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, COMPLETE_STRIKER))));
//        tacticList.add(index++, new Tactic( "4-3-1-2" , Arrays.asList(new PositionRole(GK, KEEPER),
//                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
//                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CM, BOX_TO_BOX),
//                new PositionRole(CM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CAM,
//                        PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
//        tacticList.add(index++, new Tactic( "4-4-2" , Arrays.asList(new PositionRole(GK, KEEPER),
//                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
//                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(RM, WINGER_R),
//                new PositionRole(CM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(LM, WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
//        tacticList.add(index++, new Tactic( "5-2-1-2" , Arrays.asList(new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R),
//                new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST , COMPLETE_STRIKER),
//                new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "5-1-2-2" , Arrays.asList( new PositionRole(GK , KEEPER),
                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R),
                new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CM ,
                        PLAYMAKER),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(ST , COMPLETE_STRIKER),
                new PositionRole(ST, AGILE_STRIKER) )));

        tacticList.add(index++, new Tactic( "4-1-2-1-2" , Arrays.asList(new PositionRole(GK, KEEPER),
                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
                new PositionRole(RM, WINGER_R), new PositionRole(LM, WINGER_L), new PositionRole(CAM, PLAYMAKER),
                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
//        tacticList.add(index++, new Tactic( "4-4-2(2)" , Arrays.asList(new PositionRole(GK, KEEPER),
//                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
//                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
//                new PositionRole(CDM, BOX_TO_BOX), new PositionRole(RM, WINGER_R), new PositionRole(LM, WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "3-1-4-2" , Arrays.asList(new PositionRole(GK, KEEPER),
                new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
                        CENTRAL_DEFENDER), new PositionRole(CDM, POWERFUL_DM), new PositionRole(RM, ATT_FULLBACK_R),
                new PositionRole(CM, PLAYMAKER), new PositionRole(CM, BOX_TO_BOX),  new PositionRole(LM ,
                        ATT_FULLBACK_L),  new PositionRole(ST , COMPLETE_STRIKER),  new PositionRole(ST,
                        AGILE_STRIKER))));
//        tacticList.add(index++, new Tactic( "3-4-1-2" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB  , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R),
//                new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM ,
//                        ATT_FULLBACK_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST , COMPLETE_STRIKER),
//                new PositionRole(ST, AGILE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "3-5-2" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CDM , POWERFUL_DM),
//                new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(RM , ATT_FULLBACK_R),  new PositionRole(LM ,
//                        ATT_FULLBACK_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST ,
//                        COMPLETE_STRIKER),  new PositionRole(ST, AGILE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "4-1-4-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(RM , WINGER_R),  new PositionRole(CM ,
//                        PLAYMAKER),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-2-3-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(CAM ,
//                        WINGER_R),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(CAM , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "4-2-3-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(RM ,
//                        WINGER_R),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(LM , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "4-3-2-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(CF , WINGER_R),  new PositionRole(CF , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "4-3-3" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER))));
//        tacticList.add(index++, new Tactic( "4-3-3(2)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-3-3(3)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(CM ,
//                        PLAYMAKER),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-3-3(4)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(CAM ,
//                        PLAYMAKER),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-3-3(5)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(RW , AGILE_STRIKER),  new PositionRole(LW , AGILE_STRIKER),
//                new PositionRole(CF, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-4-1-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(LM , WINGER_L),  new PositionRole(CF , PLAYMAKER),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-4-1-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(LM , WINGER_L),  new PositionRole(CAM , PLAYMAKER),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-5-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(LM ,
//                        WINGER_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(CAM , PLAYMAKER),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "4-5-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L),
//                new PositionRole(RM , WINGER_R),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM ,
//                        POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "3-4-2-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R),
//                new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM ,
//                        ATT_FULLBACK_L),  new PositionRole(CF , AGILE_STRIKER),  new PositionRole(CF ,
//                        AGILE_STRIKER),  new PositionRole(ST, COMPLETE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "3-4-3" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R),
//                new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM ,
//                        ATT_FULLBACK_L),  new PositionRole(RW , AGILE_STRIKER),  new PositionRole(ST ,
//                        COMPLETE_STRIKER),  new PositionRole(LW, AGILE_STRIKER ))));
//        tacticList.add(index++, new Tactic( "5-2-2-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R),
//                new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM ,
//                        BOX_TO_BOX),  new PositionRole(RW , AGILE_STRIKER), new PositionRole(LW , AGILE_STRIKER),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index++, new Tactic( "5-4-1" , Arrays.asList( new PositionRole(GK , KEEPER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER),
//                new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R),
//                new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(RM , WINGER_R),  new PositionRole(CM ,
//                        POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L),
//                new PositionRole(ST, COMPLETE_STRIKER) )));
//        tacticList.add(index, new Tactic( "4-2-1-3" , Arrays.asList( new PositionRole(GK, KEEPER),
//                new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB,
//                        CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM),
//                new PositionRole(CDM, BOX_TO_BOX), new PositionRole(CAM, PLAYMAKER), new PositionRole(RW, WINGER_R),
//                new PositionRole(LW, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER))));
    }

    protected static void populateManagers() {
        managerList.add(new Manager("FILIPA PATAO", "Portugal", 2228));
//        managerList.add(new Manager("GARCIA", "Spain", 53));
//        managerList.add(new Manager("JORGE JESUS", "Portugal", 350));
        managerList.add(new Manager("JOSE BORDALAS", "Spain", 53));
        managerList.add(new Manager("K. PITAK", "Czech Republic", 2230));
        managerList.add(new Manager("LUIS CASTRO", "Portugal", 350));
        managerList.add(new Manager("M. BEARD", "England", 2216));
//        managerList.add(new Manager("MENDILIBAR", "Spain", 53));
        managerList.add(new Manager("M. ALVINI", "Italy", 32));
        managerList.add(new Manager("M. KOLLNER", "Germany", 2076));
        managerList.add(new Manager("N. JELECAK", "Bosnia and Herzegovina", 2218));
//        managerList.add(new Manager("RAFA BENITEZ", "Spain", 53));
        managerList.add(new Manager("R. LUCESCU", "Romania", 63));
        managerList.add(new Manager("R. SCHMIDT", "Germany", 308));
        managerList.add(new Manager("R. VIDOSIC", "Croatia", 351));
        managerList.add(new Manager("X. MASSASWABI", "Gambia", 31));
    }
}
