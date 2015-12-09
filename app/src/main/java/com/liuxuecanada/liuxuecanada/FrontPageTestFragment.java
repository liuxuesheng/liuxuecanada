package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FrontPageTestFragment extends Fragment
        implements
        AsyncResponse {

    LinearLayout ll = null;
    private Activity activity;
    private JSONArray arr = null;

    public static FrontPageTestFragment newInstance(String text) {
        FrontPageTestFragment f = new FrontPageTestFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_test, container, false);

        TextView tv2 = new TextView(activity);
        tv2.setText("测评结果");
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                myIntent = new Intent(activity, ChoicesFeedbackActivity.class);
                activity.startActivity(myIntent);
            }
        });

        TextView tv3 = new TextView(activity);
        tv3.setText("注册");
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                myIntent.putExtra("login", EnterStudentChoicesActivity.class);
                activity.startActivity(myIntent);
            }
        });

        ll = (LinearLayout) v.findViewById(R.id.activity_test);
        ll.addView(tv2);
        ll.addView(tv3);

        ServerResponse pud = new ServerResponse(this);
        pud.execute("http://10.135.31.47/liuxuecanadaserver/frontPage_Test/index.php");

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);

            JSONObject item = null;
            TextView tv = new TextView(activity);
            ImageView iv = new ImageView(activity);


            for (int i = 0; i < arr.length(); i++) {
                FrameLayout testRow = new FrameLayout(activity);
                FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                testRow.setLayoutParams(fllp);

                try {
                    item = arr.getJSONObject(i);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (i == 0) {
                    try {
                        ImageView ivt = (ImageView) activity.findViewById(R.id.imageview_test);
                        LoadImageFromURL loadImage = new LoadImageFromURL();
                        loadImage.execute("http://10.135.31.47/liuxuecanadaserver/frontPage_Test/" + item.getString("imageURL"), ivt, true, ivt.getWidth(), ivt.getHeight());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                try {
                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute("http://10.135.31.47/liuxuecanadaserver/frontPage_Test/" + item.getString("imageURL"), iv, true, MainActivity.getTestImageWidth(), MainActivity.getTestImageHeight());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MainActivity.getTestImageWidth(), MainActivity.getTestImageHeight());
                    layoutParams.setMargins(30, 3, 30, 3);
                    iv.setLayoutParams(layoutParams);

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = null;
                            myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                            myIntent.putExtra("test1", EnterStudentChoicesActivity.class);
                            activity.startActivity(myIntent);
                        }
                    });

                    tv.setText(item.getString("name"));
                    tv.setBackgroundColor(Color.WHITE);

                    testRow.addView(iv);
                    testRow.addView(tv);

                    FrameLayout.LayoutParams fllptv = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    fllptv.gravity = Gravity.BOTTOM;
                    tv.setLayoutParams(fllptv);

                    ll.addView(testRow);

                } catch (Exception ex) {
                    ex.printStackTrace();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
