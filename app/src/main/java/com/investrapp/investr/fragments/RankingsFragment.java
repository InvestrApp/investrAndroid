package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.RankingsAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Ranking;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
                String message = getLastUpdatedFormatted(mRankings.get(0));
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    private String getLastUpdatedFormatted(Ranking ranking) {
        if (ranking == null) {
            return "Rankings are updated every 15 minutes";
        }
        Date date = ranking.getUpdatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        DateFormat df = new android.text.format.DateFormat();
        try {
            return "Rankings last updated at " + df.format("hh:mm a", sdf.parse(date.toString())).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Rankings are updated every 15 minutes";
        }
    }

}
