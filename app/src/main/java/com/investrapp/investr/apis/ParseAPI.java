package com.investrapp.investr.apis;

import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Player;
import com.parse.FindCallback;
import com.parse.ParseQuery;

public class ParseAPI {

    public static void getAllCompetitionsForPlayer(Player player, FindCallback<Competition> handler) {
        ParseQuery<CompetitionPlayer> competitionPlayerParseQuery = ParseQuery.getQuery(CompetitionPlayer.class);
        competitionPlayerParseQuery.whereEqualTo("player", player);
        ParseQuery<Competition> competitionParseQuery = ParseQuery.getQuery(Competition.class);
        competitionParseQuery.whereDoesNotMatchQuery("objectId", competitionPlayerParseQuery);
        competitionParseQuery.findInBackground(handler);
    }

    public static void getAllPlayersInCompetition(Competition competition, FindCallback<Player> handler) {
        ParseQuery<CompetitionPlayer> competitionPlayerParseQuery = ParseQuery.getQuery(CompetitionPlayer.class);
        competitionPlayerParseQuery.whereEqualTo("competition", competition);
        ParseQuery<Player> playerParseQuery = ParseQuery.getQuery(Player.class);
        playerParseQuery.whereMatchesQuery("objectId", competitionPlayerParseQuery);
        playerParseQuery.findInBackground(handler);
    }

    public static void addCompetition(final Competition competition) {
        competition.saveInBackground();
    }

    public static void getAllCompetitions(FindCallback<Competition> handler) {
        ParseQuery<Competition> query = ParseQuery.getQuery(Competition.class);
        query.findInBackground(handler);
    }

    public static void savePlayer(final Player player) {
        player.saveInBackground();
    }

    public static void getPlayer(String id, FindCallback<Player> handler) {
        ParseQuery<Player> query = ParseQuery.getQuery(Player.class);
        query.whereEqualTo("id", id);
        query.findInBackground(handler);
    }

    public static void getAllPlayers(FindCallback<Player> handler) {
        ParseQuery<Player> query = ParseQuery.getQuery(Player.class);
        query.findInBackground(handler);
    }

    public static void addPlayerToCompetition(CompetitionPlayer competition_player) {
        competition_player.saveInBackground();
    }

    public static void addCryptocurrency(Cryptocurrency cryptocurrency) {
        cryptocurrency.saveInBackground();
    }

}
