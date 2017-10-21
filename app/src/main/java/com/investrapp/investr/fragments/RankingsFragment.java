package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.RankingsAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Ranking;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class RankingsFragment extends Fragment {

    private View view;
    private Competition mCompetition;
    private List<Ranking> mRankings;
    private RecyclerView rvRankings;
    private RankingsAdapter mAdapter;

    public RankingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompetition = getArguments().getParcelable("competiton");
        mRankings = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rankings, container, false);
        mCompetition = getArguments().getParcelable("competiton");
        setupRecyclerView();
        getRankings();
        return view;
    }

    private void setupRecyclerView() {
        rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);
        mAdapter = new RankingsAdapter(view.getContext(), mRankings);
        rvRankings.setAdapter(mAdapter);
        rvRankings.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void getRankings() {
        ParseClient.getRankingsForCompetition(mCompetition, new FindCallback<Ranking>() {
            @Override
            public void done(List<Ranking> objects, ParseException e) {
                mRankings.clear();
                mRankings.addAll(objects);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public static RankingsFragment newInstance(Competition competition) {
        RankingsFragment fragment = new RankingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("competiton", competition);
        fragment.setArguments(args);
        return fragment;
    }

}
