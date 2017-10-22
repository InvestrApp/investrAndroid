package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.AssetAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.interfaces.AssetAdapterListener;
import com.investrapp.investr.interfaces.OnAssetSelectedListener;
import com.investrapp.investr.models.Asset;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Stock;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceFragment extends Fragment implements AssetAdapterListener {

    AssetAdapter assetAdapter;
    ArrayList<Asset> assets;
    RecyclerView rvAssets;

    LinearLayoutManager linearLayoutManager;

    private View view;

    public MarketplaceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_marketplace, container, false);

        rvAssets = (RecyclerView) view.findViewById(R.id.rvAssets);

        assets = new ArrayList<Asset>();

        assetAdapter = new AssetAdapter(getContext(), assets, this);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvAssets.setLayoutManager(linearLayoutManager);
        rvAssets.setAdapter(assetAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar.
        inflater.inflate(R.menu.menu_marketplace, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getMatchingCryptocurrency(newText);
                return true;
            }
        });

    }

    public static MarketplaceFragment newInstance() {
        MarketplaceFragment fragment = new MarketplaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void getMatchingCryptocurrency(String query) {
        ParseClient.getMatchingCryptocurrenciesByName(query, new FindCallback<Cryptocurrency>() {
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
