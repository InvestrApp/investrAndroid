package com.investrapp.investr.models;

import java.util.Date;

/**
 * Created by michaelsignorotti on 10/15/17.
 */

public class Price {

    private String date;
    private double price;

    public Price(String date, double price) {
        this.date = date;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
