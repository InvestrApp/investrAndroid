package com.investrapp.investr.apis;

import android.util.Log;

import com.investrapp.investr.models.Player;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ParseAPI {

    public static void updatePlayer(Player player) {
        player.saveInBackground();
    }

    public static void savePlayer(final Player player) {
        getPlayer(player.getId(),
            new FindCallback<Player>() {
                public void done(List<Player> itemList, ParseException e) {
                    if (e == null) {
                        if (itemList.size() == 0) {
                            player.saveInBackground();
                        }
                    } else {
                        Log.d("item", "Error: " + e.getMessage());
                    }
                }
            }
        );
    }

    public static void getPlayer(String id, FindCallback<Player> handler) {
        ParseQuery<Player> query = ParseQuery.getQuery(Player.class);
        query.whereEqualTo("id", id);
        query.findInBackground(handler);
    }

}
