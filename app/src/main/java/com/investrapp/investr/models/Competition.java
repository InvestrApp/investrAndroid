package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

@ParseClassName("Competition")
public class Competition extends ParseObject {

    public Competition() {
        super();
    }

    public Competition(String name, Date startDate, Date endDate, Double initialAmount) {
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setInitialAmount(initialAmount);
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

    public void setInitialAmount(Double initialAmount) {
        put("initial_amount", initialAmount);
    }

    public String getName() {
        return getString("name");
    }

    public Date getStartDate() {
        return getDate("start_date");
    }

    public Date getEndDate() {
        return getDate("end_date");
    }

    public Double getInitialAmount() {
        return getDouble("initial_amount");
    }

    public static boolean isPlayerInCompetition(Competition competition, List<Competition> playerCompetitions) {
        for (Competition playerCompetition : playerCompetitions) {
            if (playerCompetition.getObjectId().equals(competition.getObjectId())) {
                return true;
            }
        }
        return false;
    }

}
