package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.CompetitionsAdapter;
import com.investrapp.investr.apis.FacebookClient;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.parse.FindCallback;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class HomeCompetitionsFragment extends Fragment {

    protected Player mCurrentPlayer;
    protected List<Competition> mCompetitions;
    protected CompetitionsAdapter competitionsAdapter;
    protected RecyclerView rvCompetitions;
    protected LinearLayoutManager linearLayoutManager;
    protected View view;
    protected SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_competitions, container, false);
        setupRecyclerView();
        getCurrentUser();
        return view;
    }

    private void setupRecyclerView() {
        rvCompetitions = (RecyclerView) view.findViewById(R.id.rvCompetitions);
        mCompetitions = new ArrayList<>();
        competitionsAdapter = new CompetitionsAdapter(mCompetitions);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvCompetitions.setAdapter(competitionsAdapter);
        rvCompetitions.setLayoutManager(linearLayoutManager);
        setupTouchListener();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllCompetitions();
            }
        });
        swipeContainer.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryLight,
                R.color.colorPrimaryDark);
    }

    protected abstract void setupTouchListener();

    protected void getCurrentUser() {
        if (Profile.getCurrentProfile() == null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                String id = object.getString("id");
                                getCurrentUserInfo(id);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            getCurrentUserInfo(Profile.getCurrentProfile().getId());
        }
    }

    private void getCurrentUserInfo(String id) {
        ParseClient.getPlayer(id, new FindCallback<Player>() {
            @Override
            public void done(List<Player> objects, ParseException e) {
                if (objects.size() == 0) {
                    FacebookClient.getCurrentUser(new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d("fb_response", object.toString());
                            mCurrentPlayer = Player.getPlayerFromFB(object);
                            ParseClient.savePlayer(mCurrentPlayer);
                            getAllCompetitions();
                        }
                    });
                } else {
                    mCurrentPlayer = objects.get(0);
                    getAllCompetitions();
                }
            }
        });
    }

    protected abstract void getAllCompetitions();

}