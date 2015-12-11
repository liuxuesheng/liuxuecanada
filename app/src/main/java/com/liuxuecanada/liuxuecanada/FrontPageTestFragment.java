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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;
import com.liuxuecanada.liuxuecanada.Utils.GlobalVariants;
import com.liuxuecanada.liuxuecanada.Utils.LoadImageFromURL;
import com.liuxuecanada.liuxuecanada.Utils.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FrontPageTestFragment extends Fragment
        implements
        AsyncResponse {

    LinearLayout ll = null;
    GridLayout gl = null;
    private Activity activity;
    private JSONArray arr = null;
    private int numOfCol = 0;
    private int numOfRow = 0;

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

        gl = (GridLayout) v.findViewById(R.id.grid_test);

        numOfCol = gl.getColumnCount();
        numOfRow = gl.getRowCount();

        ServerResponse pud = new ServerResponse(this);
        pud.execute(GlobalVariants.serverAddress + "/frontPage_Test/index.php");

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {
        try {
            arr = new JSONArray((String) out);

            JSONObject item = null;


            for (int i = 0; i < arr.length(); i++) {

                TextView tv = new TextView(activity);
                ImageView iv = new ImageView(activity);

                FrameLayout testRow = new FrameLayout(activity);

                try {
                    item = arr.getJSONObject(i);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (i == 0) {
                    try {
                        ImageView ivt = (ImageView) activity.findViewById(R.id.imageview_test);
                        LoadImageFromURL loadImage = new LoadImageFromURL();
                        loadImage.execute(GlobalVariants.serverAddress + "/frontPage_Test/" + "image_1.jpg", ivt, true, ivt.getWidth(), ivt.getHeight());
                        ivt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = null;
                                myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                                myIntent.putExtra("test1", EnterStudentChoicesActivity.class);
                                activity.startActivity(myIntent);
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                try {
                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute(GlobalVariants.serverAddress + "/frontPage_Test/" + item.getString("imageURL"), iv, true, MainActivity.getTestImageWidth() - 60, MainActivity.getTestImageHeight() - 36);

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 10, 0);
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
                    tv.setTextColor(Color.WHITE);
                    tv.setBackgroundColor(Color.argb(100, 0, 0, 0));
                    tv.setGravity(Gravity.CENTER);

                    testRow.addView(iv);
                    testRow.addView(tv);

                    FrameLayout.LayoutParams fllptv = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    fllptv.gravity = Gravity.BOTTOM;
                    tv.setLayoutParams(fllptv);

                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.setGravity(Gravity.CENTER);
                    testRow.setLayoutParams(param);
                    testRow.setBackgroundColor(Color.LTGRAY);

                    gl.addView(testRow);

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

    private int getGridRowNumber() {
        return this.numOfRow;
    }

    private int getGridColNumber() {
        return this.numOfCol;
    }
}
