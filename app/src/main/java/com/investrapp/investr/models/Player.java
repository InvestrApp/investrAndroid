package com.investrapp.investr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Player")
public class Player extends ParseObject {

    private String mId;
    private String mName;
    private String mEmail;
    private String mGender;
    private String mProfileImageUrl;

    public Player() {
        super();
    }

    public Player(String id, String name, String email, String gender) {
        super();
        setId(id);
        setName(name);
        setEmail(email);
        setGender(gender);
        setProfileImageUrl("https://graph.facebook.com/" + id + "/picture?type=large");
    }

    public void setId(String id) {
        mId = id;
        put("id", id);
    }

    public void setName(String name) {
        mName = name;
        put("name", name);
    }

    public void setEmail(String email) {
        mEmail = email;
        put("email", email);
    }

    public void setGender(String gender) {
        mGender = gender;
        put("gender", gender);
    }

    public void setProfileImageUrl(String profileImageUrl) {
        mProfileImageUrl = profileImageUrl;
        put("profileImageUrl", profileImageUrl);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getGender() {
        return mGender;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public static Player getPlayer(JSONObject object) throws JSONException {
        String id = object.getString("id");
        String name = object.getString("name");
        String email = object.getString("email");
        String gender = object.getString("gender");
        return new Player(id, name, email, gender);
    }

}
