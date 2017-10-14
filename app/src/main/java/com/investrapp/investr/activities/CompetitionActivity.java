package com.investrapp.investr.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.investrapp.investr.R;
import com.investrapp.investr.apis.FacebookAPI;
import com.investrapp.investr.apis.ParseAPI;
import com.investrapp.investr.fragments.RankingsFragment;
import com.investrapp.investr.models.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class CompetitionActivity extends AppCompatActivity {

    private Player mCurrentPlayer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private ImageView ivHeaderPhoto;
    private TextView tvHeaderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        navigationView = (NavigationView) findViewById(R.id.nvView);

        setupToolbar();
        setupDrawerLayout();
        setupDrawerContent();
        setupNavigationViewHeader();
        getCurrentUser();
        setupInitialFragment();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupDrawerLayout() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void setupNavigationViewHeader() {
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        ivHeaderPhoto = headerLayout.findViewById(R.id.iv_player_profile);
        tvHeaderName = headerLayout.findViewById(R.id.tv_player_name);
    }

    private void getCurrentUser() {
        FacebookAPI.getCurrentUser(new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    mCurrentPlayer = Player.getPlayer(object);
                    ParseAPI.savePlayer(mCurrentPlayer);

                    tvHeaderName.setText(mCurrentPlayer.getName());
                    Glide.with(getApplicationContext())
                            .load(mCurrentPlayer.getProfileImageUrl())
                            .into(ivHeaderPhoto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_rankings_fragment:
                fragmentClass = RankingsFragment.class;
                break;
            case R.id.nav_portfolio_fragment:
                fragmentClass = RankingsFragment.class;
                break;
            case R.id.nav_marketplace_fragment:
                fragmentClass = RankingsFragment.class;
                break;
            default:
                fragmentClass = RankingsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void setupInitialFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new RankingsFragment()).commit();
        setTitle(R.string.rankings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
