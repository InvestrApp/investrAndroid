package com.investrapp.investr.fragments.assetPrice;

import android.os.Bundle;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.utils.MPAndroidChart.DayAxisValueFormatter;

/**
 * Created by michaelsignorotti on 10/19/17.
 */

public class DailyPriceFragment extends AssetPriceFragment {

    public DailyPriceFragment() {

    }

    public static DailyPriceFragment newInstance(String ticker) {
        DailyPriceFragment fragment = new DailyPriceFragment();
        Bundle args = new Bundle();
        args.putString("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadPrices(String assetTicker) {
        AlphaVantageClient.getCurrentDigitalCurrencyPricesDaily(assetTicker, new AlphaVantageDigitalCurrencyPricesCallHandler() {
            @Override
            public void onPricesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
                updatePrices(cryptocurrencyPriceTimeSeries);
            }
        });
    }

    @Override
    protected IAxisValueFormatter getFormatter(Long mintime) {
        return new DayAxisValueFormatter(mintime);
    }

}
