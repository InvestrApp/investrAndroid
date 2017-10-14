package com.investrapp.investr.apis;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

public class FacebookAPI {

    public static void getCurrentUser(GraphRequest.GraphJSONObjectCallback handler) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                handler);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
