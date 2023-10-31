package org.example.model;

import org.example.enums.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VariationTeam {
    private Integer tactic;
    private List<TeamPlayer> players;
    private int chemistry;
    private double totalRating;
    private double score;
    private Manager manager
            ;
    private List<Integer> substitutes = new ArrayList<>();
    public VariationTeam(Integer tactic, List<TeamPlayer> players) {
        this.tactic = tactic;
        this.players = players;
    }

    public Integer getTactic() {
        return tactic;
    }

    public void setTactic(Integer tactic) {
        this.tactic = tactic;
    }

    public List<TeamPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<TeamPlayer> players) {
        this.players = players;
    }

    public int getChemistry() {
        return chemistry;
    }

    public void setChemistry(int chemistry) {
        this.chemistry = chemistry;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public double getScore() {
        return score;
    }

    public void setScore() {
        double teamSize = 11.0;
        if (!substitutes.isEmpty()) {
            teamSize = teamSize + substitutes.size();
        }
        this.score = ((totalRating / teamSize) + (chemistry / 33.0) * 100.0) / 2.0;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public String toString(Map<Integer, PlayerCard> playerCardMap, List<Tactic> tacticList,
                           Map<Position, List<Integer>> playerPositionMap) {
        String playerNames = "";
        List<Integer> playerIds = players.stream().map(TeamPlayer::getPlayerId).toList();
        for (TeamPlayer player : players) {
            PlayerCard playerCard = playerCardMap.get(player.getPlayerId());
            playerNames = playerNames.concat(playerCard.getName() + "-" + playerCard.getRating() + ", ");
        }
        String managerString = manager != null ? ", manager=" + manager.getName() : "";
        String teamString =  "Team{" +
                "tactic='" + tacticList.get(tactic).getName() + '\'' +
                ", players=" + playerNames +
                managerString +
                '}';
//        if (!substitutes.isEmpty()) {
//            String substituteNames = "";
//            for (Integer cardId : substitutes) {
//                PlayerCard playerCard = playerCardMap.get(cardId);
//                Position position = playerCard.getCardInput().getPosition();
//                List<CardScore> positionPlayers = playerPositionMap.get(position);
//                List<CardScore> teamPlayerScores = positionPlayers.stream()
//                        .filter(cardScore -> playerIds.contains(cardScore.getCardId()))
//                        .toList();
//                Double substituteScore = playerCard.chemScores.get(0);
//                boolean isPlayableSubstitute = teamPlayerScores.stream()
//                        .anyMatch(cardScore -> cardScore.getScore() < substituteScore);
//                substituteNames = substituteNames.concat(playerCard.getCardInput().getName() + "-" + playerCard.getRating() + (isPlayableSubstitute ? "(~)" : "") + ", ");
//            }
//            teamString = teamString + " Substitutes{" + substituteNames + "}";
//        }
        return teamString;
    }

//    public void setSubstitutes(List<Tactic> tacticList, Map<Position, List<CardScore>> playerPositionMap, Map<Integer, PlayerCard> playerCardMap) {
//        StringBuilder key = new StringBuilder();
//        List<Integer> playerIds = players.stream().map(t -> t.getPlayerId() % 100000).toList();
//        players.forEach(playerIndex -> {
//            key.append(playerIndex.toString());
//        });
//        if (SquadBuilder.key.equals(String.valueOf(key))) {
//            substitutes = SquadBuilder.substituteMap;
//        } else {
//            List<Integer> idList = playerCardMap.entrySet()
//                    .stream()
//                    .filter(entry -> playerIds.contains(entry.getKey()))
//                    .map(entry -> entry.getValue().getId())
//                    .toList();
//            Tactic tacticDetails = tacticList.get(tactic);
//            Set<Position> positions = new HashSet<>(tacticDetails.getPositions());
//            positions.forEach(position -> {
//                List<CardScore> cardScores = playerPositionMap.get(position);
//                for (CardScore cardScore : cardScores) {
//                    if (!playerIds.contains(cardScore.getCardId() % 100000) && !idList.contains(playerCardMap.get(cardScore.getCardId()).getId())
//                            && !substitutes.contains(cardScore.getCardId() % 100000)) {
//                        substitutes.add(cardScore.getCardId() % 100000);
//                        break;
//                    }
//                }
//            });
//            Map<Integer, Double> substituteScores = new HashMap<>();
//            substitutes.forEach(index -> {
//                PlayerCard playerCard = playerCardMap.get(index);
//                if (playerCard != null) {
//                    substituteScores.put(index, playerCard.chemScores.get(0));
//                }
//            });
//            substitutes = substituteScores.entrySet().stream()
//                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
//                    .limit(7)
//                    .map(Map.Entry::getKey)
//                    .collect(Collectors.toList());
//            if (substitutes.size() < 7) {
//                List<Integer> unusedSubstitutes = playerPositionMap.values()
//                        .stream()
//                        .flatMap(List::stream)
//                        .toList()
//                        .stream()
//                        .sorted(Comparator.comparing(CardScore::getScore).reversed())
//                        .map(CardScore::getCardId)
//                        .toList();
//                int i = 0;
//                while (substitutes.size() < 7) {
//                    Integer sub = unusedSubstitutes.get(i);
//                    if (!playerIds.contains(sub % 100000) && !idList.contains(playerCardMap.get(sub).getId())
//                            && !substitutes.contains(sub % 100000)) {
//                        substitutes.add(sub % 100000);
//                    }
//                    i++;
//                }
//            }
//            SquadBuilder.key = String.valueOf(key);
//            SquadBuilder.substituteMap = substitutes;
//        }
//    }

    public List<Integer> getSubstitutes() {
        return substitutes;
    }

//    public Integer getPlayerCalculation() {
//        return players.stream().mapToInt(Integer::intValue).sum();
//    }
}
