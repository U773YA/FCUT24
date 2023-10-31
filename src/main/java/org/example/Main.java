package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.enums.ChemStyle;
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
import org.example.util.Permutations;
import org.example.util.Scraper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.example.enums.ChemStyle.GLOVE;
import static org.example.enums.Position.CAM;
import static org.example.enums.Position.CB;
import static org.example.enums.Position.CDM;
import static org.example.enums.Position.CF;
import static org.example.enums.Position.CM;
import static org.example.enums.Position.GK;
import static org.example.enums.Position.ST;
import static org.example.util.CombinationHelper.calculateETA;
import static org.example.util.CombinationHelper.combination;
import static org.example.util.CombinationHelper.getCombinations;
import static org.example.util.CombinationHelper.getFrequency;

public class Main extends InputData {

    public static Map<Integer, PlayerCard> dbPlayerCardMap = new HashMap<>();
    public static Map<Role, List<CardScore>> roleScoreMap = new HashMap<>();
    private static List<VariationTeam> possibleTeams = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        populatePlayerInput();
        populateTactics();
        populateManagers();

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

        Scraper scraper = new Scraper();
        for (CardInput cardInput : playerCardInputList) {
            PlayerCard playerCard;
            if (dbPlayerCardMap.containsKey(cardInput.getFutBinId())) {
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
                MetaInfo roleMetaInfo = metaInfoList.stream()
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
                CardScore cardScore = new CardScore(playerCard.getFutBinId(), roleMetaInfo.getMetaRating(),
                        isGK ? GLOVE : ChemStyle.getChemStyle(roleMetaInfo.getChemstyleId()), role);
                roleScoreMap.computeIfAbsent(role, k -> new ArrayList<>()).add(cardScore);
            }
        }

        for (List<CardScore> list : roleScoreMap.values()) {
            Role role = list.get(0).getRole();
            list.sort(Comparator.comparing(CardScore::getScore).reversed());
            list = list.subList(0, 5);
            roleScoreMap.put(role, list);
        }
        System.out.println("\nBest players in each role: ");
        roleScoreMap.forEach((key, value) -> {
            System.out.print(key + " -> ");
            value.forEach(cardScore -> {
                PlayerCard playerCard = playerCardMap.get(cardScore.getCardId());
                System.out.print(playerCard.getName() + " (" + cardScore.getChemStyle() + ") " + "-" + String.format(
                        "%.2f", cardScore.getScore()) + ", ");
            });
            System.out.println();
            System.out.println();
        });

        for (Map.Entry<Position, List<Role>> positionListEntry : positionRoleMap.entrySet()) {
            Position position = positionListEntry.getKey();
            Set<Integer> cards = new HashSet<>();
            for (Role role : positionListEntry.getValue()) {
                List<Integer> cardIds = roleScoreMap.get(role).stream().map(CardScore::getCardId).toList();
                cardIds.stream().filter(id -> playerCardMap.get(id).getPositions().contains(position))
                                .forEach(cards::add);
//                cards.addAll(roleScoreMap.get(role).stream().map(CardScore::getCardId).toList());
            }
            playerPositionMap.put(position, cards.stream().toList());
        }
        System.out.println("\nBest players in each position: ");
        playerPositionMap.forEach((key, value) -> {
            System.out.print(key + " -> ");
            value.forEach(playerId -> {
                PlayerCard playerCard = playerCardMap.get(playerId);
                System.out.print(playerCard.getName() + ", ");
            });
            System.out.println();
            System.out.println();
        });

        getCombinations(playerPositionMap.get(CB), playerPositionMap.get(CB).size(), 2, cb2);
        getCombinations(playerPositionMap.get(CB), playerPositionMap.get(CB).size(), 3, cb3);
        getCombinations(playerPositionMap.get(CM), playerPositionMap.get(CM).size(), 2, cm2);
        getCombinations(playerPositionMap.get(ST), playerPositionMap.get(ST).size(), 2, st2);
        getCombinations(playerPositionMap.get(CF), playerPositionMap.get(CF).size(), 2, cf2);
        getCombinations(playerPositionMap.get(CDM), playerPositionMap.get(CDM).size(), 2, cdm2);
        getCombinations(playerPositionMap.get(CAM), playerPositionMap.get(CAM).size(), 2, cam2);
        getCombinations(playerPositionMap.get(CM), playerPositionMap.get(CM).size(), 3, cm3);

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

