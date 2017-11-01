package com.investrapp.investr.apis;


import android.util.Log;

import com.investrapp.investr.databaseSetup.DatabaseSetupUtils;

import com.investrapp.investr.models.Stock;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SharadarClient {

    public static final String SHARADAR_API_ALL_STOCKS = "http://www.sharadar.com/meta/tickers.json";

    public static void queryAllStocks() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SHARADAR_API_ALL_STOCKS).newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SharadarClient", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("SharadarClient", response.toString());
                    throw new IOException("Unexpected code " + response);
                }
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<Stock> stockList = Stock.getStockList(jsonArray);
                    DatabaseSetupUtils.addAllStocks(stockList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
