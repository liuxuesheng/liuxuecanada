package com.liuxuecanada.liuxuecanada;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    FrontPageAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        //Set action bar color
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));

        //Create pager
        mAdapter = new FrontPageAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(1);

        // Watch for text click
        TextView textButton = (TextView) findViewById(R.id.goto_first);
        TextView textButton2 = (TextView) findViewById(R.id.goto_middle);
        TextView textButton3 = (TextView) findViewById(R.id.goto_last);
        textButton.setPadding(100, 50, 100, 50);
        textButton2.setPadding(100, 50, 100, 50);
        textButton3.setPadding(100, 50, 100, 50);
        textButton.setTextSize(16);
        textButton2.setTextSize(16);
        textButton3.setTextSize(16);

        textButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });

        textButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });

        textButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });

    }
}
