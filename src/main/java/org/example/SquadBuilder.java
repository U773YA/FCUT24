package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ChemStyle;
import org.example.enums.League;
import org.example.enums.Position;
import org.example.enums.Role;
import org.example.model.CardInput;
import org.example.model.CardScore;
import org.example.model.Manager;
import org.example.model.MetaInfo;
import org.example.model.PlayerCard;
import org.example.model.PositionRole;
import org.example.model.Tactic;
import org.example.model.TeamPlayer;
import org.example.model.VariationTeam;
import org.example.util.InputData;
import org.example.util.Scraper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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
import static org.example.util.CombinationHelper.combination;
import static org.example.util.CombinationHelper.getCombinations;
import static org.example.util.CombinationHelper.getFrequencyForPositionRoles;

public class SquadBuilder extends InputData {
    private static final Logger LOG = LogManager.getLogger(PlayerCard.class);
    public static Map<Integer, PlayerCard> dbPlayerCardMap = new HashMap<>();
    public static List<PlayerCard> dbPlayerCardEvoList = new ArrayList<>();
    public static Map<Role, List<CardScore>> roleScoreMap = new HashMap<>();
    private static List<VariationTeam> possibleTeams = new ArrayList<>();
    private static List<VariationTeam> almightyTeams = new ArrayList<>();
    private static final Map<String, List<Manager>> nationManagerMap = new HashMap<>();
    private static final Map<Integer, List<Manager>> leagueManagerMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        populatePlayerInput();
        populateTactics();
        populateManagers();
        for (Manager manager : managerList) {
            nationManagerMap.computeIfAbsent(manager.getNation(), k -> new ArrayList<>()).add(manager);
            leagueManagerMap.computeIfAbsent(manager.getLeague(), k -> new ArrayList<>()).add(manager);
        }

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
        Map<Integer, Integer> playerIdMap = new HashMap<>();
        for (Map.Entry<Integer, PlayerCard> playerCardEntry : playerCardMap.entrySet()) {
            playerIdMap.put(playerCardEntry.getKey(), playerCardEntry.getValue().getId());
        }
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

//        for (Map.Entry<Position, List<Integer>> positionListEntry : playerPositionMap.entrySet()) {
//            Position position = positionListEntry.getKey();
//            List<Role> positionRoles = positionRoleMap.get(position);
//            List<Pair> scoreList = new ArrayList<>();
//            for (Integer player : positionListEntry.getValue()) {
//                PlayerCard playerCard = playerCardMap.get(player);
//                boolean isGK = position == GK;
//                boolean isEvo = playerCard.getEvoId() != null && !playerCard.getEvoId().isBlank() && !playerCard.getEvoId().isEmpty();
//                List<MetaInfo> metaInfoList = playerCard.getMetaInfoList();
//                Double score = 0.0;
//                for (Role role : positionRoles) {
//                    MetaInfo roleMetaInfo = new MetaInfo();
//                    try {
//                        roleMetaInfo = metaInfoList.stream()
//                                .filter(metaInfo -> {
//                                    if (isGK) {
//                                        return metaInfo.getChemstyleId() == GLOVE.getValue() && metaInfo.getChemistry() == 3;
//                                    }
//                                    return isEvo ?
//                                            metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.getChemistry() == 3 :
//                                            metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.getChemistry() == 3
//                                                    && metaInfo.isBestChemstyleAtChem();
//                                })
//                                .findFirst().get();
//                    } catch (Exception ex) {
//                        LOG.error("Failed for " + playerCard.getName() + ", " + role.getArchetypeId());
//                    }
//                    score += roleMetaInfo.getMetaRating();
//                }
//                Pair pair = new Pair(player, score / positionRoles.size());
//                scoreList.add(pair);
//            }
//            scoreList = scoreList.stream().sorted(Comparator.comparing(Pair::getSecond).reversed()).limit(5).toList();
//            positionListEntry.setValue(scoreList.stream().map(Pair::getFirst).toList());
//        }

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

//        getCombinations(playerPositionMap.get(CB), playerPositionMap.get(CB).size(), 2, cb2);
//        getCombinations(playerPositionMap.get(CB), playerPositionMap.get(CB).size(), 3, cb3);
//        getCombinations(playerPositionMap.get(CM), playerPositionMap.get(CM).size(), 2, cm2);
//        getCombinations(playerPositionMap.get(ST), playerPositionMap.get(ST).size(), 2, st2);
//        getCombinations(playerPositionMap.get(CF), playerPositionMap.get(CF).size(), 2, cf2);
//        getCombinations(playerPositionMap.get(CDM), playerPositionMap.get(CDM).size(), 2, cdm2);
//        getCombinations(playerPositionMap.get(CAM), playerPositionMap.get(CAM).size(), 2, cam2);
//        getCombinations(playerPositionMap.get(CM), playerPositionMap.get(CM).size(), 3, cm3);

