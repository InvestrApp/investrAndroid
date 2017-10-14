package com.investrapp.investr.databaseSetup;

import com.investrapp.investr.apis.ParseAPI;
import com.investrapp.investr.interfaces.AlphaAvantageClientListener;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Stock;
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
        Competition general = new Competition("General" , currentDate, currentDatePlusThirty);
        Competition codePath = new Competition("Code Path" , currentDate, currentDatePlusThirty);
        Competition sanFrancisco = new Competition("San Francisco" , currentDate, currentDatePlusThirty);
        List<Competition> competitions = new ArrayList<>();
        competitions.add(general);
        competitions.add(codePath);
        competitions.add(sanFrancisco);

        for (Competition competition : competitions) {
            ParseAPI.addCompetition(competition);
        }
    }

    public static void addAllPlayersToAllComnpetitions() {
        ParseAPI.getAllPlayers(new FindCallback<Player>() {
            @Override
            public void done(final List<Player> players, ParseException e) {
                ParseAPI.getAllCompetitions(new FindCallback<Competition>() {
                    @Override
                    public void done(List<Competition> competitions, ParseException e) {
                        for (Competition competition : competitions) {
                            for (Player player : players) {
                                CompetitionPlayer competition_player = new CompetitionPlayer(competition, player);
                                ParseAPI.addPlayerToCompetition(competition_player);
                            }
                        }
                    }
                });
            }
        });
    }

public static void addAllCryptocurrencies(List<Cryptocurrency> cryptocurrencyList) {

    for (Cryptocurrency cryptocurrency : cryptocurrencyList) {
        ParseAPI.addCryptocurrency(cryptocurrency);
    }
}

public static void addAllStocks(List<Stock> stockList) {

    for (Stock stock : stockList) {
        ParseAPI.addStock(stock);
    }

}
}
