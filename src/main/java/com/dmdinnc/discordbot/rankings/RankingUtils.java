package com.dmdinnc.discordbot.rankings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingUtils {

    public static final int ELO_K = 32;
    public static final int ELO_START = 1200;
    public static final double ELO_MINIMIZER = 400.0;

    public static Map<String, Integer> updateRankings(Map<String, Integer> oldRankings, Map<String, Integer> placements) {
        Map<String, Integer> newRankings = new HashMap<String, Integer>(oldRankings);
        Map<String, Integer> deltaPoints = new HashMap<String, Integer>();
        // Add new players to future rankings starting now
        for (String player : placements.keySet()) {
            if (oldRankings.get(player) == null) {
                newRankings.put(player, ELO_START);
            }
        }
        // Initialize difference map
        for (String player : newRankings.keySet()) {
            deltaPoints.put(player, 0);
        }

        List<String> players = new ArrayList<>(oldRankings.keySet());

        // Loop through players comparing each matchup 1 time and adding mmr as if each player played against every other once
        for (int i = 0; i < players.size(); i++) {
            String player1 = players.get(i);
            Integer rank1 = oldRankings.get(player1);
            for (int j = i + 1; j < players.size(); j++) {
                String player2 = players.get(j);
                Integer rank2 = oldRankings.get(player2);
                if (placements.get(player1) < placements.get(player2)) {
                    //Player 1 wins
                    int pointsDifference = calculateGain(player1, player2, rank1, rank2);
                    deltaPoints.put(player1, deltaPoints.get(player1) + pointsDifference);
                    deltaPoints.put(player2, deltaPoints.get(player2) - pointsDifference);

                } else if (placements.get(player1) > placements.get(player2)) {
                    //Player 2 wins
                    int pointsDifference = calculateGain(player2, player1, rank2, rank1);
                    deltaPoints.put(player1, deltaPoints.get(player1) - pointsDifference);
                    deltaPoints.put(player2, deltaPoints.get(player2) + pointsDifference);
                } else {
                    //Draw, skip
                    continue;
                }
            }
        }

        // Update new rankings with earned points
        for (String player : oldRankings.keySet()) {
            newRankings.put(player, oldRankings.get(player) + deltaPoints.get(player));
        }
        return newRankings;
    }


    public static Integer calculateGain(String winner, String loser, Integer winELO, Integer loseELO) {
        double rankFactor = (loseELO - winELO) / ELO_MINIMIZER;
        double winProb = 1 + Math.pow(10, rankFactor);
        return (int) ((winELO + ELO_K * (1 - winProb)));
    }
}
