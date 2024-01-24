package org.example;

import org.example.enums.ChemStyle;
import org.example.model.DraftInput;
import org.example.model.PlayerCard;
import org.example.model.PositionRole;
import org.example.model.Tactic;
import org.example.util.Scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

public class FUTDraft {

    private static final List<Tactic> tacticList = new ArrayList<>();
    private static final List<PlayerCard> playerPool = new ArrayList<>();

    public static void main( String[] args ) throws IOException {
        Scanner myObj = new Scanner(System.in);
        Scraper scraper = new Scraper();

        populateTactics();
        Tactic tactic = tacticList.stream().filter(t -> t.getName().equals("4-4-2")).findFirst().get();

        System.out.println("Enter futBinId, easySbcId, name, rating, chemistryStyle for 5 players for captain choice:");
        List<DraftInput> cardChoices = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            Integer futBinId = myObj.nextInt();
            Integer easySbcId = myObj.nextInt();
            String name = myObj.next();
            Integer rating = myObj.nextInt();
            ChemStyle chemStyle = ChemStyle.getChemStyle(myObj.nextInt());
            DraftInput draftInput = new DraftInput(futBinId, easySbcId, name, rating, chemStyle);
            cardChoices.add(draftInput);
        }

        List<PlayerCard> playerCardList = new ArrayList<>();
        for (DraftInput draftInput : cardChoices) {
            PlayerCard playerCard = scraper.getCardData(draftInput);
            playerCard.setFutBinId(draftInput.getFutBinId());
            playerCard.setEasySbcId(draftInput.getEasySbcId());
            playerCard.setName(draftInput.getName());
            playerCard.setRating(draftInput.getRating());
            playerCard.setEvoId(draftInput.getEvoId());
            playerCard.setChemStyle(draftInput.getChemStyle());
            playerCardList.add(playerCard);
        }


    }

    protected static void populateTactics() {
        int index = 0;
        tacticList.add(index++, new Tactic( "3-1-4-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CDM, POWERFUL_DM), new PositionRole(RM, ATT_FULLBACK_R), new PositionRole(CM, PLAYMAKER), new PositionRole(CM, BOX_TO_BOX),  new PositionRole(LM , ATT_FULLBACK_L),  new PositionRole(ST , COMPLETE_STRIKER),  new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "3-4-1-2" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB  , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R), new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , ATT_FULLBACK_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST , COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER ))));
        tacticList.add(index++, new Tactic( "3-4-2-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R), new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , ATT_FULLBACK_L),  new PositionRole(CF , AGILE_STRIKER),  new PositionRole(CF , AGILE_STRIKER),  new PositionRole(ST, COMPLETE_STRIKER ))));
        tacticList.add(index++, new Tactic( "3-4-3" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RM , ATT_FULLBACK_R), new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , ATT_FULLBACK_L),  new PositionRole(RW , AGILE_STRIKER),  new PositionRole(ST , COMPLETE_STRIKER),  new PositionRole(LW, AGILE_STRIKER ))));
        tacticList.add(index++, new Tactic( "3-5-2" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CDM , POWERFUL_DM), new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(RM , ATT_FULLBACK_R),  new PositionRole(LM , ATT_FULLBACK_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST , COMPLETE_STRIKER),  new PositionRole(ST, AGILE_STRIKER ))));
        tacticList.add(index++, new Tactic( "4-1-2-1-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(RM, WINGER_R), new PositionRole(LM, WINGER_L), new PositionRole(CAM, PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-1-2-1-2(2)" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CAM, PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-1-3-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB,DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(RM, WINGER_R), new PositionRole(CM, BOX_TO_BOX), new PositionRole(LM, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-1-4-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(RM , WINGER_R),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-2-1-3" , Arrays.asList( new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(CDM, BOX_TO_BOX), new PositionRole(CAM, PLAYMAKER), new PositionRole(RW, WINGER_R), new PositionRole(LW, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-2-2-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(CDM, BOX_TO_BOX), new PositionRole(CAM, WINGER_R), new PositionRole(CAM, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-2-3-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(CAM , WINGER_R),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(CAM , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER ))));
        tacticList.add(index++, new Tactic( "4-2-3-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(RM , WINGER_R),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(LM , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER ))));
        tacticList.add(index++, new Tactic( "4-2-4" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(RW, WINGER_R), new PositionRole(LW, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, COMPLETE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-3-1-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CM, BOX_TO_BOX), new PositionRole(CM, POWERFUL_DM), new PositionRole(CAM, PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-3-2-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(CF , WINGER_R),  new PositionRole(CF , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER ))));
        tacticList.add(index++, new Tactic( "4-3-3" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-3-3(2)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-3-3(3)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CDM , BOX_TO_BOX),  new PositionRole(CM , PLAYMAKER),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-3-3(4)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(RW , WINGER_R),  new PositionRole(LW , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-3-3(5)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(CDM , POWERFUL_DM),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(RW , AGILE_STRIKER),  new PositionRole(LW , AGILE_STRIKER), new PositionRole(CF, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-4-1-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L),  new PositionRole(CF , PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-4-1-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L),  new PositionRole(CAM , PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-4-2" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(RM, WINGER_R), new PositionRole(CM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(LM, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-4-2(2)" , Arrays.asList(new PositionRole(GK, KEEPER), new PositionRole(RB, DEF_FULLBACK_R), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(LB, DEF_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(CDM, BOX_TO_BOX), new PositionRole(RM, WINGER_R), new PositionRole(LM, WINGER_L), new PositionRole(ST, COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "4-5-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(LM , WINGER_L),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(CAM , PLAYMAKER), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "4-5-1(2)" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(RB , DEF_FULLBACK_R),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(LB , DEF_FULLBACK_L), new PositionRole(RM , WINGER_R),  new PositionRole(CM , PLAYMAKER),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "5-2-1-2" , Arrays.asList(new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R), new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(CAM , PLAYMAKER),  new PositionRole(ST , COMPLETE_STRIKER), new PositionRole(ST, AGILE_STRIKER))));
        tacticList.add(index++, new Tactic( "5-2-2-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R), new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(RW , AGILE_STRIKER), new PositionRole(LW , AGILE_STRIKER), new PositionRole(ST, COMPLETE_STRIKER) )));
        tacticList.add(index++, new Tactic( "5-2-3" , Arrays.asList( new PositionRole(GK, KEEPER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(RWB, ATT_FULLBACK_R), new PositionRole(LWB, ATT_FULLBACK_L), new PositionRole(CM, POWERFUL_DM), new PositionRole(CM, BOX_TO_BOX), new PositionRole(RW, AGILE_STRIKER), new PositionRole(LW, AGILE_STRIKER), new PositionRole(ST, COMPLETE_STRIKER))));
        tacticList.add(index++, new Tactic( "5-3-2" , Arrays.asList( new PositionRole(GK, KEEPER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(CB, CENTRAL_DEFENDER), new PositionRole(RWB, ATT_FULLBACK_R), new PositionRole(LWB, ATT_FULLBACK_L), new PositionRole(CDM, POWERFUL_DM), new PositionRole(CM, PLAYMAKER), new PositionRole(CM, BOX_TO_BOX), new PositionRole(ST, AGILE_STRIKER), new PositionRole(ST, COMPLETE_STRIKER))));
        tacticList.add(index++, new Tactic( "5-4-1" , Arrays.asList( new PositionRole(GK , KEEPER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(CB , CENTRAL_DEFENDER), new PositionRole(CB , CENTRAL_DEFENDER),  new PositionRole(RWB , ATT_FULLBACK_R), new PositionRole(LWB , ATT_FULLBACK_L),  new PositionRole(RM , WINGER_R),  new PositionRole(CM , POWERFUL_DM),  new PositionRole(CM , BOX_TO_BOX),  new PositionRole(LM , WINGER_L), new PositionRole(ST, COMPLETE_STRIKER) )));
    }
}
