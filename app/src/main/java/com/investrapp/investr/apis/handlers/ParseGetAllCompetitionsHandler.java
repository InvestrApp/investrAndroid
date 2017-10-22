package com.investrapp.investr.apis.handlers;

import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public abstract class ParseGetAllCompetitionsHandler implements FindCallback<CompetitionPlayer> {

    @Override
    public void done(List<CompetitionPlayer> objects, ParseException e) {
        List<Competition> competitions = new ArrayList<>();
        for (CompetitionPlayer competitionPlayer : objects) {
            competitions.add(competitionPlayer.getCompetition());
        }
        done(competitions);
    }

    abstract public void done(List<Competition> competitions);

}
