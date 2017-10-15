package com.investrapp.investr.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static java.lang.System.in;

/**
 * Created by michaelsignorotti on 10/14/17.
 */

@ParseClassName("Cryptocurrency")
public class Cryptocurrency extends ParseObject {

    private String ticker;
    private String name;

    public Cryptocurrency() {
        super();
    }

    public Cryptocurrency(String ticker, String name) {
        super();
        setTicker(ticker);
        setName(name);
        this.ticker = ticker;
        this.name = name;
    }


    public String getTicker() {
        return getString("ticker");
    }

    public void setTicker(String ticker) {
        put("ticker", ticker);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }


    public static List<Cryptocurrency> getCrytocurrencyList(Response response) {

        ArrayList<Cryptocurrency> cryptocurrencyList = new ArrayList<Cryptocurrency>();

        InputStream in = response.body().byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                String ticker = row[0].toUpperCase();
                
                if (!ticker.equals("CURRENCY CODE")) {
                    String name = row[1].toUpperCase();
                    Log.d("Cryptocurrency", ticker + " " + name);
                    cryptocurrencyList.add(new Cryptocurrency(ticker, name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cryptocurrencyList;
    }
}
