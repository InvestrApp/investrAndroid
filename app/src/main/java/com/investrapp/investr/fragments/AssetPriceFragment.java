package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.PriceAdapter;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class AssetPriceFragment extends Fragment {

    PriceAdapter priceAdapter;
    ArrayList<Price> prices;
    RecyclerView rvPrices;

    LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View v = inflater.inflate(R.layout.fragment_asset_price, container, false);


        rvPrices = (RecyclerView) v.findViewById(R.id.rvAssetPrices);
        prices = new ArrayList<Price>();

        priceAdapter = new PriceAdapter(getContext(), prices);
        linearLayoutManager = new LinearLayoutManager(getContext());

        rvPrices.setLayoutManager(linearLayoutManager);


        //set the adapter
        rvPrices.setAdapter(priceAdapter);

Log.d("AssetPriceAdapter", "here here");
        //this.ticker = getArguments().getString("ticker");
        loadPrices("BTC");

        return v;
    }

    public abstract void loadPrices(String assetTicker);

    public void updatePrices(final CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.d("AssetPriceAdapter", "" + cryptocurrencyPriceTimeSeries.getPriceList().size());
                prices.addAll(cryptocurrencyPriceTimeSeries.getPriceList());
                priceAdapter.notifyDataSetChanged();
            }
        });
    }
}

