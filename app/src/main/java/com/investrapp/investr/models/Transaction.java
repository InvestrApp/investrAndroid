package com.investrapp.investr.models;

import com.parse.ParseObject;

import java.util.Date;

public class Transaction extends ParseObject {

    public final static String PLAYER_FIELD = "player";
    public final static String COMPETITION_FIELD = "competition";
    public final static String ASSET_FIELD = "asset";
    public final static String DATE_FIELD = "buy_date";
    public final static String ACTION_FIELD = "action";
    public final static String PRICE_FIELD = "price";
    public final static String UNITS_FIELD = "units";

    public Transaction() {
        super();
    }

    public Transaction(Player player, Competition  competition, Asset asset, Date date,
                       TransactionAction action, Double price, int units) {
        super();
        setPlayer(player);
        setCompetition(competition);
        setAsset(asset);
        setDate(date);
        setAction(action);
        setPrice(price);
        setUnits(units);
    }

    public void setPlayer(Player player) {
        put(PLAYER_FIELD, player);
    }

    public void setCompetition(Competition competition) {
        put(COMPETITION_FIELD, competition);
    }

    public void setAsset(Asset asset) {
        put(ASSET_FIELD, asset);
    }

    public void setDate(Date date) {
        put(DATE_FIELD, date);
    }

    public void setAction(TransactionAction action) {
        put(ACTION_FIELD, action);
    }

    public void setPrice(Double price) {
        put(PRICE_FIELD, price);
    }

    public void setUnits(int units) {
        put(UNITS_FIELD, units);
    }

    public static enum TransactionAction {

        BUY,
        SELL;

        TransactionAction() {

        };

    }

}
