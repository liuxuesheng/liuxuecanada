package com.liuxuecanada.liuxuecanada;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class FrontPageAdapter extends FragmentPagerAdapter {
    static final int NUM_ITEMS = 4;

    public FrontPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("asd8sd7sd ", "position main: " + position);
        switch (position) {
            case 0:
                return FrontPageNewsFragment.newInstance("最新消息");
            case 1:
                return FrontPageProfileFragment.newInstance("Hello");
            case 2:
                return FrontPagePlanningFragment.newInstance("Hello");
            case 3:
                return FrontPageConnectionFragment.newInstance("Hello");
            default:
                return null;
        }
    }
}
