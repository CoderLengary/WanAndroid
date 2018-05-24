package com.example.lengary_l.wanandroid.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OnboardingPagerAdapter  extends FragmentPagerAdapter {

    private final int pageCount = 3;

    public OnboardingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OnboardingFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
