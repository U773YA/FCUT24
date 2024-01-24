package org.example.model;

import org.example.enums.Position;
import org.example.enums.Role;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.enums.Position.GK;

public class VariationTeam {
    private Integer tactic;
    private List<TeamPlayer> players;
    private int chemistry;
    private double totalRating;
    private double score;
    private Manager manager;
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
        StringBuilder teamString = new StringBuilder("Team{" +
                "tactic='" + tacticList.get(tactic).getName() + '\'' +
                ", players=" + playerNames +
                managerString +
                '}');
        if (!substitutes.isEmpty()) {
            String substituteNames = "";
            for (Integer cardId : substitutes) {
                PlayerCard playerCard = playerCardMap.get(cardId);
//                Position position = playerCard.getCardInput().getPosition();
//                List<CardScore> positionPlayers = playerPositionMap.get(position);
//                List<CardScore> teamPlayerScores = positionPlayers.stream()
//                        .filter(cardScore -> playerIds.contains(cardScore.getCardId()))
//                        .toList();
//                Double substituteScore = playerCard.chemScores.get(0);
//                boolean isPlayableSubstitute = teamPlayerScores.stream()
//                        .anyMatch(cardScore -> cardScore.getScore() < substituteScore);
                substituteNames = substituteNames.concat(playerCard.getName() + "-" + playerCard.getRating() + ", ");
            }
            teamString.append(" Substitutes{").append(substituteNames).append("}");
        }
        return teamString.toString();
    }

    public void setSubstitutes(List<Tactic> tacticList, Map<PositionRole, List<CardScore>> roleListMap, Map<Integer,
            PlayerCard> playerCardMap) {
        List<CardScore> substituteScores = new ArrayList<>();
        List<Integer> substitutesList = new ArrayList<>();
        List<Integer> playerList = players.stream().map(TeamPlayer::getPlayerId).toList();
        Set<PositionRole> positionRoles = new HashSet<>(tacticList.get(tactic).getPositionRoles());
        for (PositionRole positionRole : positionRoles) {
            List<CardScore> cardScoreList = roleListMap.get(positionRole);
            for (CardScore cardScore : cardScoreList) {
                if (substitutesList.contains(cardScore.getCardId()) || playerList.contains(cardScore.getCardId())) {
                    continue;
                }
                substituteScores.add(cardScore);
                substitutesList.add(cardScore.getCardId());
                break;
            }
        }
        substituteScores =
                substituteScores.stream().sorted(Comparator.comparing(CardScore::getScore).reversed()).limit(7).toList();
        if (substitutesList.size() < 7) {
            List<CardScore> unusedPlayers = roleListMap.values().stream().flatMap(List::stream).toList().stream()
                    .filter(p -> !substitutesList.contains(p.getCardId()) && !playerList.contains(p.getCardId()))
                    .sorted(Comparator.comparing(CardScore::getScore).reversed())
                    .limit(7 - substitutesList.size())
                    .toList();
            substituteScores.addAll(unusedPlayers);
        }
        substitutes =
                substituteScores.stream().sorted(Comparator.comparing(CardScore::getScore).reversed()).map(CardScore::getCardId).toList();
    }


    public List<Integer> getSubstitutes() {
        return substitutes;
    }

//    public Integer getPlayerCalculation() {
//        return players.stream().mapToInt(Integer::intValue).sum();
//    }
}
