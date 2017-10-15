package com.investrapp.investr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.CompetitionsAdapter;
import com.investrapp.investr.apis.FacebookAPI;
import com.investrapp.investr.apis.ParseAPI;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.utils.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Player mCurrentPlayer;
    private List<Competition> mCompetitions;
    private CompetitionsAdapter competitionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupRecyclerView();
        getCurrentUser();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        RecyclerView rvCompetitions = (RecyclerView) findViewById(R.id.rvCompetitions);
        mCompetitions = new ArrayList<>();
        competitionsAdapter = new CompetitionsAdapter(this, mCompetitions);
        rvCompetitions.setAdapter(competitionsAdapter);
        rvCompetitions.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(rvCompetitions).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(HomeActivity.this, CompetitionActivity.class);
                        i.putExtra("player", mCurrentPlayer);
                        i.putExtra("competition", mCompetitions.get(position));
                        startActivity(i);
                    }
                }
        );
    }

    private void getCurrentUser() {
        ParseAPI.getPlayer(Profile.getCurrentProfile().getId(), new FindCallback<Player>() {
            @Override
            public void done(List<Player> objects, ParseException e) {
                if (objects.size() == 0) {
                    FacebookAPI.getCurrentUser(new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d("fb_response", object.toString());
                            mCurrentPlayer = Player.getPlayerFromFB(object);
                            ParseAPI.savePlayer(mCurrentPlayer);
                            getAllCompetitionsForUser();
                        }
                    });
                } else {
                    mCurrentPlayer = objects.get(0);
                    getAllCompetitionsForUser();
                }
            }
        });
    }

    private void getAllCompetitionsForUser() {
        ParseAPI.getAllCompetitionsForPlayer(mCurrentPlayer, new FindCallback<Competition>() {
            @Override
            public void done(List<Competition> objects, ParseException e) {
                mCompetitions.addAll(objects);
                competitionsAdapter.notifyDataSetChanged();
            }
        });
    }

}
