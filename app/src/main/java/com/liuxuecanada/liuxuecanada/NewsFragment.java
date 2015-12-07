package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment
        implements
        AsyncResponse {

    Activity activity;
    JSONArray arr = null;
    ImageView iv = null;
    private LinearLayout news_container = null;
    private int newsImageWidth = 0;
    private int newsImageHeight = 0;
    private int screenWidth = 0;

    public static NewsFragment newInstance(String text) {
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_news, container, false);

        //Get Screen Width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;

        //Build news image view
        ViewPager mPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity) activity).getSupportFragmentManager());
        mPager.setAdapter(newsAdapter);
        mPager.setCurrentItem(0);
        ViewGroup.LayoutParams params = mPager.getLayoutParams();
        params.height = (int) (screenWidth * 0.5);

        //Search 5 latest news on server
        ServerResponse pud = new ServerResponse(this);
        pud.execute("http://192.168.0.12/liuxuecanadaserver/news/news_list.php");

        news_container = (LinearLayout) v.findViewById(R.id.container_news);

        //Get News Image Dimensions
        newsImageWidth = screenWidth * 7 / 24;
        newsImageHeight = (int) (newsImageWidth * 0.618);

        return v;
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            //news list
            arr = new JSONArray((String) out);

            ArrayList<String> ids = new ArrayList<String>();
            JSONObject item = null;
            String currentId = null;

            for (int i = 0; i < arr.length(); i++) {
                String imageSrc = null;
                TextView tv = null;
                boolean flag = false;
                TextView sectionTextView = null;
                String item_section = null;

                try {
                    item = arr.getJSONObject(i);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    String sectionName = item.getString("section");
                    sectionTextView = new TextView(activity);
                    sectionTextView.setText(sectionName);
                    sectionTextView.setTextSize(21);
                    sectionTextView.setTextColor(Color.rgb(30, 136, 229));
                    sectionTextView.setBackgroundColor(Color.WHITE);
                    sectionTextView.setPadding(20, 30, 0, 50);
                    sectionTextView.setCompoundDrawablesWithIntrinsicBounds(PaintService.paintTextIconDrawable(activity, null, 20, 16, new ShapeDrawable(new RectShape()), Color.rgb(30, 136, 229)), null, null, null);
                    sectionTextView.setCompoundDrawablePadding(16);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    sectionTextView.setLayoutParams(params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (sectionTextView != null) {
                    View divider = new View(activity);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 6);
                    params.setMargins(0, 50, 0, 0);
                    divider.setLayoutParams(params);
                    divider.setBackgroundColor(Color.rgb(30, 136, 229));
                    news_container.addView(divider);
                    news_container.addView(sectionTextView);
                    continue;
                }

                try {
                    item_section = item.getString("item_section");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    imageSrc = item.getString("news_imageURL");
                    iv = new ImageView(activity);

                    if (newsImageHeight == 0 || newsImageWidth == 0) {
                        newsImageWidth = 500;
                        newsImageHeight = 300;
                    }
                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute("http://192.168.0.12/liuxuecanadaserver/news/" + imageSrc, iv, true, newsImageWidth, newsImageHeight);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(newsImageWidth, newsImageHeight);
                    layoutParams.setMargins(30, 3, 30, 3);
                    iv.setLayoutParams(layoutParams);

                    currentId = item.getString("id");
                    final String newsId = currentId;
                    final String current_item_section = item_section;
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = null;
                            myIntent = new Intent(activity, NewsDisplayActivity.class);
                            myIntent.putExtra("record", newsId);
                            myIntent.putExtra("section", current_item_section);
                            activity.startActivity(myIntent);
                        }
                    });

                } catch (Exception ex) {
                    flag = true;
                    ex.printStackTrace();
                }

                try {
                    currentId = item.getString("id");
                    final String newsId = currentId;
                    final String current_item_section = item_section;

                    tv = new TextView(activity);
                    tv.setText(item.getString("news_title"));
                    tv.setBackgroundColor(Color.WHITE);
                    tv.setPadding(30, 10, 30, 10);
                    tv.setTextSize(17);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = null;
                            myIntent = new Intent(activity, NewsDisplayActivity.class);
                            myIntent.putExtra("record", newsId);
                            myIntent.putExtra("section", current_item_section);
                            activity.startActivity(myIntent);
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (flag == true) {
                    news_container.addView(tv);
                } else {
                    LinearLayout ll = new LinearLayout(activity);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    ll.addView(iv);
                    ll.addView(tv);
                    news_container.addView(ll);
                }

            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskStart() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

}

