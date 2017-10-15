package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Competition_Player")
public class CompetitionPlayer extends ParseObject {

    public CompetitionPlayer() {
        super();
    }

    public CompetitionPlayer(Competition competition, Player player) {
        setCompetition(competition);
        setPlayer(player);
    }

    public void setCompetition(Competition competition) {
        put("competition", competition);
    }

    public void setPlayer(Player player) {
        put("player", player);
    }

}