package com.investrapp.investr.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.fragments.AssetPriceFragment;
import com.investrapp.investr.fragments.PricesPagerAdapter;
import com.investrapp.investr.models.Asset;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.timeZone;


public class AssetActivity extends AppCompatActivity implements AssetPriceFragment.OnPriceTimeSeriesResponseListener {

    Toolbar toolbar;

    TextView tvAssetName;
    TextView tvAssetTicker;
    TextView tvAssetInformation;
    TextView tvLastRefreshed;
    TextView tvTimeZone;
    TextView tvMarketName;
    TextView tvMarket;
    TextView tvAssetPrice;
    Button btnSellAsset;
    Button btnBuyAsset;

    ViewPager vpPager;
    TabLayout tabLayout;

    PricesPagerAdapter pricesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.AssetToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        this.tvAssetName = findViewById(R.id.tvAssetName);
        this.tvAssetTicker = findViewById(R.id.tvAssetTicker);
        this.tvAssetInformation = findViewById(R.id.tvAssetInformation);
        this.tvLastRefreshed = findViewById(R.id.tvLastRefreshed);
        this.tvTimeZone = findViewById(R.id.tvTimeZone);
        this.tvMarketName = findViewById(R.id.tvMarketName);
        this.tvMarket = findViewById(R.id.tvMarket);
        this.tvAssetPrice = findViewById(R.id.tvAssetPrice);
        this.btnSellAsset = findViewById(R.id.btnSellAsset);
        this.btnBuyAsset = findViewById(R.id.btnBuyAsset);

        vpPager = (ViewPager) findViewById(R.id.viewpager);

        pricesPagerAdapter = new PricesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(pricesPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        Intent intent = getIntent();
        String ticker = intent.getStringExtra("ticker");


    }

    @Override
    public void onPriceTimeSeriesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
        String information = cryptocurrencyPriceTimeSeries.getInformation();
        String ticker = cryptocurrencyPriceTimeSeries.getTicker();
        String cryptocurrencyName = cryptocurrencyPriceTimeSeries.getCryptocurrencyName();
        String market = cryptocurrencyPriceTimeSeries.getMarket();
        String marketName = cryptocurrencyPriceTimeSeries.getMarketName();
        String lastRefreshed = cryptocurrencyPriceTimeSeries.getLastRefreshed();
        String timeZone = cryptocurrencyPriceTimeSeries.getTimeZone();

        getSupportActionBar().setTitle(cryptocurrencyName);
        tvAssetInformation.setText(information);
        tvAssetTicker.setText(ticker);
        tvMarket.setText(market);
        tvMarketName.setText(marketName);
        tvLastRefreshed.setText(lastRefreshed);
        tvTimeZone.setText(timeZone);
    }
}
