package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.investrapp.investr.adapters.TransactionHistoryAdapter;
import com.investrapp.investr.apis.ParseClient;

import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import static com.investrapp.investr.R.id.rvPortfolioList;

public class PortfolioTransactionsFragment extends PortfolioItemsFragment {

    protected TransactionHistoryAdapter mAdapter;

    public static PortfolioTransactionsFragment newInstance(Competition competition, Player player) {
        PortfolioTransactionsFragment portfolioTransactionsFragment = new PortfolioTransactionsFragment();
        Bundle args = new Bundle();
        args.putParcelable("competition", competition);
        args.putParcelable("player", player);
        portfolioTransactionsFragment.setArguments(args);
        return portfolioTransactionsFragment;
    }

    @Override
    void setupRecyclerView() {
        mAdapter = new TransactionHistoryAdapter(view.getContext(), mPortfolio.getTransactions());
        rvTransactions = (RecyclerView) view.findViewById(rvPortfolioList);
        rvTransactions.setAdapter(mAdapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    void getItems() {
        ParseClient.getTransactionsForPlayerInCompetition(mPortfolio.getPlayer(), mPortfolio.getCompetition(), new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                mPortfolio.addTransactions(objects);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}