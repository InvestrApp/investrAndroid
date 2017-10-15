package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Competition;

public class RankingsFragment extends Fragment {

    private View view;
    private Competition mCompetition;

    public RankingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rankings, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tvPlaceholder);
        mCompetition = getArguments().getParcelable("competiton");
        textView.setText(mCompetition.getName());
        return view;
    }

    public static RankingsFragment newInstance(Competition competition) {
        RankingsFragment fragment = new RankingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("competiton", competition);
        fragment.setArguments(args);
        return fragment;
    }

}
