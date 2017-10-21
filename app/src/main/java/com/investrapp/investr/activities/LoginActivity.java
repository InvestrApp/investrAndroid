package com.investrapp.investr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.investrapp.investr.R;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton lbFbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lbFbLogin = (LoginButton) findViewById(R.id.lbFbLogin);
        lbFbLogin.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();

        lbFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.v("facebook - profile", currentProfile.getFirstName());
                            mProfileTracker.stopTracking();
                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                }
                onLoginSuccess();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(
                        LoginActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(final FacebookException exception) {
                // App code
                Toast.makeText(
                        LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }

        });

        if (isLoggedIn()) {
            onLoginSuccess();
        }
    }

    public void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        if (Profile.getCurrentProfile() == null) {
            Profile.fetchProfileForCurrentAccessToken();
        }
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

}
