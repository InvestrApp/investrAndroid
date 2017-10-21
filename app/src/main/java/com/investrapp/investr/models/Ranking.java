package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.NumberFormat;

@ParseClassName("Ranking")
public class Ranking extends ParseObject {

    private static String RANKING_FIELD = "ranking";
    private static String COMPETITION_FIELD = "competition";
    private static String PLAYER_FIELD = "player";
    private static String PORTFOLIO_VALUE_FIELD = "portfolio_value";
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public Ranking() {
        super();
    }

    public Ranking(Player player, Competition competition, int ranking, Double portfolioValue) {
        super();
        setCompetition(competition);
        setPlayer(player);
        setRanking(ranking);
        setPortfolioValue(portfolioValue);
    }

    public void setCompetition(Competition competition) {
        put(COMPETITION_FIELD, competition);
    }

    public void setPlayer(Player player) {
        put(PLAYER_FIELD, player);
    }

    public void setRanking(int ranking) {
        put(RANKING_FIELD,ranking);
    }

    public void setPortfolioValue(Double portfolioValue) {
        put(PORTFOLIO_VALUE_FIELD, portfolioValue);
    }

    public Competition getConmpetition() {
        return (Competition) get(COMPETITION_FIELD);
    }

    public Player getPlayer() {
        return (Player) get(PLAYER_FIELD);
    }

    public int getRanking() {
        return getInt(RANKING_FIELD);
    }

    public Double getPortfolioValue() {
        return getDouble(PORTFOLIO_VALUE_FIELD);
    }

    public String getValueFormatted() {
        return formatter.format(getPortfolioValue());
    }


}
