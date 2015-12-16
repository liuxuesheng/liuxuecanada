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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.GlobalVariants;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FrontPageNewsFragment extends Fragment
        implements
        AsyncResponse {

    Activity activity;
    JSONArray arr = null;
    ImageView iv = null;
    Timer timer;
    int page = 0;
    int hit = 0;
    boolean leftToRight = true;
    private LinearLayout news_container = null;
    private ViewPager newsPager = null;

    public static FrontPageNewsFragment newInstance(String text) {
        FrontPageNewsFragment f = new FrontPageNewsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_news, container, false);

        //Build news image view
        newsPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity) activity).getSupportFragmentManager());
        newsPager.setAdapter(newsAdapter);
        newsPager.setCurrentItem(0);
        newsPager.setOffscreenPageLimit(3);
        ViewGroup.LayoutParams params = newsPager.getLayoutParams();
        params.height = (int) (MainActivity.getScreenWidth() * 0.5);

        //Search 5 latest news on server
        ServerResponse pud = new ServerResponse(this);
        pud.execute(GlobalVariants.serverAddress+"/news/news_list.php");

        news_container = (LinearLayout) v.findViewById(R.id.container_news);

        pageSwitcher(5);

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
                    sectionTextView.setPadding(20, 30, 0, 50);
                    sectionTextView.setCompoundDrawablesWithIntrinsicBounds(PaintService.paintTextIconDrawable(activity, null, 19, 16, new ShapeDrawable(new RectShape()), Color.rgb(30, 136, 229)), null, null, null);
                    sectionTextView.setCompoundDrawablePadding(16);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    sectionTextView.setLayoutParams(params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (sectionTextView != null) {
                    addDivider(news_container, Color.rgb(30, 136, 229), 6);
                    news_container.addView(sectionTextView);
                    addDivider(news_container, Color.LTGRAY, 3);
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

                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute(GlobalVariants.serverAddress+"/news/" + imageSrc, iv, true, MainActivity.getNewsImageWidth(), MainActivity.getNewsImageHeight());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MainActivity.getNewsImageWidth(), MainActivity.getNewsImageHeight());
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

                addDivider(news_container, Color.LTGRAY, 3);
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

    private void addDivider(LinearLayout ll, int color, int height) {
        try {
            View divider = new View(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, height);
            params.setMargins(0, 0, 0, 0);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(color);
            ll.addView(divider);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    public void pageSwitcher(int seconds) {
        page = 0;
        hit = 0;
        timer = new Timer(); // A new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            activity.runOnUiThread(new Runnable() {
                public void run() {

                    Log.d("sds8dsds8ds ", "" + page + " h " + hit);
                    if (page >= 2) {
                        leftToRight = false;
                        hit++;
                    }

                    if (page <= 0) {
                        leftToRight = true;
                        hit++;
                    }

                    if (hit >= 3) {
                        newsPager.setCurrentItem(0);
                        timer.cancel();
                        Toast.makeText(activity, "Timer stoped", Toast.LENGTH_SHORT).show();
                    } else {
                        if (leftToRight)
                            newsPager.setCurrentItem(page++);
                        else
                            newsPager.setCurrentItem(page--);
                    }
                }
            });

        }

    }
}

