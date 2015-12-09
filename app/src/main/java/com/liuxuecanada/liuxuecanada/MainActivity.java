package com.liuxuecanada.liuxuecanada;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends FragmentActivity {

    private static int screenWidth = 0;
    private static int newsImageWidth = 0;
    private static int newsImageHeight = 0;
    private static int testImageWidth = 0;
    private static int testImageHeight = 0;
    boolean leftToRight = true;
    Timer timer;
    int page = 0;
    int hit = 0;
    private FrontPageAdapter mAdapter;
    private ViewPager mPager;
    private ViewPager newsPager = null;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getNewsImageWidth() {
        return newsImageWidth;
    }

    public static int getNewsImageHeight() {
        return newsImageHeight;
    }

    public static int getTestImageWidth() {
        return testImageWidth;
    }

    public static int getTestImageHeight() {
        return testImageHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        //Set action bar color
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));

        //Get Screen Width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;

        //Get News Image Dimensions
        newsImageWidth = getScreenWidth() * 7 / 24;
        newsImageHeight = (int) (getNewsImageWidth() * 0.618);

        //Get Test Image Dimensions
        testImageWidth = getScreenWidth() / 2;
        testImageHeight = (int) (getTestImageWidth() * 0.618);

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
