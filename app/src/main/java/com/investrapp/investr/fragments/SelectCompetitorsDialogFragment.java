package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.PlayerAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class SelectCompetitorsDialogFragment extends DialogFragment {

    protected Player mCurrentPlayer;
    protected Competition mCompetition;
    protected List<Player> mPlayers;
    protected PlayerAdapter playerAdapter;
    protected RecyclerView rvPlayers;
    protected LinearLayoutManager linearLayoutManager;
    protected View view;
    protected Button btnDoneSelectingPlayers;

    public SelectCompetitorsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SelectCompetitorsDialogFragment newInstance(Competition competition, Player currentPlayer) {
        SelectCompetitorsDialogFragment frag = new SelectCompetitorsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("player", currentPlayer);
        args.putParcelable("competition", competition);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_competitors, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCurrentPlayer = getArguments().getParcelable("player");
        mCompetition = getArguments().getParcelable("competition");
        this.view = view;
        setupRecyclerView();
        this.btnDoneSelectingPlayers = (Button) view.findViewById(R.id.btnDoneSelectingPlayers);

        btnDoneSelectingPlayers.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        loadAllPlayers();
    }

    private void setupRecyclerView() {
        rvPlayers = (RecyclerView) view.findViewById(R.id.rvPlayers);
        mPlayers = new ArrayList<Player>();
        playerAdapter = new PlayerAdapter(getContext(), mPlayers, mCompetition, mCurrentPlayer);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlayers.setAdapter(playerAdapter);
        rvPlayers.setLayoutManager(linearLayoutManager);
    }

    private void loadAllPlayers() {
        ParseClient.getAllPlayers(new FindCallback<Player>() {
            @Override
            public void done(List<Player> objects, ParseException e) {

                String currentPlayerId = mCurrentPlayer.getId();
                for (Player p : objects) {
                    //check to make sure the current player is not added to the list
                    if (!currentPlayerId.equals(p.getId())) {
                        mPlayers.add(p);
                    }
                }
                playerAdapter.notifyDataSetChanged();
            }
        });
    }

}