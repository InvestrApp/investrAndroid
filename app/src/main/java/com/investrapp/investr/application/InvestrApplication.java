package com.investrapp.investr.application;

import android.app.Application;
import android.content.Context;

import com.investrapp.investr.R;
import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Stock;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class InvestrApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        InvestrApplication.context = this;

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(Competition.class);
        ParseObject.registerSubclass(CompetitionPlayer.class);

        ParseObject.registerSubclass(Cryptocurrency.class);
        ParseObject.registerSubclass(Stock.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.parse_app_id))
                .clientKey(getResources().getString(R.string.PARSE_MASTER_KEY))
                .clientBuilder(builder)
                .server(getResources().getString(R.string.parse_server_url))
                .build());

        new AlphaVantageClient(this);
    }

}
