package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Portfolio;

public abstract class PortfolioItemsFragment extends Fragment {

    protected View view;
    protected Portfolio mPortfolio;
    protected RecyclerView rvTransactions;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Competition competition = getArguments().getParcelable("competition");
        Player player = getArguments().getParcelable("player");
        this.mPortfolio = new Portfolio(player, competition);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_portfolio_lists, container, false);
        setupRecyclerView();
        getItems();
        return view;
    }

    abstract void setupRecyclerView();

    abstract void getItems();

}