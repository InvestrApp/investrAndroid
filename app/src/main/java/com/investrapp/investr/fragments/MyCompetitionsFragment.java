package com.investrapp.investr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.investrapp.investr.activities.CompetitionActivity;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.ParseGetAllCompetitionsHandler;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.utils.ItemClickSupport;

import java.util.List;

public class MyCompetitionsFragment extends HomeCompetitionsFragment {

    public static MyCompetitionsFragment newInstance() {
        Bundle args = new Bundle();
        MyCompetitionsFragment fragment = new MyCompetitionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupTouchListener() {
        ItemClickSupport.addTo(rvCompetitions).setOnItemClickListener(
            new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(getActivity(), CompetitionActivity.class);
                    i.putExtra("player", mCurrentPlayer);
                    i.putExtra("competition", mCompetitions.get(position));
                    startActivity(i);
                }
            }
        );
    }

    @Override
    protected void getAllCompetitions() {
        ParseClient.getAllCompetitionsForPlayer(mCurrentPlayer, new ParseGetAllCompetitionsHandler() {
            @Override
            public void done(List<Competition> competitions) {
                competitionsAdapter.clear();
                competitionsAdapter.addAll(competitions);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void addCompetition(Competition competition) {
        mCompetitions.add(0, competition);
        competitionsAdapter.notifyItemInserted(0);
        rvCompetitions.smoothScrollToPosition(0);

    }

}