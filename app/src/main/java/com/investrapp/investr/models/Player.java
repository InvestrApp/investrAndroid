package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Player")
public class Player extends ParseObject {

    public Player() {
        super();
    }

    public Player(String id, String name, String email, String gender, String installation) {
        super();
        setId(id);
        setName(name);
        setEmail(email);
        setGender(gender);
        setProfileImageUrl("https://graph.facebook.com/" + id + "/picture?type=large");
        setInstallation(installation);
    }

    public void setId(String id) {
        put("id", id);
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

    public void setProfileImageUrl(String profileImageUrl) {
        put("profileImageUrl", profileImageUrl);
    }

    public void setInstallation(String installation) {
        put("installation", installation);
    }

    public void setLatitude(double latitide) {
        put("latitude", latitide);
    }

    public void setLongitude(double longitude) {
        put("longitude", longitude);
    }

    public String getId() {
        return getString("id");
    }

    public String getName() {
        return getString("name");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getGender() {
        return getString("gender");
    }

    public String getProfileImageUrl() {
        return getString("profileImageUrl");
    }

    public String getInstallation() {
        return getString("installation");
    }

    public double getLatitude() {
        return getDouble("latitude");
    }

    public double getLongitude() {
        return getDouble("longitude");
    }

    public static Player getPlayerFromFB(JSONObject object) {
        String id = null;
        String name = null;
        String email = null;
        String gender = null;
        String installation = ParseInstallation.getCurrentInstallation().getInstallationId();
        try {
            id = object.getString("id");
            name = object.getString("name");
            email = object.getString("email");
            gender = object.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Player(id, name, email, gender, installation);
    }

}
