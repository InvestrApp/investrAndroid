package com.investrapp.investr.apis;

import android.content.Context;
import android.util.Log;

import com.investrapp.investr.R;
import com.investrapp.investr.databaseSetup.DatabaseSetupUtils;
import com.investrapp.investr.models.Cryptocurrency;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.investrapp.investr.models.Cryptocurrency.getCrytocurrencyList;

/**
 * Created by michaelsignorotti on 10/13/17.
 */

public class AlphAvantageClient {

    //Alpha Avantage API Parameters
    public static final String FUNCTION = "function";
    public static final String SYMBOL = "symbol";
    public static final String INTERVAL = "interval";
    public static final String API_KEY = "apikey";


    //Alpha Avantage API Parameter Values
    public static final String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";
    public static final String PRICE_INTERVAL_ONE_MIN = "1min";
    public static final String ALPHA_AVANTAGE_URL = "https://www.alphavantage.co/query";
    public static final String ALPHA_AVANTAGE_CRYPTOCURRENCY_LIST_URL = "https://www.alphavantage.co/digital_currency_list/";

    /**
     * This method retrieves the current stock price from the Alpha Advantage API. Specifying the
     * price interval as "1min" allows us to get the most recent price. Although the callback
     * contains multiple price observations, we can extract the most recent value to present to the
     * user.
     *
     * @param symbol: The ticker symbol of a particular stock.
     */

    public static void queryCurrentStockPrice(Context context, String symbol) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_AVANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, context.getResources().getString(R.string.ALPHA_AVANTAGE_API_KEY));
        urlBuilder.addQueryParameter(FUNCTION, TIME_SERIES_INTRADAY);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AlphaAvantageClient", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("AlphaAvantageClient", response.toString());
                    throw new IOException("Unexpected code " + response);
                }
                String responseData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject timeSeries = jsonObject.getJSONObject("Time Series (1min)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void queryAllCrypotocurrencies() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_AVANTAGE_CRYPTOCURRENCY_LIST_URL).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AlphaAvantageClient", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("AlphaAvantageClient", response.toString());
                    throw new IOException("Unexpected code " + response);
                }
                List<Cryptocurrency> cryptocurrencyList = Cryptocurrency.getCrytocurrencyList(response);
                DatabaseSetupUtils.addAllCryptocurrencies(cryptocurrencyList);
                }
        });
    }
}
