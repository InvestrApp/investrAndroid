package com.investrapp.investr.apis.handlers;

import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Player;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public abstract class ParseGetAllPlayersHandler implements FindCallback<CompetitionPlayer> {

    @Override
    public void done(List<CompetitionPlayer> objects, ParseException e) {
        List<Player> players = new ArrayList<>();
        for (CompetitionPlayer competitionPlayer : objects) {
            players.add(competitionPlayer.getPlayer());
        }
        done(players);
    }

    abstract public void done(List<Player> players);

}
