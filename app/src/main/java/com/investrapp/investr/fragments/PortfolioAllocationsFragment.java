package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.investrapp.investr.adapters.PortfolioAllocationsAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Allocation;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.investrapp.investr.R.id.rvPortfolioList;

public class PortfolioAllocationsFragment extends PortfolioItemsFragment {

    protected PortfolioAllocationsAdapter mAdapter;

    public static PortfolioAllocationsFragment newInstance(Competition competition, Player player) {
        PortfolioAllocationsFragment portfolioAllocationsFragment = new PortfolioAllocationsFragment();
        Bundle args = new Bundle();
        args.putParcelable("competition", competition);
        args.putParcelable("player", player);
        portfolioAllocationsFragment.setArguments(args);
        return portfolioAllocationsFragment;
    }

    @Override
    void setupRecyclerView() {
        mAdapter = new PortfolioAllocationsAdapter(mPortfolio);
        rvTransactions = (RecyclerView) view.findViewById(rvPortfolioList);
        rvTransactions.setAdapter(mAdapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    void getItems() {
        ParseClient.getTransactionsForPlayerInCompetition(mPortfolio.getPlayer(), mPortfolio.getCompetition(), new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                HashMap<String, Integer> allocationsMap = new HashMap<String, Integer>();
                for (Transaction t : objects) {
                    String ticker = t.getAssetTicker();
                    int units = t.getUnits();

                    if (!allocationsMap.containsKey(ticker)) {
                        allocationsMap.put(ticker, units);
                    } else {
                        int currentUnitCount = allocationsMap.get(ticker);
                        currentUnitCount += units;
                        allocationsMap.put(ticker, currentUnitCount);
                    }

                    if (ticker.equals(Cash.TICKER)) {
                        mPortfolio.setCash(t.getPrice());
                    }
                }
                ArrayList<Allocation> allocations = new ArrayList<Allocation>();

                for (String key : allocationsMap.keySet()) {
                    int units = allocationsMap.get(key);
                    if (units >= 1) {
                        allocations.add(new Allocation(key, allocationsMap.get(key)));
                    }
                }

                mPortfolio.addAllocations(allocations);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}