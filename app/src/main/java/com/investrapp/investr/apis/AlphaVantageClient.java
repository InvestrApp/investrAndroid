package com.investrapp.investr.apis;

import android.content.Context;
import android.util.Log;

import com.investrapp.investr.R;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.apis.handlers.AlphaVantageStockCurrentPriceCallHandler;
import com.investrapp.investr.databaseSetup.DatabaseSetupUtils;
import com.investrapp.investr.models.Cryptocurrency;

import java.io.IOException;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by michaelsignorotti on 10/13/17.
 */

public class AlphaVantageClient {

    //Alpha Avantage API Parameters
    public static final String FUNCTION = "function";
    public static final String SYMBOL = "symbol";
    public static final String INTERVAL = "interval";
    public static final String API_KEY = "apikey";
    public static final String MARKET = "market";

    //Alpha Avantage API Parameter Values
    public static final String USD = "USD";
    public static final String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";
    public static final String DIGITAL_CURRENCY_INTRADAY = "DIGITAL_CURRENCY_INTRADAY";
    public static final String DIGITAL_CURRENCY_DAILY = "DIGITAL_CURRENCY_DAILY";
    public static final String DIGITAL_CURRENCY_WEEKLY = "DIGITAL_CURRENCY_WEEKLY";
    public static final String DIGITAL_CURRENCY_MONTHLY = "DIGITAL_CURRENCY_MONTHLY";
    public static final String PRICE_INTERVAL_ONE_MIN = "1min";
    public static final String ALPHA_VANTAGE_URL = "https://www.alphavantage.co/query";
    public static final String ALPHA_VANTAGE_CRYPTOCURRENCY_LIST_URL = "https://www.alphavantage.co/digital_currency_list/";
    public static String ALPHA_VANTAGE_API_KEY;

    public AlphaVantageClient(Context context) {
        ALPHA_VANTAGE_API_KEY = context.getResources().getString(R.string.ALPHA_VANTAGE_API_KEY);
    }

    /**
     * This method retrieves the current stock price from the Alpha Vantage API. Specifying the
     * price interval as "1min" allows us to get the most recent price. Although the callback
     * contains multiple price observations, we can extract the most recent value to present to the
     * user.
     *
     * @param symbol: The ticker symbol of a particular stock.
     */

    public static void getCurrentStockPrice(String symbol, AlphaVantageStockCurrentPriceCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, TIME_SERIES_INTRADAY);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }

    /**
     * This method gets the current price for a digital currency from the Alpha Vantage API.
     * @param ticker
     * @param handler
     */
    public static void getCurrentDigitalCurrencyPrice(String ticker, AlphaVantageDigitalCurrencyCurrentPriceCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, DIGITAL_CURRENCY_INTRADAY);
        urlBuilder.addQueryParameter(MARKET, USD);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, ticker);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }

    /**
     * This method gets a list of cryptocurrency historical prices in five minute intervals from the Alpha Vantage API.
     * @param symbol
     */
    public static void getCurrentDigitalCurrencyPricesIntraday(String symbol, AlphaVantageDigitalCurrencyPricesCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, DIGITAL_CURRENCY_INTRADAY);
        urlBuilder.addQueryParameter(MARKET, USD);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }

    /**
     * This method gets a list of cryptocurrency historical prices in daily intervals from the Alpha Vantage API.
     * @param symbol
     */
    public static void getCurrentDigitalCurrencyPricesDaily(String symbol, AlphaVantageDigitalCurrencyPricesCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, DIGITAL_CURRENCY_DAILY);
        urlBuilder.addQueryParameter(MARKET, USD);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }

    /**
     * This method gets a list of cryptocurrency historical prices in weekly intervals from the Alpha Vantage API.
     * @param symbol
     */
    public static void getCurrentDigitalCurrencyPricesWeekly(String symbol, AlphaVantageDigitalCurrencyPricesCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, DIGITAL_CURRENCY_WEEKLY);
        urlBuilder.addQueryParameter(MARKET, USD);
        urlBuilder.addQueryParameter(INTERVAL, PRICE_INTERVAL_ONE_MIN);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }



    /**
     * This method gets a list of cryptocurrency historical prices in monthly intervals from the Alpha Vantage API.
     * @param symbol
     * @param handler
     */
    public static void getCurrentDigitalCurrencyPricesMonthly(String symbol, AlphaVantageDigitalCurrencyPricesCallHandler handler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, ALPHA_VANTAGE_API_KEY);
        urlBuilder.addQueryParameter(FUNCTION, DIGITAL_CURRENCY_MONTHLY);
        urlBuilder.addQueryParameter(MARKET, USD);
        urlBuilder.addQueryParameter(SYMBOL, symbol);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(handler);
    }

    /**
     * This method is used for loading data into our database.
     */
    public static void queryAllCrypotocurrencies() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ALPHA_VANTAGE_CRYPTOCURRENCY_LIST_URL).newBuilder();
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
