package com.investrapp.investr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.investrapp.investr.activities.CompetitionActivity;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.ParseGetAllCompetitionsHandler;
import com.investrapp.investr.apis.handlers.ParseGetAllPlayersHandler;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.investrapp.investr.utils.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AllCompetitionsFragment extends HomeCompetitionsFragment {

    private List<Competition> playerCompetitions;

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

    protected void getAllCompetitionsForPlayer() {
        playerCompetitions = new ArrayList<>();
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
        if (isPlayerInCompetition(competition)) {
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
        Transaction transaction = new Transaction(mCurrentPlayer, competition, Cash.ASSET_TYPE, Cash.TICKER,
                currentDate, Transaction.TransactionAction.BUY, initialCash, 1);
        ParseClient.addTransaction(transaction);

        CompetitionPlayer competitionPlayer = new CompetitionPlayer(competition, mCurrentPlayer);
        ParseClient.addPlayerToCompetition(competitionPlayer);

        ParseClient.getAllPlayersInCompetition(competition, new ParseGetAllPlayersHandler() {
            @Override
            public void done(List<Player> players) {
                for (Player player : players) {
                    ParseClient.sendPushto(player, "A new player has joined " + competition.getName());
                }
            }
        });

        Intent i = new Intent(getActivity(), CompetitionActivity.class);
        i.putExtra("player", mCurrentPlayer);
        i.putExtra("competition", competition);
        startActivity(i);
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