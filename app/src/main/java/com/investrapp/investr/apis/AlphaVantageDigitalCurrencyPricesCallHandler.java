package com.investrapp.investr.apis;

import android.util.Log;

import com.investrapp.investr.models.Price;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class AlphaVantageDigitalCurrencyPricesCallHandler implements Callback {


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
            ArrayList<Price> pricesList = new ArrayList<Price>();
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Digital Currency Monthly)");

            Iterator<String> keys = timeSeries.keys();
            int count = 0;
            while (keys.hasNext() && count < 25) {
                String date = keys.next();
                JSONObject values = timeSeries.getJSONObject(date);
                Double price = values.getDouble(values.keys().next());

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = null;
                Log.d("DEBUG", date);

                try {
                    parsedDate = format.parse(date);
                    Log.d("DEBUG", parsedDate.toString());
                    pricesList.add(new Price(parsedDate, price));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                count++;
            }

            onPricesResponse(pricesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onPricesResponse(List<Price> prices);

}