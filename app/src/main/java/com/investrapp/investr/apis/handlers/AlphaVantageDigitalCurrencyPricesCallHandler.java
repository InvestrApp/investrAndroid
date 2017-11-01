package com.investrapp.investr.apis.handlers;

import android.util.Log;

import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class AlphaVantageDigitalCurrencyPricesCallHandler implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        String responseData = response.body().string();
        try {
            ArrayList<Price> priceList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(responseData);
            Log.d("DEBUG", jsonObject.toString());
            //get the meta data
            Iterator<String> mainKeys = jsonObject.keys();
            String metaDataKey = null;
            if(mainKeys.hasNext()) {
                metaDataKey = mainKeys.next();
            }
            JSONObject metaData = jsonObject.getJSONObject(metaDataKey);
            String information = metaData.getString("1. Information");
            String ticker = metaData.getString("2. Digital Currency Code");
            String cryptocurrencyName = metaData.getString("3. Digital Currency Name");
            String market = metaData.getString("4. Market Code");
            String marketName = metaData.getString("5. Market Name");
            String lastRefreshed = null;
            if (metaData.has("6. Last Refreshed")) {
                lastRefreshed = metaData.getString("6. Last Refreshed");
            } else {
                lastRefreshed = metaData.getString("7. Last Refreshed");
            }
            String timeZone = null;
            if (metaData.has("7. Time Zone")) {
                timeZone = metaData.getString("7. Time Zone");
            } else {
                timeZone = metaData.getString("8. Time Zone");
            }

            //get the time series of digital currency values in the specified market
            JSONObject timeSeries = jsonObject.getJSONObject(mainKeys.next());
            Iterator<String> keys = timeSeries.keys();
            while (keys.hasNext()) {
                String date = keys.next();
                JSONObject jsonObjectValue = timeSeries.getJSONObject(date);
                Double valuation = jsonObjectValue.getDouble(jsonObjectValue.keys().next());
                priceList.add(new Price(date, valuation));
            }
            Collections.sort(priceList);

            CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries =
                    new CryptocurrencyPriceTimeSeries(information, ticker, cryptocurrencyName, market, marketName, lastRefreshed, timeZone, priceList);
            if (cryptocurrencyPriceTimeSeries != null) {
                onPricesResponse(cryptocurrencyPriceTimeSeries);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onPricesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries);

}