package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;
import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;

public class FrontPageTestFragment extends Fragment
        implements
        AsyncResponse {

    private Activity activity;

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

        TextView tv = new TextView(activity);
        tv.setText("测评开始");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                myIntent.putExtra("test1", EnterStudentChoicesActivity.class);
                activity.startActivity(myIntent);
            }
        });

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

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.activity_test);
        ll.addView(tv);
        ll.addView(tv2);
        ll.addView(tv3);

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {

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
