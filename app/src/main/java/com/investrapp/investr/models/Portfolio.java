package com.investrapp.investr.models;

import android.os.AsyncTask;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.apis.handlers.AlphaVantageStockCurrentPriceCallHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Portfolio {

    private Player mPlayer;
    private Competition mCompetition;
    private List<Transaction> mTransactions;
    private PortfolioListener mPortfolioListener;
    private Double value = 0.0;
    private Double cash = 0.0;
    private NumberFormat formatter;

    public Portfolio(Player player, Competition competition) {
        formatter = NumberFormat.getCurrencyInstance();
        mPlayer = player;
        mCompetition = competition;
        mTransactions = new ArrayList<>();
    }

    public void addTransactions(List<Transaction> transactions) {
        mTransactions.addAll(transactions);
        calculateAvailableCash();
        calculateTotalPortfolioValue();
    }

    public void setPortfolioListener(PortfolioListener portfolioListener) {
        mPortfolioListener = portfolioListener;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public Double getValue() {
        return value;
    }

    public String getValueFormatted() {
        return formatter.format(value);
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Competition getCompetition() {
        return mCompetition;
    }

    private void calculateAvailableCash() {
        for (Transaction transaction : mTransactions) {
            if (transaction.getAssetType().equals(Cash.ASSET_TYPE)) {
                cash = transaction.getPrice();
            }
        }
    }

    public Double getCash() {
        return cash;
    }

    public String getCashFormatted() {
        return formatter.format(cash);
    }

    public void calculateTotalPortfolioValue() {
        final HashMap<String, Integer> cryptocurrencyMap = new HashMap<>();
        HashMap<String, Integer> stockMap = new HashMap<>();
        for (Transaction transaction : mTransactions) {
            if (transaction.getAssetType().equals(Cash.ASSET_TYPE)) {
                value += transaction.getPrice() * transaction.getUnits();
            } else if (transaction.getAssetType().equals(Stock.ASSET_TYPE)) {
                value += handleTransctions(transaction, stockMap);
            } else if (transaction.getAssetType().equals(Cryptocurrency.ASSET_TYPE)) {
                value += handleTransctions(transaction, cryptocurrencyMap);
            }
        }

        for (final String ticker : cryptocurrencyMap.keySet()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    AlphaVantageClient.getCurrentDigitalCurrencyPrice(ticker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
                        @Override
                        public void onPriceResponse(Double price) {
                            Double assetValue = price * cryptocurrencyMap.get(ticker);
                            System.out.println(ticker + " " + assetValue + " " + value);
                            value += assetValue;
                            mPortfolioListener.onValueUpdate();
                        }
                    });
                }
            });
        }

        for (final String ticker : stockMap.keySet()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    AlphaVantageClient.getCurrentStockPrice(ticker, new AlphaVantageStockCurrentPriceCallHandler() {
                        @Override
                        public void onPriceResponse(Double price) {
                            Double assetValue = price * cryptocurrencyMap.get(ticker);
                            value += assetValue;
                            mPortfolioListener.onValueUpdate();
                        }
                    });
                }
            });
        }

    }

    private double handleTransctions(Transaction transaction, HashMap<String, Integer> map) {
        if (transaction.getAction().equals(Transaction.TransactionAction.BUY.toString())) {
            if (map.containsKey(transaction.getAssetTicker())) {
                map.put(transaction.getAssetTicker(), transaction.getUnits() + map.get(transaction.getAssetTicker()));
            } else {
                map.put(transaction.getAssetTicker(), transaction.getUnits());
            }
            return 0;
        } else {
            return transaction.getPrice() * transaction.getUnits();
        }
    }

}
