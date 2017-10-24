package com.investrapp.investr.fragments.assetPrice;

import android.os.Bundle;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;

/**
 * Created by michaelsignorotti on 10/19/17.
 */

public class WeeklyPriceFragment extends AssetPriceFragment {

    public WeeklyPriceFragment() {

    }

    public static WeeklyPriceFragment newInstance(String ticker) {
        WeeklyPriceFragment fragment = new WeeklyPriceFragment();
        Bundle args = new Bundle();
        args.putString("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadPrices(String assetTicker) {
        AlphaVantageClient.getCurrentDigitalCurrencyPricesWeekly(assetTicker, new AlphaVantageDigitalCurrencyPricesCallHandler() {
            @Override
            public void onPricesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
                updatePrices(cryptocurrencyPriceTimeSeries);
            }
        });
    }

}
