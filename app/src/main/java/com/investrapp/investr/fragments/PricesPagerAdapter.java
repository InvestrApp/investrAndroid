package com.investrapp.investr.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public class PricesPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Home", "Mentions"};
    private Context context;

    public PricesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return the total number of fragments
    @Override
    public int getCount() {
        return 2;
    }


    //return the fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AssetPriceFragment();
        } else if (position == 1) {
            return new AssetPriceFragment();
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
