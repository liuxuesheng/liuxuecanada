package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.GlobalVariants;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        /*newsPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity) activity).getSupportFragmentManager());
        newsPager.setAdapter(newsAdapter);
        newsPager.setCurrentItem(0);
        newsPager.setOffscreenPageLimit(3);
        ViewGroup.LayoutParams params = newsPager.getLayoutParams();
        params.height = (int) (MainActivity.getScreenWidth() * 0.5);*/

        //Search 5 latest news on server
        ServerResponse pud = new ServerResponse(this);
        pud.execute(GlobalVariants.serverAddress + "/news/news_list.php");

        news_container = (LinearLayout) v.findViewById(R.id.container_news);

        /*pageSwitcher(5);*/

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

            JSONObject item = null;
            String currentId = null;

            for (int i = 0; i < arr.length(); i++) {
                String imageSrc = null;
                TextView title = null;
                TextView subtitle = null;
                TextView date = null;
                boolean flag = false;
                String item_section = null;

                LinearLayout carViewHolder = new LinearLayout(activity);
                LinearLayout.LayoutParams carViewHolderLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                carViewHolderLayoutParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
                carViewHolder.setLayoutParams(carViewHolderLayoutParams);
                carViewHolder.setElevation(3);
                carViewHolder.setBackgroundColor(Color.WHITE);
                carViewHolder.setGravity(Gravity.CENTER_HORIZONTAL);
                carViewHolder.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout cardView = new RelativeLayout(activity);
                RelativeLayout.LayoutParams cardViewLayoutParams = new RelativeLayout.LayoutParams(dpToPx(395), RelativeLayout.LayoutParams.MATCH_PARENT);
                cardView.setLayoutParams(cardViewLayoutParams);

                // Get response
                try {
                    item = arr.getJSONObject(i);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //Get section
                try {
                    item_section = item.getString("item_section");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Get image
                try {
                    imageSrc = item.getString("news_imageURL");
                    iv = new ImageView(activity);

                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute(GlobalVariants.serverAddress + "/news/" + imageSrc, iv, true, dpToPx(360), dpToPx(150));

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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

                // Get title
                try {
                    currentId = item.getString("id");
                    final String newsId = currentId;
                    final String current_item_section = item_section;

                    title = new TextView(activity);
                    title.setText(item.getString("news_title"));
                    title.setBackgroundColor(Color.WHITE);
                    title.setPadding(30, 10, 30, 10);
                    title.setTextSize(16);
                    title.setBackgroundColor(Color.TRANSPARENT);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = null;
                            myIntent = new Intent(activity, NewsDisplayActivity.class);
                            myIntent.putExtra("record", newsId);
                            myIntent.putExtra("section", current_item_section);
                            activity.startActivity(myIntent);
                        }
                    });

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(30, 3, 30, 3);
                    title.setLayoutParams(layoutParams);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Get subtitle
                try {
                    subtitle = new TextView(activity);
                    subtitle.setText(item.getString("news_subtitle"));
                    subtitle.setBackgroundColor(Color.WHITE);
                    subtitle.setPadding(30, 10, 30, 10);
                    subtitle.setTextSize(14);
                    subtitle.setBackgroundColor(Color.TRANSPARENT);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layoutParams.setMargins(30, 3, 30, 3);
                    subtitle.setLayoutParams(layoutParams);

                    subtitle.setId(987 + i);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Get date
                try {
                    date = new TextView(activity);
                    date.setText(item.getString("news_date"));
                    date.setBackgroundColor(Color.WHITE);
                    date.setPadding(30, 10, 30, 10);
                    date.setTextSize(14);
                    date.setBackgroundColor(Color.TRANSPARENT);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(30, 3, 30, 3);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    date.setLayoutParams(layoutParams);

                    date.setId(988 + i);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //Create action bar
                LinearLayout actionBar = new LinearLayout(activity);
                actionBar.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams actionBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                actionBarLayoutParams.setMargins(30, 3, 30, 3);
                actionBarLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                actionBar.setLayoutParams(actionBarLayoutParams);

                Button btLike = new Button(activity);
                Button btComment = new Button(activity);
                Button btShare = new Button(activity);

                LinearLayout.LayoutParams btLayoutParams = new LinearLayout.LayoutParams(dpToPx(20), dpToPx(20));
                btLayoutParams.setMargins(150, 0, 150, 0);

                btLike.setBackgroundResource(R.drawable.ic_main_like);
                btComment.setBackgroundResource(R.drawable.ic_main_comment);
                btShare.setBackgroundResource(R.drawable.ic_main_share);

                btLike.setLayoutParams(btLayoutParams);
                btComment.setLayoutParams(btLayoutParams);
                btShare.setLayoutParams(btLayoutParams);

                LinearLayout actionBarLike = new LinearLayout(activity);
                actionBarLike.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams actionBarLikeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                actionBarLike.setLayoutParams(actionBarLikeLayoutParams);

                LinearLayout actionBarComment = new LinearLayout(activity);
                actionBarComment.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams actionBarCommentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                actionBarComment.setLayoutParams(actionBarCommentLayoutParams);

                LinearLayout actionBarShare = new LinearLayout(activity);
                actionBarShare.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams actionBarShareLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                actionBarShare.setLayoutParams(actionBarShareLayoutParams);

                actionBarLike.addView(btLike);
                actionBarShare.addView(btShare);
                actionBarComment.addView(btComment);

                actionBar.addView(actionBarShare);
                actionBar.addView(actionBarLike);
                actionBar.addView(actionBarComment);


                // Put components in layout
                if (flag == true) {
                   /* news_container.addView(title);
                    news_container.addView(subtitle);
                    news_container.addView(date);*/
                } else {
                    carViewHolder.addView(iv);
                    carViewHolder.addView(title);
                    cardView.addView(subtitle);
                    cardView.addView(date);
                    carViewHolder.addView(cardView);
                    carViewHolder.addView(actionBar);

                    news_container.addView(carViewHolder);
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

    @Override
    public void onPause() {
        super.onPause();
        //timer.cancel();
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

