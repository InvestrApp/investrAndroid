package com.investrapp.investr.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.investrapp.investr.fragments.assetPrice.AssetPriceFragment;
import com.investrapp.investr.fragments.assetPrice.DailyPriceFragment;
import com.investrapp.investr.fragments.assetPrice.IntradayPriceFragment;
import com.investrapp.investr.fragments.assetPrice.MonthlyPriceFragment;
import com.investrapp.investr.fragments.assetPrice.WeeklyPriceFragment;

/**
 * Created by michaelsignorotti on 10/7/17.
 */

public class PricesPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Intraday", "Daily", "Weekly", "Monthly"};
    private Context context;
    private String ticker;

    public PricesPagerAdapter(FragmentManager fm, Context context, String ticker) {
        super(fm);
        this.context = context;
        this.ticker = ticker;
    }

    //return the total number of fragments
    @Override
    public int getCount() {
        return 4;
    }


    //return the fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        AssetPriceFragment fragment = null;
        if (position == 0) {
            fragment = IntradayPriceFragment.newInstance(ticker);
        } else if (position == 1) {
            fragment = DailyPriceFragment.newInstance(ticker);
        } else if (position == 2) {
            fragment =  WeeklyPriceFragment.newInstance(ticker);
        } else if (position == 3) {
            fragment = MonthlyPriceFragment.newInstance(ticker);
        }

        return fragment;
    }


    //return the title
    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }
}
