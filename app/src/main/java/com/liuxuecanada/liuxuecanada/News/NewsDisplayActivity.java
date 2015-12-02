package com.liuxuecanada.liuxuecanada.News;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
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
        final LinearLayout mainView = (LinearLayout) this.findViewById(R.id.activity_news);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final JSONObject item = jsonArray.getJSONObject(i);

                if (item.getString("type").equals("textview")) {
                    final TextView tv = JSONToComponentService.createTextView(item, this);
                    mainView.addView(tv);
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
