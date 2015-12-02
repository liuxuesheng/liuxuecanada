package com.liuxuecanada.liuxuecanada;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;

public class NewsImageFragment extends Fragment {

    Activity activity;

    public static NewsImageFragment newInstance(String text) {
        NewsImageFragment f = new NewsImageFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_news_images, container, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
                myIntent.putExtra("test1", EnterStudentChoicesActivity.class);
                activity.startActivity(myIntent);
            }
        });
        Log.d("sad8dn ", "1");
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
