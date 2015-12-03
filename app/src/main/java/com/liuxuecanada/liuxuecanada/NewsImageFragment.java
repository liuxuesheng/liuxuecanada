package com.liuxuecanada.liuxuecanada;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsImageFragment extends Fragment
        implements
        AsyncResponse,
        View.OnTouchListener, Handler.Callback {

    Activity activity;
    ArrayList<String> new_image_ids = null;
    ArrayList<String> imagsrc = null;
    JSONArray arr = null;
    private WebView imageWebView = null;
    private String id = null;
    private String srcUrl = null;

    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

    private final Handler handler = new Handler(this);


    public static NewsImageFragment newInstance(int pos) {
        Log.d("asd8sd7sd ", "pos " + pos);
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
        pud.execute("http://10.135.31.47/liuxuecanadaserver/news/news_image_list.php");

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);
            Log.d("asd8d ", "4 " + arr);

            //Image slider
            new_image_ids = new ArrayList<String>();
            imagsrc = new ArrayList<String>();

            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject item = arr.getJSONObject(i);

                    imagsrc.add(item.getString("news_imageURL"));
                    new_image_ids.add(item.getString("id"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            //10.135.31.47
            imageWebView = (WebView) activity.findViewById(R.id.webview_news_slider);
            String imgsrc = (getArguments().getInt("pos") == 1) ? imagsrc.get(0) : imagsrc.get(1);

            Log.d("asd8sd7sd ", "src " + imagsrc.get(0) + " " + imagsrc.get(1));

            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            String data = "<html><body ><img id=\"resizeImage\" src=\"" + "http://10.135.30.40/liuxuecanadaserver/news/" + imgsrc + "\" width=\"100%\" style=\"display:block; margin:auto; \"/></body></html>";
            //alt="" align="middle"
            Log.d("sdasdasds ", "" + data);
            imageWebView.loadData(data, "text/html; charset=UTF-8", null);

            imageWebView.setOnTouchListener(this);

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
        Log.d("asd8sd7sd ", "AAA ");
        if (v.getId() == R.id.webview_news_slider && event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("asd8sd7sd ", "BBB ");
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d("asd8sd7sd ", "A ");
        if (msg.what == CLICK_ON_WEBVIEW) {
            Intent myIntent = null;
            myIntent = new Intent(activity, NewsDisplayActivity.class);
            myIntent.putExtra("record", ""+getArguments().getInt("pos"));
            activity.startActivity(myIntent);
            return true;
        }
        return false;
    }
}
