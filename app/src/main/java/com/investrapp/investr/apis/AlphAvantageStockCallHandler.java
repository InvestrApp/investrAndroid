package com.investrapp.investr.apis;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class AlphAvantageStockCallHandler implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("AlphaAvantageClient", e.toString());
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            Log.e("AlphaAvantageClient", response.toString());
            throw new IOException("Unexpected code " + response);
        }
        String responseData = response.body().string();
        Log.d("AlphaAvantageClient",responseData);
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject timeSeries = jsonObject.getJSONObject("Time Series (1min)");
            JSONObject timeData = timeSeries.getJSONObject(timeSeries.keys().next());
            Double price = timeData.getDouble(timeData.keys().next());
            onPriceResponse(price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onPriceResponse(Double price);

}
