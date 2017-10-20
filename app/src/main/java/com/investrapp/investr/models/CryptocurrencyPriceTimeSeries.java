package com.investrapp.investr.models;

import java.util.ArrayList;

/**
 * Created by michaelsignorotti on 10/19/17.
 */

public class CryptocurrencyPriceTimeSeries {

    String information;
    String ticker;
    String cryptocurrencyName;
    String market;
    String marketName;
    String lastRefreshed;
    String timeZone;
    ArrayList<Price> priceList;

    public CryptocurrencyPriceTimeSeries(String information, String ticker, String cryptocurrencyName, String market, String marketName, String lastRefreshed, String timeZone, ArrayList<Price> priceList) {
        this.information = information;
        this.ticker = ticker;
        this.cryptocurrencyName = cryptocurrencyName;
        this.market = market;
        this.marketName = marketName;
        this.lastRefreshed = lastRefreshed;
        this.timeZone = timeZone;
        this.priceList = priceList;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCryptocurrencyName() {
        return cryptocurrencyName;
    }

    public void setCryptocurrencyName(String cryptocurrencyName) {
        this.cryptocurrencyName = cryptocurrencyName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ArrayList<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(ArrayList<Price> priceList) {
        this.priceList = priceList;
    }
}
