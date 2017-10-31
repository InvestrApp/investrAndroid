package com.investrapp.investr.models;

/**
 * Created by michaelsignorotti on 10/29/17.
 */

public class Allocation {

    private String ticker;
    private int units;

    public Allocation(String ticker, int units) {
        this.ticker = ticker;
        this.units = units;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

}