        for (Tactic tactic : tacticList
//                .stream()
//                .filter(t -> Arrays.asList("4-1-3-2","4-1-2-1-2(2)","4-1-4-1","4-3-3(2)","4-5-1(2)","4-4-2")
//                        .contains(t.getName()))
//                .toList()
        ) {
            build(tactic);
        }
    }

    public static void build(Tactic tacticToBeConsidered) {
        List<Position> positions =
                tacticToBeConsidered.getPositionRoles().stream().map(PositionRole::getPosition).toList();
        if (!playerPositionMap.keySet().containsAll(positions)) {
            return;
        }

        AtomicLong tacticTeams = new AtomicLong(1);
        Map<Position, Integer> positionFrequencies = getFrequency(positions);
        positionFrequencies.forEach((key, value) -> tacticTeams.updateAndGet(v -> v * combination(playerPositionMap.get(key).size(), value)));
        tacticToBeConsidered.setTeamCount(tacticTeams.get());
        allTeamsCount += tacticTeams.get();
        System.out.println("\nAll possible teams : " + allTeamsCount + "\n");

        AtomicLong currentTacticCounter = new AtomicLong();
        List<VariationTeam> sortedTeamsByScore;
//        List<VariationTeam> finalSortedTeamsByScore = sortedTeamsByScore;
        if (!playerPositionMap.keySet().containsAll(positions)) {
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
                .limit(10)
                .collect(Collectors.toList());

        for (VariationTeam variationTeam : sortedTeamsByScore) {
//            variationTeam.setSubstitutes(tacticList, playerPositionMap, playerCardMap);
            System.out.print(variationTeam.toString(playerCardMap, tacticList, playerPositionMap) + "\t\t");
            System.out.print(variationTeam.getChemistry() + "\t");
            System.out.print(variationTeam.getTotalRating() + "\t");
            System.out.println(variationTeam.getScore());
        }

        possibleTeams = new ArrayList<>();

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("\nTime taken = " + elapsedTime / 1000000000 + " s");
    }

    private static void constructVariationTeam(int position, Tactic tactic, VariationTeam team) {
        List<Position> positions = tactic.getPositionRoles().stream().map(PositionRole::getPosition).toList();
        if (position >= 11) {
            teamCounter++;
            double percentage = teamCounter / (double) allTeamsCount * 100.0;
            if (teamCounter % 500000 == 0) {
                System.out.println("Percentage completed : " + percentage + " VariationTeams : " + teamCounter + " " +
                        "ETA: " + calculateETA(percentage,startTime) +" s");
            }
            List<Integer> teamPlayerCardIds = team.getPlayers().stream().map(p -> p.getPlayerId() % 100000).toList();
            if (!mandatoryPlayers.isEmpty() && !new HashSet<>(teamPlayerCardIds).containsAll(mandatoryPlayers)) {
                return;
            }
            validateTeams(team);
            return;
        }
        Position positionName = positions.get(position);
        List<Integer> players = playerPositionMap.get(positionName);
        List<List<Integer>> duplicatesList = combinationList(positionName, positions, position);
        if (duplicatesList != null && !duplicatesList.isEmpty()) {
            int size = duplicatesList.get(0).size();
            duplicatesList.forEach(item -> {
                int modifiedCount = 0;
                for (int i = 0; i < size; i++) {
                    int player = item.get(i);
                    PlayerCard playerCard = playerCardMap.get(player);
                    String name = playerCard.getName();
//                    boolean isModifiedPlayer = !Objects.equals(positionName, playerCard.getPosition());
                    List<TeamPlayer> existingPlayers = team.getPlayers();
                    if (existingPlayers.size() > position + i) {
                        existingPlayers.set(position + i, new TeamPlayer(player));
                    } else {
                        existingPlayers.add(position + i, new TeamPlayer(player));
                    }
//                    if (isModifiedPlayer) {
//                        team.setModifiedPlayers(team.getModifiedPlayers() + 1);
//                        modifiedCount++;
//                        if (team.getModifiedPlayers() > modifiedPlayers) {
//                            team.setModifiedPlayers(team.getModifiedPlayers() - 1);
//                            modifiedCount--;
//                            return;
//                        }
//                    }
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
//                team.setModifiedPlayers(team.getModifiedPlayers() - modifiedCount);
            });
        } else {
            AtomicReference<List<Integer>> playerIds = new AtomicReference<>(team.getPlayers().stream().map(TeamPlayer::getPlayerId).toList());
            players.stream().filter(player -> !playerIds.get().contains(player))
                    .filter(player -> {
                        Integer id = playerCardMap.get(player).getId();
                        return team.getPlayers().stream().map(p -> playerCardMap.get(p.getPlayerId()).getId()).noneMatch(p -> Objects.equals(p, id));
                    }).forEach(player -> {
                        PlayerCard playerCard = playerCardMap.get(player);
                        String name = playerCard.getName();
//                        boolean isModifiedPlayer = !Objects.equals(positionName, playerCard.getPosition());
                        List<TeamPlayer> existingPlayers = team.getPlayers();
                        if (existingPlayers.size() > position) {
                            existingPlayers.set(position, new TeamPlayer(player));
                        } else {
                            existingPlayers.add(position, new TeamPlayer(player));
                        }
//                        if (isModifiedPlayer) {
//                            team.setModifiedPlayers(team.getModifiedPlayers() + 1);
//                            if (team.getModifiedPlayers() > modifiedPlayers) {
//                                team.setModifiedPlayers(team.getModifiedPlayers() - 1);
//                                return;
//                            }
//                        }
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
//                        if (isModifiedPlayer) {
//                            team.setModifiedPlayers(team.getModifiedPlayers() - 1);
//                        }
                    });
        }
    }

    private static void validateTeams(VariationTeam team) {
//        if (team.getPlayerCalculation() == 497658) {
//            System.out.println("STOP");
//        }
        List<TeamPlayer> playerList = team.getPlayers();
        List<Integer> playerIds = playerList.stream().map(TeamPlayer::getPlayerId).toList();
        Map<String, Integer> nationMap = new HashMap<>();
        Map<Integer, Integer> leagueMap = new HashMap<>();
        Map<Integer, Integer> clubMap = new HashMap<>();
        playerList.forEach(playerIndex -> {
            PlayerCard playerCard = playerCardMap.get(playerIndex.getPlayerId());
            nationMap.merge(playerCard.getNation(), 1, Integer::sum);
            if (playerCard.getClubId().equals(112658)) {   // ICON club id = 112658
                nationMap.merge(playerCard.getNation(), 1, Integer::sum);
            }
            leagueMap.merge(playerCard.getLeagueId(), 1, Integer::sum);
            if (playerCard.getClubId().equals(114605) || playerCard.getName().contains("PREMIUM")) {  // Hero club id = 114605
                leagueMap.merge(playerCard.getLeagueId(), 1, Integer::sum);
            }
            clubMap.merge(playerCard.getClubId(), 1, Integer::sum);
        });
        Manager prevManager = null;
        List<VariationTeam> allTeams = new ArrayList<>();
        for (Manager manager : managerList) {
            playerIds = playerIds.stream().map(p -> p % 100000).collect(Collectors.toList());
            if (prevManager != null) {
                nationMap.merge(prevManager.getNation(), -1, Integer::sum);
                leagueMap.merge(prevManager.getLeague(), -1, Integer::sum);
            }
            nationMap.merge(manager.getNation(), 1, Integer::sum);
            leagueMap.merge(manager.getLeague(), 1, Integer::sum);
            prevManager = manager;
            if (new HashSet<>(playerList.stream().map(TeamPlayer::getPlayerId).toList()).containsAll(List.of(54259,154226,154042,54058,54180,54240,53921,54252,354210,54164,54275)) &&
                    manager.getName().equals("E. TEN HAG")) {
                System.out.println();
            }
            int chemistry = calculateChemistry(playerList, nationMap, leagueMap, clubMap);
            VariationTeam newTeam = new VariationTeam(team.getTactic(), new ArrayList<>(team.getPlayers()));
            double rating = calculateTeamTotalRating(newTeam.getPlayers(), newTeam.getSubstitutes(), team.getTactic());
            newTeam.setChemistry(chemistry);
            newTeam.setTotalRating(rating);
            newTeam.setScore();
            newTeam.setManager(manager);
            allTeams.add(newTeam);
//            if (chemistry == 33) {
//                teamHashMap.put(teamKey.toString(), newTeam);
//                possibleTeams.add(newTeam);
//                if (possibleTeams.size() % 10000 == 0) {
//                    System.out.println("No. of teams : " + possibleTeams.size());
//                }
//            }
        }
        VariationTeam bestTeam = allTeams.stream().max(Comparator.comparing(VariationTeam::getChemistry)).get();

//        Team bestTeam = allTeams.stream().max(Comparator.comparing(Team::getChemistry)).get();
        possibleTeams.add(bestTeam);
        if (possibleTeams.size() % 1000000 == 0) {
            System.out.println("No. of teams : " + possibleTeams.size());
        }
    }

    private static int calculateChemistry(List<TeamPlayer> playerList, Map<String, Integer> nationMap, Map<Integer, Integer> leagueMap, Map<Integer, Integer> clubMap) {
        int totalChemistry = 0;
        for (TeamPlayer player : playerList) {
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
                List<List<Integer>> duplicatesList = combinationList(position, positions, i);

                if (duplicatesList != null && !duplicatesList.isEmpty()) {
                    int size = duplicatesList.get(0).size();
                    List<Integer> players = new ArrayList<>();

                    for (int j = 0; j < size; j++) {
                        players.add(playerList.get(i + j).getPlayerId());
                    }

                    List<List<Integer>> permutations = Permutations.permute(players);
                    double maxScore = 0.0;

                    for (List<Integer> permutation : permutations) {
                        double score = 0.0;

                        for (int j = 0; j < size; j++) {
                            Role role = tacticList.get(tacticIndex).getPositionRoles().get(i + j).getRole();
                            int finalJ = j;
                            TeamPlayer teamPlayer1 = playerList.stream().filter(p -> Objects.equals(p.getPlayerId(), permutation.get(finalJ))).findFirst().get();
                            PlayerCard playerCard1 = playerCardMap.get(playerList.get(i + j).getPlayerId());
                            boolean isEvo =
                                    playerCard1.getEvoId() != null && !playerCard1.getEvoId().isBlank() && !playerCard1.getEvoId().isEmpty();
                            score += playerCard1.getMetaInfoList().stream()
                                    .filter(metaInfo ->
                                            isEvo ?
                                                    metaInfo.getChemistry() == teamPlayer1.getChemistry() && metaInfo.getArchetypeId().equals(role.getArchetypeId()) :
                                                    metaInfo.getChemistry() == teamPlayer1.getChemistry() && metaInfo.getArchetypeId().equals(role.getArchetypeId()) && metaInfo.isBestChemstyleAtChem())
                                    .findFirst().get().getMetaRating();
                        }

                        if (score > maxScore) {
                            maxScore = score;
                        }
                    }

                    totalTeamRating += maxScore;
                    i += size;
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

    private static List<List<Integer>> combinationList(Position positionName, List<Position> positions, int position) {
        if (duplicatePositions.contains(positionName)) {
            if (position < 9 && positions.get(position + 1).equals(positionName) && positions.get(position + 2).equals(positionName)) {
                switch (positionName) {
                    case CB -> {
                        return cb3;
                    }
                    case CM -> {
                        return cm3;
                    }
                }
            } else if (position < 10 && positions.get(position + 1).equals(positionName)) {
                switch (positionName) {
                    case CB -> {
                        return cb2;
                    }
                    case CM -> {
                        return cm2;
                    }
                    case ST -> {
                        return st2;
                    }
                    case CF -> {
                        return cf2;
                    }
                    case CDM -> {
                        return cdm2;
                    }
                    case CAM -> {
                        return cam2;
                    }
                }
            }
        }
        return null;
    }
}