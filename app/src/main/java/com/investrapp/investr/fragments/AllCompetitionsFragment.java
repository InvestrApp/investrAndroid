package com.investrapp.investr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.investrapp.investr.R;
import com.investrapp.investr.activities.CompetitionActivity;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.ParseGetAllCompetitionsHandler;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Transaction;
import com.investrapp.investr.utils.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AllCompetitionsFragment extends HomeCompetitionsFragment {


    private OnAddCompetitionListener listener;

    public interface OnAddCompetitionListener {
        void onAddCompetition(Competition competition, CompetitionPlayer competitionPlayer);
    }

    public static AllCompetitionsFragment newInstance() {
        Bundle args = new Bundle();
        AllCompetitionsFragment fragment = new AllCompetitionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupTouchListener() {
        ItemClickSupport.addTo(rvCompetitions).setOnItemClickListener(
            new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    joinCompetition(mCompetitions.get(position));
                }
            }
        );
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCompetitionListener) {
            listener = (OnAddCompetitionListener) context;
        } else {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    protected void getAllCompetitions() {
        ParseClient.getAllCompetitions(new FindCallback<Competition>() {
            @Override
            public void done(List<Competition> objects, ParseException e) {
                getAllCompetitionsForPlayer();
                competitionsAdapter.clear();
                mCompetitions.addAll(objects);
            }
        });
    }

    private void getAllCompetitionsForPlayer() {
        ParseClient.getAllCompetitionsForPlayer(mCurrentPlayer, new ParseGetAllCompetitionsHandler() {
            @Override
            public void done(List<Competition> competitions) {
              playerCompetitions.addAll(competitions);
                competitionsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void joinCompetition(final Competition competition) {
        if (Competition.isPlayerInCompetition(competition, playerCompetitions)) {
            Toast.makeText(getContext(), "You're already in this competition", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = new GregorianCalendar();
        Date currentDate = calendar.getTime();
        if (competition.getEndDate().before(currentDate)) {
            Toast.makeText(getContext(), "Competition has already ended", Toast.LENGTH_SHORT).show();
            return;
        }

        Double initialCash = 10000.00;
        if (competition.getInitialAmount() != null) {
            initialCash = competition.getInitialAmount();
        }

        Transaction transaction = new Transaction(mCurrentPlayer, competition, Cash.ASSET_TYPE, Cash.TICKER,
                currentDate, Transaction.TransactionAction.BUY, initialCash, 1);
        ParseClient.addTransaction(transaction);
        CompetitionPlayer competitionPlayer = new CompetitionPlayer(competition, mCurrentPlayer);
        ParseClient.addPlayerToCompetition(competitionPlayer);
        ParseClient.sendPushToAllCompetitors(competition, mCurrentPlayer, "A new player has joined " + competition.getName());
        listener.onAddCompetition(competition, competitionPlayer);
    }

    private boolean isPlayerInCompetition(Competition competition) {
        for (Competition playerCompetition : playerCompetitions) {
            if (playerCompetition.getObjectId().equals(competition.getObjectId())) {
                return true;
            }
        }
        return false;
    }
}