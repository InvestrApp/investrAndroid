package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.PriceAdapter;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.AlphaVantageDigitalCurrencyPricesCallHandler;
import com.investrapp.investr.models.Price;

import java.util.ArrayList;
import java.util.List;


public class AssetPriceFragment extends Fragment  {

    PriceAdapter priceAdapter;
    ArrayList<Price> prices;
    RecyclerView rvPrices;

    String ticker;

    LinearLayoutManager linearLayoutManager;


    public AssetPriceFragment() {}

    public static AssetPriceFragment newInstance(String ticker) {
        AssetPriceFragment fragment = new AssetPriceFragment();
        Bundle args = new Bundle();
        args.putString("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View v = inflater.inflate(R.layout.fragment_asset_price, container, false);


        rvPrices = (RecyclerView) v.findViewById(R.id.rvAssetPrices);
        prices = new ArrayList<Price>();

        priceAdapter = new PriceAdapter(prices);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPrices.setLayoutManager(linearLayoutManager);


        //set the adapter
        rvPrices.setAdapter(priceAdapter);


        this.ticker = getArguments().getString("ticker");
        loadPrices(ticker);

        return v;
    }

    public void loadPrices(String assetTicker) {
        AlphaVantageClient.getCurrentDigitalCurrencyPricesMonthly(assetTicker, new AlphaVantageDigitalCurrencyPricesCallHandler() {
            @Override
            public void onPricesResponse(List<Price> prices) {
                updatePrices(prices);
            }
        });
    }

    public void updatePrices(final List<Price> pricesList) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                prices.addAll(pricesList);
                priceAdapter.notifyDataSetChanged();
            }
        });

    }
}

