package com.investrapp.investr.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.investrapp.investr.R;
import com.investrapp.investr.adapters.HomeFragmentPagerAdapter;
import com.investrapp.investr.fragments.AllCompetitionsFragment;
import com.investrapp.investr.fragments.CreateCompetitionDialogFragment;
import com.investrapp.investr.fragments.DatePickerFragment;
import com.investrapp.investr.fragments.HomeCompetitionsFragment;
import com.investrapp.investr.fragments.MyCompetitionsFragment;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Player;


public class HomeActivity extends AppCompatActivity implements CreateCompetitionDialogFragment.FinishCreateCompetitionDetailsListener{


    private Toolbar toolbar;
    ViewPager viewPager;
    HomeFragmentPagerAdapter homeFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupTabs();
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
                CreateCompetitionDialogFragment  createCompetitionDialogFragment = CreateCompetitionDialogFragment.newInstance();
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
        Player player = allCompetitionsFragment.getmCurrentPlayer();
        CompetitionPlayer competitionPlayer = new CompetitionPlayer(competition, player);
        competitionPlayer.saveInBackground();
        allCompetitionsFragment.addCompetition(competition);
    }
}
