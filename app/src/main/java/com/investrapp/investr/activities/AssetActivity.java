package com.investrapp.investr.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.investrapp.investr.R;
import com.investrapp.investr.fragments.AssetPriceFragment;
import com.investrapp.investr.fragments.PricesPagerAdapter;
import com.investrapp.investr.models.Asset;

import org.parceler.Parcels;


public class AssetActivity extends AppCompatActivity {

    Toolbar toolbar;

    ViewPager vpPager;
    TabLayout tabLayout;

    PricesPagerAdapter pricesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        // Find the toolbar view inside the activity layout
        //toolbar = (Toolbar) findViewById(R.id.AssetToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String ticker = intent.getStringExtra("ticker");
        //Asset asset = (Asset) Parcels.unwrap(getIntent().getParcelableExtra("asset"));


        vpPager = (ViewPager) findViewById(R.id.viewpager);

        pricesPagerAdapter = new PricesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(pricesPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

    }
}
