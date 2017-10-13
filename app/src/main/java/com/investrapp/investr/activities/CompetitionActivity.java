package com.investrapp.investr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.investrapp.investr.R;
import com.investrapp.investr.apis.FacebookAPI;
import com.investrapp.investr.apis.ParseAPI;
import com.investrapp.investr.models.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class CompetitionActivity extends AppCompatActivity {

    private Player mCurrentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        fetchProfile();
    }

    private void fetchProfile() {
        FacebookAPI.getCurrentUser(new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    mCurrentPlayer = Player.getPlayer(object);
                    ParseAPI.savePlayer(mCurrentPlayer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
