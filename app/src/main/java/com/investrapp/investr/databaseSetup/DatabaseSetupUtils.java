package com.investrapp.investr.databaseSetup;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Ranking;
import com.investrapp.investr.models.Stock;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseSetupUtils {

    public static void addInitialSetOfCompetitions() {
        Calendar calendar = new GregorianCalendar();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date currentDatePlusThirty = calendar.getTime();
        Competition general = new Competition("General" , currentDate, currentDatePlusThirty, 10000.00);
        Competition codePath = new Competition("Code Path" , currentDate, currentDatePlusThirty, 10000.00);
        Competition sanFrancisco = new Competition("San Francisco" , currentDate, currentDatePlusThirty, 10000.00);
        List<Competition> competitions = new ArrayList<>();
        competitions.add(general);
        competitions.add(codePath);
        competitions.add(sanFrancisco);

        for (Competition competition : competitions) {
            ParseClient.addCompetition(competition);
        }
    }

    public static void addAllPlayersToAllComnpetitions() {
        ParseClient.getAllPlayers(new FindCallback<Player>() {
            @Override
            public void done(final List<Player> players, ParseException e) {
                ParseClient.getAllCompetitions(new FindCallback<Competition>() {
                    @Override
                    public void done(List<Competition> competitions, ParseException e) {
                        for (Competition competition : competitions) {
                            for (Player player : players) {
                                CompetitionPlayer competition_player = new CompetitionPlayer(competition, player);
                                ParseClient.addPlayerToCompetition(competition_player);
                            }
                        }
                    }
                });
            }
        });
    }

    public static void addAllCryptocurrencies(List<Cryptocurrency> cryptocurrencyList) {
        for (Cryptocurrency cryptocurrency : cryptocurrencyList) {
            ParseClient.addCryptocurrency(cryptocurrency);
        }
    }

    public static void addAllStocks(List<Stock> stockList) {
        for (Stock stock : stockList) {
            ParseClient.addStock(stock);
        }
    }

    public static void addCashAsset() {
        Cash cash = new Cash();
        ParseClient.addCash(cash);
    }

    public static void addCryptoTransaction(final Player player, final Competition competition, final String cryptoTicker,
                                            final int units, final Transaction.TransactionAction transactionAction) {
        Calendar calendar = new GregorianCalendar();
        final Date currentDate = calendar.getTime();
        AlphaVantageClient.getCurrentDigitalCurrencyPrice(cryptoTicker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
            @Override
            public void onPriceResponse(final Double price) {
                Transaction transaction = new Transaction(player, competition, Cryptocurrency.ASSET_TYPE, cryptoTicker,
                        currentDate, transactionAction, price, units);
                ParseClient.addTransaction(transaction);
            }
        });
    }

    public static void addCryptoTransactionAndSubstractCash(final Player player, final Competition competition, final String cryptoTicker,
                                            final int units, final Transaction.TransactionAction transactionAction) {
        Calendar calendar = new GregorianCalendar();
        final Date currentDate = calendar.getTime();
        AlphaVantageClient.getCurrentDigitalCurrencyPrice(cryptoTicker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
            @Override
            public void onPriceResponse(final Double price) {
                ParseClient.getCashForPlayerInCompetition(player, competition, new FindCallback<Transaction>() {
                    @Override
                    public void done(List<Transaction> objects, ParseException e) {
                        Transaction cashTransaction = objects.get(0);
                        Double cash = cashTransaction.getPrice();
                        if (cash >= units * price) {
                            Transaction transaction = new Transaction(player, competition, Cryptocurrency.ASSET_TYPE, cryptoTicker,
                                    currentDate, transactionAction, price, units);
                            ParseClient.addTransaction(transaction);
                            cashTransaction.setPrice(cash - units * price);
                            ParseClient.addTransaction(cashTransaction);
                        } else {
                            System.out.println("Insufficient funds");
                        }
                    }
                });
            }
        });
    }

    public static void addCashTransaction(final Player player, final Competition competition, final double value,
                                          final Transaction.TransactionAction transactionAction) {
        Calendar calendar = new GregorianCalendar();
        final Date currentDate = calendar.getTime();
        ParseClient.getCashObject(new FindCallback<Cash>() {
            @Override
            public void done(List<Cash> objects, ParseException e) {
                System.out.println(objects);
                Transaction transaction = new Transaction(player, competition, objects.get(0).assetType(), objects.get(0).getTicker(),
                        currentDate, transactionAction, value, 1);
                ParseClient.addTransaction(transaction);
            }
        });
    }

    public static void createRankingTable(final Player player, final Competition competition,
                                          final Double value, final int rank) {
        Ranking ranking = new Ranking(player, competition, rank, value);
        ranking.saveInBackground();
    }

}
