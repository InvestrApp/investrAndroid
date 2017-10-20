package com.investrapp.investr.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public class PricesPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Intraday", "Daily", "Weekly", "Monthly"};
    private Context context;

    public PricesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return the total number of fragments
    @Override
    public int getCount() {
        return 4;
    }


    //return the fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new IntradayPriceFragment();
        } else if (position == 1) {
            return new DailyPriceFragment();
        } else if (position == 2) {
            return new WeeklyPriceFragment();
        } else if (position == 3) {
            return new MonthlyPriceFragment();
        } else {
            return null;
        }
    }


    //return the title
    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }
}
