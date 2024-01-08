package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ChemStyle;
import org.example.enums.Position;
import org.example.enums.Role;
import org.example.model.CardInput;
import org.example.model.CardScore;
import org.example.model.MetaInfo;
import org.example.model.PlayerCard;
import org.example.model.PositionRole;
import org.example.util.InputData;
import org.example.util.Scraper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.enums.ChemStyle.GLOVE;
import static org.example.enums.Position.CAM;
import static org.example.enums.Position.CB;
import static org.example.enums.Position.CF;
import static org.example.enums.Position.CM;
import static org.example.enums.Position.GK;
import static org.example.enums.Position.ST;
import static org.example.enums.Role.AGILE_STRIKER;
import static org.example.enums.Role.BOX_TO_BOX;
import static org.example.enums.Role.CENTRAL_DEFENDER;
import static org.example.enums.Role.COMPLETE_STRIKER;
import static org.example.enums.Role.PLAYMAKER;
import static org.example.util.CombinationHelper.getCombinations;

public class FodderCalculator extends InputData {

    private static final Logger LOG = LogManager.getLogger(PlayerCard.class);
    public static Map<Integer, PlayerCard> dbPlayerCardMap = new HashMap<>();
    public static List<PlayerCard> dbPlayerCardEvoList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        populatePlayerInput();