        for (Tactic tactic : tacticList
//                .stream()
//                .filter(t -> Arrays.asList("4-1-3-2","4-1-2-1-2(2)","4-1-4-1","4-3-3(2)","4-5-1(2)","4-4-2")
//                        .contains(t.getName()))
//                .toList()
        ) {
            build(tactic);
        }

        almightyTeams = almightyTeams.stream().sorted(Comparator.comparing(VariationTeam::getTotalRating).reversed())
                .limit(15)
                .collect(Collectors.toList());
        System.out.println("\nAlmighty teams: ");
        for (VariationTeam variationTeam : almightyTeams) {
            variationTeam.setSubstitutes(tacticList, positionRoleListMap, playerCardMap);
            System.out.print(variationTeam.toString(playerCardMap, tacticList, playerPositionMap) + "\t\t");
            System.out.print(variationTeam.getChemistry() + "\t");
            System.out.print(variationTeam.getTotalRating() + "\t");
            System.out.println(variationTeam.getScore());
        }

//        Set<PlayerCard> playersConsidered = positionRoleListMap.values()
//                .stream()
//                .flatMap(List::stream)
//                .map(p -> playerCardMap.get(p.getCardId()))
//                .collect(Collectors.toSet());
//        List<PlayerCard> playerCards = new ArrayList<>(playersConsidered);
//        playerCards = playerCards.stream().sorted(Comparator.comparing(PlayerCard::getName)).toList();

//        System.out.println("\nImportant players: ");
//        for (PlayerCard playerCard : playerCards) {
//            System.out.print(playerCard.getName() + " ");
//            System.out.println(playerCard.getRating() + " ");
//        }
//        System.out.println("\nPlayers that can be thrown away: ");
//        Set<Integer> playerIds = playerCards.stream().map(PlayerCard::getFutBinId).collect(Collectors.toSet());
//        List<PlayerCard> playersToBeThrown = playerCardMap.entrySet().stream()
//                .filter(p -> !playerIds.contains(p.getKey()))
//                .map(Map.Entry::getValue)
//                .sorted(Comparator.comparing(PlayerCard::getRating).reversed())
//                .toList();
//        List<PlayerCard> unimportantPlayers = new ArrayList<>();
//        for(int i = 99; i >= 1; i--) {
//            int finalI = i;
//            List<PlayerCard> list = playersToBeThrown.stream().filter(p -> p.getRating() == finalI).collect(Collectors.toList());
//            list.sort(Comparator.comparingDouble(objA -> -objA.getMetaInfoList().stream()
//                    .mapToDouble(MetaInfo::getMetaRating)
//                    .max()
//                    .orElse(Double.NEGATIVE_INFINITY)));
//            unimportantPlayers.addAll(list);
//        }
//        unimportantPlayers.forEach(playerCard -> {
//            System.out.print(playerCard.getName() + " ");
//            System.out.println(playerCard.getRating() + " ");
//        });

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("\nTime taken = " + elapsedTime / 1000000000 + " s");
    }

    public static void build(Tactic tacticToBeConsidered) {
        List<PositionRole> positions = tacticToBeConsidered.getPositionRoles();
        if (!positionRoleListMap.keySet().containsAll(positions)) {
            return;
        }

        AtomicLong tacticTeams = new AtomicLong(1);
        Map<PositionRole, Integer> positionFrequencies = getFrequencyForPositionRoles(positions);
        positionFrequencies.forEach((key, value) -> tacticTeams.updateAndGet(v -> v * combination(positionRoleListMap.get(key).size(),
                value)));
        tacticToBeConsidered.setTeamCount(tacticTeams.get());
        allTeamsCount += tacticTeams.get();
        System.out.println("\nAll possible teams : " + allTeamsCount + "\n");

        AtomicLong currentTacticCounter = new AtomicLong();
        List<VariationTeam> sortedTeamsByScore;
//        List<VariationTeam> finalSortedTeamsByScore = sortedTeamsByScore;
        if (!positionRoleListMap.keySet().containsAll(positions)) {
            return;
        }
        System.out.println("Computing for tactic (" + tacticToBeConsidered.getTeamCount() + ") : " + tacticToBeConsidered.getName() + " ... ");
        long tacticStartTime = System.nanoTime();
        VariationTeam team = new VariationTeam(tacticList.indexOf(tacticToBeConsidered), new ArrayList<>());
        constructVariationTeam(0, tacticToBeConsidered, team);
        long tacticElapsedTime = System.nanoTime() - tacticStartTime;
        currentTacticCounter.set(teamCounter - currentTacticCounter.get());
        System.out.println("Completed in " + tacticElapsedTime / 1000000 + " ms with " + currentTacticCounter + " teams");
        currentTacticCounter.set(teamCounter);
//            if (possibleTeams.size() > 0) {
//                finalSortedTeamsByScore.add(possibleTeams.stream().max(Comparator.comparing(VariationTeam::getScore)).get());
//            }
//            possibleTeams = new ArrayList<>();

        System.out.println("\nTeams sorted by overall score: ");
        sortedTeamsByScore = possibleTeams.stream().sorted(Comparator.comparing(VariationTeam::getTotalRating).reversed())
                .limit(1)
                .collect(Collectors.toList());

        if (!sortedTeamsByScore.isEmpty()) {
            almightyTeams.add(sortedTeamsByScore.get(0));
        }

        for (VariationTeam variationTeam : sortedTeamsByScore) {
//            variationTeam.setSubstitutes(tacticList, playerPositionMap, playerCardMap);
            System.out.print(variationTeam.toString(playerCardMap, tacticList, playerPositionMap) + "\t\t");
            System.out.print(variationTeam.getChemistry() + "\t");
            System.out.print(variationTeam.getTotalRating() + "\t");
            System.out.println(variationTeam.getScore());
        }

        possibleTeams = new ArrayList<>();
    }

    private static void constructVariationTeam(int position, Tactic tactic, VariationTeam team) {
//        List<Position> positions = tactic.getPositionRoles().stream().map(PositionRole::getPosition).toList();
        if (position >= 11) {
            teamCounter++;
//            double percentage = teamCounter / (double) allTeamsCount * 100.0;
//            if (teamCounter % 500000 == 0) {
//                System.out.println("Percentage completed : " + percentage + " VariationTeams : " + teamCounter + " " +
//                        "ETA: " + calculateETA(percentage,startTime) +" s");
//            }
            List<Integer> teamPlayerCardIds = team.getPlayers().stream().map(TeamPlayer::getPlayerId).toList();
            if (!mandatoryPlayers.isEmpty() && !new HashSet<>(teamPlayerCardIds).containsAll(mandatoryPlayers)) {
                return;
            }
//            if (new HashSet<>(teamPlayerCardIds).containsAll(List.of(19654,47,49,423,19908,404,20031,87,19091,19261,487296))) {
//                System.out.println("break");
//            }
            if (isConstraintsCheckFailed(team)) {
                return;
            }
            validateTeams(team);
            return;
        }
        PositionRole positionRole = tactic.getPositionRoles().get(position);
        List<Integer> players = positionRoleListMap.get(positionRole).stream().map(CardScore::getCardId).toList();
        List<List<Integer>> duplicatesList = combinationList(positionRole, tactic.getPositionRoles(), position);
        if (duplicatesList != null && !duplicatesList.isEmpty()) {
            int size = duplicatesList.get(0).size();
            duplicatesList.forEach(item -> {
                for (int i = 0; i < size; i++) {
                    int player = item.get(i);
                    PlayerCard playerCard = playerCardMap.get(player);
//                    if (roleScoreMap.get(tactic.getPositionRoles().get(position + i).getRole()).stream()
//                            .map(CardScore::getCardId).noneMatch(p -> p == player)) {
//                        return;
//                    }
                    String name = playerCard.getName();
                    List<TeamPlayer> existingPlayers = team.getPlayers();
                    if (existingPlayers.size() > position + i) {
                        existingPlayers.set(position + i, new TeamPlayer(player));
                    } else {
                        existingPlayers.add(position + i, new TeamPlayer(player));
                    }
                }
                List<Integer> playerIds = team.getPlayers().stream().map(TeamPlayer::getPlayerId).toList();
                if (containsDuplicates(playerIds)) {
                    for (int i = size - 1; i >= 0; i--) {
                        team.getPlayers().remove(position + i);
                    }
                    return;
                }
                List<Integer> idList = playerCardMap.entrySet()
                        .stream()
                        .filter(entry -> playerIds.contains(entry.getKey()))
                        .map(entry -> entry.getValue().getId())
                        .toList();
                if (containsDuplicates(idList)) {
                    for (int i = size - 1; i >= 0; i--) {
                        team.getPlayers().remove(position + i);
                    }
                    return;
                }
                constructVariationTeam(position + size, tactic, team);
                for (int i = size - 1; i >= 0; i--) {
                    team.getPlayers().remove(position + i);
                }
            });
        } else {
            AtomicReference<List<Integer>> playerIds = new AtomicReference<>(team.getPlayers().stream().map(TeamPlayer::getPlayerId).toList());
            players.stream().filter(player -> !playerIds.get().contains(player))
                    .filter(player -> {
                        Integer id = playerCardMap.get(player).getId();
                        return team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getId()).noneMatch(p -> Objects.equals(p, id));
                    })
//                    .filter(player -> roleScoreMap.get(tactic.getPositionRoles().get(position).getRole()).stream()
//                            .map(CardScore::getCardId).anyMatch(p -> Objects.equals(p, player)))
                    .forEach(player -> {
                        PlayerCard playerCard = playerCardMap.get(player);
                        String name = playerCard.getName();
                        List<TeamPlayer> existingPlayers = team.getPlayers();
                        if (existingPlayers.size() > position) {
                            existingPlayers.set(position, new TeamPlayer(player));
                        } else {
                            existingPlayers.add(position, new TeamPlayer(player));
                        }
                        if (containsDuplicates(team.getPlayers())) {
                            existingPlayers.remove(position);
                            return;
                        }
                        playerIds.set(team.getPlayers().stream().map(TeamPlayer::getPlayerId).toList());
                        List<Integer> idList = playerCardMap.entrySet()
                                .stream()
                                .filter(entry -> playerIds.get().contains(entry.getKey()))
                                .map(entry -> entry.getValue().getId())
                                .toList();
                        if (containsDuplicates(idList)) {
                            existingPlayers.remove(position);
                            return;
                        }
                        constructVariationTeam(position + 1, tactic, team);
                        existingPlayers.remove(position);
                    });
        }
    }

    private static boolean isConstraintsCheckFailed(VariationTeam team) {
//        List<String> countryList = team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getNation()).toList();
//        List<Integer> leagueList = team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getLeagueId()).toList();
//        List<Integer> ratingList = team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getRating()).toList();
//        List<String> nameList = team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getName()).toList();
//        if (Collections.frequency(leagueList, League.PREMIER_LEAGUE.getValue()) < 6) {
//            return true;
//        }
//        if (Collections.frequency(countryList, "Spain") < 5) {
//            return true;
//        }



        return false;
    }

    private static void validateTeams(VariationTeam team) {
//        if (team.getPlayerCalculation() == 497658) {
//            System.out.println("STOP");
//        }
        List<TeamPlayer> playerList = team.getPlayers();
        List<Integer> playerIds = playerList.stream().map(TeamPlayer::getPlayerId).toList();
//        if (playerIds.contains(18950)) {
//            System.out.println("");
//        }
        AtomicInteger iconCount = new AtomicInteger();
        Map<String, Integer> nationMap = new HashMap<>();
        Map<Integer, Integer> leagueMap = new HashMap<>();
        Map<Integer, Integer> clubMap = new HashMap<>();
        playerList.forEach(playerIndex -> {
            PlayerCard playerCard = playerCardMap.get(playerIndex.getPlayerId());
            nationMap.merge(playerCard.getNation(), 1, Integer::sum);
            if (playerCard.getClubId().equals(112658)) {   // ICON club id = 112658
                nationMap.merge(playerCard.getNation(), 1, Integer::sum);
//                leagueMap.forEach((key, value) -> leagueMap.compute(key, (k, v) -> v + 1));
                iconCount.getAndIncrement();
            }
            leagueMap.merge(playerCard.getLeagueId(), 1, Integer::sum);
            if (playerCard.getClubId().equals(114605) || playerCard.getName().contains("PREMIUM")) {  // Hero club id = 114605
                leagueMap.merge(playerCard.getLeagueId(), 1, Integer::sum);
            }
            clubMap.merge(playerCard.getClubId(), 1, Integer::sum);
            if (playerCard.getName().contains("RADIOACTIVE")) {
                nationMap.merge(playerCard.getNation(), 1, Integer::sum);
                leagueMap.merge(playerCard.getLeagueId(), 1, Integer::sum);
                clubMap.merge(playerCard.getClubId(), 1, Integer::sum);
            }
            if (playerCard.getName().contains("VERSUS FIRE")) {
                nationMap.merge(playerCard.getNation(), 4, Integer::sum);
            }
            if (playerCard.getName().contains("VERSUS ICE")) {
                clubMap.merge(playerCard.getClubId(), 4, Integer::sum);
            }
        });
        leagueMap.forEach((key, value) -> leagueMap.compute(key, (k, v) -> v + iconCount.get()));
        List<VariationTeam> allTeams = new ArrayList<>();
        for (Map.Entry<String, Integer> nationEntry : nationMap.entrySet()) {
            if (Arrays.asList(1, 4, 7).contains(nationEntry.getValue())) {
                List<Manager> managers = nationManagerMap.get(nationEntry.getKey());
                if (managers != null) {
                    for (Manager manager : managers) {
                        nationMap.merge(manager.getNation(), 1, Integer::sum);
                        leagueMap.merge(manager.getLeague(), 1, Integer::sum);
                        int chemistry = calculateChemistry(playerList, nationMap, leagueMap, clubMap);
                        if (chemistry < chemistryCap) {
                            nationMap.merge(manager.getNation(), -1, Integer::sum);
                            leagueMap.merge(manager.getLeague(), -1, Integer::sum);
                            continue;
                        }
                        VariationTeam newTeam = new VariationTeam(team.getTactic(), new ArrayList<>(team.getPlayers()));
                        double rating = calculateTeamTotalRating(newTeam.getPlayers(), newTeam.getSubstitutes(), newTeam.getTactic());
                        newTeam.setChemistry(chemistry);
                        newTeam.setTotalRating(rating);
                        newTeam.setScore();
                        newTeam.setManager(manager);
                        allTeams.add(newTeam);
                        nationMap.merge(manager.getNation(), -1, Integer::sum);
                        leagueMap.merge(manager.getLeague(), -1, Integer::sum);
                    }
                }
            }
        }
        for (Map.Entry<Integer, Integer> leagueEntry : leagueMap.entrySet()) {
            if (Arrays.asList(2, 4, 7).contains(leagueEntry.getValue())) {
                List<Manager> managers = leagueManagerMap.get(leagueEntry.getKey());
                if (managers != null) {
                    for (Manager manager : managers) {
                        nationMap.merge(manager.getNation(), 1, Integer::sum);
                        leagueMap.merge(manager.getLeague(), 1, Integer::sum);
                        int chemistry = calculateChemistry(playerList, nationMap, leagueMap, clubMap);
                        if (chemistry < chemistryCap) {
                            nationMap.merge(manager.getNation(), -1, Integer::sum);
                            leagueMap.merge(manager.getLeague(), -1, Integer::sum);
                            continue;
                        }
                        VariationTeam newTeam = new VariationTeam(team.getTactic(), new ArrayList<>(team.getPlayers()));
                        double rating = calculateTeamTotalRating(newTeam.getPlayers(), newTeam.getSubstitutes(), newTeam.getTactic());
                        newTeam.setChemistry(chemistry);
                        newTeam.setTotalRating(rating);
                        newTeam.setScore();
                        newTeam.setManager(manager);
                        allTeams.add(newTeam);
                        nationMap.merge(manager.getNation(), -1, Integer::sum);
                        leagueMap.merge(manager.getLeague(), -1, Integer::sum);
                    }
                }
            }
        }
        Optional<VariationTeam> optBestTeam = allTeams.stream().max(Comparator.comparing(VariationTeam::getChemistry));
        if (optBestTeam.isPresent()) {
            VariationTeam bestTeam = optBestTeam.get();
            if (bestTeam.getChemistry() >= chemistryCap) {
                possibleTeams.add(bestTeam);
//                if (possibleTeams.size() % 100000 == 0) {
//                    System.out.println("No. of teams : " + possibleTeams.size());
//                }
            }
        }
    }

    private static int calculateChemistry(List<TeamPlayer> playerList, Map<String, Integer> nationMap, Map<Integer, Integer> leagueMap, Map<Integer, Integer> clubMap) {
        int totalChemistry = 0;
        int chemDeficit = 0;
        List<Integer> numbers = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Collections.shuffle(numbers, ThreadLocalRandom.current());
        for (int i = playerList.size() - 1; i >= 0; i--) {
            TeamPlayer player = playerList.get(numbers.get(i));
            int chem = 0;
            PlayerCard playerCard = playerCardMap.get(player.getPlayerId());
            if (playerCard.getClubId().equals(114605) || playerCard.getClubId().equals(112658) || playerCard.getName().contains("PREMIUM")) {
                chem = 3;
                totalChemistry = totalChemistry + chem;
                player.setChemistry(chem);
                continue;
            }
            int nationSize = nationMap.get(playerCard.getNation());
            int clubSize = clubMap.get(playerCard.getClubId());
            int leagueSize = leagueMap.get(playerCard.getLeagueId());
            chem = chem + (nationSize < 2 ? 0 : nationSize < 5 ? 1 : nationSize < 8 ? 2 : 3);
            chem = chem + (clubSize < 2 ? 0 : clubSize < 4 ? 1 : clubSize < 7 ? 2 : 3);
            chem = chem + (leagueSize < 3 ? 0 : leagueSize < 5 ? 1 : leagueSize < 8 ? 2 : 3);
            int playerChem = Math.min(chem, 3);
            totalChemistry = totalChemistry + playerChem;
            player.setChemistry(playerChem);
            chemDeficit += 3 - playerChem;
            if (chemDeficit > (33 - chemistryCap)) {
                break;
            }
        }
        return totalChemistry;
    }

    private static double calculateTeamTotalRating(List<TeamPlayer> playerList, List<Integer> substitutesList,
                                                   Integer tacticIndex) {
        double totalTeamRating = 0;
        List<Position> positions = tacticList.get(tacticIndex).getPositionRoles().stream().map(PositionRole::getPosition).toList();

        for (int i = 0; i < 11; ) {
            Position position = positions.get(i);
            TeamPlayer teamPlayer = playerList.get(i);
            PlayerCard playerCard = playerCardMap.get(teamPlayer.getPlayerId());

            if (position == GK) {
                double score = playerCard.getMetaInfoList().stream()
                        .filter(metaInfo -> (teamPlayer.getChemistry() == 0) ?
                                (metaInfo.getChemistry() == 0 && metaInfo.getChemstyleId() == 250) :
                                (metaInfo.getChemstyleId() == GLOVE.getValue() && metaInfo.getChemistry() == teamPlayer.getChemistry()))
                        .findFirst().get().getMetaRating();
                totalTeamRating += score;
                i++;
            } else {
                Role role = tacticList.get(tacticIndex).getPositionRoles().get(i).getRole();
                boolean isEvo = playerCard.getEvoId() != null && !playerCard.getEvoId().isBlank() && !playerCard.getEvoId().isEmpty();
                double score = playerCard.getMetaInfoList().stream().filter(metaInfo ->
                                isEvo ?
                                        metaInfo.getChemistry() == teamPlayer.getChemistry() && metaInfo.getArchetypeId().equals(role.getArchetypeId()) :
                                        metaInfo.getChemistry() == teamPlayer.getChemistry() && metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.isBestChemstyleAtChem())
                        .findFirst().get().getMetaRating();
                totalTeamRating += score;
                i++;
            }
        }

        return totalTeamRating;

//        for (TeamPlayer player : playerList) {
//            PlayerCard playerCard = playerCardMap.get(player.getPlayerId());
//            totalTeamRating += playerCard.chemScores.get(player.getChemistry());
//        }
//        for (Integer sub : substitutesList) {
//            PlayerCard playerCard = playerCardMap.get(sub);
//            totalTeamRating += playerCard.chemScores.get(0);
//        }
//        return totalTeamRating;
    }

    private static <T> boolean containsDuplicates(Collection<T> collection) {
        Set<T> uniques = new HashSet<>();
        Set<T> set = collection.stream()
                .filter(e -> !uniques.add(e))
                .collect(Collectors.toSet());
        return !set.isEmpty();
    }

    private static List<List<Integer>> combinationList(PositionRole positionRole, List<PositionRole> positionRoles,
                                                       int position) {
        if (duplicatePositionRoles.contains(positionRole)) {
            if (position < 9 && positionRoles.get(position + 1).equals(positionRole) && positionRoles.get(position + 2).equals(positionRole)) {
                if (positionRole.equals(new PositionRole(CB, CENTRAL_DEFENDER))) {
                    return cb_cb3;
                }
            } else if (position < 10 && positionRoles.get(position + 1).equals(positionRole)) {
                if (positionRole.equals(new PositionRole(CB, CENTRAL_DEFENDER))) {
                    return cb_cb2;
                } else if (positionRole.equals(new PositionRole(CM, BOX_TO_BOX))) {
                    return cm_b2b2;
                } else if (positionRole.equals(new PositionRole(ST, COMPLETE_STRIKER))) {
                    return st_st2;
                } else if (positionRole.equals(new PositionRole(CAM, PLAYMAKER))) {
                    return cam_cam2;
                } else if (positionRole.equals(new PositionRole(CF, AGILE_STRIKER))) {
                    return cf_rat2;
                }
            }
        }
        return null;
    }
}
