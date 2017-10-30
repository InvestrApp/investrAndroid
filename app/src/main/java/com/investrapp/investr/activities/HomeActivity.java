package com.investrapp.investr.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.HomeFragmentPagerAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.fragments.AllCompetitionsFragment;
import com.investrapp.investr.fragments.CreateCompetitionDialogFragment;

import com.investrapp.investr.fragments.MyCompetitionsFragment;
import com.investrapp.investr.fragments.SelectCompetitorsDialogFragment;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class HomeActivity extends AppCompatActivity implements CreateCompetitionDialogFragment.FinishCreateCompetitionDetailsListener {


    //constants used for GPS services
    private int REQUEST_FINE_LOCATION = 1;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Toolbar toolbar;
    ViewPager viewPager;
    HomeFragmentPagerAdapter homeFragmentPagerAdapter;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupTabs();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Competitions");
    }

    private void setupTabs() {
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this);
        viewPager.setAdapter(homeFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miAddCompetition:
                FragmentManager fm = getSupportFragmentManager();
                CreateCompetitionDialogFragment createCompetitionDialogFragment = CreateCompetitionDialogFragment.newInstance();
                createCompetitionDialogFragment.show(fm, "fragment_create_competition");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onFinishCompetitionDetails(Competition competition) {

        MyCompetitionsFragment MyCompetitionsFragment = (MyCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(0);
        MyCompetitionsFragment.addCompetition(competition);
        viewPager.setCurrentItem(1);
        AllCompetitionsFragment allCompetitionsFragment = (AllCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(1);
        Player currentPlayer = allCompetitionsFragment.getCurrentPlayer();
        allCompetitionsFragment.addCompetition(competition);

        Calendar calendar = new GregorianCalendar();
        Date currentDate = calendar.getTime();
        Transaction transaction = new Transaction(currentPlayer, competition, Cash.ASSET_TYPE, Cash.TICKER,
                currentDate, Transaction.TransactionAction.BUY, competition.getInitialAmount(), 1);
        ParseClient.addTransaction(transaction);
        CompetitionPlayer competitionPlayer = new CompetitionPlayer(competition, currentPlayer);
        competitionPlayer.saveInBackground();


        //Launch the fragment for adding other players to the competition
        FragmentManager fm = getSupportFragmentManager();
        SelectCompetitorsDialogFragment selectCompetitorsDialogFragment = SelectCompetitorsDialogFragment.newInstance(competition, currentPlayer);
        selectCompetitorsDialogFragment.show(fm, "fragment_select_competitors");
    }


    /**
     * This method checks whether the user should be prompted to turn on GPS services or authorize the app to use
     * the current location.
     *
     * @return
     */
    public boolean checkPermissions() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }


    public void startLocationUpdates(final Player player) {
        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        onLocationResponse(player, location);
                    }
                },
                Looper.myLooper());
    }

    public void onLocationResponse(Player player, Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        player.setLatitude(latitude);
        player.setLongitude(longitude);
        ParseClient.savePlayer(player);
    }


    /**
     * This method is from the following Stack Overflow post. An alert dialog gives
     * users the option to open the phone's settings and enable GPS services.
     * https://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
     */
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
