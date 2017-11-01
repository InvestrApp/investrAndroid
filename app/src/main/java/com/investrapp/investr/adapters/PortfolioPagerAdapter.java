package com.investrapp.investr.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.investrapp.investr.fragments.PortfolioAllocationsFragment;

import com.investrapp.investr.fragments.PortfolioTransactionsFragment;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;

public class PortfolioPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Portfolio", "Transactions"};
    private Competition competition;
    private Player player;
    private static int NUM_ITEMS = 2;

    public PortfolioPagerAdapter(FragmentManager fragmentManager, Competition competition, Player player) {
        super(fragmentManager);
        this.competition = competition;
        this.player = player;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return PortfolioAllocationsFragment.newInstance(competition, player);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return PortfolioTransactionsFragment.newInstance(competition, player);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}