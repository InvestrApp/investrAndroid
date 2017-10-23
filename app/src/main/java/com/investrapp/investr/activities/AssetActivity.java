package com.investrapp.investr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.fragments.AssetTransactionDialogFragment;
import com.investrapp.investr.fragments.PricesPagerAdapter;
import com.investrapp.investr.fragments.assetPrice.AssetPriceFragment;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.NumberFormat;
import java.util.List;


public class AssetActivity extends AppCompatActivity implements AssetPriceFragment.OnPriceTimeSeriesResponseListener {

    private Toolbar toolbar;
    private TextView tvAssetName;
    private TextView tvAssetTicker;
    private Button btnSellAsset;
    private Button btnBuyAsset;
    private ViewPager vpPager;
    private TabLayout tabLayout;
    private PricesPagerAdapter pricesPagerAdapter;
    private Competition mCompetition;
    private Player mPlayer;
    private String mTicker;
    private String mCryptocurrencyName;
    private int totalOwned;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        getDataFromIntent();
        setupView();
        setupToolbar();

        getAssetPrice(mTicker);
      
        pricesPagerAdapter = new PricesPagerAdapter(getSupportFragmentManager(), this, mTicker);
        vpPager.setAdapter(pricesPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        btnBuyAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AssetTransactionDialogFragment assetTransactionDialogFragment =
                        AssetTransactionDialogFragment.newInstance(mCompetition, mPlayer, mCryptocurrencyName, mTicker, totalOwned, price, "BUY");
                assetTransactionDialogFragment.show(fm, "fragment_asset_transaction");
            }
        });

        btnSellAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AssetTransactionDialogFragment assetTransactionDialogFragment =
                        AssetTransactionDialogFragment.newInstance(mCompetition, mPlayer, mCryptocurrencyName, mTicker, totalOwned, price, "SELL");
                assetTransactionDialogFragment.show(fm, "fragment_asset_transaction");
            }
        });

        getCryptocurrencyTransactionsByTickerUserCompetition();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mTicker = intent.getStringExtra("ticker");
        mCompetition = (Competition) getIntent().getParcelableExtra("competition");
        mPlayer = (Player) getIntent().getParcelableExtra("player");
    }

    private void setupView() {
        tvAssetName = findViewById(R.id.tvAssetName);
        tvAssetTicker = findViewById(R.id.tvAssetTicker);
        btnSellAsset = findViewById(R.id.btnSellAsset);
        btnBuyAsset = findViewById(R.id.btnBuyAsset);
        vpPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Buy and Sell Assets");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onPriceTimeSeriesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
        mCryptocurrencyName = cryptocurrencyPriceTimeSeries.getCryptocurrencyName();
        tvAssetName.setText(mCryptocurrencyName);
        tvAssetTicker.setText(cryptocurrencyPriceTimeSeries.getTicker());
    }

    public void getAssetPrice(String assetTicker) {
        AlphaVantageClient.getCurrentDigitalCurrencyPrice(assetTicker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
            @Override
            public void onPriceResponse(Double priceResponse) {
                setAssetPrice(priceResponse);
                String priceFormatted = NumberFormat.getCurrencyInstance().format(priceResponse);
                btnBuyAsset.setText("BUY @ " + priceFormatted);
                btnSellAsset.setText("SELL @ " + priceFormatted);
            }
        });
    }

    public void setAssetPrice(final Double priceResponse) {
        runOnUiThread(new Runnable() {
            public void run() {
                price  = priceResponse;
            }
        });
    }

    public void getCryptocurrencyTransactionsByTickerUserCompetition() {
        ParseClient.getCryptocurrencyTransactionsByTickerUserCompetition(mTicker, mCompetition, mPlayer, new FindCallback<Transaction>() {
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
