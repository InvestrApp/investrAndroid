package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.AssetAdapter;
import com.investrapp.investr.apis.ParseAPI;
import com.investrapp.investr.interfaces.AssetAdapterListener;
import com.investrapp.investr.interfaces.OnAssetSelectedListener;
import com.investrapp.investr.models.Asset;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Stock;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import static com.investrapp.investr.R.string.stock;

public class MarketplaceFragment extends Fragment implements AssetAdapterListener {

    AssetAdapter assetAdapter;
    ArrayList<Asset> assets;
    RecyclerView rvAssets;

    EditText etMarketplaceSearch;
    Button btnMarketplaceSearch;
    CheckBox cbCryptocurrency;
    CheckBox cbCurrency;
    CheckBox cbCommodities;
    CheckBox cbStock;

    LinearLayoutManager linearLayoutManager;

    private View view;

    public MarketplaceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_marketplace, container, false);

        rvAssets = (RecyclerView) view.findViewById(R.id.rvAssets);
        etMarketplaceSearch = (EditText) view.findViewById(R.id.etMarketplaceSearch);
        btnMarketplaceSearch = (Button) view.findViewById(R.id.btnMarketplaceSearch);
        cbCryptocurrency = (CheckBox) view.findViewById(R.id.cbCryptocurrency);
        cbCurrency = (CheckBox) view.findViewById(R.id.cbForeignCurrency);
        cbCommodities = (CheckBox) view.findViewById(R.id.cbCommodities);
        cbStock = (CheckBox) view.findViewById(R.id.cbStocks);


        assets = new ArrayList<Asset>();

        assetAdapter = new AssetAdapter(assets, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvAssets.setLayoutManager(linearLayoutManager);

        rvAssets.setAdapter(assetAdapter);

        etMarketplaceSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String query = s.toString();

                boolean cryptocurrencyChecked = cbCryptocurrency.isChecked();
                boolean currencyChecked = cbCurrency.isChecked();
                boolean commoditiesChecked = cbCommodities.isChecked();
                boolean stockChecked = cbStock.isChecked();

                //TODO
                //add additional conditions below and combine queries
                if (cryptocurrencyChecked) {
                    getMatchingCryptocurrency(query);
                }
            }
        });

        return view;
    }

    public static MarketplaceFragment newInstance() {
        MarketplaceFragment fragment = new MarketplaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void getMatchingStocks(String query) {
        ParseAPI.getMatchingStocks(query, new FindCallback<Stock>() {
            @Override
            public void done(List<Stock> stocks, ParseException e) {
                Log.d("CompetitionActivity", "size:  " + stocks.size());
                for (Stock stock : stocks) {
                    Log.d("CompetitionActivity", stock.getName());
                }
            }
        });
    }

    private void getMatchingCryptocurrency(String query) {
        ParseAPI.getMatchingCryptocurrencies(query, new FindCallback<Cryptocurrency>() {
            @Override
            public void done(List<Cryptocurrency> cryptocurrencies, ParseException e) {
                Log.d("CompetitionActivity", "size:  " + cryptocurrencies.size());
                assets.clear();
                assets.addAll(cryptocurrencies);
                assetAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void assetSelected(int position) {
        Asset asset = assets.get(position);
        ((OnAssetSelectedListener) getContext()).onAssetSelected(asset);
    }
}
