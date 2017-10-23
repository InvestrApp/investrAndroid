package com.investrapp.investr.apis;

import com.investrapp.investr.apis.handlers.ParseGetAllCompetitionsHandler;
import com.investrapp.investr.apis.handlers.ParseGetAllPlayersHandler;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Ranking;
import com.investrapp.investr.models.Stock;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseQuery;

import java.util.HashMap;

public class ParseClient {

    public static void getAllCompetitionsForPlayer(Player player, ParseGetAllCompetitionsHandler handler) {
        ParseQuery<CompetitionPlayer> competitionPlayerParseQuery = ParseQuery.getQuery(CompetitionPlayer.class);
        competitionPlayerParseQuery.include("competition");
        competitionPlayerParseQuery.whereEqualTo("player", player);
        competitionPlayerParseQuery.findInBackground(handler);
    }

    public static void getAllPlayersInCompetition(Competition competition, ParseGetAllPlayersHandler handler) {
        ParseQuery<CompetitionPlayer> competitionPlayerParseQuery = ParseQuery.getQuery(CompetitionPlayer.class);
        competitionPlayerParseQuery.include("player");
        competitionPlayerParseQuery.whereEqualTo("competition", competition);
        competitionPlayerParseQuery.findInBackground(handler);
    }

    public static void addCompetition(final Competition competition) {
        competition.saveInBackground();
    }

    public static void getAllCompetitions(FindCallback<Competition> handler) {
        ParseQuery<Competition> query = ParseQuery.getQuery(Competition.class);
        query.orderByDescending("end_date");
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

    public static void addStock(Stock stock) {
        stock.saveInBackground();
    }

    public static void getMatchingStocksByName(String query, FindCallback<Stock> handler) {
        ParseQuery<Stock> stockQuery = ParseQuery.getQuery(Stock.class);
        stockQuery.whereContains("name", query.toUpperCase());
        stockQuery.setLimit(300);
        stockQuery.findInBackground(handler);
    }

    public static void getMatchingCryptocurrenciesByName(String query, FindCallback<Cryptocurrency> handler) {
        ParseQuery<Cryptocurrency> cryptocurrencyQuery = ParseQuery.getQuery(Cryptocurrency.class);
        cryptocurrencyQuery.whereContains("name", query.toUpperCase());
        cryptocurrencyQuery.setLimit(300);
        cryptocurrencyQuery.findInBackground(handler);
    }

    public static void getStockByTicker(String query, FindCallback<Stock> handler) {
        ParseQuery<Stock> stockQuery = ParseQuery.getQuery(Stock.class);
        stockQuery.whereEqualTo("ticker", query.toUpperCase());
        stockQuery.setLimit(1);
        stockQuery.findInBackground(handler);
    }

    public static void getCryptocurrencyByTicker(String query, FindCallback<Cryptocurrency> handler) {
        ParseQuery<Cryptocurrency> cryptocurrencyQuery = ParseQuery.getQuery(Cryptocurrency.class);
        cryptocurrencyQuery.whereEqualTo("ticker", query.toUpperCase());
        cryptocurrencyQuery.setLimit(1);
        cryptocurrencyQuery.findInBackground(handler);
    }

    public static void addCash(Cash cash) {
        cash.saveInBackground();
    }

    public static void updateCash(Player player, Competition competition, FindCallback<Transaction> findCallback) {
        ParseQuery<Transaction> query = ParseQuery.getQuery(Transaction.class);
        query.whereEqualTo("asset_type", "cash");
        query.whereEqualTo("player", player);
        query.whereEqualTo("competition", competition);
        query.findInBackground(findCallback);
    }

    public static void getCashObject(FindCallback<Cash> handler) {
        ParseQuery<Cash> query = ParseQuery.getQuery(Cash.class);
        query.whereEqualTo("ticker", "CASH");
        query.findInBackground(handler);
    }

    public static void addTransaction(Transaction transaction) {
        transaction.saveInBackground();
    }

    public static void getCashForPlayerInCompetition(Player player, Competition competition, FindCallback<Transaction> handler) {
        ParseQuery<Transaction> transactionParseQuery = ParseQuery.getQuery(Transaction.class);
        transactionParseQuery.whereEqualTo("player", player);
        transactionParseQuery.whereEqualTo("competition", competition);
        transactionParseQuery.whereEqualTo("asset_type", Cash.ASSET_TYPE);
        transactionParseQuery.setLimit(1);
        transactionParseQuery.findInBackground(handler);
    }

    public static void getTransactionsForPlayerInCompetition(Player player, Competition competition, FindCallback<Transaction> handler) {
        ParseQuery<Transaction> transactionParseQuery = ParseQuery.getQuery(Transaction.class);
        transactionParseQuery.whereEqualTo("player", player);
        transactionParseQuery.whereEqualTo("competition", competition);
        transactionParseQuery.orderByDescending("buy_date");
        transactionParseQuery.setLimit(999);
        transactionParseQuery.findInBackground(handler);
    }

    public static void getRankingsForCompetition(Competition competition, FindCallback<Ranking> handler) {
        ParseQuery<Ranking> query = ParseQuery.getQuery(Ranking.class);
        query.whereEqualTo("competition", competition);
        query.include("player");
        query.orderByAscending("ranking");
        query.findInBackground(handler);
    }

    public static void getCryptocurrencyTransactionsByTickerUserCompetition(String query, Competition competition, Player player, FindCallback<Transaction> handler) {
        ParseQuery<Transaction> transactionParseQuery = ParseQuery.getQuery(Transaction.class);
        transactionParseQuery.whereEqualTo("asset_ticker", query.toUpperCase());
        transactionParseQuery.whereEqualTo("competition", competition);
        transactionParseQuery.whereEqualTo("player", player);
        transactionParseQuery.findInBackground(handler);
    }

    public static void sendPushto(Player player, String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("installationId", player.getInstallation());
        data.put("message", message);
        ParseCloud.callFunctionInBackground("push", data);
    }

}
