package com.investrapp.investr.models;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by michaelsignorotti on 10/15/17.
 */

public class Price implements Comparable<Price> {

    private Date mDate;
    private boolean mLongDate;
    private double mPrice;
    private NumberFormat formatter;
    private DateFormat df;
    private SimpleDateFormat sdfWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public Price(String date, double price) {
        sdfWithTime.setTimeZone(TimeZone.getTimeZone("PST"));
        sdfDate.setTimeZone(TimeZone.getTimeZone("PST"));
        try {
            if (date.length() > 11) {
                mDate = sdfWithTime.parse(date);
                mLongDate = true;
            } else {
                mDate = sdfDate.parse(date);
                mLongDate = false;
            }
        } catch (Exception e) {
            Log.e("ERROR", "Date incorrectly formatted: " + date);
            e.printStackTrace();
        }
        mPrice = price;
        formatter = NumberFormat.getCurrencyInstance();
        df = new android.text.format.DateFormat();
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public Date getDate() {
        return mDate;
    }

    public Long getDateInTime() {
        return mDate.getTime();
    }

    public String getDateFormatted() {
        if (mLongDate) {
            return df.format("MM/dd/yyyy hh:mm a", mDate).toString();
        }
        return df.format("MM/dd/yyyy", mDate).toString();
    }

    public double getPrice() {
        return mPrice;
    }

    public String getPriceFormatted() {
        return formatter.format(mPrice);
    }

    @Override
    public int compareTo(@NonNull Price o) {
        return (this.getDate().compareTo(o.getDate()));
    }

}
