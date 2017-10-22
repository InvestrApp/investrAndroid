package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Cash")
public class Cash extends ParseObject implements Asset {

    public static String ASSET_TYPE = "cash";
    public static String TICKER = "CASH";

    public Cash() {
        super();
    }

    public Cash(String name, String ticker) {
        super();
        setName(name);
        setTicker(ticker);
    }

    public String getTicker() {
        return getString("ticker");
    }

    public String getName() {
        return getString("name");
    }

    public void setTicker(String ticker) {
        put("ticker", ticker);
    }

    public void setName(String name) {
        put("name", name);
    }

    public String assetType() {
        return ASSET_TYPE;
    }

}
