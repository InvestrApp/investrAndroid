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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
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
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class HomeActivity extends AppCompatActivity implements CreateCompetitionDialogFragment.FinishCreateCompetitionDetailsListener, AllCompetitionsFragment.OnAddCompetitionListener, SelectCompetitorsDialogFragment.OnFinishSelectingCompetitors {


    //constants used for GPS services
    private int REQUEST_FINE_LOCATION = 1;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Toolbar toolbar;
    private ViewPager viewPager;
    private HomeFragmentPagerAdapter homeFragmentPagerAdapter;
    private LocationManager locationManager;


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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
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

    @Override
    public void onAddCompetition(Competition competition, CompetitionPlayer competitionPlayer) {
        String competitionName = competition.getName();
        String sbMessage = "You joined " + competitionName;
        Snackbar.make(findViewById(R.id.activity_home), sbMessage, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), new UndoAddCompetitionListener(competitionPlayer))
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(3500)
                .show();
        MyCompetitionsFragment fragment = (MyCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(0);
        fragment.addCompetition(competition);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onFinishSelectingCompetitors(Competition competition, List<Player> players) {

        String competitionName = competition.getName();
        String sbMessage = "Competition Created:   " + competitionName;
        Snackbar.make(findViewById(R.id.activity_home), sbMessage, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), new UndoCreateCompetitionListener(competition, players))
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(3500)
                .show();

    }

    public class UndoAddCompetitionListener implements View.OnClickListener {
        CompetitionPlayer competitionPlayer;

        public UndoAddCompetitionListener(CompetitionPlayer competitionPlayer) {
            this.competitionPlayer = competitionPlayer;
        }

        @Override
        public void onClick(View v) {
            MyCompetitionsFragment fragment = (MyCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(0);
            fragment.removeAddedCompetition();
            ParseClient.removePlayerFromCompetition(competitionPlayer);
            ParseClient.removeTransactionsForPlayerInCompetition(competitionPlayer.getCompetition(), competitionPlayer.getPlayer(), new FindCallback<Transaction>() {
                @Override
                public void done(List<Transaction> objects, ParseException e) {
                    for (Transaction transaction : objects) {
                        ParseClient.removeTransaction(transaction);
                    }
                }
            });
        }
    }

    public class UndoCreateCompetitionListener implements View.OnClickListener {
        Competition competition;
        List<Player> players;

        public UndoCreateCompetitionListener(Competition competition, List<Player> players) {
            this.competition = competition;
            this.players = players;
        }

        @Override
        public void onClick(View v) {
            MyCompetitionsFragment myCompetitionsFragment = (MyCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(0);
            myCompetitionsFragment.removeAddedCompetition();
            AllCompetitionsFragment allCompetitionsFragment = (AllCompetitionsFragment) homeFragmentPagerAdapter.getRegisteredFragment(1);
            allCompetitionsFragment.removeAddedCompetition();

            //delete data from the database
            ParseClient.removeAllCompetitionInfo(competition, players, new FindCallback<CompetitionPlayer>() {
                        @Override
                        public void done(List<CompetitionPlayer> objects, ParseException e) {
                            for (CompetitionPlayer competitionPlayer : objects) {
                                competitionPlayer.deleteInBackground();
                            }

                        }
                    }, new FindCallback<Transaction>() {
                        @Override
                        public void done(List<Transaction> objects, ParseException e) {
                            for (Transaction transaction : objects) {
                                transaction.deleteInBackground();
                            }
                        }
                    });
            ParseClient.removeCompetition(competition);
        }
    }
}
