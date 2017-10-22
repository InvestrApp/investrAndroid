package com.investrapp.investr.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.fragments.AssetPriceFragment;
import com.investrapp.investr.fragments.AssetTransactionDialogFragment;
import com.investrapp.investr.fragments.PricesPagerAdapter;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;


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

    Competition competition;
    Player player;
    String ticker;
    String cryptocurrencyName;

    int totalOwned;
    double price;

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

        Intent intent = getIntent();
        this.ticker = intent.getStringExtra("ticker");
        this.competition = (Competition) getIntent().getParcelableExtra("competition");
        this.player = (Player) getIntent().getParcelableExtra("player");

        getAssetPrice(ticker);
      
        pricesPagerAdapter = new PricesPagerAdapter(getSupportFragmentManager(), this, ticker);
        vpPager.setAdapter(pricesPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        btnBuyAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AssetTransactionDialogFragment assetTransactionDialogFragment = AssetTransactionDialogFragment.newInstance(competition, player, cryptocurrencyName, ticker, totalOwned, price, "BUY");

                assetTransactionDialogFragment.show(fm, "fragment_asset_transaction");
            }
        });

        btnSellAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AssetTransactionDialogFragment assetTransactionDialogFragment = AssetTransactionDialogFragment.newInstance(competition, player, cryptocurrencyName, ticker, totalOwned, price, "SELL");

                assetTransactionDialogFragment.show(fm, "fragment_asset_transaction");
            }
        });

        getCryptocurrencyTransactionsByTickerUserCompetition();

    }

    @Override
    public void onPriceTimeSeriesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
        String information = cryptocurrencyPriceTimeSeries.getInformation();
        String ticker = cryptocurrencyPriceTimeSeries.getTicker();
        this.cryptocurrencyName = cryptocurrencyPriceTimeSeries.getCryptocurrencyName();
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

    public void getAssetPrice(String assetTicker) {
        AlphaVantageClient.getCurrentDigitalCurrencyPrice(assetTicker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
            @Override
            public void onPriceResponse(Double priceResponse) {
                setAssetPrice(priceResponse);
            }
        });
    }

    public void setAssetPrice(final Double priceResponse) {
        runOnUiThread(new Runnable() {
            public void run() {
                price  = priceResponse;
                tvAssetPrice.setText("" + String.format("%,.2f", price));
            }
        });
    }

    public void getCryptocurrencyTransactionsByTickerUserCompetition() {
        ParseClient.getCryptocurrencyTransactionsByTickerUserCompetition(ticker, competition, player, new FindCallback<Transaction>() {

            @Override
            public void done(List<Transaction> transactions, ParseException e) {
                for (Transaction t : transactions) {
                    totalOwned+=t.getUnits();
                }

                //hide the sell button if the player does not own the selected asset
                if (totalOwned == 0) {
                    btnSellAsset.setVisibility(View.GONE);
                }
            }
        });
    }

}