        File file = new File("playerCardMap.json");
        if (file.exists()) {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(file)) {
                dbPlayerCardMap = gson.fromJson(reader, new TypeToken<HashMap<Integer, PlayerCard>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // handle the case when the file does not exist
            System.out.println("File 'playerCardMap.json' does not exist.");
        }

        file = new File("playerCardEvoList.json");
        if (file.exists()) {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(file)) {
                dbPlayerCardEvoList = gson.fromJson(reader, new TypeToken<ArrayList<PlayerCard>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // handle the case when the file does not exist
            System.out.println("File 'playerCardEvoList.json' does not exist.");
        }

        Scraper scraper = new Scraper();
        for (CardInput cardInput : playerCardInputList) {
            PlayerCard playerCard;
            boolean isEvo =
                    cardInput.getEvoId() != null && !cardInput.getEvoId().isBlank() && !cardInput.getEvoId().isEmpty();
            if (isEvo) {
                List<PlayerCard> playerEvolutions =
                        dbPlayerCardEvoList.stream().filter(p -> Objects.equals(p.getFutBinId(), cardInput.getFutBinId())).toList();
                PlayerCard evoPlayerCard = playerEvolutions.stream().filter(p -> Objects.equals(p.getEvoId(),
                        cardInput.getEvoId())).findFirst().orElse(null);
                if (evoPlayerCard != null) {
                    playerCard = evoPlayerCard;
                } else {
                    playerCard = scraper.getCardData(cardInput);
                }
            } else if (dbPlayerCardMap.containsKey(cardInput.getFutBinId())) {
                playerCard = dbPlayerCardMap.get(cardInput.getFutBinId());
            } else {
                playerCard = scraper.getCardData(cardInput);
            }
            playerCard.setFutBinId(cardInput.getFutBinId());
            playerCard.setEasySbcId(cardInput.getEasySbcId());
            playerCard.setName(cardInput.getName());
            playerCard.setRating(cardInput.getRating());
            playerCard.setEvoId(cardInput.getEvoId());
            playerCardMap.put(playerCard.getFutBinId(), playerCard);
            System.out.println("Fetched data for card: " + cardInput.getName());
        }

        for (Map.Entry<Integer, PlayerCard> playerCard : playerCardMap.entrySet()) {
            if (!dbPlayerCardMap.containsKey(playerCard.getKey())) {
                dbPlayerCardMap.put(playerCard.getKey(), playerCard.getValue());
            }
        }
        Gson gson3 = new Gson();
        try (Writer writer = new FileWriter("playerCardMap.json")) {
            gson3.toJson(dbPlayerCardMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, PlayerCard> playerCard : playerCardMap.entrySet()) {
            boolean isEvo =
                    playerCard.getValue().getEvoId() != null && !playerCard.getValue().getEvoId().isBlank() && !playerCard.getValue().getEvoId().isEmpty();
            if (isEvo && (dbPlayerCardEvoList.stream()
                    .noneMatch(p -> Objects.equals(p.getFutBinId(), playerCard.getValue().getFutBinId())
                            && Objects.equals(p.getEvoId(), playerCard.getValue().getEvoId())))) {
                dbPlayerCardEvoList.add(playerCard.getValue());
            }
        }
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("playerCardEvoList.json")) {
            gson.toJson(dbPlayerCardEvoList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, PlayerCard> playerCardEntry : playerCardMap.entrySet()) {
            PlayerCard playerCard = playerCardEntry.getValue();
            List<Position> positions = playerCard.getPositions();
            boolean isGK = positions.contains(GK);
            boolean isEvo = playerCard.getEvoId() != null && !playerCard.getEvoId().isBlank() && !playerCard.getEvoId().isEmpty();
            Set<Role> roleSet = new HashSet<>();
            positions.forEach(position -> {
                roleSet.addAll(positionRoleMap.get(position));
            });
            List<MetaInfo> metaInfoList = playerCard.getMetaInfoList();
            for (Role role : roleSet) {
                MetaInfo roleMetaInfo = new MetaInfo();
                try {
                    roleMetaInfo = metaInfoList.stream()
                            .filter(metaInfo -> {
                                if (isGK) {
                                    return metaInfo.getChemstyleId() == GLOVE.getValue() && metaInfo.getChemistry() == 3;
                                }
                                return isEvo ?
                                        metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.getChemistry() == 3 :
                                        metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.getChemistry() == 3
                                                && metaInfo.isBestChemstyleAtChem();
                            })
                            .findFirst().get();
                } catch (Exception ex) {
                    LOG.error("Failed for " + playerCard.getName() + ", " + role.getArchetypeId());
                }
                CardScore cardScore = new CardScore(playerCard.getFutBinId(), roleMetaInfo.getMetaRating(),
                        isGK ? GLOVE : ChemStyle.getChemStyle(roleMetaInfo.getChemstyleId()), role);
                for (Position position : positions)
                {
                    if (positionRoleMap.get(position).contains(role)) {
                        positionRoleListMap.computeIfAbsent(new PositionRole(position, role), k -> new ArrayList<>()).add(cardScore);
                    }
                }
            }
        }
//        Map<Integer, Integer> playerIdMap = new HashMap<>();
//        for (Map.Entry<Integer, PlayerCard> playerCardEntry : playerCardMap.entrySet()) {
//            playerIdMap.put(playerCardEntry.getKey(), playerCardEntry.getValue().getId());
//        }
        for (Map.Entry<PositionRole, List<CardScore>> positionRoleListEntry : positionRoleListMap.entrySet()) {
            List<CardScore> cardScoreList = positionRoleListEntry.getValue();
            List<Integer> playersToKeep =
                    cardScoreList.stream().map(CardScore::getCardId).filter(cardId -> mandatoryPlayers.contains(cardId)).toList();
            List<CardScore> newCardScoreList = new ArrayList<>(cardScoreList);
            Map<Integer, CardScore> cardScoreMap = new HashMap<>();
            for (CardScore cardScore : newCardScoreList){
                int id = playerCardMap.get(cardScore.getCardId()).getId();
                if (!cardScoreMap.containsKey(id) || cardScoreMap.get(id).getScore() < cardScore.getScore()) {
                    cardScoreMap.put(id, cardScore);
                }
            }
            newCardScoreList = new ArrayList<>(cardScoreMap.values());
            newCardScoreList = newCardScoreList.stream().sorted(Comparator.comparing(CardScore::getScore).reversed())
                    .limit(5).collect(Collectors.toList());
            for (Integer player : playersToKeep) {
                if (newCardScoreList.stream().map(CardScore::getCardId).toList().contains(player)) {
                    continue;
                }
                CardScore cardScore =
                        cardScoreList.stream().filter(p -> p.getCardId().equals(player)).findFirst().get();
                newCardScoreList.add(cardScore);
                newCardScoreList =
                        newCardScoreList.stream().sorted(Comparator.comparing(CardScore::getScore).reversed()).toList();
            }
            positionRoleListEntry.setValue(newCardScoreList);
        }

        System.out.println("\nBest players in each position role: ");
        positionRoleListMap.forEach((key, value) -> {
            System.out.print(key + " -> ");
            value.forEach(playerScore -> {
                PlayerCard playerCard = playerCardMap.get(playerScore.getCardId());
                System.out.print(playerCard.getName() + " " + String.format("%.2f", playerScore.getScore()) + ", ");
            });
            System.out.println();
            System.out.println();
        });

        getCombinations(positionRoleListMap.get(new PositionRole(CB, CENTRAL_DEFENDER)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(CB, CENTRAL_DEFENDER)).size(), 2, cb_cb2);
        getCombinations(positionRoleListMap.get(new PositionRole(CB, CENTRAL_DEFENDER)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(CB, CENTRAL_DEFENDER)).size(), 3, cb_cb3);
        getCombinations(positionRoleListMap.get(new PositionRole(CM, BOX_TO_BOX)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(CM, BOX_TO_BOX)).size(), 2, cm_b2b2);
        getCombinations(positionRoleListMap.get(new PositionRole(ST, COMPLETE_STRIKER)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(ST, COMPLETE_STRIKER)).size(), 2, st_st2);
        getCombinations(positionRoleListMap.get(new PositionRole(CAM, PLAYMAKER)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(CAM, PLAYMAKER)).size(), 2, cam_cam2);
        getCombinations(positionRoleListMap.get(new PositionRole(CF, AGILE_STRIKER)).stream().map(CardScore::getCardId).toList(),
                positionRoleListMap.get(new PositionRole(CF, AGILE_STRIKER)).size(), 2, cf_rat2);

        List<PlayerCard> playerCards = positionRoleListMap.values()
                .stream()
                .flatMap(List::stream)
                .map(p -> playerCardMap.get(p.getCardId())).distinct().collect(Collectors.toList());
        playerCards = playerCards.stream().sorted(Comparator.comparing(PlayerCard::getName)).toList();

        System.out.println("\nImportant players: ");
        for (PlayerCard playerCard : playerCards) {
            System.out.print(playerCard.getName() + " ");
            System.out.println(playerCard.getRating() + " ");
        }
        System.out.println("\nPlayers that can be thrown away: ");
        Set<Integer> playerIds = playerCards.stream().map(PlayerCard::getFutBinId).collect(Collectors.toSet());
        List<PlayerCard> playersToBeThrown = playerCardMap.entrySet().stream()
                .filter(p -> !playerIds.contains(p.getKey()))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(PlayerCard::getRating).reversed())
                .toList();
        List<PlayerCard> unimportantPlayers = new ArrayList<>();
        for(int i = 99; i >= 1; i--) {
            int finalI = i;
            List<PlayerCard> list = playersToBeThrown.stream().filter(p -> p.getRating() == finalI).collect(Collectors.toList());
            list.sort(Comparator.comparingDouble(objA -> -objA.getMetaInfoList().stream()
                    .mapToDouble(MetaInfo::getMetaRating)
                    .max()
                    .orElse(Double.NEGATIVE_INFINITY)));
            unimportantPlayers.addAll(list);
        }
        unimportantPlayers.forEach(playerCard -> {
            System.out.print(playerCard.getName() + " ");
            System.out.println(playerCard.getRating() + " ");
        });

        double totalMetaRating = 0;
        for (PlayerCard playerCard : playerCards) {
            double metaRating = playerCard.getMetaInfoList().stream()
                    .mapToDouble(MetaInfo::getMetaRating)
                    .max()
                    .orElse(Double.NEGATIVE_INFINITY);
            totalMetaRating += metaRating;
        }
        System.out.println("\nAverage Meta Rating of squad : " + (totalMetaRating / playerCards.size()));

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("\nTime taken = " + elapsedTime / 1000000000 + " s");
    }
}
