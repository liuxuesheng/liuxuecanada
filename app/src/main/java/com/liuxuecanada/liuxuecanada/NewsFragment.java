package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackItem;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment
        implements
        AsyncResponse {

    Activity activity;
    JSONArray arr = null;
    ImageView iv = null;
    private List<ChoicesFeedbackItem> choicesFeedbackItems;
    private ListView lv;
    private LinearLayout news_container = null;

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

        //Build news image view
        ViewPager mPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity) activity).getSupportFragmentManager());
        mPager.setAdapter(newsAdapter);
        mPager.setCurrentItem(0);

        //Search 5 latest news on server
        ServerResponse pud = new ServerResponse(this);
        pud.execute("http://192.168.0.12/liuxuecanadaserver/news/news_list.php");

        news_container = (LinearLayout) v.findViewById(R.id.container_news);

        return v;
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
                    sectionTextView.setTextSize(22);
                    //sectionTextView.setTypeface(null, Typeface.BOLD);
                    //sectionTextView.setTextColor(Color.parseColor("#1e90ff"));
                    //sectionTextView.setBackgroundColor(Color.parseColor("#1e90ff"));

                    GradientDrawable gd = new GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[]{0xFFF0F8FF, 0xFFffffff});

                    gd.setCornerRadius(1.0f);

                    sectionTextView.setBackground(gd);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 50, 0, 0);
                    sectionTextView.setLayoutParams(params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (sectionTextView != null) {
                    news_container.addView(sectionTextView);
                    Log.d("sdd9s9d ", "K");
                    continue;
                }

                try {
                    item_section = item.getString("item_section");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    imageSrc = item.getString("news_imageURL");
                    Log.d("sdd9s9d ", "src " + imageSrc);
                    iv = new ImageView(activity);

                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute("http://192.168.0.12/liuxuecanadaserver/news/" + imageSrc, iv, true);

                    Log.d("sdd9s9d ", "src after");
                    iv.setBackgroundColor(Color.CYAN);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 300);
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
                    tv.setPadding(20, 10, 20, 10);
                    tv.setTextSize(18);
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
                    Log.d("sdd9s9d ", "A");
                    news_container.addView(tv);
                } else {
                    Log.d("sdd9s9d ", "B" + (iv == null));
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

