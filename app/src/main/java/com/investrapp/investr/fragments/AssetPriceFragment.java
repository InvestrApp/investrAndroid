package com.investrapp.investr.fragments;

import android.content.Context;
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
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import java.util.ArrayList;

public abstract class AssetPriceFragment extends Fragment {

    PriceAdapter priceAdapter;
    ArrayList<Price> prices;
    RecyclerView rvPrices;

    LinearLayoutManager linearLayoutManager;

    String ticker;

    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private OnPriceTimeSeriesResponseListener listener;

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

        this.ticker = getArguments().getString("ticker");
        loadPrices(ticker);

        return v;
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPriceTimeSeriesResponseListener) {
            listener = (OnPriceTimeSeriesResponseListener) context;
        } else {
            throw new ClassCastException(context.toString());
        }
    }

    public abstract void loadPrices(String assetTicker);

    public void updatePrices(final CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
        if(getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                listener.onPriceTimeSeriesResponse(cryptocurrencyPriceTimeSeries);
                prices.addAll(cryptocurrencyPriceTimeSeries.getPriceList());
                priceAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnPriceTimeSeriesResponseListener {
        void onPriceTimeSeriesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries);
    }

}

