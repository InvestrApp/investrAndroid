package com.investrapp.investr.models;

import android.os.AsyncTask;

import com.investrapp.investr.apis.AlphaVantageClient;
import com.investrapp.investr.apis.handlers.AlphaVantageDigitalCurrencyCurrentPriceCallHandler;
import com.investrapp.investr.apis.handlers.AlphaVantageStockCurrentPriceCallHandler;
import com.investrapp.investr.interfaces.PortfolioValueListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Portfolio {

    private Player mPlayer;
    private Competition mCompetition;
    private List<Transaction> mTransactions;
    private List<Allocation> mAllocations;
    private PortfolioValueListener mPortfolioListener;
    private Double value = 0.0;
    private Double cash = 0.0;
    private NumberFormat formatter;

    // empty constructor needed by the Parceler library
    public Portfolio() {

    }

    public Portfolio(Player player, Competition competition) {
        formatter = NumberFormat.getCurrencyInstance();
        mPlayer = player;
        mCompetition = competition;
        mTransactions = new ArrayList<>();
        mAllocations = new ArrayList<>();
    }

    public void addTransactions(List<Transaction> transactions) {
        mTransactions.addAll(transactions);
    }

    public void addAllocations(List<Allocation> allocations) {
        mAllocations.addAll(allocations);
    }

    public void setPortfolioListener(PortfolioValueListener portfolioListener) {
        mPortfolioListener = portfolioListener;
    }

    public void setCash(Double cash) {
        this.cash = cash;
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

    public void calculateAvailableCash() {
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

    public List<Allocation> getAllocations() {
        return mAllocations;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.mAllocations = allocations;
    }

    public void calculateTotalPortfolioValue() {
        value = 0.0;
        final HashMap<String, Integer> cryptocurrencyMap = new HashMap<>();
        final HashMap<String, Integer> stockMap = new HashMap<>();
        for (Transaction transaction : mTransactions) {
            if (transaction.getAssetType().equals(Cash.ASSET_TYPE)) {
                value += transaction.getPrice() * transaction.getUnits();
            } else if (transaction.getAssetType().equals(Stock.ASSET_TYPE)) {
                value += handleTransctions(transaction, stockMap);
            } else if (transaction.getAssetType().equals(Cryptocurrency.ASSET_TYPE)) {
                value += handleTransctions(transaction, cryptocurrencyMap);
            }
        }
        addCurrentAssetValue(cryptocurrencyMap, Cryptocurrency.ASSET_TYPE);
        addCurrentAssetValue(stockMap, Stock.ASSET_TYPE);
    }

    private double handleTransctions(Transaction transaction, HashMap<String, Integer> map) {
        if (map.containsKey(transaction.getAssetTicker())) {
            map.put(transaction.getAssetTicker(), transaction.getUnits() + map.get(transaction.getAssetTicker()));
        } else {
            map.put(transaction.getAssetTicker(), transaction.getUnits());
        }

        if (transaction.getAction().equals(Transaction.TransactionAction.BUY.toString())) {
            return 0;
        } else {
            return transaction.getPrice() * Math.abs(transaction.getUnits());
        }
    }

    private void addCurrentAssetValue(final HashMap<String, Integer> map, final String assetType) {
        for (final String ticker : map.keySet()) {
            if (map.get(ticker) <= 0) {
                return;
            }
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (assetType.equals(Cryptocurrency.ASSET_TYPE)) {
                        AlphaVantageClient.getCurrentDigitalCurrencyPrice(ticker, new AlphaVantageDigitalCurrencyCurrentPriceCallHandler() {
                            @Override
                            public void onPriceResponse(Double price) {
                                Double assetValue = price * map.get(ticker);
                                System.out.println(ticker + " " + assetValue + " " + value);
                                value += assetValue;
                                mPortfolioListener.onValueUpdate();
                            }
                        });
                    } else if (assetType.equals(Stock.ASSET_TYPE)) {
                        AlphaVantageClient.getCurrentStockPrice(ticker, new AlphaVantageStockCurrentPriceCallHandler() {
                            @Override
                            public void onPriceResponse(Double price) {
                                Double assetValue = price * map.get(ticker);
                                System.out.println(ticker + " " + assetValue + " " + value);
                                value += assetValue;
                                mPortfolioListener.onValueUpdate();
                            }
                        });
                    }
                }
            });
        }
    }

}
