package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.investrapp.investr.fragments.AllCompetitionsFragment;
import com.investrapp.investr.fragments.MyCompetitionsFragment;

public class HomeFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {

    private final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"My Competitions", "Find Competitions"};
    private Context context;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return MyCompetitionsFragment.newInstance();
        } else {
            return AllCompetitionsFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
