package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelsignorotti on 10/14/17.
 */

@ParseClassName("Stock")
public class Stock extends ParseObject implements Asset {

    public static String ASSET_TYPE = "stock";

    public Stock() {
        super();
    }

    public Stock(String ticker, String name, String sector, String exchange, String industry, boolean isForeign) {
        super();
        setTicker(ticker);
        setName(name);
        setSector(sector);
        setExchange(exchange);
        setIndustry(industry);
        setForeign(isForeign);
    }

    public String getTicker() {
        return getString("ticker");
    }

    public void setTicker(String ticker) {
        put("ticker", ticker);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getSector() {
        return getString("sector");
    }

    public void setSector(String sector) {
        put("sector", sector);
    }

    public String getExchange() {
        return getString("exchange");
    }

    public void setExchange(String exchange) {
        put("exchange", exchange);
    }

    public String getIndustry() {
        return getString("industry");
    }

    public void setIndustry(String industry) {
        put("industry", industry);
    }

    public boolean isForeign() {
        return getBoolean("is_foreign");
    }

    public void setForeign(boolean foreign) {
        put("is_foreign", foreign);
    }

    public String assetType() {
        return ASSET_TYPE;
    }

    public static List<Stock> getStockList(JSONArray jsonArray) {
        ArrayList<Stock> stockList = new ArrayList<Stock>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String exchangeTemp = jsonObject.getString("Exchange");
                if (!exchangeTemp.equals("DELISTED")) {

                    String tickerTemp = jsonObject.getString("Ticker").toUpperCase();
                    String nameTemp = jsonObject.getString("Name").toUpperCase();

                    String sectorTemp = jsonObject.getString("Sector");
                    String industryTemp = jsonObject.getString("Industry");
                    String foreignTemp = jsonObject.getString("Is Foreign");
                    boolean isForeignTemp = true;
                    if (foreignTemp.equals("N")) {
                        isForeignTemp = false;
                    }
                    stockList.add(new Stock(tickerTemp, nameTemp, sectorTemp, exchangeTemp, industryTemp, isForeignTemp));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stockList;
    }

}
