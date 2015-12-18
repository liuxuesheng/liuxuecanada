package com.liuxuecanada.liuxuecanada;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private static int screenWidth = 0;
    private static int newsImageWidth = 0;
    private static int newsImageHeight = 0;
    private static int testImageWidth = 0;
    private static int testImageHeight = 0;
    private FrontPageAdapter mAdapter;
    private ViewPager mPager;
    private ViewPager newsPager = null;
    private LinearLayout lastLayout = null;
    private TextView lastTextView = null;
    private ImageView lastImageView = null;
    private int pageIndex = 0;

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

        //Linear layout
        final LinearLayout ll = (LinearLayout) findViewById(R.id.goto_news_ll);
        final LinearLayout ll1 = (LinearLayout) findViewById(R.id.goto_profile_ll);
        final LinearLayout ll2 = (LinearLayout) findViewById(R.id.goto_suggestions_ll);
        final LinearLayout ll3 = (LinearLayout) findViewById(R.id.goto_messages_ll);

        // Image view
        final ImageView imageButton = (ImageView) findViewById(R.id.goto_news_iv);
        final ImageView imageButton1 = (ImageView) findViewById(R.id.goto_profile_iv);
        final ImageView imageButton2 = (ImageView) findViewById(R.id.goto_suggestions_iv);
        final ImageView imageButton3 = (ImageView) findViewById(R.id.goto_messages_iv);

        // Watch for text click
        final TextView textButton = (TextView) findViewById(R.id.goto_news);
        final TextView textButton1 = (TextView) findViewById(R.id.goto_profile);
        final TextView textButton2 = (TextView) findViewById(R.id.goto_suggestions);
        final TextView textButton3 = (TextView) findViewById(R.id.goto_messages);

        // Set last visited
        this.lastImageView = imageButton;
        this.lastLayout = ll;
        this.lastTextView = textButton;

        //Set initial color
        ll.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
        imageButton.setImageResource(R.drawable.ic_home_white_24dp);
        textButton.setTextColor(Color.WHITE);

        // Set image view properties
        imageButton.setPadding(0, 10, 0, 0);
        imageButton1.setPadding(0, 10, 0, 0);
        imageButton2.setPadding(0, 10, 0, 0);
        imageButton3.setPadding(0, 10, 0, 0);

        // Set text view properties
        textButton.setTextSize(14);
        textButton1.setTextSize(14);
        textButton2.setTextSize(14);
        textButton3.setTextSize(14);

        textButton1.setTextColor(Color.BLACK);
        textButton2.setTextColor(Color.BLACK);
        textButton3.setTextColor(Color.BLACK);

        ll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("viewpager_test: ", "onPageSelected bt1");
                ResetLayout();
                ResetLast(ll, imageButton, textButton);
                mPager.setCurrentItem(0);
                ll.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                imageButton.setImageResource(R.drawable.ic_home_white_24dp);
                textButton.setTextColor(Color.WHITE);
                setPageIndex(0);
            }
        });

        ll1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("viewpager_test: ", "onPageSelected bt2");
                ResetLayout();
                ResetLast(ll1, imageButton1, textButton1);
                mPager.setCurrentItem(1);
                ll1.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                imageButton1.setImageResource(R.drawable.ic_account_box_white_24dp);
                textButton1.setTextColor(Color.WHITE);
                setPageIndex(1);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("viewpager_test: ", "onPageSelected bt3");
                ResetLayout();
                ResetLast(ll2, imageButton2, textButton2);
                mPager.setCurrentItem(2);
                ll2.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                imageButton2.setImageResource(R.drawable.ic_filter_drama_white_24dp);
                textButton2.setTextColor(Color.WHITE);
                setPageIndex(2);
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("viewpager_test: ", "onPageSelected bt4");
                ResetLayout();
                ResetLast(ll3, imageButton3, textButton3);
                mPager.setCurrentItem(3);
                ll3.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                imageButton3.setImageResource(R.drawable.ic_message_white_24dp);
                textButton3.setTextColor(Color.WHITE);
                setPageIndex(3);
            }
        });

        //Create pager
        mAdapter = new FrontPageAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("viewpager_test: ", "onPageScrolled " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("viewpager_test: ", "onPageSelected " + position);
                LinearLayout myll = null;
                ImageView myImageButton = null;
                TextView myTextButton = null;

                switch (position) {
                    case 0:
                        myll = ll;
                        myImageButton = imageButton;
                        myTextButton = textButton;
                        break;
                    case 1:
                        myll = ll1;
                        myImageButton = imageButton1;
                        myTextButton = textButton1;
                        break;
                    case 2:
                        myll = ll2;
                        myImageButton = imageButton2;
                        myTextButton = textButton2;
                        break;
                    case 3:
                        myll = ll3;
                        myImageButton = imageButton3;
                        myTextButton = textButton3;
                        break;
                    default:
                        myll = ll;
                        myImageButton = imageButton;
                        myTextButton = textButton;
                        break;
                }

                if (position != getPageIndex()) {
                    ResetLayout();
                    ResetLast(myll, myImageButton, myTextButton);
                    myll.setBackground(new ColorDrawable(Color.rgb(30, 136, 229)));
                    myImageButton.setImageResource(R.drawable.ic_home_white_24dp);
                    myTextButton.setTextColor(Color.WHITE);
                    setPageIndex(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("viewpager_test: ", "onPageScrollStateChanged " + state);
            }
        });
    }

    private void ResetLayout() {
        this.lastLayout.setBackground(new ColorDrawable(Color.rgb(238, 238, 238)));
        String lastString = this.lastTextView.getText().toString();
        if (lastString.equals("首页"))
            this.lastImageView.setImageResource(R.drawable.ic_home_black_24dp);
        else if (lastString.equals("足迹"))
            this.lastImageView.setImageResource(R.drawable.ic_account_box_black_24dp);
        else if (lastString.equals("规划"))
            this.lastImageView.setImageResource(R.drawable.ic_filter_drama_black_24dp);
        else if (lastString.equals("互联"))
            this.lastImageView.setImageResource(R.drawable.ic_message_black_24dp);
        this.lastTextView.setTextColor(Color.BLACK);
    }

    private void ResetLast(LinearLayout ll, ImageView iv, TextView tv) {
        this.lastLayout = ll;
        this.lastImageView = iv;
        this.lastTextView = tv;
    }

    private int getPageIndex() {
        return this.pageIndex;
    }

    private void setPageIndex(int index) {
        this.pageIndex = index;
    }

}
