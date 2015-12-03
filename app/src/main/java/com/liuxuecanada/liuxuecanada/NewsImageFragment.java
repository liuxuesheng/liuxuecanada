package com.liuxuecanada.liuxuecanada;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ContentItem;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ListAdapter;
import com.liuxuecanada.liuxuecanada.News.NewsDisplayActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsImageFragment extends Fragment
        implements
        AsyncResponse{

    Activity activity;
    private WebView imageWebView = null;
    private String id = null;
    private String srcUrl = null;
    ArrayList<String> new_image_ids = null;
    ArrayList<String> imagsrc = null;
    JSONArray arr = null;

    public static NewsImageFragment newInstance(int pos)
   {
       Log.d("asd8sd7sd ", "pos "+pos);
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
            String imgsrc = (getArguments().getInt("pos") == 1)?imagsrc.get(0):imagsrc.get(1);

            Log.d("asd8sd7sd ", "src "+imagsrc.get(0)+" "+imagsrc.get(1));

            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            String data="<html><body ><img id=\"resizeImage\" src=\""+"http://10.135.30.40/liuxuecanadaserver/news/" + imgsrc +"\" width=\"100%\" style=\"display:block; margin:auto; \"/></body></html>";
            //alt="" align="middle"
            Log.d("sdasdasds ",""+data);
            imageWebView.loadData(data, "text/html; charset=UTF-8", null);

            imageWebView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent myIntent = null;
                    myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                    myIntent.putExtra("test1", EnterStudentChoicesActivity.class);
                    activity.startActivity(myIntent);
                }
            });

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
}
