package com.investrapp.investr.fragments;

import android.os.Bundle;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import java.util.List;

/**
 * Created by michaelsignorotti on 10/19/17.
 */

public class MonthlyPriceFragment extends AssetPriceFragment {



    public MonthlyPriceFragment() {
    }


    public static MonthlyPriceFragment newInstance(String ticker) {
        MonthlyPriceFragment fragment = new MonthlyPriceFragment();
        Bundle args = new Bundle();
        args.putString("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void loadPrices(String assetTicker) {

        AlphaVantageClient.getCurrentDigitalCurrencyPricesMonthly(assetTicker, new AlphaVantageDigitalCurrencyPricesCallHandler() {
            @Override
            public void onPricesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
                updatePrices(cryptocurrencyPriceTimeSeries);
            }
        });
    }
}
