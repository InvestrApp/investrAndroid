package com.investrapp.investr.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.investrapp.investr.R;
import com.investrapp.investr.fragments.AssetPriceFragment;
import com.investrapp.investr.models.Asset;

import org.parceler.Parcels;


public class AssetActivity extends AppCompatActivity {

    Toolbar toolbar;

    Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.AssetToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String ticker = intent.getStringExtra("ticker");
        //Asset asset = (Asset) Parcels.unwrap(getIntent().getParcelableExtra("asset"));



        //create the user fragment
        AssetPriceFragment userTimelineFragment = AssetPriceFragment.newInstance(ticker);

        //display the user timeline fragment inside the container dynamically
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make changes
        ft.replace(R.id.flContainer, userTimelineFragment);
        //commit
        ft.commit();

    }

}
