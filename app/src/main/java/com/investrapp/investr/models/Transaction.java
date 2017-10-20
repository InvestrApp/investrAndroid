package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.NumberFormat;
import java.util.Date;

@ParseClassName("Transaction")
public class Transaction extends ParseObject {

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public final static String PLAYER_FIELD = "player";
    public final static String COMPETITION_FIELD = "competition";
    public final static String ASSET_TYPE_FIELD = "asset_type";
    public final static String ASSET_TICKER = "asset_ticker";
    public final static String DATE_FIELD = "buy_date";
    public final static String ACTION_FIELD = "action";
    public final static String PRICE_FIELD = "price";
    public final static String UNITS_FIELD = "units";

    public Transaction() {
        super();
    }

    public Transaction(Player player, Competition competition, String assetType, String assetTicker,
                       Date date, TransactionAction action, Double price, int units) {
        super();
        setPlayer(player);
        setCompetition(competition);
        setAssetType(assetType);
        setAssetTicker(assetTicker);
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

    public void setAssetType(String assetType) {
        put(ASSET_TYPE_FIELD, assetType);
    }

    public void setAssetTicker(String assetTicker) {
        put(ASSET_TICKER, assetTicker);
    }

    public void setDate(Date date) {
        put(DATE_FIELD, date);
    }

    public void setAction(TransactionAction action) {
        put(ACTION_FIELD, action.toString());
    }

    public void setPrice(Double price) {
        put(PRICE_FIELD, price);
    }

    public void setUnits(int units) {
        put(UNITS_FIELD, units);
    }

    public String getAssetType() {
        return getString(ASSET_TYPE_FIELD);
    }

    public String getAssetTicker() {
        return getString(ASSET_TICKER);
    }

    public Double getPrice() {
        return getDouble(PRICE_FIELD);
    }

    public String getPriceFormatted() {
        return formatter.format(getDouble(PRICE_FIELD));
    }

    public String getValueFormatted() {
        return formatter.format(getDouble(PRICE_FIELD) * Math.abs(getUnits()));
    }

    public int getUnits() {
        return getInt(UNITS_FIELD);
    }

    public String getAction() {
        return getString(ACTION_FIELD);
    }

    public Date getDate() {
        return getDate(DATE_FIELD);
    }

    public enum TransactionAction {
        BUY,
        SELL;
        TransactionAction() {};
    }

}
