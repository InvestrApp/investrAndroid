package com.investrapp.investr.fragments;

import android.os.Bundle;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;


/**
 * Created by michaelsignorotti on 10/19/17.
 */

public class IntradayPriceFragment extends AssetPriceFragment {



    public IntradayPriceFragment() {
    }


    public static IntradayPriceFragment newInstance(String ticker) {
        IntradayPriceFragment fragment = new IntradayPriceFragment();
        Bundle args = new Bundle();
        args.putString("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void loadPrices(String assetTicker) {

        AlphaVantageClient.getCurrentDigitalCurrencyPricesIntraday(assetTicker, new AlphaVantageDigitalCurrencyPricesCallHandler() {
            @Override
            public void onPricesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
                updatePrices(cryptocurrencyPriceTimeSeries);
            }
        });
    }
}
