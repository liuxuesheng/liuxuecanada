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
        final TextView textButton = (TextView) findViewById(R.id.goto_news);
        final TextView textButton1 = (TextView) findViewById(R.id.goto_profile);
        final TextView textButton2 = (TextView) findViewById(R.id.goto_suggestions);
        final TextView textButton3 = (TextView) findViewById(R.id.goto_messages);
        textButton.setPadding(60, 50, 60, 50);
        textButton1.setPadding(60, 50, 60, 50);
        textButton2.setPadding(60, 50, 60, 50);
        textButton3.setPadding(60, 50, 60, 50);
        textButton.setTextSize(14);
        textButton1.setTextSize(14);
        textButton2.setTextSize(14);
        textButton3.setTextSize(14);

        textButton.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textButton1.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textButton2.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textButton3.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textButton.setTextColor(Color.BLACK);
        textButton1.setTextColor(Color.BLACK);
        textButton2.setTextColor(Color.BLACK);
        textButton3.setTextColor(Color.BLACK);


        textButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(0);
                textButton.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                textButton.setTextColor(Color.WHITE);
                ResetTextView(textButton1, textButton2, textButton3);
            }
        });

        textButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(1);
                textButton1.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                textButton1.setTextColor(Color.WHITE);
                ResetTextView(textButton, textButton2, textButton3);
            }
        });

        textButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(2);
                textButton2.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                textButton2.setTextColor(Color.WHITE);
                ResetTextView(textButton, textButton1, textButton3);
            }
        });

        textButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(3);
                textButton3.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                textButton3.setTextColor(Color.WHITE);
                ResetTextView(textButton, textButton1, textButton2);
            }
        });
    }

    private void ResetTextView(TextView textView1, TextView textView2, TextView textView3) {
        textView1.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textView2.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textView3.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        textView3.setTextColor(Color.BLACK);
    }
}
