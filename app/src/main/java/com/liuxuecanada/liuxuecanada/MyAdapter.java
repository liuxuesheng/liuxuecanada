package com.liuxuecanada.liuxuecanada;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class MyAdapter extends FragmentPagerAdapter {
    static final int NUM_ITEMS = 3;

    public MyAdapter(FragmentManager fm) {
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
                return NewsFragment.newInstance("最新消息");
            case 1:
                return ArrayListFragment.newInstance("2");
            case 2:
                return ArrayListFragment.newInstance("2");
            default:
                return NewsFragment.newInstance("最新消息");
        }
    }
}
