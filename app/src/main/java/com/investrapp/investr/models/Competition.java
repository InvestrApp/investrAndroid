package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Competition")
public class Competition extends ParseObject {

    public Competition() {
        super();
    }

    public Competition(String name, Date startDate, Date endDate) {
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setStartDate(Date startDate) {
        put("start_date", startDate);
    }

    public void setEndDate(Date endDate) {
        put("end_date", endDate);
    }

    public String getName() {
        return getString("name");
    }

    public Date getmStartDate() {
        return getDate("start_date");
    }

    public Date getmEndDate() {
        return getDate("end_date");
    }

}
