package com.liuxuecanada.liuxuecanada.News;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.JSONToComponentService;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class NewsDisplayActivity extends Activity
        implements
        AsyncResponse {

    LinearLayout layout = null;
    LinkedList<JSONArray> pagell = null;
    JSONArray arr = null;
    private String mainURL = "";

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);
            Log.d("asdh8sdd ", "6");

            addObjectsToView(arr, getMainURL());
            Log.d("asdh8sdd ", "7");
            Log.d("asd8d ", "4 " + arr);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintService.setBackgroundPainted(false);
        PaintService.setTextPainted(false);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        Intent intent = getIntent();
        setMaintURL("http://10.135.30.40/liuxuecanadaserver/news/news.php");

        setContentView(R.layout.activity_news);

        ServerResponse pud = new ServerResponse(this);
        pud.execute(getMainURL());

    }


    private void addObjectsToView(JSONArray jsonArray, String url) {
        LinearLayout mainView = (LinearLayout) this.findViewById(R.id.activity_news);
        WebView wv = (WebView) this.findViewById(R.id.webview_news);
        TextView title = (TextView) this.findViewById(R.id.title_news);
        TextView subtitle = (TextView) this.findViewById(R.id.subtitle_news);
        TextView date = (TextView) this.findViewById(R.id.date_news);
        TextView body_news = (TextView) this.findViewById(R.id.body_news);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final JSONObject item = jsonArray.getJSONObject(i);

                if (item.getString("type").equals("webview")){
                    wv.loadUrl("http://10.135.30.40/liuxuecanadaserver/news/" + item.getString("url"));
                }else if (item.getString("type").equals("title_news") ){
                    title.setText(item.getString("name"));
                }else if (item.getString("type").equals("subtitle_news")){
                    subtitle.setText(item.getString("name"));
                }else if (item.getString("type").equals("date_news")){
                    date.setText(item.getString("name"));
                }else if (item.getString("type").equals("body_news")){
                    body_news.setText(item.getString("name"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getMainURL() {
        return this.mainURL;
    }

    private void setMaintURL(String page) {
        this.mainURL = getMainURL() + page;
    }


}
