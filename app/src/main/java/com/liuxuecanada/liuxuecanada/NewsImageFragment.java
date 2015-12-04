package com.liuxuecanada.liuxuecanada;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NewsImageFragment extends Fragment
        implements
        AsyncResponse,
        View.OnTouchListener, Handler.Callback {

    static final int MAX_CLICK_DURATION = 200;
    private static final int CLICK_ON_WEBVIEW = 1;
    static long startClickTime = 0;
    private final Handler handler = new Handler(this);
    Activity activity;
    JSONArray arr = null;
    private String new_image_ids = null;
    private String imagsrc = null;
    private WebView imageWebView = null;
    private TextView imageTextView = null;
    private String news_title = null;

    public static NewsImageFragment newInstance(int pos) {
        NewsImageFragment f = new NewsImageFragment();
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_news_images, container, false);

        ServerResponse pud = new ServerResponse(this);
        pud.execute("http://10.135.31.47/liuxuecanadaserver/news/news_image_list.php" + "?image=" + getArguments().getInt("pos"));

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);
            JSONObject item = arr.getJSONObject(0);

            //Image slider
            try {

                new_image_ids = item.getString("id");
                imagsrc = item.getString("news_imageURL");
                news_title = item.getString("news_title");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            imageWebView = (WebView) getView().findViewById(R.id.webview_news_slider);
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            String data = "<html><body ><img id=\"resizeImage\" src=\"" + "http://10.135.31.47/liuxuecanadaserver/news/" + imagsrc + "\" width=\"100%\" style=\"display:block; margin:auto; \"/></body></html>";
            imageWebView.loadData(data, "text/html; charset=UTF-8", null);
            imageWebView.setOnTouchListener(this);

            imageTextView = (TextView) getView().findViewById(R.id.textview_news_slider);
            imageTextView.setText(news_title);
            imageTextView.setBackgroundColor(Color.argb(125,255,255,255));

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskStart() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webview_news_slider) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        //click event has occurred
                        Intent myIntent = null;
                        myIntent = new Intent(activity, NewsDisplayActivity.class);
                        myIntent.putExtra("record", "" + new_image_ids);
                        activity.startActivity(myIntent);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_WEBVIEW) {

            return true;
        }
        return false;
    }

}
